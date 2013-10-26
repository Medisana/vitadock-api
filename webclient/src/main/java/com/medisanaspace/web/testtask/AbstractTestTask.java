package com.medisanaspace.web.testtask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.printer.AbstractPrinter;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.main.CloudClient;
import com.medisanaspace.web.testconfig.OAuthData;
import com.medisanaspace.web.testconfig.ServerConfig;

/**
 * Abstract TestTask to derive tests from.
 * A TestTask needs the OAuthdata, ServerConfig and 
 * a printer from the TestRunner.
 * 
 * From every request, that is made to the server, the latency is
 * measured and saved in the latency List.
 * 
 * @author Jan Krause (c) Medisana Space Technologies GmbH, 2013
 * 
 * 
 * @version $Revision: 1.0 $
 */
public abstract class AbstractTestTask implements Runnable{

	private static final String ENCODING = "UTF-8";
	protected static final String AUTHORIZATION_STRING = "Authorization";

	protected ArrayList<String> latency = new ArrayList<String>();
	protected OAuthData oauthData;
	protected ServerConfig serverConfig;
	protected AbstractPrinter printer;
	protected final int numberOfEntries;
	
	public AbstractTestTask(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}

	/**
	 * Called by ExecutorService. Calls executeTask() and catches it's exceptions.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		try{
			latency.clear();
			executeTask();
		}catch(Exception e){
			CloudClient.printer.logError("Error in Test: "
					+ this.getClass().toString(), e);
		}
	}
	
	/**
	 * Main method of the task. Override with your test.
	 * @throws Exception
	 */
	protected void executeTask() throws Exception{
	}

	/**
	 * Count the number of entries for this module.
	 * 
	 * @param token
	 *            The application/device token
	 * @param secret
	 *            The application/device secret
	 * @param moduleId
	 *            The module id (0-5)
	 * @param accessToken
	 *            The access token
	 * @param accessSecret
	 *            The access secret	
	 * @return the number of entries for this module * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request. */
	protected int countData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {
		String dateSince = "0";
		String authorization = AuthorizationBuilder
				.createCountDataRequestAuthorizationHeader(dateSince,
						AuthorizationBuilder.createCountUrl(serverConfig,
								moduleId), token, secret, accessToken,
						accessSecret);
		HttpGet httpget = new HttpGet(AuthorizationBuilder.createCountUrl(
				serverConfig, moduleId));
		httpget.setHeader(AUTHORIZATION_STRING, authorization);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		CloudClient.printer.logPost(httpget);
		
		long curr = System.currentTimeMillis();
		HttpResponse response = httpClient.execute(httpget);
		int datacount = Integer.parseInt(IOUtils.toString(response.getEntity()
				.getContent(), ENCODING));
		latency.add(String.valueOf(System.currentTimeMillis() - curr));
		return datacount;
	}

	/**
	 * Copy data to the server.
	 * 
	 * @param token
	 *            The application/device token
	 * @param secret
	 *            The application/device secret
	 * @param jsonString
	 *            The string with the JSON data to submit
	 * @param moduleId
	 *            The module id (0-5)
	 * @param accessToken
	 *            The access token
	 * @param accessSecret
	 *            The access secret
	
	
	 * @return the server response (a JSON string with the generated ids if the
	 *         process was successful). * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request. */
	protected String saveJSONData(final String token, final String secret,
			final String jsonString, final int moduleId,
			final String accessToken, final String accessSecret)
			throws Exception {

		String authorization = AuthorizationBuilder
				.createSaveDataRequestAuthorizationHeader(AuthorizationBuilder
						.createRequestArrayUrl(serverConfig, moduleId),
						jsonString, token, secret, accessToken, accessSecret);
		HttpPost httppost = new HttpPost(
				AuthorizationBuilder.createRequestArrayUrl(serverConfig,
						moduleId));
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		httppost.setHeader("Content-Type", "application/json;charset=utf-8");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httppost.setEntity(new StringEntity(jsonString, ENCODING));

		CloudClient.printer.logPost(httppost);
		
		long curr = System.currentTimeMillis();
		HttpResponse response = httpClient.execute(httppost);
		String responseString = IOUtils.toString(response.getEntity()
				.getContent(), ENCODING);
		latency.add(String.valueOf(System.currentTimeMillis() - curr));
		CloudClient.printer.logJSONData(responseString);

		return responseString;

	}

	/**
	 * Delete data on the server.
	 * 
	 * @param token
	 *            The application/device token
	 * @param secret
	 *            The application/device secret
	 * @param moduleId
	 *            The module id (0-5)
	 * @param accessToken
	 *            The access token
	 * @param accessSecret
	 *            The access secret
	 * @param idList
	 *            A collection of ids to be deleted on the server
	
	 * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request. */
	protected void deleteJSONData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret, final Collection<String> idList)
			throws Exception {

		String parameterString = StringUtil.toJsonArray(idList);

		String authorization = AuthorizationBuilder
				.createSaveDataRequestAuthorizationHeader(AuthorizationBuilder
						.createDeleteArrayUrl(serverConfig, moduleId),
						parameterString, token, secret, accessToken,
						accessSecret);
		HttpPost httppost = new HttpPost(
				AuthorizationBuilder.createDeleteArrayUrl(serverConfig,
						moduleId));
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		httppost.setHeader("Content-Type", "application/json");

		DefaultHttpClient httpClient = new DefaultHttpClient();

		httppost.setEntity(new StringEntity(parameterString));

		CloudClient.printer.logPost(httppost);
		long curr = System.currentTimeMillis();
		HttpResponse response = httpClient.execute(httppost);
		String responseString = IOUtils.toString(response.getEntity()
				.getContent(), ENCODING);

		CloudClient.printer.logJSONData(responseString);
		
		latency.add(String.valueOf(System.currentTimeMillis() - curr));
	}

	/**
	 * Function to retrieve data since a certain date.
	 * 
	 * @param token
	 *            The application/device token
	 * @param secret
	 *            The application/device secret
	 * @param moduleId
	 *            The module id (0-5)
	 * @param accessToken
	 *            The access token
	 * @param accessSecret
	 *            The access secret
	 * @return the JSON data from the server (or an error message). 
	 * 
	 * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request. */
	protected String loadData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {
		int start = 1;
		int max = 10;
		String dateSince = "0";
		DefaultHttpClient httpClient = new DefaultHttpClient();

		String authorization = AuthorizationBuilder
				.createLoadDataRequestAuthorizationHeader(AuthorizationBuilder
						.createRequestUrl(serverConfig, moduleId), start, max,
						dateSince, token, secret, accessToken, accessSecret);
		String requestUrl = AuthorizationBuilder.createRequestUrl(serverConfig,
				moduleId, start, max, dateSince);
		HttpGet httpget = new HttpGet(requestUrl);
		httpget.setHeader(AUTHORIZATION_STRING, authorization);

		CloudClient.printer.logPost(httpget);
		long curr = System.currentTimeMillis();
		HttpResponse response = httpClient.execute(httpget);

		String data= IOUtils.toString(response.getEntity().getContent(), ENCODING);
		latency.add(String.valueOf(System.currentTimeMillis() - curr));
		
		return data;
	}

	/**
	 * Function to retrieve data since a certain date and output it through the
	 * printer.
	 * 
	 * @param token
	 *            The application/device token
	 * @param secret
	 *            The application/device secret
	 * @param moduleId
	 *            The module id (0-5)
	 * @param accessToken
	 *            The access token
	 * @param accessSecret
	 *            The access secret	
	 * @return the JSON data from the server (or an error message). * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request. */
	protected String syncData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {
		int start = -1;
		int max = 1;
		String dateSince = "0";
		DefaultHttpClient httpClient = new DefaultHttpClient();

		String authorization = AuthorizationBuilder
				.createLoadDataRequestAuthorizationHeader(AuthorizationBuilder
						.createSyncUrl(serverConfig, moduleId), start, max,
						dateSince, token, secret, accessToken, accessSecret);
		String requestUrl = AuthorizationBuilder.createSyncUrl(serverConfig,
				moduleId, start, max, dateSince);
		HttpGet httpget = new HttpGet(requestUrl);
		httpget.setHeader(AUTHORIZATION_STRING, authorization);
		
		CloudClient.printer.logPost(httpget);
		
		long curr = System.currentTimeMillis();
		HttpResponse response = httpClient.execute(httpget);


		InputStream testStream = response.getEntity().getContent();
		byte[] bytes = IOUtils.toByteArray(testStream);
		String responseString = new String(bytes, ENCODING);
		latency.add(String.valueOf(System.currentTimeMillis() - curr));
//
//		CloudClient.printer.logData(responseString);

		return responseString;
	}

	/**
	 * Delete all entries permanently on the server for this module and user
	 * (note: works only on the test server).
	 * 
	 * @param token
	 *            The application/device token
	 * @param secret
	 *            The application/device secret
	 * @param moduleId
	 *            The module id (0-5)
	 * @param accessToken
	 *            The access token
	 * @param accessSecret
	 *            The access secret	
	 * @return the number of deleted entries. * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request. */
	protected int deleteAllDataFromModule(final String token,
			final String secret, final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {

		String authorization = AuthorizationBuilder
				.createSaveDataRequestAuthorizationHeader(AuthorizationBuilder
						.createDeleteAllArrayUrl(serverConfig, moduleId), null,
						token, secret, accessToken, accessSecret);
		HttpPost httppost = new HttpPost(
				AuthorizationBuilder.createDeleteAllArrayUrl(serverConfig,
						moduleId));
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		CloudClient.printer.logPost(httppost);
		long curr = System.currentTimeMillis();
		HttpResponse response = httpClient.execute(httppost);
		String responseString = IOUtils.toString(response.getEntity()
				.getContent(), ENCODING);
		latency.add(String.valueOf(System.currentTimeMillis() - curr));
		int number = 0;
		try {
			number = Integer.parseInt(responseString);
		} catch (NumberFormatException e) {
			throw new Exception(response.getStatusLine().getReasonPhrase());
		}
		return number;
	}

	/**
	 * Method getOauthData.
	 * @return OAuthData
	 */
	public OAuthData getOauthData() {
		return oauthData;
	}

	/**
	 * Method setOauthData.
	 * @param oauthData OAuthData
	 */
	public void setOauthData(OAuthData oauthData) {
		this.oauthData = oauthData;
	}

	/**
	 * Method getServerConfig.
	 * @return ServerConfig
	 */
	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	/**
	 * Method setServerConfig.
	 * @param serverConfig ServerConfig
	 */
	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	/**
	 * Method getPrinter.
	 * @return PrinterInterface
	 */
	public AbstractPrinter getPrinter() {
		return printer;
	}

	/**
	 * Method setPrinter.
	 * @param printer PrinterInterface
	 */
	public void setPrinter(AbstractPrinter printer) {
		this.printer = printer;
	}

	/**
	 * Retrieve the latency array
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getLatency() {
		return latency;
	}

}
