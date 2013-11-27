package com.medisanaspace.web.testconfig;

import com.medisanaspace.model.User;
import com.medisanaspace.printer.AbstractPrinter;

/**
 * Configuration object for the TestRunner. Application Token, application
 * secret and server URLs are hard-coded and can be changed here. Parameters for
 * the configuration are:
 * 
 * user: When null a new user object will be created by random and registered on
 * the server.
 * 
 * mobile: select if the test should imitate a mobile device
 * 
 * numberOfThreads: choose how many parallel threads will be used; 1 for single
 * thread;
 * 
 * printer: printer to print the results and/or errors
 * 
 * - vitacloud.medisanaspace.com is the test server to which you can register
 * yourself as developer during registration. Please note that this is an own
 * server with no connection to cloud.vitadock.com.
 * 
 * - cloud.vitadock.com is the production server. After registration please
 * contact us at cloud@vitadock.com to get upgraded to a developer account.
 * 
 * 
 * @author Jan Krause (c) Medisana Space Technologies GmbH, 2013
 * @version $Revision: 1.0 $
 */
public final class TestRunnerConfig {

	private String APPLICATION_TOKEN;
	private String APPLICATION_SECRET;

	private String EXTERNAL_AUTH_URL;
	private String EXTERNAL_LOGIN_URL;
	private String EXTERNAL_DATA_URL;

	private User user = null;
	private boolean mobile;
	private int numberOfThreads = 1;
	private boolean createNewUserOnServer = false;
	
	private AbstractPrinter printer;
	private ServerConfig serverConfig;
	private OAuthData oauthData;

	/**
	 * 
	 * @param serverType
	 *            Choose either the TEST_SERVER, PRODUCTION_SERVER or LOCALHOST.
	 *            
	 * @param user
	 *            If set to null a random user will be created and registered on
	 *            the server.
	 * @param mobile
	 *            Select if the test should imitate a mobile device.
	 * @param numberOfThreads
	 *            Choose how many parallel threads will be used; 1 for single
	 *            thread;
	 * @param printer 
	 *            
	 */
	public TestRunnerConfig(ServerType serverType, User user, boolean createNewUser, boolean mobile,
			int numberOfThreads, AbstractPrinter printer) {
		serverConfig = new ServerConfig();
		this.printer = printer;
		this.setNumberOfThreads(numberOfThreads);
		this.createNewUserOnServer = createNewUser;
		oauthData = new OAuthData();
		switch (serverType) {
		case TEST_SERVER:
			serverConfig
					.setEXTERNAL_AUTH_URL("vitacloud.medisanaspace.com/auth");
			serverConfig.setEXTERNAL_LOGIN_URL("vitacloud.medisanaspace.com");
			serverConfig
					.setEXTERNAL_DATA_URL("vitacloud.medisanaspace.com/data");
			//local callback
			APPLICATION_TOKEN = "8DtxMWMDfWWh2e5TgxB9wfP0kenvDwtMM6MBFiK0pa6CSs1T8FcLpwzSRn1P0q4w";
			APPLICATION_SECRET = "Zb8zyVslrKBPqVySVNYAC8vsXjRJM9V3UwoTab7dvnA57EPevtT7nT6y5ySjzufx";
			//APPLICATION_TOKEN = "5bS2TiPfe6oRo5ihqwgDwwTmyGWZFyqvKGAmjUDayw1xS4vyVB9KJU9EC9lebxwV";
			//APPLICATION_SECRET = "F4MffyvaMAXJCMghbAjbry2wk66FgbKK9iTfh5WzntoaM1aYyev3ujyT1LSZbpfh";
			break;
		case PRODUCTION_SERVER:
			serverConfig.setEXTERNAL_AUTH_URL("cloud.vitadock.com/auth");
			serverConfig.setEXTERNAL_LOGIN_URL("cloud.vitadock.com");
			serverConfig.setEXTERNAL_DATA_URL("cloud.vitadock.com/data");
			// local callback
			APPLICATION_TOKEN = "DBdmbEoKtt7K31PpGodoEA7nMfM1X4MtX7yiKxNw8pKfCKEhM7zrr3WTej6yvVEJ";
			APPLICATION_SECRET = "NErqF1gdhSf9vY9pUAtSA3VvUCgVdD54Ey7jsjbrJGLiApf7w5WXqEJuVn7KX9Gx";
			break;
		case LOCALHOST:
			serverConfig.setEXTERNAL_AUTH_URL("localhost/auth");
			serverConfig.setEXTERNAL_LOGIN_URL("localhost");
			serverConfig.setEXTERNAL_DATA_URL("localhost/data");

			APPLICATION_TOKEN = "kpVxi8aRrPB9RphDLixM5uacLU99UZ2g8gEtiwWEfRr7BY99D9ifTmhnLmTLKbEM";
			APPLICATION_SECRET = "Pwb81Dc7lR4F6FWejDBmkNrLJfFxeXlc3GmFlBm41nJL9x5pDG0kGovdSdiZWPJc";
			break;
		}
		this.mobile = mobile;
		this.user = user;

	}

	/**
	 * Method setUser.
	 * 
	 * @param user
	 *            User
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Method getUser.
	 * 
	 * @return User
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Method getAPPLICATION_TOKEN.
	 * 
	 * @return String
	 */
	public String getAPPLICATION_TOKEN() {
		return APPLICATION_TOKEN;
	}

	/**
	 * Method getAPPLICATION_SECRET.
	 * 
	 * @return String
	 */
	public String getAPPLICATION_SECRET() {
		return APPLICATION_SECRET;
	}

	/**
	 * Method setAPPLICATION_TOKEN.
	 * 
	 * @param aPPLICATION_TOKEN
	 *            String
	 */
	public void setAPPLICATION_TOKEN(String aPPLICATION_TOKEN) {
		APPLICATION_TOKEN = aPPLICATION_TOKEN;
	}

	/**
	 * Method setAPPLICATION_SECRET.
	 * 
	 * @param aPPLICATION_SECRET
	 *            String
	 */
	public void setAPPLICATION_SECRET(String aPPLICATION_SECRET) {
		APPLICATION_SECRET = aPPLICATION_SECRET;
	}

	/**
	 * Method getEXTERNAL_AUTH_URL.
	 * 
	 * @return String
	 */
	public String getEXTERNAL_AUTH_URL() {
		return EXTERNAL_AUTH_URL;
	}

	/**
	 * Method getEXTERNAL_LOGIN_URL.
	 * 
	 * @return String
	 */
	public String getEXTERNAL_LOGIN_URL() {
		return EXTERNAL_LOGIN_URL;
	}

	/**
	 * Method getEXTERNAL_DATA_URL.
	 * 
	 * @return String
	 */
	public String getEXTERNAL_DATA_URL() {
		return EXTERNAL_DATA_URL;
	}

	/**
	 * Method getPrinter.
	 * 
	 * @return PrinterInterface
	 */
	public AbstractPrinter getPrinter() {
		return printer;
	}

	/**
	 * Method setPrinter.
	 * 
	 * @param printer
	 *            PrinterInterface
	 */
	public void setPrinter(AbstractPrinter printer) {
		this.printer = printer;
	}

	/**
	 * Method isMobile.
	 * 
	 * @return boolean
	 */
	public boolean isMobile() {
		return mobile;
	}

	/**
	 * Method getServerConfig.
	 * 
	 * @return ServerConfig
	 */
	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	/**
	 * Method getOauthData.
	 * 
	 * @return OAuthData
	 */
	public OAuthData getOauthData() {
		return oauthData;
	}

	/**
	 * Method setOauthData.
	 * 
	 * @param oauthData
	 *            OAuthData
	 */
	public void setOauthData(OAuthData oauthData) {
		this.oauthData = oauthData;
	}

	/**
	 * Method getHTTPS_AUTH_URL.
	 * 
	 * @return String
	 */
	public String getHTTPS_AUTH_URL() {
		return serverConfig.getHTTPS_AUTH_URL();
	}

	/**
	 * Method getHTTPS_LOGIN_URL.
	 * 
	 * @return String
	 */
	public String getHTTPS_LOGIN_URL() {
		return serverConfig.getHTTPS_LOGIN_URL();
	}

	/**
	 * Method getHTTPS_DATA_URL.
	 * 
	 * @return String
	 */
	public String getHTTPS_DATA_URL() {
		return serverConfig.getHTTPS_DATA_URL();
	}

	/**
	 * Method getNumberOfThreads.
	 * 
	 * @return int
	 */
	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	/**
	 * Choose how many parallel threads will be used; 1 for single
	 * thread;
	 * 
	 * @param numberOfThreads 
	 *            
	 */
	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public boolean isCreateNewUserOnServer() {
		return createNewUserOnServer;
	}

	public void setCreateNewUserOnServer(boolean createNewUserOnServer) {
		this.createNewUserOnServer = createNewUserOnServer;
	}

}
