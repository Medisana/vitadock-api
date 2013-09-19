package com.medisanaspace.model.base;

import org.apache.commons.lang3.RandomStringUtils;

/**
 */
public abstract class Token {
	protected static final int TOKEN_LENGTH = 64;

	private String token;

	public Token() {
		setToken(RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH));
	}

	/**
	 * Method getToken.
	 * @return String
	 */
	public final String getToken() {
		return this.token;
	}

	/**
	 * Method setToken.
	 * @param id String
	 */
	public void setToken(final String id) {
		this.token = id;
	}
}
