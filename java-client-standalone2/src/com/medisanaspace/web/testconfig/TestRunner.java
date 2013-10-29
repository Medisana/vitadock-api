package com.medisanaspace.web.testconfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.medisanaspace.library.RandomHelper;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.main.CloudClient;
import com.medisanaspace.web.testtask.AbstractTestTask;
import com.medisanaspace.web.testtask.User;

/**
 * The TestRunner runs all the testTasks given to him. 
 * He also handles the the token and secret handshakes.
 * 
 * @author Jan Krause (c) Medisana Space Technologies GmbH, 2013
 * 
 * @version $Revision: 1.0 $
 */
public class TestRunner {

	private static final String AUTHORIZATION_STRING = "Authorization";

	private ArrayList<AbstractTestTask> testTasks = new ArrayList<AbstractTestTask>();
	private TestRunnerConfig testRunnerConfig;

	/**
	 * Constructor for TestRunner.
	 * @param testRunnerConfig TestRunnerConfig
	 * @throws Exception
	 */
	public TestRunner(TestRunnerConfig testRunnerConfig) throws Exception {
		this.testRunnerConfig = testRunnerConfig;

	}
	/**
	 * runTests runs the test with the given TestRunnerConfig
	 */
	public void runTests() {
		CloudClient.printer.startLog("Start with tests");
		try {
			OAuthData oAuthData = retrieveOAuthData();
			testRunnerConfig.setOauthData(oAuthData);
		} catch (Exception e) {
			CloudClient.printer.logError("Error when retrieving oAuth data: ",e);
		}

		ExecutorService executor = Executors
				.newFixedThreadPool(testRunnerConfig.getNumberOfThreads());
		// run tests
		for (AbstractTestTask task : testTasks) {
				task.setServerConfig(testRunnerConfig.getServerConfig());
				task.setOauthData(testRunnerConfig.getOauthData());
				task.setPrinter(testRunnerConfig.getPrinter());
				executor.execute(task);
				
		}
		
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			CloudClient.printer.logError("thread executor error",e);
			
		}

		CloudClient.printer.endLog("finished successfully.");
	}

	/**
	 * Method retrieveOAuthData.
	 * @return OAuthData
	 * @throws Exception
	 */
	private OAuthData retrieveOAuthData() throws Exception {
		OAuthData oauthData = new OAuthData();
		// temporary tokens
		String unauthorizedAccessToken = "";
		String unauthorizedAccessSecret = "";
		String verifierToken = "";

		HashMap<String, String> tokenAndSecret = getDeviceTokenAndDeviceSecret();
		oauthData.setDeviceToken(tokenAndSecret.get("token"));
		oauthData.setDeviceSecret(tokenAndSecret.get("secret"));

		tokenAndSecret = getUnauthorizedAccessTokenAndSecret(
				oauthData.getDeviceToken(), oauthData.getDeviceSecret());
		unauthorizedAccessToken = tokenAndSecret.get("token");
		unauthorizedAccessSecret = tokenAndSecret.get("secret");

		if (testRunnerConfig.getUser() == null) {
			verifierToken = createRandomUserAndGetVerifierToken(unauthorizedAccessToken);
		} else {

			/*
			 * If you have set up your application with a callback URL no user
			 * interaction is necessary here. But you have to provide the
			 * appropriate REST interface to accept the call and receive the
			 * verifier token directly.
			 */
			System.out
					.println("Please open your browser with the following URL: "
							+ testRunnerConfig.getHTTPS_LOGIN_URL()
							+ "/desiredaccessrights/request?oauth_token="
							+ unauthorizedAccessToken);
			/*
			 * alternatively call '
			 * VitaDockServer.HTTPS_LOGIN_URL+"/signup?oauth_token=" +
			 * oauthToken ' to redirect to the sign up page and continue from
			 * there
			 */

			// --- Wait For Verifier Token ---
			System.out.print("Please enter verifier token: ");
			verifierToken = readLine();
		}

		tokenAndSecret = getAccessTokenAndSecret(verifierToken,
				unauthorizedAccessToken, unauthorizedAccessSecret);
		oauthData.setAccessToken(tokenAndSecret.get("token"));
		oauthData.setAccessSecret(tokenAndSecret.get("secret"));
		return oauthData;
	}

	/**
	 * Method readLine.
	 * @return String
	 */
	protected static String readLine() {
		String string = "";
		try {
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			string = in.readLine();
		} catch (Exception e) {
			CloudClient.printer.logError("Error! Exception: ", e);
		}
		return string;
	}

	/**
	 * Get device token and device secret
	 * 
	 * @return HashMap<String,String>
	 * @throws Exception */
	protected HashMap<String, String> getDeviceTokenAndDeviceSecret()
			throws Exception {
		HashMap<String, String> tokenAndSecret = new HashMap<String, String>();
		// --- Register Device ---
		/*
		 * For mobile applications (desktop, smartphone etc.) only. This
		 * exchanges the application token with the device token.
		 */
		CloudClient.printer.logMessage("Get device token and device secret\n");
		if (testRunnerConfig.isMobile()) {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httppost = null;
			String str = null;
			BufferedReader bufferReader = null;
			HttpResponse response = null;
			final String uriDeviceRequest = testRunnerConfig
					.getHTTPS_AUTH_URL() + "/devices";
			httppost = new HttpPost(uriDeviceRequest);
			final String authorization = AuthorizationBuilder
					.createUnauthorizedAccessRequestAuthorizationHeader(
							uriDeviceRequest,
							testRunnerConfig.getAPPLICATION_TOKEN(),
							testRunnerConfig.getAPPLICATION_SECRET());
			httppost.setHeader(AUTHORIZATION_STRING, authorization);
			httppost.setHeader("device_id", "123ABC");
			CloudClient.printer.logPost(httppost);

			response = httpClient.execute(httppost);
			try {
				bufferReader = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				final StringBuffer strBuffer = new StringBuffer();
				while ((str = bufferReader.readLine()) != null) {
					strBuffer.append(str);
				}

				str = strBuffer.toString();

				CloudClient.printer.logData(str);

				if (str.split("&").length < 2) {
					throw new Exception(str);
				}
				tokenAndSecret.put("token", str.split("&")[0].split("=")[1]);
				tokenAndSecret.put("secret", str.split("&")[1].split("=")[1]);
			} finally {
				if (bufferReader != null) {
					bufferReader.close();
				}
				httpClient.getConnectionManager().shutdown();
			}

		} else {
			tokenAndSecret
					.put("token", testRunnerConfig.getAPPLICATION_TOKEN());
			tokenAndSecret.put("secret",
					testRunnerConfig.getAPPLICATION_SECRET());
			// testRunnerConfig.setDeviceToken(testRunnerConfig
			// .getAPPLICATION_TOKEN());
			// testRunnerConfig.setDeviceSecret(testRunnerConfig
			// .getAPPLICATION_SECRET());
		}
		return tokenAndSecret;
	}

	/**
	 * Method getUnauthorizedAccessTokenAndSecret.
	 * @param deviceToken String
	 * @param deviceSecret String
	 * @return HashMap<String,String>
	 * @throws Exception
	 */
	protected HashMap<String, String> getUnauthorizedAccessTokenAndSecret(
			String deviceToken, String deviceSecret) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = null;
		String str = null;
		BufferedReader bufferReader = null;
		HttpResponse response = null;
		HashMap<String, String> tokenAndSecret = new HashMap<String, String>();

		CloudClient.printer
				.logMessage("Get unauthorized access token and secret\n");

		httpClient = new DefaultHttpClient();
		String uriOauthRequest = testRunnerConfig.getHTTPS_AUTH_URL()
				+ "/unauthorizedaccesses";
		String authorization = AuthorizationBuilder
				.createUnauthorizedAccessRequestAuthorizationHeader(
						uriOauthRequest, deviceToken, deviceSecret);
		httppost = new HttpPost(uriOauthRequest);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		CloudClient.printer.logPost(httppost);

		response = httpClient.execute(httppost);

		try {
			bufferReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			final StringBuffer strBuffer = new StringBuffer();
			while ((str = bufferReader.readLine()) != null) {
				strBuffer.append(str);
			}

			str = strBuffer.toString();
			CloudClient.printer.logData(str);
			if (str.split("&").length < 2) {
				CloudClient.printer
						.logError("Invalid response from server when acquiring Unauthorized Access Token!");
				throw new Exception(str);
			}
			tokenAndSecret.put("token", str.split("&")[0].split("=")[1]);
			tokenAndSecret.put("secret", str.split("&")[1].split("=")[1]);

		} finally {
			if (bufferReader != null) {
				bufferReader.close();
			}
			httpClient.getConnectionManager().shutdown();
		}
		return tokenAndSecret;

	}

	/**
	 * Method getAccessTokenAndSecret.
	 * @param verifierToken String
	 * @param unauthorizedAccessToken String
	 * @param unauthorizedAccessSecret String
	 * @return HashMap<String,String>
	 * @throws Exception
	 */
	public HashMap<String, String> getAccessTokenAndSecret(
			String verifierToken, String unauthorizedAccessToken,
			String unauthorizedAccessSecret) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = null;
		BufferedReader bufferReader = null;
		HttpResponse response = null;
		HashMap<String, String> tokenAndSecret = new HashMap<String, String>();
		// --- Check Verifier Token ---

		CloudClient.printer.logMessage("Check verifier token\n");

		String uriOauthRequest = testRunnerConfig.getHTTPS_AUTH_URL()
				+ "/accesses/verify";
		httpClient = new DefaultHttpClient();
		bufferReader = null;
		try {
			String authorization = AuthorizationBuilder
					.createAccessRequestAuthorizationHeader(
							testRunnerConfig.getServerConfig(),
							testRunnerConfig.getAPPLICATION_TOKEN(),
							unauthorizedAccessToken, unauthorizedAccessSecret,
							verifierToken,
							testRunnerConfig.getAPPLICATION_SECRET());
			httppost = new HttpPost(uriOauthRequest);
			httppost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httppost.setHeader(AUTHORIZATION_STRING, authorization);
			CloudClient.printer.logPost(httppost);

			response = httpClient.execute(httppost);
			String str = null;
			bufferReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			try {
				while ((str = bufferReader.readLine()) != null) {
					CloudClient.printer.logData(str);
					tokenAndSecret
							.put("token", str.split("&")[0].split("=")[1]);
					tokenAndSecret.put("secret",
							str.split("&")[1].split("=")[1]);
					break;
				}
			} catch (Exception e) {
				CloudClient.printer.logError(
						"error while checking the verifier token: " + str, e);
				throw e;
			}
		} finally {
			if (bufferReader != null) {
				bufferReader.close();
			}
			httpClient.getConnectionManager().shutdown();
		}
		return tokenAndSecret;

	}

	/**
	 * Create a random user on the server. Also retrieve the verifier token.
	 * 
	 * @param unauthorizedAccessToken String
	 * @return String
	 * @throws Exception
	 */
	public String createRandomUserAndGetVerifierToken(
			String unauthorizedAccessToken) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = null;
		String str = null;
		BufferedReader bufferReader = null;
		HttpResponse response = null;
		String verifierToken = "";
		CloudClient.printer
				.logMessage("Create a random user and retrieve verifier token\n");
		// generate new test user
		testRunnerConfig.setUser(new User(RandomHelper.randomString(4) + "."
				+ RandomHelper.randomString(4), RandomHelper.randomString(10),
				"de_DE"));

		httpClient = new DefaultHttpClient();
		String uriOauthRequest = testRunnerConfig.getHTTPS_LOGIN_URL()
				+ "/desiredaccessrights/request?oauth_token="
				+ unauthorizedAccessToken;

		httppost = new HttpPost(uriOauthRequest);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader("email", testRunnerConfig.getUser().getEmail());
		httppost.setHeader("password", testRunnerConfig.getUser().getPassword());
		httppost.setHeader("locale", testRunnerConfig.getUser().getLocale());
		CloudClient.printer.logPost(httppost);

		response = httpClient.execute(httppost);

		try {
			bufferReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			final StringBuffer strBuffer = new StringBuffer();
			while ((str = bufferReader.readLine()) != null) {
				strBuffer.append(str);
			}

			str = strBuffer.toString();
			CloudClient.printer.logData(str);

			if (str.equals("") || str.split("&").length < 1) {
				CloudClient.printer.logError("Error when creating a new user!");
				throw new Exception(str);
			} else {
				verifierToken = str.split("&")[0].split("=")[1];
			}
		} finally {
			if (bufferReader != null) {
				bufferReader.close();
			}
			httpClient.getConnectionManager().shutdown();
		}
		return verifierToken;

	}



	/**
	 * Method setTestTasks.
	 * @param testTasks ArrayList<AbstractTestTask>
	 */
	public void setTestTasks(ArrayList<AbstractTestTask> testTasks) {
		this.testTasks = testTasks;
	}

}