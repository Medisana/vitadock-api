package com.medisanaspace.model;

public class Application {

	private String uuid;
	private String name;
	private String applicationToken;
	private String applicationSecret;

	public final String getUuid() {
		return this.uuid;
	}

	public void setUuid(final String id) {
		this.uuid = id;
	}

	public final String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public final String getApplicationToken() {
		return this.applicationToken;
	}

	public void setApplicationToken(final String applicationToken) {
		this.applicationToken = applicationToken;
	}

	public final String getApplicationSecret() {
		return this.applicationSecret;
	}

	public void setApplicationSecret(final String applicationSecret) {
		this.applicationSecret = applicationSecret;
	}

}
