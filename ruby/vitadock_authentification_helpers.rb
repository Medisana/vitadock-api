require 'rubygems'
require 'oauth' #needed for CGI
require 'net/http'
require 'uri'
require 'uuid'
require 'hmac-sha2' #gem install ruby-hmac

require File.expand_path('../../config/boot',  __FILE__)

module VitadockAuthentificationHelpers

  # howto get access tokens with Vitadoc Authorization Process (inspired by OAuth)
  # ---------------------------------------------
  # 1) create an Application Developer Account at the web interface (BASE_URL)
  # 2) create a new application, web inferface creates application_token and application_secret,
  #    copy both in this file
  # 3) create Access Rights for the application via web interface. Use '-1' for unlimited access.
  # 4) create a User Account at the web interface (no application developer privileges)
  # 5) run 'ruby tools/initial_vitadock_authorization.rb'. 
  # 6) User should be logged in at web interface. Follow link from console.
  # 7) allow application to access what it needs.
  # 8) copy verifier_token from web interface to console
  # 9) console generates access token and secret
  # 10) copy access_token and access_secret to app_settings

  my_settings_hash  = YAML.load_file("#{RAILS_ROOT}/config/app_settings.yml")[RAILS_ENV]
  APP_SETTINGS      = HashWithIndifferentAccess.new(my_settings_hash)

  APPLICATION_TOKEN  = "TODO"
  APPLICATION_SECRET = "TODO"

  ACCESS_TOKEN  = APP_SETTINGS[:access_token]
  ACCESS_SECRET = APP_SETTINGS[:access_secret]

  BASE_URL = "https://vitacloud.medisanaspace.com"
  AUTH_SERVER_URL = BASE_URL + "/auth"
  UNAUTHORIZED_ACCESS_URL = AUTH_SERVER_URL + "/unauthorizedaccesses"  
  DESIRED_ACCESS_URL = BASE_URL + "/desiredaccessrights" 
  VERIFY_URL = AUTH_SERVER_URL + "/accesses/verify" 
  DATA_SERVER_URL = BASE_URL + "/data"
  
  OAUTH_CONSUMER_KEY_STRING = "oauth_consumer_key"
  OAUTH_SIGNATURE_METHOD_STRING = "oauth_signature_method"
  OAUTH_TIMESTAMP_STRING = "oauth_timestamp"
  OAUTH_NONCE_STRING = "oauth_nonce"
  OAUTH_VERSION_STRING = "oauth_version"
  OAUTH_SIGNATURE_STRING = "oauth_signature"
  SIGNATURE_METHOD = "HMAC-SHA256"
  START_STRING = "start"
  MAX_STRING = "max"
  DATE_SINCE_STRING = "date_since"
  MODULE_NAME = [ "cardiodocks", "glucodockglucoses", "glucodockinsulins", "glucodockmeals", "targetscales", "thermodocks" ]
  
  VERSION = "1.0a"
  
  NONCE_LENGTH = 36
  POST_STRING = "POST"
  GET_STRING = "GET"
  
  AUTHORIZATION_STRING = "Authorization"
  OAUTH_TOKEN_STRING = "oauth_token"
  VERIFIER_TOKEN_STRING = "oauth_verifier"
  
  def self.create_unauthorized_access_request_authorization_header the_application_token, the_application_secret
    request_url = UNAUTHORIZED_ACCESS_URL
    timestamp = (Time.now.to_f * 1000).to_i.to_s 
    nonce = UUID.new.generate
  
    base_parameter_hash = { OAUTH_CONSUMER_KEY_STRING => the_application_token,
                            OAUTH_SIGNATURE_METHOD_STRING => SIGNATURE_METHOD,
                            OAUTH_TIMESTAMP_STRING => timestamp,
                            OAUTH_NONCE_STRING => nonce,
                            OAUTH_VERSION_STRING => VERSION} 
    base_parameter_string = create_parameter_string(base_parameter_hash)
    LOGGER.debug "base_parameter_string: " + base_parameter_string 
 
    base_signature_string = POST_STRING + "&" +
                            CGI::escape(request_url) + "&" +
                            CGI::escape(base_parameter_string)
    LOGGER.debug "base_signature_string: " + base_signature_string
  
    signature = calculate_signature(base_signature_string, the_application_secret + "&") 
    LOGGER.debug "signature: " + signature + " length: " + signature.length.to_s
    signature = CGI::escape(signature)
    LOGGER.debug "encoded signature: " + signature + " length: " + signature.length.to_s
  
  
    authorization = OAUTH_CONSUMER_KEY_STRING + "=\"" + the_application_token + "\"," + 
                    OAUTH_NONCE_STRING + "=\"" + nonce + "\"," + 
                    OAUTH_SIGNATURE_METHOD_STRING + "=\"" + SIGNATURE_METHOD + "\"," + 
                    OAUTH_TIMESTAMP_STRING + "=\"" + timestamp + "\"," + 
                    OAUTH_VERSION_STRING + "=\"" + VERSION + "\"," + 
                    OAUTH_SIGNATURE_STRING + "=\"" + signature + "\""
    authorization = "OAuth " + authorization
    LOGGER.debug "authorization: " + authorization
  
    return authorization
  end
  
  def self.create_access_request_authorization_header the_application_token, the_application_secret, the_unauthorized_token, the_unauthorized_secret, the_verifier_token
    request_url = VERIFY_URL
    timestamp = (Time.now.to_f * 1000).to_i.to_s 
    nonce = UUID.new.generate
  
    base_parameter_hash = { OAUTH_CONSUMER_KEY_STRING => the_application_token,
                            OAUTH_SIGNATURE_METHOD_STRING => SIGNATURE_METHOD,
                            OAUTH_TOKEN_STRING => the_unauthorized_token,
                            VERIFIER_TOKEN_STRING => the_verifier_token,
                            OAUTH_TIMESTAMP_STRING => timestamp,
                            OAUTH_NONCE_STRING => nonce,
                            OAUTH_VERSION_STRING => VERSION }
    base_parameter_string = create_parameter_string(base_parameter_hash)
    LOGGER.debug "base_parameter_string: " + base_parameter_string
  
    base_signature_string = POST_STRING + "&" +
                            CGI::escape(request_url) + "&" +
                            CGI::escape(base_parameter_string)
    LOGGER.debug "base_signature_string: " + base_signature_string
  
    signature = calculate_signature(base_signature_string, the_application_secret + "&" + the_unauthorized_secret) 
    LOGGER.debug "signature: " + signature + " length: " + signature.length.to_s
    signature = CGI::escape(signature)
    LOGGER.debug "encoded signature: " + signature + " length: " + signature.length.to_s
  
  
    authorization = OAUTH_CONSUMER_KEY_STRING + "=\"" + the_application_token + "\"," + 
                    OAUTH_SIGNATURE_METHOD_STRING + "=\"" + SIGNATURE_METHOD + "\"," + 
                    OAUTH_TIMESTAMP_STRING + "=\"" + timestamp + "\"," + 
                    OAUTH_NONCE_STRING + "=\"" + nonce + "\"," + 
                    OAUTH_TOKEN_STRING + "=\"" + the_unauthorized_token + "\"," +
                    VERIFIER_TOKEN_STRING + "=\"" + the_verifier_token + "\"," +
                    OAUTH_VERSION_STRING + "=\"" + VERSION + "\"," + 
                    OAUTH_SIGNATURE_STRING + "=\"" + signature + "\""
    authorization = "OAuth " + authorization
    LOGGER.debug "authorization: " + authorization
  
    return authorization
  end
  
  #start is optional. If start is not given the values are ordered with decreasing measurementDate. If it is given with increasing measurementDate.
  def self.create_data_request_authorization_header the_application_token, the_application_secret, the_access_token, the_access_secret, the_module_id, the_max, the_since = 0, the_start = nil
    request_url = create_request_url_module(the_module_id)
  
    timestamp = (Time.now.to_f * 1000).to_i.to_s 
    nonce = UUID.new.generate
  
    base_parameter_hash = { OAUTH_CONSUMER_KEY_STRING => the_application_token,
                            OAUTH_NONCE_STRING => nonce,
                            OAUTH_SIGNATURE_METHOD_STRING => SIGNATURE_METHOD,
                            OAUTH_TIMESTAMP_STRING => timestamp,
                            OAUTH_TOKEN_STRING => the_access_token,
                            OAUTH_VERSION_STRING => VERSION }
    parameter_hash = create_request_parameter_hash(the_max, the_since, the_start)
    base_parameter_hash.merge!(parameter_hash)
    base_parameter_string = create_parameter_string(base_parameter_hash)
    LOGGER.debug "base_parameter_string: " + base_parameter_string
  
    base_signature_string = GET_STRING + "&" +
                            CGI::escape(request_url) + "&" +
                            CGI::escape(base_parameter_string)
    LOGGER.debug "base_signature_string: " + base_signature_string
  
    signature = calculate_signature(base_signature_string, the_application_secret + "&" + the_access_secret)
  
    LOGGER.debug "signature: " + signature + " length: " + signature.length.to_s
    signature = CGI::escape(signature)
    LOGGER.debug "encoded signature: " + signature + " length: " + signature.length.to_s
  
  
    authorization = OAUTH_CONSUMER_KEY_STRING + "=\"" + the_application_token + "\"," + 
                    OAUTH_SIGNATURE_METHOD_STRING + "=\"" + SIGNATURE_METHOD + "\"," + 
                    OAUTH_TIMESTAMP_STRING + "=\"" + timestamp + "\"," + 
                    OAUTH_NONCE_STRING + "=\"" + nonce + "\"," + 
                    OAUTH_TOKEN_STRING + "=\"" + the_access_token + "\"," +
                    OAUTH_VERSION_STRING + "=\"" + VERSION + "\"," + 
                    OAUTH_SIGNATURE_STRING + "=\"" + signature + "\""
    authorization = "OAuth " + authorization
    LOGGER.debug "authorization: " + authorization
  
    return authorization
  end
 
  #unused
  def self.create_save_data_request_authorization_header the_application_token, the_application_secret, the_access_token, the_access_secret, the_module_id, the_parameter_hash
  
    request_url = create_request_url_module(the_module_id)
  
    timestamp = (Time.now.to_f * 1000).to_i.to_s 
    nonce = UUID.new.generate
  
    base_parameter_hash = { OAUTH_CONSUMER_KEY_STRING => the_application_token,
                            OAUTH_SIGNATURE_METHOD_STRING => SIGNATURE_METHOD,
                            OAUTH_TIMESTAMP_STRING => timestamp,
                            OAUTH_NONCE_STRING => nonce,
                            OAUTH_TOKEN_STRING => the_access_token,
                            OAUTH_VERSION_STRING => VERSION }
    base_parameter_hash.merge!(the_parameter_hash)
    the_parameter_string = create_parameter_string(base_parameter_hash)
    LOGGER.debug "base_parameter_string: " + base_parameter_string
  
    base_signature_string = POST_STRING + "&" +
                            CGI::escape(request_url) + "&" +
                            CGI::escape(base_parameter_string)
    LOGGER.debug "base_signature_string: " + base_signature_string
  
    signature = calculate_signature(base_signature_string, the_application_secret + "&" + the_access_secret)
  
    LOGGER.debug "signature: " + signature + " length: " + signature.length.to_s

    signature = CGI::escape(signature)
    LOGGER.debug "encoded signature: " + signature + " length: " + signature.length.to_s
  
    authorization = OAUTH_CONSUMER_KEY_STRING + "=\"" + the_application_token + "\"," + 
                    OAUTH_NONCE_STRING + "=\"" + nonce + "\"," + 
                    OAUTH_SIGNATURE_METHOD_STRING + "=\"" + SIGNATURE_METHOD + "\"," + 
                    OAUTH_TIMESTAMP_STRING + "=\"" + timestamp + "\"," + 
                    OAUTH_TOKEN_STRING + "=\"" + the_access_token + "\"," +
                    OAUTH_VERSION_STRING + "=\"" + VERSION + "\"," + 
                    OAUTH_SIGNATURE_STRING + "=\"" + signature + "\""
    authorization = "OAuth " + authorization
    LOGGER.debug "authorization: " + authorization
  
    return authorization
  end
  
  def self.create_request_url module_id, max, date_since = 0, start = nil
    DATA_SERVER_URL + "/" + MODULE_NAME[module_id] + "?" + 
        create_parameter_string(create_request_parameter_hash(max, date_since, start))
  end
  
  def self.create_request_url_module module_id 
    DATA_SERVER_URL + "/" + MODULE_NAME[module_id]
  end
  
  def self.create_request_parameter_hash max, date_since, start = nil
    hash = { DATE_SINCE_STRING => date_since.to_s,
             MAX_STRING => max.to_s}
    if start
      hash[START_STRING] = start.to_s
    end
    hash
  end
  
  def self.parse_token str_to_parse
    if (str_to_parse.count('=') != 2 || str_to_parse.count('&') != 1) 
      LOGGER.error("Error. Not parseable. (was: " + str_to_parse + ")")
      exit
    end
    access_token = str_to_parse.split("&")[0].split("=")[1]
    access_secret = str_to_parse.split("&")[1].split("=")[1]
    return access_token, access_secret
  end
 


  ## private 

  #secret is used as key
  def self.calculate_signature string_to_sign, secret
    hmac = HMAC::SHA256.hexdigest(secret, string_to_sign)   
    Base64.encode64( hmac ).chomp.gsub( /\n/, '' )
  end

  def self.create_parameter_string hash
    hash = hash.sort
    hash.map! do |entry|
      entry[0].to_s + "=" + entry[1].to_s
    end
    hash.join("&")
  end
end

