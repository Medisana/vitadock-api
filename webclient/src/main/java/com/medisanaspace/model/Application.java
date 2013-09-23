package com.medisanaspace.model;

/**
 */
public class Application {

	private String uuid;
	private String name;
	private String applicationToken;
	private String applicationSecret;

	/**
	 * Method getUuid.
	 * @return String
	 */
	public final String getUuid() {
		return this.uuid;
	}

	/**
	 * Method setUuid.
	 * @param id String
	 */
	public void setUuid(final String id) {
		this.uuid = id;
	}

	/**
	 * Method getName.
	 * @return String
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Method setName.
	 * @param name String
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Method getApplicationToken.
	 * @return String
	 */
	public final String getApplicationToken() {
		return this.applicationToken;
	}

	/**
	 * Method setApplicationToken.
	 * @param applicationToken String
	 */
	public void setApplicationToken(final String applicationToken) {
		this.applicationToken = applicationToken;
	}

	/**
	 * Method getApplicationSecret.
	 * @return String
	 */
	public final String getApplicationSecret() {
		return this.applicationSecret;
	}

	/**
	 * Method setApplicationSecret.
	 * @param applicationSecret String
	 */
	public void setApplicationSecret(final String applicationSecret) {
		this.applicationSecret = applicationSecret;
	}

}
