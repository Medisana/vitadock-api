package com.medisanaspace.web.library;

/**
 * Placeholder of main Server URLs.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public final class VitaDockServer {
	private VitaDockServer() {
	}

	/*
	 * Please note that you also have to change the application token in
	 * CloudClient.java if you switch the server.
	 * 
	 * - vitacloud.medisanaspace.com is the test server to which you can
	 * register yourself as developer during registration. Please note that this
	 * is an own server with no connection to cloud.vitadock.com.
	 * 
	 * - cloud.vitadock.com is the production server. After registration please
	 * contact us at cloud@vitadock.com to get upgraded to a developer account.
	 */

	public static final String EXTERNAL_AUTH_URL = "test-cloud.vitadock.com/auth"; //"cloud.vitadock.com/auth";
	public static final String EXTERNAL_LOGIN_URL = "test-cloud.vitadock.com"; //"cloud.vitadock.com";
	public static final String EXTERNAL_DATA_URL = "test-cloud.vitadock.com/data"; //"cloud.vitadock.com/data";

	public static final String HTTPS_AUTH_URL = "http://" + EXTERNAL_AUTH_URL;
	public static final String HTTPS_LOGIN_URL = "http://"
			+ EXTERNAL_LOGIN_URL;
	public static final String HTTPS_DATA_URL = "http://" + EXTERNAL_DATA_URL;
}
