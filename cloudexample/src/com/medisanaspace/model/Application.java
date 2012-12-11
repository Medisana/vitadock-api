package com.medisanaspace.model;

public class Application {

	private String uuid;
	private String name;
	private String applicationToken;
	private String applicationSecret;

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String id) {
		this.uuid = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplicationToken() {
		return this.applicationToken;
	}

	public void setApplicationToken(String applicationToken) {
		this.applicationToken = applicationToken;
	}

	public String getApplicationSecret() {
		return this.applicationSecret;
	}

	public void setApplicationSecret(String applicationSecret) {
		this.applicationSecret = applicationSecret;
	}

}
