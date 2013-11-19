package com.medisanaspace.web.library;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import com.medisanaspace.library.MapUtil;
import com.medisanaspace.library.StringUtil;
import com.medisanaspace.web.main.CloudClient;
import com.medisanaspace.web.testconfig.ServerConfig;

/**
 * String builder for the authorization header block for accesses to the
 * VitaDock Server.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 * @version $Revision: 1.0 $
 */
public final class AuthorizationBuilder {

	public static final String START_STRING = "start";
	public static final String MAX_STRING = "max";
	public static final String DATE_SINCE_STRING = "date_since";

	public static final Integer NONCE_LENGTH = 36;
	public static final String POST_STRING = "POST";
	public static final String GET_STRING = "GET";

	public static final int CARDIODOCK_MODULE_ID = 0;
	public static final int GLUCODOCK_GLUCOSE_MODULE_ID = 1;
	public static final int GLUCODOCK_INSULIN_MODULE_ID = 2;
	public static final int GLUCODOCK_MEAL_MODULE_ID = 3;
	public static final int TARGETSCALE_MODULE_ID = 4;
	public static final int THERMODOCK_MODULE_ID = 5;
	public static final int ACTIVITY_MODULE_ID = 6;
	public static final int TRACKER_ACTIVITY_MODULE_ID = 7;
	public static final int TRACKER_SLEEP_MODULE_ID = 8;

	public static final int ORDER_MODULE_ID = 10;
	public static final int MAILING_MODULE_ID = 11;

	public static final int CARDIODOCK_SETTINGS_MODULE_ID = 12;
	public static final int GLUCODOCK_GLUCOSE_SETTINGS_MODULE_ID = 13;
	public static final int GLUCODOCK_INSULIN_SETTINGS_MODULE_ID = 14;
	public static final int GLUCODOCK_MEAL_SETTINGS_MODULE_ID = 15;
	public static final int TARGETSCALE_SETTINGS_MODULE_ID = 16;
	public static final int THERMODOCK_SETTINGS_MODULE_ID = 17;

	public static final int USER_SETTINGS_MODULE_ID = 18;

	public static final int TRACKER_PHASE_MODULE_ID = 30;
	public static final int TRACKER_STATS_MODULE_ID = 31;

	private static final String[] MODULE_NAME = { "cardiodocks",
			"glucodockglucoses", "glucodockinsulins", "glucodockmeals",
			"targetscales", "thermodocks", "activitydocks", "tracker/activity",
			"tracker/sleep", "", "orders", "mailings", "cardiodocks/settings",
			"glucodockglucoses/settings", "glucodockinsulins/settings",
			"glucodockmeals/settings", "targetscales/settings",
			"thermodocks/settings", "user/settings", "", "", "", "", "", "",
			"", "", "", "", "", "tracker/phase", "tracker/stats" };

	private AuthorizationBuilder() {
	}

	/**
	 * Build authorization header for requesting an Unauthorized Access.
	 * 
	 * @param applicationToken
	 *            Token of the requesting Application
	 * @param applicationSecret
	 *            Secret of the requesting Application
	 * 
	 * 
	 * @param requestUrl
	 *            String
	 * @return The Authorization header as a string * @throws Exception
	 */
	public static String createUnauthorizedAccessRequestAuthorizationHeader(
			final String requestUrl, final String applicationToken,
			final String applicationSecret) throws Exception {
		final String timestamp = Long.toString(new Date().getTime());
		final String nonce = RandomStringUtils.randomAlphanumeric(NONCE_LENGTH);

		final Map<String, String> signatureMap = new HashMap<String, String>();

		signatureMap.put(SignatureHelper.OAUTH_CONSUMER_KEY_STRING,
				applicationToken);
		signatureMap.put(SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING,
				SignatureHelper.SIGNATURE_METHOD);
		signatureMap.put(SignatureHelper.OAUTH_TIMESTAMP_STRING, timestamp);
		signatureMap.put(SignatureHelper.OAUTH_NONCE_STRING, nonce);
		signatureMap.put(SignatureHelper.OAUTH_VERSION_STRING,
				SignatureHelper.VERSION);

		final String baseParameterString = MapUtil.createBaseParameterString(
				signatureMap, null);

		CloudClient.printer.logMessage("    Base Parameter String: "
				+ baseParameterString + "\n");

		final String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(POST_STRING, requestUrl,
						baseParameterString);

		CloudClient.printer.logMessage("    Base Signature String: "
				+ baseSignatureString + "\n");

		final String signature = SignatureHelper.calculateSignature(
				baseSignatureString, applicationSecret, null);

		final String authorization = SignatureHelper.OAUTH_STRING
				+ SignatureHelper.OAUTH_CONSUMER_KEY_STRING + "=\""
				+ applicationToken + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING + "=\""
				+ SignatureHelper.SIGNATURE_METHOD + "\","
				+ SignatureHelper.OAUTH_TIMESTAMP_STRING + "=\"" + timestamp
				+ "\"," + SignatureHelper.OAUTH_NONCE_STRING + "=\"" + nonce
				+ "\"," + SignatureHelper.OAUTH_VERSION_STRING + "=\""
				+ SignatureHelper.VERSION + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_STRING + "=\"" + signature
				+ "\"";

		return authorization;
	}

	/**
	 * Build authorization header for requesting an Access.
	 * 
	 * @param applicationToken
	 *            Token of the Consumer's application
	 * @param applicationSecret
	 *            Secret of the Consumer's application
	 * @param unauthorizedAccessToken
	 *            Valid Unauthorized Access Token received earlier from the
	 *            server
	 * @param unauthorizedAccessSecret
	 *            Unauthorized Secret received earlier from the server
	 * @param verifierToken
	 *            Verifier Token received from a callback from the server
	 * 
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @return The Authorization header as a string * @throws Exception
	 */
	public static String createAccessRequestAuthorizationHeader(
			ServerConfig serverConfig, String applicationToken,
			String unauthorizedAccessToken, String unauthorizedAccessSecret,
			String verifierToken, String applicationSecret) throws Exception {

		String requestUrl = serverConfig.getHTTPS_AUTH_URL()
				+ "/accesses/verify";
		String timestamp = Long.toString(new Date().getTime());
		String nonce = RandomStringUtils.randomAlphanumeric(NONCE_LENGTH);

		Map<String, String> signatureMap = new HashMap<String, String>();

		signatureMap.put(SignatureHelper.OAUTH_CONSUMER_KEY_STRING,
				applicationToken);
		// testRunnerConfig.getAPPLICATION_TOKEN());
		signatureMap.put(SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING,
				SignatureHelper.SIGNATURE_METHOD);
		signatureMap.put(SignatureHelper.OAUTH_TIMESTAMP_STRING, timestamp);
		signatureMap.put(SignatureHelper.OAUTH_NONCE_STRING, nonce);
		signatureMap.put(SignatureHelper.OAUTH_TOKEN_STRING,
				unauthorizedAccessToken);
		signatureMap.put(SignatureHelper.OAUTH_VERIFIER_TOKEN_STRING,
				verifierToken);
		signatureMap.put(SignatureHelper.OAUTH_VERSION_STRING,
				SignatureHelper.VERSION);

		String baseParameterString = MapUtil.createBaseParameterString(
				signatureMap, null);

		CloudClient.printer.logMessage("    Base Parameter String: "
				+ baseParameterString + "\n");

		String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(POST_STRING, requestUrl,
						baseParameterString);

		CloudClient.printer.logMessage("    Base Signature String: "
				+ baseSignatureString + "\n");

		String signature = SignatureHelper.calculateSignature(
				baseSignatureString, applicationSecret,
				unauthorizedAccessSecret);

		String authorization = SignatureHelper.OAUTH_STRING
				+ SignatureHelper.OAUTH_CONSUMER_KEY_STRING + "=\""
				+ applicationToken + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING + "=\""
				+ SignatureHelper.SIGNATURE_METHOD + "\","
				+ SignatureHelper.OAUTH_TIMESTAMP_STRING + "=\"" + timestamp
				+ "\"," + SignatureHelper.OAUTH_NONCE_STRING + "=\"" + nonce
				+ "\"," + SignatureHelper.OAUTH_TOKEN_STRING + "=\""
				+ unauthorizedAccessToken + "\","
				+ SignatureHelper.OAUTH_VERIFIER_TOKEN_STRING + "=\""
				+ verifierToken + "\"," + SignatureHelper.OAUTH_VERSION_STRING
				+ "=\"" + SignatureHelper.VERSION + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_STRING + "=\"" + signature
				+ "\"";

		return authorization;
	}

	/**
	 * Build authorization header for accessing an Access for saving data on the
	 * server.
	 * 
	 * 
	 * @param parameterString
	 *            The encoded parameter string
	 * @param applicationToken
	 *            Token of the Consumer's application
	 * @param applicationSecret
	 *            Secret of the Consumer's application
	 * 
	 * @param accessToken
	 *            Access Token received earlier from the server
	 * @param accessSecret
	 *            Access Secret received earlier from the server
	 * 
	 * 
	 * @param requestUrl
	 *            String
	 * @return String
	 * @throws Exception
	 *             * @throws UnsupportedEncodingException if there was an error
	 *             encoding the URL
	 */
	public static String createSaveDataRequestAuthorizationHeader(
			final String requestUrl, final String parameterString,
			final String applicationToken, final String applicationSecret,
			final String accessToken, final String accessSecret)
			throws Exception {

		String timestamp = Long.toString(new Date().getTime());
		String nonce = RandomStringUtils.randomAlphanumeric(NONCE_LENGTH);

		Map<String, String> signatureMap = new HashMap<String, String>();

		signatureMap.put(SignatureHelper.OAUTH_CONSUMER_KEY_STRING,
				applicationToken);
		signatureMap.put(SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING,
				SignatureHelper.SIGNATURE_METHOD);
		signatureMap.put(SignatureHelper.OAUTH_TIMESTAMP_STRING, timestamp);
		signatureMap.put(SignatureHelper.OAUTH_NONCE_STRING, nonce);
		signatureMap.put(SignatureHelper.OAUTH_TOKEN_STRING, accessToken);
		signatureMap.put(SignatureHelper.OAUTH_VERSION_STRING,
				SignatureHelper.VERSION);

		String baseParameterString = MapUtil.createBaseParameterString(
				signatureMap, null);

		if (StringUtil.isNotNullOrEmpty(parameterString)) {
			baseParameterString += "&" + parameterString;
		}

		CloudClient.printer.logMessage("    Base Parameter String: "
				+ baseParameterString + "\n");

		String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(POST_STRING, requestUrl,
						baseParameterString);

		CloudClient.printer.logMessage("    Base Signature String: "
				+ baseSignatureString + "\n");

		String signature = SignatureHelper.calculateSignature(
				baseSignatureString, applicationSecret, accessSecret);

		String authorization = SignatureHelper.OAUTH_STRING
				+ SignatureHelper.OAUTH_CONSUMER_KEY_STRING + "=\""
				+ applicationToken + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING + "=\""
				+ SignatureHelper.SIGNATURE_METHOD + "\","
				+ SignatureHelper.OAUTH_TIMESTAMP_STRING + "=\"" + timestamp
				+ "\"," + SignatureHelper.OAUTH_NONCE_STRING + "=\"" + nonce
				+ "\"," + SignatureHelper.OAUTH_TOKEN_STRING + "=\""
				+ accessToken + "\"," + SignatureHelper.OAUTH_VERSION_STRING
				+ "=\"" + SignatureHelper.VERSION + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_STRING + "=\"" + signature
				+ "\"";

		return authorization;
	}

	/**
	 * Build authorization header for accessing an Access for loading data from
	 * the server.
	 * 
	 * 
	 * @param start
	 *            first entry of entry returned
	 * @param max
	 *            maximum number of entries returned
	 * @param dateSince
	 *            retrieve all entries with a measurement date later than this
	 * @param applicationToken
	 *            Token of the Consumer's application
	 * @param applicationSecret
	 *            Secret of the Consumer's application
	 * @param accessToken
	 *            Access Token received earlier from the server
	 * @param accessSecret
	 *            Access Secret received earlier from the server
	 * 
	 * 
	 * @param requestUrl
	 *            String
	 * @return The Authorization header as a string * @throws Exception
	 */
	public static String createLoadDataRequestAuthorizationHeader(
			final String requestUrl, final int start, final int max,
			final String dateSince, final String applicationToken,
			final String applicationSecret, final String accessToken,
			final String accessSecret) throws Exception {

		String parameterString = (start >= 0 ? START_STRING + "="
				+ Integer.toString(start) + "&" : "")
				+ MAX_STRING
				+ "="
				+ Integer.toString(max)
				+ "&"
				+ DATE_SINCE_STRING + "=" + dateSince;
		String timestamp = Long.toString(new Date().getTime());
		String nonce = RandomStringUtils.randomAlphanumeric(NONCE_LENGTH);

		Map<String, String> signatureMap = new HashMap<String, String>();

		signatureMap.put(SignatureHelper.OAUTH_CONSUMER_KEY_STRING,
				applicationToken);
		signatureMap.put(SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING,
				SignatureHelper.SIGNATURE_METHOD);
		signatureMap.put(SignatureHelper.OAUTH_TIMESTAMP_STRING, timestamp);
		signatureMap.put(SignatureHelper.OAUTH_NONCE_STRING, nonce);
		signatureMap.put(SignatureHelper.OAUTH_TOKEN_STRING, accessToken);
		signatureMap.put(SignatureHelper.OAUTH_VERSION_STRING,
				SignatureHelper.VERSION);

		String baseParameterString = MapUtil.createBaseParameterString(
				signatureMap, parameterString);

		CloudClient.printer.logMessage("    Base Parameter String: "
				+ baseParameterString + "\n");

		String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(GET_STRING, requestUrl,
						baseParameterString);

		CloudClient.printer.logMessage("    Base Signature String: "
				+ baseSignatureString + "\n");

		String signature = SignatureHelper.calculateSignature(
				baseSignatureString, applicationSecret, accessSecret);

		String authorization = SignatureHelper.OAUTH_STRING
				+ SignatureHelper.OAUTH_CONSUMER_KEY_STRING + "=\""
				+ applicationToken + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING + "=\""
				+ SignatureHelper.SIGNATURE_METHOD + "\","
				+ SignatureHelper.OAUTH_TIMESTAMP_STRING + "=\"" + timestamp
				+ "\"," + SignatureHelper.OAUTH_NONCE_STRING + "=\"" + nonce
				+ "\"," + SignatureHelper.OAUTH_TOKEN_STRING + "=\""
				+ accessToken + "\"," + SignatureHelper.OAUTH_VERSION_STRING
				+ "=\"" + SignatureHelper.VERSION + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_STRING + "=\"" + signature
				+ "\"";
		return authorization;
	}

	/**
	 * Method createCountDataRequestAuthorizationHeader.
	 * 
	 * @param dateSince
	 *            String
	 * @param requestUrl
	 *            String
	 * @param applicationToken
	 *            String
	 * @param applicationSecret
	 *            String
	 * @param accessToken
	 *            String
	 * @param accessSecret
	 *            String
	 * @return String
	 * @throws Exception
	 */
	public static String createCountDataRequestAuthorizationHeader(
			final String dateSince, final String requestUrl,
			final String applicationToken, final String applicationSecret,
			final String accessToken, final String accessSecret)
			throws Exception {
		final String parameterString = DATE_SINCE_STRING + "=" + dateSince;
		final String timestamp = Long.toString(new Date().getTime());
		final String nonce = RandomStringUtils.randomAlphanumeric(NONCE_LENGTH);

		final Map<String, String> signatureMap = new HashMap<String, String>();

		signatureMap.put(SignatureHelper.OAUTH_CONSUMER_KEY_STRING,
				applicationToken);
		signatureMap.put(SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING,
				SignatureHelper.SIGNATURE_METHOD);
		signatureMap.put(SignatureHelper.OAUTH_TIMESTAMP_STRING, timestamp);
		signatureMap.put(SignatureHelper.OAUTH_NONCE_STRING, nonce);
		signatureMap.put(SignatureHelper.OAUTH_TOKEN_STRING, accessToken);
		signatureMap.put(SignatureHelper.OAUTH_VERSION_STRING,
				SignatureHelper.VERSION);

		// TODO replace null with parameterString
		String baseParameterString = MapUtil.createBaseParameterString(
				signatureMap, parameterString);

		CloudClient.printer.logMessage("    Base Parameter String: "
				+ baseParameterString );

		String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(GET_STRING, requestUrl,
						baseParameterString);

		CloudClient.printer.logMessage("    Base Signature String: "
				+ baseSignatureString );

		String signature = SignatureHelper.calculateSignature(
				baseSignatureString, applicationSecret, accessSecret);

		String authorization = SignatureHelper.OAUTH_STRING
				+ SignatureHelper.OAUTH_CONSUMER_KEY_STRING + "=\""
				+ applicationToken + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_METHOD_STRING + "=\""
				+ SignatureHelper.SIGNATURE_METHOD + "\","
				+ SignatureHelper.OAUTH_TIMESTAMP_STRING + "=\"" + timestamp
				+ "\"," + SignatureHelper.OAUTH_NONCE_STRING + "=\"" + nonce
				+ "\"," + SignatureHelper.OAUTH_TOKEN_STRING + "=\""
				+ accessToken + "\"," + SignatureHelper.OAUTH_VERSION_STRING
				+ "=\"" + SignatureHelper.VERSION + "\","
				+ SignatureHelper.OAUTH_SIGNATURE_STRING + "=\"" + signature
				+ "\"";
		return authorization;
	}

	/**
	 * Helper function to build the request URL.
	 * 
	 * @param moduleId
	 *            id of the module to load data from
	 * @param start
	 *            first entry of entry returned
	 * @param max
	 *            maximum number of entries returned
	 * @param dateSince
	 *            retrieve all entries with a measurement date later than this
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @return URL of the service to send the request to
	 */
	public static String createRequestUrl(final ServerConfig serverConfig,
			final int moduleId, final int start, final int max,
			final String dateSince) {
		return createRequestUrl(serverConfig, moduleId)
				+ "?"
				+ (start >= 0 ? START_STRING + "=" + Integer.toString(start)
						+ "&" : "") + MAX_STRING + "=" + Integer.toString(max)
				+ "&" + DATE_SINCE_STRING + "=" + dateSince;
	}

	/**
	 * Method createSyncUrl.
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @param moduleId
	 *            int
	 * @param start
	 *            int
	 * @param max
	 *            int
	 * @param dateSince
	 *            String
	 * @return String
	 */
	public static String createSyncUrl(final ServerConfig serverConfig,
			final int moduleId, final int start, final int max,
			final String dateSince) {
		return createSyncUrl(serverConfig, moduleId)
				+ "?"
				+ (start >= 0 ? START_STRING + "=" + Integer.toString(start)
						+ "&" : "") + MAX_STRING + "=" + Integer.toString(max)
				+ "&" + DATE_SINCE_STRING + "=" + dateSince;
	}

	/**
	 * Helper class to build the request URL.
	 * 
	 * @param moduleId
	 *            id of the module to load data from
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @return URL of the service to save/load data to/from.
	 */
	public static String createRequestUrl(final ServerConfig serverConfig,
			final int moduleId) {
		return serverConfig.getHTTPS_DATA_URL() + "/" + MODULE_NAME[moduleId];
	}

	/**
	 * Method createRequestArrayUrl.
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @param moduleId
	 *            int
	 * @return String
	 */
	public static String createRequestArrayUrl(final ServerConfig serverConfig,
			final int moduleId) {
		return createRequestUrl(serverConfig, moduleId) + "/array";
	}

	/**
	 * Method createSyncUrl.
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @param moduleId
	 *            int
	 * @return String
	 */
	public static String createSyncUrl(final ServerConfig serverConfig,
			final int moduleId) {
		return serverConfig.getHTTPS_DATA_URL() + "/" + MODULE_NAME[moduleId]
				+ "/sync";
	}

	/**
	 * Method createDeleteArrayUrl.
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @param moduleId
	 *            int
	 * @return String
	 */
	public static String createDeleteArrayUrl(final ServerConfig serverConfig,
			final int moduleId) {
		return serverConfig.getHTTPS_DATA_URL() + "/" + MODULE_NAME[moduleId]
				+ "/delete/array";
	}

	/**
	 * Method createCountUrl.
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @param moduleId
	 *            int
	 * @return String
	 */
	public static String createCountUrl(final ServerConfig serverConfig,
			final int moduleId) {
		return serverConfig.getHTTPS_DATA_URL() + "/" + MODULE_NAME[moduleId]
				+ "/count";
	}

	/**
	 * Method createDeleteAllArrayUrl.
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @param moduleId
	 *            int
	 * @return String
	 */
	public static String createDeleteAllArrayUrl(
			final ServerConfig serverConfig, final int moduleId) {
		return serverConfig.getHTTPS_DATA_URL() + "/" + MODULE_NAME[moduleId]
				+ "/delete/all";
	}

	/**
	 * Method createGenerateUrl.
	 * 
	 * @param serverConfig
	 *            ServerConfig
	 * @param moduleId
	 *            int
	 * @return String
	 */
	public static String createGenerateUrl(final ServerConfig serverConfig,
			final int moduleId) {
		return serverConfig.getHTTPS_DATA_URL() + "/" + MODULE_NAME[moduleId]
				+ "/generate";
	}
}
