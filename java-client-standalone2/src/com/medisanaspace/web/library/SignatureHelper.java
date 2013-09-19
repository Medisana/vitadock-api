package com.medisanaspace.web.library;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.medisanaspace.library.HmacShaEncoder;
import com.medisanaspace.library.StringUtil;
import org.apache.commons.codec.binary.Base64;

/**
 * Helper class to calculate the signature and check the authorization header.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 * @version $Revision: 1.0 $
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
	public static final String VERSION = "1.0";
	public static final String ENCODING = "UTF-8";
	public static final String OAUTH_STRING = "OAuth ";

	private SignatureHelper() {
	}

	/**
	 * Creates the encoded base signature string.
	 * 
	 * @param method
	 *            The HTTP method (GET or POST)
	 * @param requestUrl
	 *            The URL where the data was sent to or received from
	 * @param baseParameterString
	 *            The sorted base parameter string containing the OAuth
	 *            parameters and additional function parameters (e.g.
	 *            'date_since=0&max=10')
	
	
	 * @return An URL encoded string consisting of a combination of the method,
	 *         the URL and the base parameter string and by URL encoding the
	 *         result. 
	 * @throws Exception */
	public static String calculateBaseSignatureString(final String method,
			final String requestUrl, final String baseParameterString)
			throws Exception {

		String baseSignatureString = null;
		final String realRequestUrl = requestUrl.replace("http://", "https://");

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

	/**
	 * Calculates the signature encoding it with HMAC SHA256 and Base64 using
	 * the encoded base signature string and the secrets.
	 * 
	 * @param baseSignatureString
	 *            The URL encoded base signature string with method, URL, OAuth
	 *            parameters and function parameters (e.g.
	 *            'date_since=0&max=10')
	 * @param consumerSecret
	 *            The application secret
	 * @param tokenSecret
	 *            The access secret
	
	
	 * @return a short string containing the signature. This signature will be
	 *         compared on the server side in order to test if the secrets are
	 *         valid and if the message was not changed during transportation to
	 *         the server. * @throws Exception
	 *             if there was an error calculating the signature (e.g. missing
	 *             internal methods). */
	public static String calculateSignature(final String baseSignatureString,
			final String consumerSecret, final String tokenSecret)
			throws Exception {
		String secrets = consumerSecret + "&";
		if (StringUtil.isNotNullOrEmpty(tokenSecret)) {
			secrets += tokenSecret;
		}
		try {
			return URLEncoder.encode(
					new String(Base64.encodeBase64(HmacShaEncoder
							.calculateRFC2104HMAC(baseSignatureString, secrets))), ENCODING);
		} catch (SignatureException e) {
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.SEVERE,
					"Could not calculate signature [" + e.toString() + ", "
							+ StringUtil.getStackTraceAsString(e)
							+ "] (BaseSignatureString: " + baseSignatureString
							+ ").");
			throw new Exception("Could not calculate signature.");
		}
	}

	/**
	 * Do some basic checks on a received authorization string from the server
	 * and extract the OAuth parameters.
	 * 
	 * @param authorization
	 *            The complete authorization header string
	
	
	 * @return A map with name/value pairs of the individual OAuth parameters 
	 * @throws Exception
	 *             if there was a problem with one of the parameters or the
	 *             whole OAuth parameter string (e.g. missing parameter). */
	public static Map<String, String> checkBasicAuthorization(
			final String authorization) throws Exception {

		if (!"OAuth ".equals(authorization.substring(0, 6))) {
			final String errorString = "Missing \"OAuth\" element at the beginning (entry is missing \"=\": "
					+ authorization + ").";
			Logger.getLogger(SignatureHelper.class.getName()).log(
					Level.WARNING, errorString);
			throw new Exception("Missing \"OAuth\" element at the beginning.");
		}
		final String auth = authorization.substring(6);

		final String[] authorizationList = auth.split(",");
		final Map<String, String> authorizationMap = new HashMap<String, String>();
		for (String authorizationString : authorizationList) {
			final String[] pair = authorizationString.split("=");
			if (pair.length != 2) {
				final String errorString = "Invalid authorization header (entry is missing \"=\":"
						+ auth + ").";
				Logger.getLogger(SignatureHelper.class.getName()).log(
						Level.WARNING, errorString);
				throw new Exception(
						"Invalid authorization header (entry is missing \"=\")");
			}
			final String key = pair[0];
			final String value = pair[1].replaceAll("\"", "");
			authorizationMap.put(key.trim(), value);
		}

		final String oauthToken = authorizationMap.get(OAUTH_TOKEN_STRING);
		final String consumerKey = authorizationMap.get(OAUTH_CONSUMER_KEY_STRING);
		final String signatureMethod = authorizationMap
				.get(OAUTH_SIGNATURE_METHOD_STRING);
		final String timestamp = authorizationMap.get(OAUTH_TIMESTAMP_STRING);
		final String nonce = authorizationMap.get(OAUTH_NONCE_STRING);
		final String verifierToken = authorizationMap
				.get(OAUTH_VERIFIER_TOKEN_STRING);
		final String signature = authorizationMap.get(OAUTH_SIGNATURE_STRING);

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
}
