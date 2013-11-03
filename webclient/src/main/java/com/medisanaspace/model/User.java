package com.medisanaspace.model;

/**
 */
public class User {

	private String email;
	private String locale;
	private String password;
	
	/**
	 * Constructor for User.
	 * @param email String
	 * @param password String
	 * @param locale String
	 */
	public User(String email, String password, String locale) {
		this.email=email;
		this.password=password;
		this.locale=locale;
	}
	
	/**
	 * Method getEmail.
	 * @return String
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Method setEmail.
	 * @param email String
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Method getLocale.
	 * @return String
	 */
	public String getLocale() {
		return locale;
	}
	/**
	 * Method setLocale.
	 * @param locale String
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
	 * Method getPassword.
	 * @return String
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Method setPassword.
	 * @param password String
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
}
