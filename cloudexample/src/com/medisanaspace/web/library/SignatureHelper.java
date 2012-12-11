package com.medisanaspace.web.library;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.medisanaspace.library.HmacShaEncoder;
import com.medisanaspace.library.MapUtil;
import com.medisanaspace.library.StringUtil;

/**
 * Helper class to calculate the signature and check the authorization header.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public final class SignatureHelper {

	public static final String OAUTH_CONSUMER_KEY_STRING = "oauth_consumer_key";
	public static final String OAUTH_SIGNATURE_METHOD_STRING = "oauth_signature_method";
	public static final String OAUTH_TIMESTAMP_STRING = "oauth_timestamp";
	public static final String OAUTH_NONCE_STRING = "oauth_nonce";
	public static final String OAUTH_VERSION_STRING = "oauth_version";
	public static final String OAUTH_TOKEN_STRING = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET_STRING = "oauth_token_secret";
	public static final String OAUTH_VERIFIER_TOKEN_STRING = "oauth_verifier";
	public static final String OAUTH_SIGNATURE_STRING = "oauth_signature";

	public static final String SIGNATURE_METHOD = "HMAC-SHA256";
	public static final String VERSION = "1.0a";
	public static final String ENCODING = "UTF-8";
	public static final String OAUTH_STRING = "OAuth ";

	private SignatureHelper() {
	}

	public static String calculateBaseSignatureString(final String method,
			final String requestUrl, final String baseParameterString)
			throws Exception {

		String baseSignatureString = null;

		String realRequestUrl = requestUrl.replace("http://", "https://");

		try {
			baseSignatureString = method + "&"
					+ URLEncoder.encode(realRequestUrl, ENCODING) + "&"
					+ URLEncoder.encode(baseParameterString, ENCODING);
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.SEVERE,
					"Could not encode strings (" + realRequestUrl + ", "
							+ baseParameterString + ")");
			throw new Exception("Could not create base signature string.");
		}
		return baseSignatureString;
	}

	public static String calculateSignature(final String baseSignatureString,
			final String consumerSecret, final String tokenSecret)
			throws Exception {
		String secrets = consumerSecret + "&";
		if (StringUtil.isNotNullOrEmpty(tokenSecret)) {
			secrets += tokenSecret;
		}
		try {
			String signature = Base64.encodeString(HmacShaEncoder
					.calculateRFC2104HMAC(baseSignatureString, secrets));
			return URLEncoder.encode(signature, ENCODING);
		} catch (SignatureException e) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.SEVERE,
					"Could not calculate signature [" + e.toString()
							+ "] (BaseSignatureString: " + baseSignatureString
							+ ").");
			throw new Exception("Could not calculate signature.");
		}

	}

	public static Map<String, String> checkBasicAuthorization(
			final String authorization) throws Exception {

		if (!"OAuth ".equals(authorization.substring(0, 6))) {
			String errorString = "Missing \"OAuth\" element at the beginning (entry is missing \"=\": "
					+ authorization + ").";
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.WARNING, errorString);
			throw new Exception("Missing \"OAuth\" element at the beginning.");
		}
		String auth = authorization.substring(6);

		String[] authorizationList = auth.split(",");
		Map<String, String> authorizationMap = new HashMap<String, String>();
		for (String authorizationString : authorizationList) {
			String[] pair = authorizationString.split("=");
			if (pair.length != 2) {
				String errorString = "Invalid authorization header (entry is missing \"=\":"
						+ auth + ").";
				Logger.getLogger(SignatureHelper.class.getName()).log(
						Level.WARNING, errorString);
				throw new Exception(
						"Invalid authorization header (entry is missing \"=\")");
			}
			String key = pair[0];
			String value = pair[1].replaceAll("\"", "");
			authorizationMap.put(key.trim(), value);
		}

		String oauthToken = authorizationMap.get(OAUTH_TOKEN_STRING);
		String consumerKey = authorizationMap.get(OAUTH_CONSUMER_KEY_STRING);
		String signatureMethod = authorizationMap
				.get(OAUTH_SIGNATURE_METHOD_STRING);
		String timestamp = authorizationMap.get(OAUTH_TIMESTAMP_STRING);
		String nonce = authorizationMap.get(OAUTH_NONCE_STRING);
		String verifierToken = authorizationMap
				.get(OAUTH_VERIFIER_TOKEN_STRING);
		String signature = authorizationMap.get(OAUTH_SIGNATURE_STRING);

		if (StringUtil.isNullOrEmpty(consumerKey)) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.WARNING,
					"Missing parameter in authorization header "
							+ OAUTH_CONSUMER_KEY_STRING + " (" + authorization
							+ ").");
			throw new Exception("Missing parameter in authorization header "
					+ OAUTH_CONSUMER_KEY_STRING + ".");
		}

		if (StringUtil.isNullOrEmpty(signatureMethod)) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.WARNING,
					"Missing parameter in authorization header "
							+ OAUTH_SIGNATURE_METHOD_STRING + " ("
							+ authorization + ").");
			throw new Exception("Missing parameter in authorization header "
					+ OAUTH_SIGNATURE_METHOD_STRING + ".");
		}

		if (StringUtil.isNullOrEmpty(timestamp)) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.WARNING,
					"Missing parameter in authorization header "
							+ OAUTH_TIMESTAMP_STRING + " (" + authorization
							+ ").");
			throw new Exception("Missing parameter in authorization header "
					+ OAUTH_TIMESTAMP_STRING + ".");
		}

		if (StringUtil.isNullOrEmpty(nonce)) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.WARNING,
					"Missing parameter in authorization header "
							+ OAUTH_NONCE_STRING + " (" + authorization + ").");
			throw new Exception("Missing parameter in authorization header "
					+ OAUTH_NONCE_STRING + ".");
		}

		String version = authorizationMap.get(OAUTH_VERSION_STRING);
		if (version != null && !SignatureHelper.VERSION.equals(version)) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.WARNING,
					"Invalid version, current version: "
							+ SignatureHelper.VERSION + " (found:" + version
							+ ").");
			throw new Exception("Invalid version, current version: "
					+ SignatureHelper.VERSION + ".");
		}

		if (!SignatureHelper.SIGNATURE_METHOD.equals(signatureMethod)) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.WARNING,
					"Invalid signature method, supported signature methods: "
							+ SignatureHelper.SIGNATURE_METHOD + " (found:"
							+ signatureMethod + ").");
			throw new Exception(
					"Invalid signature method, supported signature methods: "
							+ SignatureHelper.SIGNATURE_METHOD + ".");
		}

		Map<String, String> signatureMap = new HashMap<String, String>();

		if (StringUtil.isNotNullOrEmpty(verifierToken)) {
			signatureMap.put(OAUTH_VERIFIER_TOKEN_STRING, verifierToken);
		}
		if (StringUtil.isNotNullOrEmpty(oauthToken)) {
			signatureMap.put(OAUTH_TOKEN_STRING, oauthToken);
		}
		if (StringUtil.isNotNullOrEmpty(version)) {
			signatureMap.put(OAUTH_VERSION_STRING, version);
		}
		signatureMap.put(OAUTH_CONSUMER_KEY_STRING, consumerKey);
		signatureMap.put(OAUTH_SIGNATURE_METHOD_STRING, signatureMethod);
		signatureMap.put(OAUTH_TIMESTAMP_STRING, timestamp);
		signatureMap.put(OAUTH_NONCE_STRING, nonce);
		signatureMap.put(OAUTH_SIGNATURE_STRING, signature);

		return signatureMap;
	}

	public static String createBaseParameterString(
			Map<String, String> signatureMap, final String parameterString) {

		if (StringUtil.isNotNullOrEmpty(parameterString)) {
			String[] parameterList = parameterString.split("&");

			for (String parameter : parameterList) {
				String[] pair = parameter.split("=");
				if (pair.length != 2) {
					String errorString = "Invalid parameter string (entry is missing \"=\":"
							+ parameterString + ").";
					Logger.getLogger(SignatureHelper.class.getName()).log(
							Level.WARNING, errorString);
					return "Invalid parameter string (entry is missing \"=\")";
				}
				String key = pair[0];
				String value = pair[1];
				signatureMap.put(key, value);
			}
		}

		Map<String, String> sortedSignatureMap = MapUtil
				.sortByKey(signatureMap);

		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;

		for (Map.Entry<String, String> entry : sortedSignatureMap.entrySet()) {
			if (!first) {
				stringBuilder.append("&");
			}
			stringBuilder.append(entry.getKey() + "=" + entry.getValue());
			first = false;
		}

		String baseParameterString = stringBuilder.toString();

		return baseParameterString;
	}
}
