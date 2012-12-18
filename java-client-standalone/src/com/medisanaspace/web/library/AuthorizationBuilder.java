package com.medisanaspace.web.library;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import com.medisanaspace.library.MapUtil;
import com.medisanaspace.library.StringUtil;

/**
 * String builder for the authorization header block for accesses to the
 * VitaDock Server.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
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

	private static final String[] MODULE_NAME = { "cardiodocks",
			"glucodockglucoses", "glucodockinsulins", "glucodockmeals",
			"targetscales", "thermodocks" };

	private AuthorizationBuilder() {
	}

	/**
	 * Build authorization header for requesting an Unauthorized Access.
	 * 
	 * @param applicationToken
	 *            Token of the requesting Application
	 * @param applicationSecret
	 *            Secret of the requesting Application
	 * @return The Authorization header as a string
	 * @throws Exception
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

		final String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(POST_STRING, requestUrl,
						baseParameterString);

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
	 * @return The Authorization header as a string
	 * @throws Exception
	 */
	public static String createAccessRequestAuthorizationHeader(
			final String applicationToken, final String applicationSecret,
			final String unauthorizedAccessToken,
			final String unauthorizedAccessSecret, final String verifierToken)
			throws Exception {

		String requestUrl = VitaDockServer.HTTPS_AUTH_URL + "/accesses/verify";
		String timestamp = Long.toString(new Date().getTime());
		String nonce = RandomStringUtils.randomAlphanumeric(NONCE_LENGTH);

		Map<String, String> signatureMap = new HashMap<String, String>();

		signatureMap.put(SignatureHelper.OAUTH_CONSUMER_KEY_STRING,
				applicationToken);
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

		String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(POST_STRING, requestUrl,
						baseParameterString);

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
	 * @param moduleId
	 *            The id of the module to access
	 * @param parameterString
	 *            The encoded parameter string
	 * @param applicationToken
	 *            Token of the Consumer's application
	 * @param applicationSecret
	 *            Secret of the Consumer's application
	 * @param unauthorizedAccessToken
	 *            Unauthorized Secret received earlier from the server
	 * @param accessToken
	 *            Access Token received earlier from the server
	 * @param accessSecret
	 *            Access Secret received earlier from the server
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 *             if there was an error encoding the URL
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

		System.out.println("    Base Parameter String: " + baseParameterString
				+ "\n");

		String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(POST_STRING, requestUrl,
						baseParameterString);

		System.out.println("    Base Signature String: " + baseSignatureString
				+ "\n");

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
	 * @param moduleId
	 *            id of the module to load data from
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
	 * @return The Authorization header as a string
	 * @throws Exception
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

		String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(GET_STRING, requestUrl,
						baseParameterString);

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

		String baseParameterString = MapUtil.createBaseParameterString(
				signatureMap, parameterString);

		String baseSignatureString = SignatureHelper
				.calculateBaseSignatureString(GET_STRING, requestUrl,
						baseParameterString);

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
	 * @return URL of the service to send the request to
	 */
	public static String createRequestUrl(final int moduleId, final int start,
			final int max, final String dateSince) {
		return createRequestUrl(moduleId)
				+ "?"
				+ (start >= 0 ? START_STRING + "=" + Integer.toString(start)
						+ "&" : "") + MAX_STRING + "=" + Integer.toString(max)
				+ "&" + DATE_SINCE_STRING + "=" + dateSince;
	}

	public static String createSyncUrl(final int moduleId, final int start,
			final int max, final String dateSince) {
		return createSyncUrl(moduleId)
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
	 * @return URL of the service to save/load data to/from.
	 */
	public static String createRequestUrl(final int moduleId) {
		return VitaDockServer.HTTPS_DATA_URL + "/" + MODULE_NAME[moduleId];
	}

	public static String createRequestArrayUrl(final int moduleId) {
		return createRequestUrl(moduleId) + "/array";
	}

	public static String createSyncUrl(final int moduleId) {
		return VitaDockServer.HTTPS_DATA_URL + "/" + MODULE_NAME[moduleId]
				+ "/sync";
	}

	public static String createDeleteArrayUrl(final int moduleId) {
		return VitaDockServer.HTTPS_DATA_URL + "/" + MODULE_NAME[moduleId]
				+ "/delete/array";
	}

	public static String createCountUrl(final int moduleId) {
		return VitaDockServer.HTTPS_DATA_URL + "/" + MODULE_NAME[moduleId]
				+ "/count";
	}

	public static String createDeleteAllArrayUrl(final int moduleId) {
		return VitaDockServer.HTTPS_DATA_URL + "/" + MODULE_NAME[moduleId]
				+ "/delete/all";
	}

	public static String createGenerateUrl(final int moduleId) {
		return VitaDockServer.HTTPS_DATA_URL + "/" + MODULE_NAME[moduleId]
				+ "/generate";
	}
}
