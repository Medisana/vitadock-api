package com.medisanaspace.web.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.library.HeaderPrinter;
import com.medisanaspace.web.library.VitaDockServer;

/**
 * Main class for the test program, requests security tokens for the task
 * (TestTask) that does the actual data transfer.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public class CloudClient {
	private static final String AUTHORIZATION_STRING = "Authorization";

	private static final int MAX_TASKS = 1;
	private static final int MAX_ENTRIES = 2;

	/*
	 * These are test applications on the test server and productive server
	 * (localhost only works if deployed on the test server, you can ignore that
	 * one). Note to also switch the server URLs (see VitaDockServer.java).
	 */
	private static final String APPLICATION_TOKEN = "5bS2TiPfe6oRo5ihqwgDwwTmyGWZFyqvKGAmjUDayw1xS4vyVB9KJU9EC9lebxwV";
	// cloud.vitadock.com
	// "wqR6Tu245t1VVPViJTJGvcf2AkW3G06niYsn655AG3umZS3s6E6fAXvSkiEhrYTm"
	// localhost
	// "kpVxi8aRrPB9RphDLixM5uacLU99UZ2g8gEtiwWEfRr7BY99D9ifTmhnLmTLKbEM";
	// vitacloud.medisanaspace.com
	// "5bS2TiPfe6oRo5ihqwgDwwTmyGWZFyqvKGAmjUDayw1xS4vyVB9KJU9EC9lebxwV";
	private static final String APPLICATION_SECRET = "F4MffyvaMAXJCMghbAjbry2wk66FgbKK9iTfh5WzntoaM1aYyev3ujyT1LSZbpfh";

	// cloud.vitadock.com
	// "WSc3hplyunPa4SgLncJFKthZWZTdsJy4uZFXEgJ308GCnZq3eY1xGeJVJWUePGhp"
	// localhost
	// "Pwb81Dc7lR4F6FWejDBmkNrLJfFxeXlc3GmFlBm41nJL9x5pDG0kGovdSdiZWPJc";
	// vitacloud.medisanaspace.com
	// "F4MffyvaMAXJCMghbAjbry2wk66FgbKK9iTfh5WzntoaM1aYyev3ujyT1LSZbpfh";

	public static void main(String[] args) throws Exception {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = null;
		String str = null;
		BufferedReader bufferReader = null;
		HttpResponse response = null;

		String oauthToken = "";
		String oauthSecret = "";
		String accessToken = "";
		String accessSecret = "";
		String deviceToken = "";
		String deviceSecret = "";

		// --- Register Device ---

		/*
		 * For mobile applications (desktop, smartphone etc.) only. This
		 * exchanges the application token with the device token.
		 */

		final boolean mobile = false;

		if (mobile) {
			final String uriDeviceRequest = VitaDockServer.HTTPS_AUTH_URL
					+ "/devices";
			httppost = new HttpPost(uriDeviceRequest);
			final String authorization = AuthorizationBuilder
					.createUnauthorizedAccessRequestAuthorizationHeader(
							uriDeviceRequest, APPLICATION_TOKEN,
							APPLICATION_SECRET);
			httppost.setHeader(AUTHORIZATION_STRING, authorization);
			httppost.setHeader("device_id", "123ABC");
			HeaderPrinter.printPost(httppost);

			response = httpClient.execute(httppost);
			try {
				bufferReader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				final StringBuffer strBuffer = new StringBuffer();
				while ((str = bufferReader.readLine()) != null) {
					strBuffer.append(str);
				}

				str = strBuffer.toString();
				System.out.println("    Response: " + str + "\n--------------");
				if (str.split("&").length < 2) {
					throw new Exception(str);
				}
				deviceToken = str.split("&")[0].split("=")[1];
				deviceSecret = str.split("&")[1].split("=")[1];
			} finally {
				if (bufferReader != null) {
					bufferReader.close();
				}
				httpClient.getConnectionManager().shutdown();
			}

		} else {
			deviceToken = APPLICATION_TOKEN;
			deviceSecret = APPLICATION_SECRET;
		}

		// --- Acquire Unauthorized Access Token ---
		httpClient = new DefaultHttpClient();
		String uriOauthRequest = VitaDockServer.HTTPS_AUTH_URL
				+ "/unauthorizedaccesses";
		String authorization = AuthorizationBuilder
				.createUnauthorizedAccessRequestAuthorizationHeader(
						uriOauthRequest, deviceToken, deviceSecret);
		httppost = new HttpPost(uriOauthRequest);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		HeaderPrinter.printPost(httppost);

		response = httpClient.execute(httppost);

		try {
			bufferReader = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			final StringBuffer strBuffer = new StringBuffer();
			while ((str = bufferReader.readLine()) != null) {
				strBuffer.append(str);
			}

			str = strBuffer.toString();
			System.out.println("    Response: " + str + "\n--------------");
			if (str.split("&").length < 2) {
				throw new Exception(str);
			}
			oauthToken = str.split("&")[0].split("=")[1];
			oauthSecret = str.split("&")[1].split("=")[1];
		} finally {
			if (bufferReader != null) {
				bufferReader.close();
			}
			httpClient.getConnectionManager().shutdown();
		}

		System.out.println("Please open your browser with the following URL: "
				+ VitaDockServer.HTTPS_LOGIN_URL
				+ "/desiredaccessrights/request?oauth_token=" + oauthToken);
		/*
		 * alternatively call '
		 * VitaDockServer.HTTPS_LOGIN_URL+"/signup?oauth_token=" + oauthToken '
		 * to redirect to the sign up page and continue from there
		 */

		// --- Wait For Verifier Token ---
		System.out.print("Please enter verifier token: ");
		String verifierToken = readLine();

		/*
		 * If you have set up your application with a callback URL no user
		 * interaction is necessary here. But you have to provide the
		 * appropriate REST interface to accept the call and receive the
		 * verifier token directly.
		 */

		// --- Check Verifier Token ---
		uriOauthRequest = VitaDockServer.HTTPS_AUTH_URL + "/accesses/verify";
		httpClient = new DefaultHttpClient();
		bufferReader = null;
		try {
			authorization = AuthorizationBuilder
					.createAccessRequestAuthorizationHeader(deviceToken,
							deviceSecret, oauthToken, oauthSecret,
							verifierToken);
			httppost = new HttpPost(uriOauthRequest);
			httppost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httppost.setHeader(AUTHORIZATION_STRING, authorization);
			HeaderPrinter.printPost(httppost);

			response = httpClient.execute(httppost);
			str = null;
			bufferReader = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			try {
				while ((str = bufferReader.readLine()) != null) {
					System.out.println("    Response: " + str
							+ "\n--------------");
					accessToken = str.split("&")[0].split("=")[1];
					accessSecret = str.split("&")[1].split("=")[1];
					break;
				}
			} catch (Exception e) {
				System.out.println(str);
				throw e;
			}
		} finally {
			if (bufferReader != null) {
				bufferReader.close();
			}
			httpClient.getConnectionManager().shutdown();
		}

		// --- Execute deletion of all data (note: works only for the test server) ---
		/**
		 * TestTask.deleteAllData(deviceToken, deviceSecret,
		 * AuthorizationBuilder.CARDIODOCK_MODULE_ID, accessToken,
		 * accessSecret); TestTask.deleteAllData(deviceToken, deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
		 * accessSecret); TestTask.deleteAllData(deviceToken, deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
		 * accessSecret); TestTask.deleteAllData(deviceToken, deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
		 * accessSecret); TestTask.deleteAllData(deviceToken, deviceSecret,
		 * AuthorizationBuilder.TARGETSCALE_MODULE_ID, accessToken,
		 * accessSecret); TestTask.deleteAllData(deviceToken, deviceSecret,
		 * AuthorizationBuilder.THERMODOCK_MODULE_ID, accessToken,
		 * accessSecret);
		 */

		// --- Start test tasks ---
		for (int i = 0; i < MAX_TASKS; i++) {
			TestTask testTask = new TestTask(MAX_ENTRIES, deviceToken,
					deviceSecret, accessToken, accessSecret);
			testTask.run();
		}
	}

	private static String readLine() {
		String string = "";
		try {
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			string = in.readLine();
		} catch (Exception e) {
			System.out.println("Error! Exception: " + e);
		}
		return string;
	}
}
