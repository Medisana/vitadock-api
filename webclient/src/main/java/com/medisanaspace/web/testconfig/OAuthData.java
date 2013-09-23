package com.medisanaspace.web.testconfig;

/**
 * Data object that holds the access token, access secret,
 * device token and device secret.
 */
public class OAuthData {
	
	private String accessToken = "";
	private String accessSecret = "";
	private String deviceToken = "";
	private String deviceSecret = "";


	/**
	 * Method getAccessToken.
	 * @return String
	 */
	public String getAccessToken() {
		return accessToken;
	}
	/**
	 * Method setAccessToken.
	 * @param accessToken String
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	/**
	 * Method getAccessSecret.
	 * @return String
	 */
	public String getAccessSecret() {
		return accessSecret;
	}
	/**
	 * Method setAccessSecret.
	 * @param accessSecret String
	 */
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
	/**
	 * Method getDeviceToken.
	 * @return String
	 */
	public String getDeviceToken() {
		return deviceToken;
	}
	/**
	 * Method setDeviceToken.
	 * @param deviceToken String
	 */
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	/**
	 * Method getDeviceSecret.
	 * @return String
	 */
	public String getDeviceSecret() {
		return deviceSecret;
	}
	/**
	 * Method setDeviceSecret.
	 * @param deviceSecret String
	 */
	public void setDeviceSecret(String deviceSecret) {
		this.deviceSecret = deviceSecret;
	}

}
