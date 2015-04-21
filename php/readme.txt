
 // calculate signature
  function calculateRFC2104HMAC ($data,$key) {
     $chain = "";
     $HMAC_SHA256_ALGORITHM = "sha256";
     $ret= hash_hmac ( $HMAC_SHA256_ALGORITHM, $data, $key, true );
     return(base64_encode($ret));
}

  // request unauthorized access token and secret
  function
oAuthHeaderWithApplication($application_token,$application_secret) {
     $nonce = $this->create_guid();
     $time = $this->uniqueTimeStamp();
         $url = "/auth/unauthorizedaccesses";
         $key=$application_secret."&";

$data="POST&".urlencode($this->host).urlencode($url)."&oauth_consumer_key%3D".$application_token."%26oauth_nonce%3D".$nonce."%26oauth_signature_method%3DHMAC-SHA256%26oauth_timestamp%3D".$time."%26oauth_version%3D1.0";
         $signature=urlencode($this->calculateRFC2104HMAC($data, $key));
         $authorization='OAuth
oauth_consumer_key="'.$application_token.'",oauth_signature_method="HMAC
-SHA256",oauth_timestamp="'.$time.'",oauth_nonce="'.$nonce.',oauth_version="1.0",oauth_signature="'.$signature.'"';
         $opts = array(
             'http'=>array(
             'method'=>"POST",
             'header'=>"Authorization: ".$authorization."\r\n"
             )
     );
$context = stream_context_create($opts);
         if (!$response = file_get_contents($this->host.$url, false,
$context))
         {
             header("HTTP/1.0 400 '".urlencode($data)."'");
         }
         else {
             $oauth["all"]=$response;
             $oAuths= explode("&",$response);
             $oauth["token"]=(strstr($oAuths[0],"="));
             $oauth["token_secret"]=(strstr($oAuths[1],"="));
             return $oauth;
         }
     }




  // retrieve access token and access secret with verifier
function
oAuthHeaderRequestAccessWithApplication($oauth_token,$oauth_verifier,$oauth_token_secret,$application_token,$application_secret)
{
     $url = "/auth/accesses/verify";
      $timeout = 30;
     $key=$application_secret."&".$oauth_token_secret;
     $nonce = $this->create_guid();
     $time = $this->uniqueTimeStamp();

$data="POST&".urlencode($this->host).urlencode($url)."&oauth_consumer_key%3D".$application_token."%26oauth_nonce%3D".$nonce."%26oauth_signature_method%3DHMAC-SHA256%26oauth_timestamp%3D".$time."%26oauth_token%3D".$oauth_token."%26oauth_verifier%3D".$oauth_verifier."%26oauth_version%3D1.0";
     $signature=urlencode($this->calculateRFC2104HMAC($data, $key));
     $authorization='OAuth
oauth_consumer_key="'.$application_token.'",oauth_signature_method="HMAC
-SHA256",oauth_timestamp="'.$time.'",oauth_nonce="'.$nonce.'",oauth_token="'.$oauth_token.'",oauth_verifier="'.$oauth_verifier.'",oauth_version="1.0",oauth_signature="'.$signature.'"';

      $opts = array(
         'http'=>array(
             'method'=>"POST",
             'header'=>"Authorization: ".$authorization."\r\n"
             )
         );
     $context = stream_context_create($opts);
     if (!$response = file_get_contents($this->host.$url, false,
$context))
     {
         header("HTTP/1.0 400 '".urlencode($data)."'");
     }
     else {
         $oauth["all"]=$response;
         $oAuths= explode("&",$response);
         $oauth["oauth_token"]=(strstr($oAuths[0],"="));
         $oauth["oauth_token_secret"]=(strstr($oAuths[1],"="));
         return $oauth;
     }
  }

  // retrieve data
  function connector($max=100,$modul="targetscales",$access) {

  $access_token = $access["AccessToken"];
$access_secret = $access["AccessSecret"];
$application_token = $access["ApplicationToken"];
$application_secret = $access["ApplicationSecret"];

  if(empty($application_token)) {
$application_token = self::$ApplicationToken;
$application_secret = self::$ApplicationSecret;
}

  $dataArray=array();
     $tmpArray=array();
     $start=0;
     do {
     $nonce = $this->create_guid();
     $time = $this->uniqueTimeStamp();
         $since=0; // TODO
         $key=$application_secret."&".$access_secret;
         $url = "/data/".$modul;
         $parameter="?date_since=".$since."&max=".$max."&start=".$start;


$data="GET&".urlencode($this->host).urlencode($url)."&date_since%3D".$since."%26max%3D".$max."%26oauth_consumer_key%3D".$application_token."%26oauth_nonce%3D".$nonce."%26oauth_signature_method%3DHMAC-SHA256%26oauth_timestamp%3D".$time."%26oauth_token%3D".$access_token."%26oauth_version%3D1.0%26start%3D".$start;
         $signature=urlencode($this->calculateRFC2104HMAC($data, $key));
         $authorization='OAuth
oauth_consumer_key="'.$application_token.'",oauth_signature_method="HMAC
-SHA256",oauth_timestamp="'.$time.'",oauth_nonce="'.$nonce.'",oauth_token="'.$access_token.'",oauth_version="1.0",oauth_signature="'.$signature.'"';

          $timeout = 30;
         $opts = array(
             'http'=>array(
                 'method'=>"GET",
                 'header'=>"Authorization: ".$authorization."\r\n"
                 )
);
         $context = stream_context_create($opts);
         if (!$data = file_get_contents($this->host.$url.$parameter,
false, $context))
{
              header("HTTP/1.0 400 '".urlencode($data)."'");
         }
         else {
}
if($data == "[]") {
return $dataArray;
}

if(!$tmpArray=json_decode($data,true)) {
             return NULL;
}
$dataArray=array_merge($dataArray,$tmpArray);
$start=$start+100;
    }
     while (count($tmpArray)==100 );

     return($dataArray);
}

...

  // callback for verifier token: (e.g. in a separate file /callback.php)
  if (isset($_GET['oauth_token'])&&  isset($_GET['oauth_verifier'])) {

// ... db initialization ...
     $selectUnauthorizedAccess = $Connection->query('SELECT secret,module
FROM vdp_unauthorized_access WHERE token =
"'.mysql_real_escape_string($_GET['oauth_token']).'"');
     while($row =  mysql_fetch_object($selectUnauthorizedAccess))
     {
     $oauth_token_secret = $row->secret;
     $module = $row->module;
     }

      $access =
$conx->oAuthHeaderRequestAccessWithApplication($_GET['oauth_token'],$_GET['oauth_verifier'],$oauth_token_secret,$application_token,$application_secret);

     $oauth_token = str_replace("=","",$access['oauth_token']);
     $oauth_token_secret =
str_replace("=","",$access['oauth_token_secret']);
// ... save access token ...
}