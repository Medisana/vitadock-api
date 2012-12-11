package com.medisanaspace.library;

import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class defines common routines for generating authentication signatures
 * for AWS requests. Sample code used from:
 * http://docs.amazonwebservices.com/AWSSimpleQueueService
 * /latest/SQSDeveloperGuide/AuthJavaSampleHMACSignature.html
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public final class HmacShaEncoder {
	private static final String HMAC_SHA256_ALGORITHM = "HMACSHA256";

	private HmacShaEncoder() {
	}

	/**
	 * Computes RFC 2104-compliant HMAC signature.
	 * 
	 * 
	 * @param data
	 *            The data to be signed.
	 * @param key
	 *            The signing key.
	 * @return The Base64-encoded RFC 2104-compliant HMAC signature.
	 * @throws java.security.SignatureException
	 *             when signature generation fails
	 */
	public static String calculateRFC2104HMAC(final String data,
			final String key) throws java.security.SignatureException {
		try {

			// get an hmac_sha256 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
					HMAC_SHA256_ALGORITHM);

			// get an hmac_sha256 Mac instance and initialize with the signing
			// key
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());

			StringBuffer buf = new StringBuffer();
			for (final byte element : rawHmac) {
				buf.append(Integer.toString((element & 0xff) + 0x100, 16)
						.substring(1));
			}
			return buf.toString();
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : "
					+ e.getMessage());
		}
	}

}
