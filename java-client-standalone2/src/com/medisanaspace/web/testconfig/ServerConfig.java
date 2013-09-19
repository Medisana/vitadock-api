package com.medisanaspace.web.testconfig;

/**
 * Holds the auth url, login url and data url.
 */
public class ServerConfig {

	private String EXTERNAL_AUTH_URL;
	private String EXTERNAL_LOGIN_URL;
	private String EXTERNAL_DATA_URL;

	public ServerConfig() {
	}

	/**
	 * Method getEXTERNAL_AUTH_URL.
	 * @return String
	 */
	public String getEXTERNAL_AUTH_URL() {
		return EXTERNAL_AUTH_URL;
	}

	/**
	 * Method getEXTERNAL_LOGIN_URL.
	 * @return String
	 */
	public String getEXTERNAL_LOGIN_URL() {
		return EXTERNAL_LOGIN_URL;
	}

	/**
	 * Method getEXTERNAL_DATA_URL.
	 * @return String
	 */
	public String getEXTERNAL_DATA_URL() {
		return EXTERNAL_DATA_URL;
	}

	/**
	 * Method setEXTERNAL_AUTH_URL.
	 * @param eXTERNAL_AUTH_URL String
	 */
	public void setEXTERNAL_AUTH_URL(String eXTERNAL_AUTH_URL) {
		EXTERNAL_AUTH_URL = eXTERNAL_AUTH_URL;
	}

	/**
	 * Method setEXTERNAL_LOGIN_URL.
	 * @param eXTERNAL_LOGIN_URL String
	 */
	public void setEXTERNAL_LOGIN_URL(String eXTERNAL_LOGIN_URL) {
		EXTERNAL_LOGIN_URL = eXTERNAL_LOGIN_URL;
	}

	/**
	 * Method setEXTERNAL_DATA_URL.
	 * @param eXTERNAL_DATA_URL String
	 */
	public void setEXTERNAL_DATA_URL(String eXTERNAL_DATA_URL) {
		EXTERNAL_DATA_URL = eXTERNAL_DATA_URL;
	}

	// --- calculate URLs
	/**
	 * Method getHTTPS_AUTH_URL.
	 * @return String
	 */
	public String getHTTPS_AUTH_URL() {
		return "https://" + EXTERNAL_AUTH_URL;
	}

	/**
	 * Method getHTTPS_LOGIN_URL.
	 * @return String
	 */
	public String getHTTPS_LOGIN_URL() {
		return "https://" + EXTERNAL_LOGIN_URL;
	}

	/**
	 * Method getHTTPS_DATA_URL.
	 * @return String
	 */
	public String getHTTPS_DATA_URL() {
		return "https://" + EXTERNAL_DATA_URL;
	}

}
