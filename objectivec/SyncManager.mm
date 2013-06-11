#import "SyncManager.h"
#import "ConnectionManager.h"
#import "EventDataJsonConverter.h"
#import "HttpConsts.h"
#import "NSString+NSStringUtility.h"
#import "NSStringAdditions.h"
#import "PersonalData.h"

#import "NSDateUtility.h"

//newest ones with created app
#define HARDCODED_APPLICATION_TOKEN  @"123"
#define HARDCODED_APPLICATION_SECRET @"456"

#define KEY_VALUE_STRING_FORMAT @"%@=%@"

enum _SyncManagerRequest
{
    SMR_OBTAIN_DEVICE_TOKEN_SECRET,
    SMR_OBTAIN_UNAUTH_TOKEN_SECRET,
    SMR_OBTAIN_ACCESS_TOKEN_SECRET,
    SMR_SEND_ARRAY,
    SMR_SEND_DELETED_ARRAY,
    SMR_GET_EVENTS_SYNC,
    SMR_GET_EVENTS_RESTORE,
    SMR_LOGOUT_USER,    
}SyncManagerRequest;


@interface SyncManager()

@property (nonatomic,retain) SBJsonParser*           jsonParser;
@property (nonatomic,retain) ConnectionManager*      connectionManager;
@property (nonatomic,retain) EventData*              eventDataToBeSend;
@property (nonatomic,retain) NSString*               accessSecret;
@property (nonatomic,retain) NSString*               accessToken;
@property (nonatomic,retain) EventDataJsonConverter* jsonEventConverter;
@property (nonatomic,retain) NSDate*                 getDataStartDate;
@property (nonatomic,retain) NSArray*                eventsArrayToBeSend;
@property (nonatomic,retain) NSString*               sessionId;

-(NSString*)getDeviceToken;
-(NSString*)getDeviceSecret;

- (void)clearOperation;

- (NSString*)getSeverModuleNameForEventDataType:(EventDataType)eventType;

- (void)sendMeasurementsFromArray:(NSArray*)singleTypeEventsArray withOperation:(SyncManagerOperation)op withAccessToken:(NSString*)at withAccessSecret:(NSString*)as withDelegate:(id<SyncManagerDelegate>)delegate;
- (void)v2sendMeasurements;
- (void)v2sendMeasurementsArrayWithUrl:(NSString*)url dataArray:(NSArray*)array forMainJsonKey:(NSString*)mainJsonKey;

- (void)getEventsInternal;

-(NSString*)prepareAuthorizationHeaderStringWithMethod:(NSString*)method withUrl:(NSString*)url withAccessToken:(NSString*)accToken withAccessSecret:(NSString*)accSecret withVerifierToken:(NSString*)verifier withData:(NSString*)dataString withAppTokenSecretInsteadOfDevice:(BOOL)appInsteadOfDevice;

@end


@implementation SyncManager

@synthesize jsonParser          = _jsonParser;
@synthesize connectionManager   = _connectionManager;
@synthesize eventDataToBeSend   = _eventDataToBeSend;
@synthesize jsonEventConverter  = _jsonEventConverter;
@synthesize eventsArrayToBeSend = _eventsArrayToBeSend;
@synthesize sessionId           = _sessionId;
@synthesize accessSecret        = _accessSecret;
@synthesize accessToken         = _accessToken;
@synthesize getDataStartDate    = _getDataStartDate;

#pragma mark Construction&Destruction

- (id)init
{
    self = [super init];
    if( !self )
        return nil;
    
    self.jsonParser         = [[MainController sharedInstance] getJsonParser];
    self.connectionManager  = [[ConnectionManager alloc] init];
    self.jsonEventConverter = [[MainController sharedInstance] getJsonEventConverter];
    
    [Mediator addTarget:self selector:@selector( applicationGoesBackground ) forEvent:AppEventApplicationPaused];
    
    return self;
}

- (void)dealloc
{
    self.jsonParser          = nil;
    self.connectionManager   = nil;
    self.eventDataToBeSend   = nil;
    self.accessToken         = nil;
    self.accessSecret        = nil;
    self.jsonEventConverter  = nil;
    self.eventsArrayToBeSend = nil;
    self.sessionId           = nil;
    
    [super dealloc];
}

-(NSString*)getHardcodedApplicationToken
{
    BOOL isUsingTestServer = NO;

    if( isUsingTestServer )
    {
        return [NSString deobfuscateString:HARDCODED_APPLICATION_TOKEN_TEST];
    }
    else
    {
        return [NSString deobfuscateString:HARDCODED_APPLICATION_TOKEN_RELEASE];
    }  
}

-(NSString*)getHardcodedApplicationSecret
{
    BOOL isUsingTestServer = NO;
     
    if( isUsingTestServer )
    {
        return [NSString deobfuscateString:HARDCODED_APPLICATION_SECRET_TEST];
    }
    else
    {
        return [NSString deobfuscateString:HARDCODED_APPLICATION_SECRET_RELEASE];
    }  
   
}

-(NSString*)getDeviceToken
{
    PersonalData* pd = [[MainController sharedInstance] needPersonalInformation];
    assert(pd != nil);
    return pd.serverDeviceToken;
}

-(NSString*)getDeviceSecret
{
    PersonalData* pd = [[MainController sharedInstance] needPersonalInformation];
    assert(pd != nil);
    return pd.serverDeviceSecret;
}

-(NSString*)getServerUrl
{
    return SERVER_URL;
}

// ---------------------------------------------------------------------------------------------------------------------------------------------------
//
//
#pragma mark Public Methods
//
//
// ---------------------------------------------------------------------------------------------------------------------------------------------------

-(BOOL)isBusy
{
    return (_operation != SMO_NO_OPERATION);
}

-(void)clearOperation
{
    _operation = SMO_NO_OPERATION;
    self.accessToken  = nil;
    self.accessSecret = nil;
}

-(void)updateRangeValues
{
    [self.jsonEventConverter updateGreenRangeValues];
}

-(void)obtainDeviceTokenAndSecretWithDelegate:(id<SyncManagerDelegate>)delegate
{
    if( [self isBusy] )
    {
        NSLog(@"Cannot start obtaining unauthorized token/secret because operation == %d",_operation);
        return;
    }

    _operation = SMO_OBTAIN_DEVICE_TOKEN_SECRET;
    _delegate  = delegate;

    NSString* path = @"auth/devices";
    NSString* url = [NSString stringWithFormat:@"%@/%@", [self getServerUrl], path];
    
    NSLog(@"Get Device T/S url: %@", url);
    
    NSMutableURLRequest* urlRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]
                                                              cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                          timeoutInterval:20.0];

    [urlRequest setHTTPMethod:@"POST"];
	[urlRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [urlRequest addValue:[[MainController sharedInstance] getDeviceUniqueIdentifier] forHTTPHeaderField:@"device_id"];

    NSString* authorizationHeader = [self prepareAuthorizationHeaderStringWithMethod:@"POST"
                                                                             withUrl:url
                                                                     withAccessToken:nil
                                                                    withAccessSecret:nil
                                                                   withVerifierToken:nil
                                                                            withData:nil
                                                   withAppTokenSecretInsteadOfDevice:YES];
    [urlRequest addValue:authorizationHeader forHTTPHeaderField:@"Authorization"];

    [self.connectionManager startRequest:urlRequest
                                  withId:[NSNumber numberWithInt:SMR_OBTAIN_DEVICE_TOKEN_SECRET]
                            withDelegate:self];
}

-(void)obtainUnauthorizedAccessTokenAndSecretWithDelegate:(id<SyncManagerDelegate>)delegate
{
    if( [self isBusy] )
    {
        NSLog(@"Cannot start obtaining unauthorized token/secret because operation == %d",_operation);
        return;
    }
    
    _operation = SMO_OBTAIN_UNAUTH_TOKEN_SECRET;
    _delegate  = delegate;

    NSString* url = [NSString stringWithFormat:@"%@/%@", [self getServerUrl], SERVER_GET_AUTH_PATH];

    NSMutableURLRequest *urlRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]
															  cachePolicy:NSURLRequestUseProtocolCachePolicy
														  timeoutInterval:20.0];
	[urlRequest setHTTPMethod:@"POST"];
	[urlRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    
    NSString* authorizationHeader = [self prepareAuthorizationHeaderStringWithMethod:@"POST"
                                                                             withUrl:url 
                                                                     withAccessToken:nil
                                                                    withAccessSecret:nil
                                                                   withVerifierToken:nil
                                                                            withData:nil
                                                  withAppTokenSecretInsteadOfDevice:NO];
    
    [urlRequest addValue: authorizationHeader forHTTPHeaderField:@"Authorization"];
    
    [self.connectionManager startRequest:urlRequest 
                                  withId:[NSNumber numberWithInt:SMR_OBTAIN_UNAUTH_TOKEN_SECRET] 
                            withDelegate:self];    
}

-(void)obtainAuthorizedAccessTokenAndSecretWithDelegate:(id<SyncManagerDelegate>)delegate withToken:(NSString*)token withSecret:(NSString*)secret withVerifier:(NSString*)verifier
{
    if( [self isBusy] )
    {
        NSLog(@"Cannot start obtaining authorized token/secret because operation == %d",_operation);
        return;
    }
    
    _operation = SMO_OBTAIN_ACCESS_TOKEN_SECRET;
    _delegate  = delegate;
    
    NSString* url = [NSString stringWithFormat:@"%@/%@", [self getServerUrl], SERVER_GET_ACCESS_PATH];
    
    NSMutableURLRequest *urlRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]
															  cachePolicy:NSURLRequestUseProtocolCachePolicy
														  timeoutInterval:20.0];
	[urlRequest setHTTPMethod:@"POST"];
	[urlRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    
    NSString* authorizationHeader = [self prepareAuthorizationHeaderStringWithMethod:@"POST"
                                                                             withUrl:url 
                                                                     withAccessToken:token
                                                                    withAccessSecret:secret 
                                                                   withVerifierToken:verifier
                                                                            withData:nil
                                                  withAppTokenSecretInsteadOfDevice:NO];
    
    [urlRequest addValue: authorizationHeader forHTTPHeaderField:@"Authorization"];

    [self.connectionManager startRequest:urlRequest
                                  withId:[NSNumber numberWithInt:SMR_OBTAIN_ACCESS_TOKEN_SECRET]
                            withDelegate:self];
}

-(void)sendMeasurementsFromArray:(NSArray*)singleTypeEventsArray withAccessToken:(NSString*)at withAccessSecret:(NSString*)as withDelegate:(id<SyncManagerDelegate>)delegate
{
    [self sendMeasurementsFromArray:singleTypeEventsArray withOperation:SMO_SEND_ARRAY withAccessToken:at withAccessSecret:as withDelegate:delegate];
}

-(void)sendDeletedMeasurementsFromArray:(NSArray*)singleTypeEventsArray withAccessToken:(NSString*)at withAccessSecret:(NSString*)as withDelegate:(id<SyncManagerDelegate>)delegate
{
    [self sendMeasurementsFromArray:singleTypeEventsArray withOperation:SMO_SEND_DELETED_ARRAY withAccessToken:at withAccessSecret:as withDelegate:delegate];
}

-(void)sendMeasurementsFromArray:(NSArray*)singleTypeEventsArray withOperation:(SyncManagerOperation)op withAccessToken:(NSString*)at withAccessSecret:(NSString*)as withDelegate:(id<SyncManagerDelegate>)delegate
{
    assert( op == SMO_SEND_ARRAY || op == SMO_SEND_DELETED_ARRAY );
    
    if( [self isBusy] )
    {

        NSLog(@"Cannot start sendMeasurement (with operation:%d) because _operation == %d", op, _operation);
        return;
    }
    
    self.accessToken  = at;
    self.accessSecret = as;
    
    _operation = op;
    _eventsArrayToBeSendRange.location = 0;
    _eventsArrayToBeSendRange.length   = 0;
    _delegate  = delegate;
    self.eventsArrayToBeSend = singleTypeEventsArray;
    
    [self v2sendMeasurements];
}

-(void)getEventsRestoreWithType:(EventDataType)type withAccessToken:(NSString*)accessToken withAccessSecret:(NSString*)accessSecret withDelegate:(id<SyncManagerDelegate>)delegate
{
    if( [self isBusy] )
        return;
    
    _operation                         = SMO_GET_EVENTS_RESTORE;
    _delegate                          = delegate;
    _getDataEventType                  = type;
    _getDataRestoreTypeFilenameCounter = 0;
    _getDataRange                      = NSMakeRange( 0 , MEASUREMENTS_COUNT_FOR_SINGLE_REQUEST );
    self.getDataStartDate              = [NSDate dateWithTimeIntervalSince1970:0];
    self.accessToken                   = accessToken;
    self.accessSecret                  = accessSecret;

    [self getEventsInternal];
}

-(void)getEventsSyncWithType:(EventDataType)type fromDate:(NSDate*)startDate withAccessToken:(NSString*)accessToken withAccessSecret:(NSString*)accessSecret withDelegate:(id<SyncManagerDelegate>)delegate
{
    if( [self isBusy] )
        return;
    
    _operation                         = SMO_GET_EVENTS_SYNC;
    _delegate                          = delegate;
    _getDataEventType                  = type;
    _getDataRange                      = NSMakeRange( 0 , MEASUREMENTS_COUNT_FOR_SINGLE_REQUEST );
    self.getDataStartDate              = startDate;
    self.accessToken                   = accessToken;
    self.accessSecret                  = accessSecret;
    
    [self getEventsInternal];
}

-(void)cancel
{
    _operation = SMO_NO_OPERATION;
    [self.connectionManager cancel:self];
}

// ---------------------------------------------------------------------------------------------------------------------------------------------------
//
//
#pragma mark Internal communication methods
//
//
// ---------------------------------------------------------------------------------------------------------------------------------------------------

- (void) v2sendMeasurements
{
    assert( _operation == SMO_SEND_ARRAY || _operation == SMO_SEND_DELETED_ARRAY );

    if( self.eventsArrayToBeSend == nil || [self.eventsArrayToBeSend count] == 0 )
    {
        [self clearOperation];

        return;
    }
    
    EventDataType eventType = [((EventData*)[self.eventsArrayToBeSend objectAtIndex:0]) getType];
    NSString* moduleName = [self getSeverModuleNameForEventDataType:eventType];
    
    if( moduleName )
    {
        NSMutableArray* arr = [NSMutableArray arrayWithCapacity: MIN( [self.eventsArrayToBeSend count], MEASUREMENTS_COUNT_FOR_SINGLE_REQUEST) ];
                
        //update location according to previously sent chunk (method may be called few times for single array,
        //array is splitted to chunks with up to MEASUREMENTS_COUNT_FOR_SINGLE_POST length)
        //at first time location and length are set to 0, so operation is safe
        _eventsArrayToBeSendRange.location += _eventsArrayToBeSendRange.length;
        _eventsArrayToBeSendRange.length    = MIN( MEASUREMENTS_COUNT_FOR_SINGLE_REQUEST, [self.eventsArrayToBeSend count] - _eventsArrayToBeSendRange.location );
        
        const int BEG = _eventsArrayToBeSendRange.location;
        const int END = BEG + _eventsArrayToBeSendRange.length;
        for( int i = BEG; i < END; ++i )
        {            
            EventData* ed = (EventData*)[self.eventsArrayToBeSend objectAtIndex:i];
            id obj = nil;
            if( _operation == SMO_SEND_ARRAY )
            {
                obj = [self.jsonEventConverter eventToJsonDictionary:ed];
            }
            else if( _operation == SMO_SEND_DELETED_ARRAY )
            {
                obj = [NSString stringWithString:ed.uniqueId];
            }
            else
            {
                assert( 0 );
            }
            
            if( obj )
            {
                [arr addObject:obj];
            }
        }

        
        NSString* path;
        
        switch( _operation ) 
        {
            case SMO_SEND_ARRAY:
                path = [NSString stringWithFormat:SERVER_SEND_ARRAY_FORMAT_PATH, moduleName];
                break;
                
            case SMO_SEND_DELETED_ARRAY:
                path = [NSString stringWithFormat:SERVER_SEND_DELETED_ARRAY_FORMAT_PATH, moduleName];
                break;
                
            default:
                assert( 0 );
                break;
        }
        NSString* url = [NSString stringWithFormat:@"%@/%@",[self getServerUrl], path];                     
        
        [self v2sendMeasurementsArrayWithUrl:url dataArray:arr forMainJsonKey:moduleName];
    }
}

- (void)v2sendMeasurementsArrayWithUrl:(NSString*)url dataArray:(NSArray*)array forMainJsonKey:(NSString*)mainJsonKey
{
    assert( _operation == SMO_SEND_ARRAY || _operation == SMO_SEND_DELETED_ARRAY );

    NSArray* jsonData = array;
    
    
    SBJsonWriter* jsonWritter = [[[SBJsonWriter alloc] init] autorelease];
    NSString* jsonString = [jsonWritter stringWithObject:jsonData];
    
    NSLog(@"JsonString:%@",jsonString);

	NSMutableURLRequest *urlRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]
															  cachePolicy:NSURLRequestUseProtocolCachePolicy
														  timeoutInterval:20.0];
	[urlRequest setHTTPMethod:@"POST"];
	[urlRequest setValue:@"application/json;charset=UTF-8" forHTTPHeaderField:@"Content-Type"];
	[urlRequest setValue:@"application/json;charset=UTF-8" forHTTPHeaderField:@"Accept"];
 
 

    NSString* authorizationHeader = [self prepareAuthorizationHeaderStringWithMethod:@"POST"
                                                                             withUrl:url
                                                                     withAccessToken:self.accessToken
                                                                    withAccessSecret:self.accessSecret
                                                                   withVerifierToken:nil
                                                                            withData:jsonString
                                                  withAppTokenSecretInsteadOfDevice:NO];
    
    [urlRequest addValue:authorizationHeader forHTTPHeaderField:@"Authorization"];
    
    const char* UTF8_JSON     = [jsonString UTF8String];
    const int   UTF8_JSON_LEN = strlen( UTF8_JSON );

	NSData* body = [NSData dataWithBytes:UTF8_JSON length:UTF8_JSON_LEN];
	[urlRequest setHTTPBody:body];

    int smr;
    
    switch( _operation )
    {
        case SMO_SEND_ARRAY:
            smr = SMR_SEND_ARRAY;
            break;
        case SMO_SEND_DELETED_ARRAY:
            smr = SMR_SEND_DELETED_ARRAY;
            break;
        default:
            assert( 0 );
            break;
    }
    
    [self.connectionManager startRequest:urlRequest
                                  withId:[NSNumber numberWithInt:smr]
                            withDelegate:self];
    
}

-(void)getEventsInternal
{
    assert( _operation == SMO_GET_EVENTS_RESTORE || _operation == SMO_GET_EVENTS_SYNC );
    
    
    NSString* module = [self getSeverModuleNameForEventDataType:_getDataEventType];
    
    NSString* parameter = [NSString stringWithFormat:@"start=%d&max=%d&date_since=%@",
                           _getDataRange.location,
                           _getDataRange.length,
                           [self.getDataStartDate timeIntervalMilisecondsSince1970String] ];

    NSString* url;
    NSNumber* connectionId;
    if( _operation == SMO_GET_EVENTS_SYNC )
    {
        NSString* path = [NSString stringWithFormat: SERVER_GET_SYNC_FORMAT_PATH, module];
        url            = [NSString stringWithFormat: @"%@/%@?%@", [self getServerUrl], path, parameter];
        connectionId   = [NSNumber numberWithInt: SMR_GET_EVENTS_SYNC];
    }
    else if( _operation == SMO_GET_EVENTS_RESTORE )
    {
        NSString* path = [NSString stringWithFormat: SERVER_GET_RESTORE_FROMAT_PATH, module];
        url            = [NSString stringWithFormat: @"%@/%@?%@", [self getServerUrl], path, parameter];
        connectionId   = [NSNumber numberWithInt: SMR_GET_EVENTS_RESTORE];
    }
    else
    {
        assert( 0 );//should be handled by entry assert, though
    }
    
    NSLog(@"SyncMan:getEventsUrl:%@",url);
    
    NSString* method = @"GET";
    
    NSString* authorizationHeader = [self prepareAuthorizationHeaderStringWithMethod:method
                                                                             withUrl:url
                                                                     withAccessToken:self.accessToken 
                                                                    withAccessSecret:self.accessSecret
                                                                   withVerifierToken:nil
                                                                            withData:nil
                                                  withAppTokenSecretInsteadOfDevice:NO];
    
    NSMutableURLRequest *urlRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]
															  cachePolicy:NSURLRequestUseProtocolCachePolicy
														  timeoutInterval:20.0];
	[urlRequest setHTTPMethod:method];
	[urlRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [urlRequest setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    
    [urlRequest addValue: authorizationHeader forHTTPHeaderField:@"Authorization"];
    
    [self.connectionManager startRequest:urlRequest 
                                  withId:connectionId
                            withDelegate:self];    
}


// ---------------------------------------------------------------------------------------------------------------------------------------------------
//
//
#pragma mark private methods
//
//
// ---------------------------------------------------------------------------------------------------------------------------------------------------

- (NSString*)getSeverModuleNameForEventDataType:(EventDataType)eventType
{
    switch( eventType ) 
    {
        case EventGlucoMeasurement:
            return SERVER_MODULE_NAME_GLUCO;
            
        case EventThermoMeasurement:
            return SERVER_MODULE_NAME_THERMO;
            
        case EventCardioMeasurement:
            return SERVER_MODULE_NAME_CARDIO;
            
        case EventWeightMeasurement:
            return SERVER_MODULE_NAME_TARGET;
            
        case EventFood:
            return SERVER_MODULE_NAME_FOOD;
            
        case EventInsulin:
            return SERVER_MODULE_NAME_INSULIN;
            
        default:

            assert(!"syncManager - unknown event type");
            break;
    }
    return nil;
}

-(NSString*)prepareAuthorizationHeaderStringWithMethod:(NSString*)method withUrl:(NSString*)url withAccessToken:(NSString*)accToken withAccessSecret:(NSString*)accSecret withVerifierToken:(NSString*)verifier withData:(NSString*)dataString withAppTokenSecretInsteadOfDevice:(BOOL)appInsteadOfDevice
{
    NSDate*   now    = [NSDate date];
    NSString* nowStr = [now timeIntervalMilisecondsSince1970String];
 
    NSRange textRange;
    textRange =[url rangeOfString:@"?"];
    
    NSString* parameters = nil;
    if(textRange.location != NSNotFound)
    {
        
        parameters = [url substringFromIndex:textRange.location+1];
        url        = [url substringToIndex:textRange.location];
    }

    NSLog(@"URL without parameters:%@", url);
    NSLog(@"Parameters from URL   :%@", parameters);
        
    NSMutableArray* oauthStrings = [NSMutableArray array];
    [oauthStrings addObjectsFromArray:[parameters stringsArrayFromStringWithSeparationString:@"&"]];
    
    [oauthStrings addObject: [NSString stringWithFormat:KEY_VALUE_STRING_FORMAT, @"oauth_consumer_key", appInsteadOfDevice ? [self getHardcodedApplicationToken] : [self getDeviceToken]]];
    [oauthStrings addObject: [NSString stringWithFormat:KEY_VALUE_STRING_FORMAT, @"oauth_nonce", [NSString randomString]]];
    [oauthStrings addObject: [NSString stringWithFormat:KEY_VALUE_STRING_FORMAT, @"oauth_signature_method", @"HMAC-SHA256"]];
    [oauthStrings addObject: [NSString stringWithFormat:KEY_VALUE_STRING_FORMAT, @"oauth_timestamp", nowStr]];
    if( accToken != nil )
        [oauthStrings addObject: [NSString stringWithFormat:KEY_VALUE_STRING_FORMAT, @"oauth_token", accToken]];
    if( verifier != nil )
        [oauthStrings addObject: [NSString stringWithFormat:KEY_VALUE_STRING_FORMAT, @"oauth_verifier", verifier]];
    [oauthStrings addObject:[NSString stringWithFormat:KEY_VALUE_STRING_FORMAT, @"oauth_version", @"1.0a"]];
    
    [oauthStrings sortUsingSelector:@selector(localizedCaseInsensitiveCompare:)];
    
    //baseParameterString is OAuth header for HMAC
    NSString* baseParameterString = [NSString stringWithStringsArray:oauthStrings withConcatenationString:@"&"];
    
    if( dataString != nil )
    {
        baseParameterString = [NSString stringWithFormat:@"%@&%@", baseParameterString, dataString];
    }
    
    NSLog(@"\nBASE_PARAMETER_STRING:\n%@", baseParameterString );
    
    NSString* hmacInput = [NSString stringWithFormat:@"%@&%@&%@",
                           method,
                           [url urlEncodeString:NSUTF8StringEncoding],
                           [baseParameterString urlEncodeString:NSUTF8StringEncoding]];
    
    
    NSLog(@"\nHMAC_INPUT(aka BASE_SIGNATURE_STRING):\n%@",hmacInput);
    
    
    NSString* key;
    NSString* baseSecret = appInsteadOfDevice ? [self getHardcodedApplicationSecret] : [self getDeviceSecret];
    if( accSecret != nil )
    {
        key = [NSString stringWithFormat:@"%@&%@",baseSecret, accSecret];
    }
    else
    {
        key = [NSString stringWithFormat:@"%@&", baseSecret ];
    }
    NSLog(@"\nHMAC key:\n%@", key);
    
    NSString* hmacOutput = [NSString hmacWithKey:key withData:hmacInput];
    NSLog(@"\nHMAC_OUTPUT:\n%@", hmacOutput);
    
    NSString* hmacOut64  = [hmacOutput base64String];
    NSLog(@"\nHMAC_OUT64:\n%@", hmacOut64);
    
    NSString* signature = [hmacOut64 urlEncodeString:NSUTF8StringEncoding];
    NSLog(@"\nSIGNATURE:\n%@", signature);
    
    
    //convert oauth strings to authorization header format
    for( int i = 0; i < [oauthStrings count]; ++i )
    {
        NSString* s = [oauthStrings objectAtIndex:i];
        s = [s stringByReplacingOccurrencesOfString:@"=" withString:@"=\""];
        s = [s stringByAppendingString:@"\""];
        [oauthStrings removeObjectAtIndex:i];
        [oauthStrings insertObject:s atIndex:i];
    }
    [oauthStrings addObject:[NSString stringWithFormat:@"oauth_signature=\"%@\"", signature]];
    
    NSString* authString = [NSString stringWithStringsArray:oauthStrings withConcatenationString:@","];
    authString = [NSString stringWithFormat:@"OAuth %@", authString];
    
    NSLog(@"FINAL_AUTHORIZATION_FIELD:%@", authString);
    return authString;
}

- (BOOL)isResponseJsonEmpty:(NSString*)jsonString
{
    int strlen = [jsonString length];
    return (strlen <= 2); //just brackets with no content
}

- (void)applicationGoesBackground
{
    NSLog(@"SyncMan:app goes bg");
    _appWentBackground = YES;
    self.eventDataToBeSend = nil;
    self.eventsArrayToBeSend = nil;
}

// ---------------------------------------------------------------------------------------------------------------------------------------------------
//
//
#pragma mark HttpRequestProtocol
//
//
// ---------------------------------------------------------------------------------------------------------------------------------------------------

- (void)requestError:(id)request error:(NSString*)error
{
    [self clearOperation];
    if( _delegate )
    {
        [_delegate connectionError];
    }
}

- (void)requestFinished:(id)request statusCode:(int)status data:(NSMutableData*)data
{
    int req = [((NSNumber*)request) intValue];
    
    if( status != 200 )
    {

        [self clearOperation];
        if( _delegate )
        {
            [_delegate connectionFinishedWithErrorCode:status withData:data];
        }
        return;
    }
    
    switch( req ) 
    {
        case SMR_OBTAIN_DEVICE_TOKEN_SECRET:
        {
            [self clearOperation];
            NSString* dataStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            assert( _delegate );//need delegate to set token and secret!
            if( _delegate )
            {
                NSArray* arr = [dataStr componentsSeparatedByString:@"&"];
                NSString* t  = [[[arr objectAtIndex:0] componentsSeparatedByString:@"="] objectAtIndex:1];
                NSString* s  = [[[arr objectAtIndex:1] componentsSeparatedByString:@"="] objectAtIndex:1];
                [_delegate obtainedDeviceToken:t secret:s];
            }
        }
            break;
            
        case SMR_OBTAIN_UNAUTH_TOKEN_SECRET:
        case SMR_OBTAIN_ACCESS_TOKEN_SECRET:
        {
            [self clearOperation];
            NSString* dataStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            BOOL hasToken  = ( [dataStr rangeOfString:@"oauth_token"].location        != NSNotFound );
            BOOL hasSecret = ( [dataStr rangeOfString:@"oauth_token_secret"].location != NSNotFound );
            if( !hasToken || !hasSecret )
            {
                //SYNC:TODO:
            }
            else
            {
                if( _delegate )
                {
                    NSArray* arr = [dataStr componentsSeparatedByString:@"&"];
                    NSString* t  = [[[arr objectAtIndex:0] componentsSeparatedByString:@"="] objectAtIndex:1];
                    NSString* s  = [[[arr objectAtIndex:1] componentsSeparatedByString:@"="] objectAtIndex:1];
                    if( req == SMR_OBTAIN_UNAUTH_TOKEN_SECRET )
                    {
                        [_delegate obtainedUnauthorizedToken:t secret:s];
                    }
                    else //req == SMR_OBTAIN_ACCESS_TOKEN_SECRET
                    {
                        [_delegate obtainedAccessToken:t secret:s];
                    }
                }
            }
        }
            break;
            
        case SMR_SEND_ARRAY:
        case SMR_SEND_DELETED_ARRAY:
        {
            NSString *dataStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            NSLog(@"SyncMan:sendArrayRespStr:%@",dataStr);
            
            const int ARRAY_COUNT       = [self.eventsArrayToBeSend count];
            const int TOTAL_SENT_COUNT  = _eventsArrayToBeSendRange.location + _eventsArrayToBeSendRange.length;
            assert( TOTAL_SENT_COUNT <= ARRAY_COUNT );
            
            BOOL continueNextPart = YES;
            if( _delegate )
            {
                if( req == SMR_SEND_ARRAY )
                {
                    id arr = [self.jsonParser objectWithString:dataStr];
                    assert([arr isKindOfClass:[NSArray class]]);
                    continueNextPart = [_delegate measurementsArrayPartSentWithRange:_eventsArrayToBeSendRange withReturnedServerIds:(NSArray*)arr];
                }
                else if( req == SMR_SEND_DELETED_ARRAY )
                {
                    continueNextPart = [_delegate deletedMeasurementsArrayPartSentWithRange:_eventsArrayToBeSendRange];
                }
                
            }

            if( continueNextPart == NO ) 
            {
                [self clearOperation];
            }
            else
            {
                if( TOTAL_SENT_COUNT >= ARRAY_COUNT )//actually '=='
                {
                    [self clearOperation];
                    if( _delegate )
                    {
                        if( req == SMR_SEND_ARRAY )
                            [_delegate measurementsArraySingleTypeSent];
                        else if( req == SMR_SEND_DELETED_ARRAY )
                            [_delegate deletedMeasurementsArraySingleTypeSent];
                    }                
                }
                else
                {
                    [self v2sendMeasurements];
                }
            }            
        }
            break;
            
        case SMR_GET_EVENTS_SYNC:
        case SMR_GET_EVENTS_RESTORE:
        {
            NSString *dataStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            NSLog( req == SMR_GET_EVENTS_RESTORE ? @"SyncMan:getEventsRestore" : @"SyncMan:getEventsSync" );
            
            BOOL continueNextPart = YES;
            if( _delegate != nil )
            {
                if( req == SMR_GET_EVENTS_RESTORE )
                    continueNextPart = [_delegate getEventsRestorePartCompleteWithReturnString:dataStr forEventType:_getDataEventType];
                else if( req == SMR_GET_EVENTS_SYNC )
                    continueNextPart = [_delegate getEventsSyncPartCompleteWithReturnString:dataStr forEventType:_getDataEventType];
            }

            if( continueNextPart == NO )
            {
 
                [self clearOperation];
            }
            else
            {
                BOOL isEmpty = [self isResponseJsonEmpty:dataStr];
                if( isEmpty )
                {
                    [self clearOperation];
                    if( _delegate != nil )
                    {
                        if( req == SMR_GET_EVENTS_RESTORE )
                            [_delegate getEventsRestoreSingleTypeComplete];
                        else if( req == SMR_GET_EVENTS_SYNC )
                            [_delegate getEventsSyncSingleTypeComplete];
                    }
                }
                else
                {
                    _getDataRange.location += _getDataRange.length;
                    [self getEventsInternal];
                }
            }
        }
            break;

        default:
            assert(!"SyncManager::requestFinished:Unexpected request id");
            break;
    }
}

// ---------------------------------------------------------------------------------------------------------------------------------------------------
//
//
#pragma mark TEMPORARY METHODS and debug
//
//
// ---------------------------------------------------------------------------------------------------------------------------------------------------

@end
