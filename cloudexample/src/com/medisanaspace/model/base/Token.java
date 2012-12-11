package com.medisanaspace.model.base;

import org.apache.commons.lang3.RandomStringUtils;

public abstract class Token {
	protected static final int TOKEN_LENGTH = 64;

	private String token;

	public Token() {
		setToken(RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH));
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String id) {
		this.token = id;
	}
}
