package com.medisanaspace.web.main;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.medisanaspace.library.RandomHelper;
import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.Cardiodock;
import com.medisanaspace.model.Glucodockglucose;
import com.medisanaspace.model.Glucodockinsulin;
import com.medisanaspace.model.Glucodockmeal;
import com.medisanaspace.model.Targetscale;
import com.medisanaspace.model.Thermodock;
import com.medisanaspace.model.fixture.CardiodockFixture;
import com.medisanaspace.model.fixture.GlucodockglucoseFixture;
import com.medisanaspace.model.fixture.GlucodockinsulinFixture;
import com.medisanaspace.model.fixture.GlucodockmealFixture;
import com.medisanaspace.model.fixture.TargetscaleFixture;
import com.medisanaspace.model.fixture.ThermodockFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.library.HeaderPrinter;

/**
 * Worker task that uploads, retrieves and deletes data on the server.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public class TestTask {

	public static final int DELETE_TIME_ID = 0;
	public static final int SYNC_TIME_ID = 1;
	public static final int SAVE_TIME_ID = 2;

	private static final String ENCODING = "UTF-8";

	private long maxEntries = 100;
	private static final String AUTHORIZATION_STRING = "Authorization";

	private String deviceToken = null;
	private String deviceSecret = null;
	private String accessToken = null;
	private String accessSecret = null;

	public TestTask(final long maxEntries, final String deviceToken,
			final String deviceSecret, final String accessToken,
			final String accessSecret) {
		this.maxEntries = maxEntries;
		this.deviceToken = deviceToken;
		this.deviceSecret = deviceSecret;
		this.accessToken = accessToken;
		this.accessSecret = accessSecret;
	}

	public void run() throws Exception {
		final List<Cardiodock> cardiodockList = new ArrayList<Cardiodock>();
		final List<Glucodockglucose> glucodockglucoseList = new ArrayList<Glucodockglucose>();
		final List<Glucodockinsulin> glucodockinsulinList = new ArrayList<Glucodockinsulin>();
		final List<Glucodockmeal> glucodockmealList = new ArrayList<Glucodockmeal>();
		final List<Targetscale> targetscaleList = new ArrayList<Targetscale>();
		final List<Thermodock> thermodockList = new ArrayList<Thermodock>();

		// fill the lists with generated random entries
		int index = 0;
		for (int i = 0; i < this.maxEntries; i++) {
			if (RandomHelper.generateBoolean()
					&& RandomHelper.generateBoolean()) {
				index++;
			}
			index++;
			if (i == 0) {
				cardiodockList.add(new CardiodockFixture(index, null)
						.getCardiodock());
			} else {
				cardiodockList.add(new CardiodockFixture(index, cardiodockList
						.get(i - 1)).getCardiodock());
			}
		}
		index = 0;
		for (int i = 0; i < this.maxEntries; i++) {
			if (RandomHelper.generateBoolean()
					&& RandomHelper.generateBoolean()) {
				index++;
			}
			index++;
			if (i == 0) {
				glucodockglucoseList.add(new GlucodockglucoseFixture(index,
						null).getGlucodockglucose());
			} else {
				glucodockglucoseList.add(new GlucodockglucoseFixture(index,
						glucodockglucoseList.get(i - 1)).getGlucodockglucose());
			}
		}
		index = 0;
		for (int i = 0; i < this.maxEntries; i++) {
			if (RandomHelper.generateBoolean()
					&& RandomHelper.generateBoolean()) {
				index++;
			}
			index++;
			if (i == 0) {
				glucodockinsulinList.add(new GlucodockinsulinFixture(index)
						.getGlucodockinsulin());
			} else {
				glucodockinsulinList.add(new GlucodockinsulinFixture(index)
						.getGlucodockinsulin());
			}
		}
		index = 0;
		for (int i = 0; i < this.maxEntries; i++) {
			if (RandomHelper.generateBoolean()
					&& RandomHelper.generateBoolean()) {
				index++;
			}
			index++;
			if (i == 0) {
				glucodockmealList.add(new GlucodockmealFixture(index)
						.getGlucodockmeal());
			} else {
				glucodockmealList.add(new GlucodockmealFixture(index)
						.getGlucodockmeal());
			}
		}
		index = 0;
		for (int i = 0; i < this.maxEntries; i++) {
			if (RandomHelper.generateBoolean()
					&& RandomHelper.generateBoolean()) {
				index++;
			}
			index++;
			if (i == 0) {
				targetscaleList.add(new TargetscaleFixture(index, null)
						.getTargetscale());
			} else {
				targetscaleList.add(new TargetscaleFixture(index,
						targetscaleList.get(i - 1)).getTargetscale());
			}
		}
		index = 0;
		for (int i = 0; i < this.maxEntries; i++) {
			if (RandomHelper.generateBoolean()
					&& RandomHelper.generateBoolean()) {
				index++;
			}
			index++;
			if (i == 0) {
				thermodockList.add(new ThermodockFixture(index, null)
						.getThermodock());
			} else {
				thermodockList.add(new ThermodockFixture(index, thermodockList
						.get(i - 1)).getThermodock());
			}
		}

		// upload generated data to the server
		String responseCardiodock = saveRandomArrayJSONData(this.deviceToken,
				this.deviceSecret, Cardiodock.toJsonArray(cardiodockList),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, this.accessToken,
				this.accessSecret);
		String responseGlucodockglucose = saveRandomArrayJSONData(
				this.deviceToken, this.deviceSecret,
				Glucodockglucose.toJsonArray(glucodockglucoseList),
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID,
				this.accessToken, this.accessSecret);
		String responseGlucodockinsulin = saveRandomArrayJSONData(
				this.deviceToken, this.deviceSecret,
				Glucodockinsulin.toJsonArray(glucodockinsulinList),
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID,
				this.accessToken, this.accessSecret);
		String responseGlucodockmeal = saveRandomArrayJSONData(
				this.deviceToken, this.deviceSecret,
				Glucodockmeal.toJsonArray(glucodockmealList),
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID,
				this.accessToken, this.accessSecret);
		String responseTargetscale = saveRandomArrayJSONData(this.deviceToken,
				this.deviceSecret, Targetscale.toJsonArray(targetscaleList),
				AuthorizationBuilder.TARGETSCALE_MODULE_ID, this.accessToken,
				this.accessSecret);
		String responseThermodock = saveRandomArrayJSONData(this.deviceToken,
				this.deviceSecret, Thermodock.toJsonArray(thermodockList),
				AuthorizationBuilder.THERMODOCK_MODULE_ID, this.accessToken,
				this.accessSecret);

		// retrieve the ids from the response
		Collection<String> idCardiodockList = StringUtil
				.fromJsonArrayToStrings(responseCardiodock);
		Collection<String> idGlucodockglucoseList = StringUtil
				.fromJsonArrayToStrings(responseGlucodockglucose);
		Collection<String> idGlucodockinsulinList = StringUtil
				.fromJsonArrayToStrings(responseGlucodockinsulin);
		Collection<String> idGlucodockmealList = StringUtil
				.fromJsonArrayToStrings(responseGlucodockmeal);
		Collection<String> idTargetscaleList = StringUtil
				.fromJsonArrayToStrings(responseTargetscale);
		Collection<String> idThermodockList = StringUtil
				.fromJsonArrayToStrings(responseThermodock);

		// count the data
		int countCardiodock = countData(this.deviceToken, this.deviceSecret,
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, this.accessToken,
				this.accessSecret);
		int countGlucodockglucose = countData(this.deviceToken,
				this.deviceSecret,
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID,
				this.accessToken, this.accessSecret);
		int countGlucodockinsulin = countData(this.deviceToken,
				this.deviceSecret,
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID,
				this.accessToken, this.accessSecret);
		int countGlucodockmeal = countData(this.deviceToken, this.deviceSecret,
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID,
				this.accessToken, this.accessSecret);
		int countTargetscale = countData(this.deviceToken, this.deviceSecret,
				AuthorizationBuilder.TARGETSCALE_MODULE_ID, this.accessToken,
				this.accessSecret);
		int countThermodock = countData(this.deviceToken, this.deviceSecret,
				AuthorizationBuilder.THERMODOCK_MODULE_ID, this.accessToken,
				this.accessSecret);

		// delete the data from the server
		/*
		 * deleteJSONData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.CARDIODOCK_MODULE_ID, this.accessToken,
		 * this.accessSecret, idCardiodockList);
		 * deleteJSONData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, this.accessToken,
		 * this.accessSecret, idGlucodockglucoseList);
		 * deleteJSONData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, this.accessToken,
		 * this.accessSecret, idGlucodockinsulinList);
		 * deleteJSONData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, this.accessToken,
		 * this.accessSecret, idGlucodockmealList);
		 * deleteJSONData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.TARGETSCALE_MODULE_ID, this.accessToken,
		 * this.accessSecret, idTargetscaleList);
		 * deleteJSONData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.THERMODOCK_MODULE_ID, this.accessToken,
		 * this.accessSecret, idThermodockList);
		 */

		// retrieve the data back from the server (note that the active flag is
		// set to zero if you uncomment the deletion process above)
		responseCardiodock = syncData(this.deviceToken, this.deviceSecret,
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, this.accessToken,
				this.accessSecret);
		responseGlucodockglucose = syncData(this.deviceToken,
				this.deviceSecret,
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID,
				this.accessToken, this.accessSecret);
		responseGlucodockinsulin = syncData(this.deviceToken,
				this.deviceSecret,
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID,
				this.accessToken, this.accessSecret);
		responseGlucodockmeal = syncData(this.deviceToken, this.deviceSecret,
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID,
				this.accessToken, this.accessSecret);
		responseTargetscale = syncData(this.deviceToken, this.deviceSecret,
				AuthorizationBuilder.TARGETSCALE_MODULE_ID, this.accessToken,
				this.accessSecret);
		responseThermodock = syncData(this.deviceToken, this.deviceSecret,
				AuthorizationBuilder.THERMODOCK_MODULE_ID, this.accessToken,
				this.accessSecret);
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
	 * @return the number of entries for this module
	 * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request.
	 */
	private static int countData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {
		String dateSince = "0";
		String authorization = AuthorizationBuilder
				.createCountDataRequestAuthorizationHeader(dateSince,
						AuthorizationBuilder.createCountUrl(moduleId), token,
						secret, accessToken, accessSecret);
		HttpGet httpget = new HttpGet(
				AuthorizationBuilder.createCountUrl(moduleId));
		httpget.setHeader(AUTHORIZATION_STRING, authorization);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HeaderPrinter.printPost(httpget);

		HttpResponse response = httpClient.execute(httpget);
		return Integer.parseInt(IOUtils.toString(response.getEntity()
				.getContent(), ENCODING));
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
	 *         process was successful).
	 * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request.
	 */
	private static String saveRandomArrayJSONData(final String token,
			final String secret, final String jsonString, final int moduleId,
			final String accessToken, final String accessSecret)
			throws Exception {

		String authorization = AuthorizationBuilder
				.createSaveDataRequestAuthorizationHeader(
						AuthorizationBuilder.createRequestArrayUrl(moduleId),
						jsonString, token, secret, accessToken, accessSecret);
		HttpPost httppost = new HttpPost(
				AuthorizationBuilder.createRequestArrayUrl(moduleId));
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		httppost.setHeader("Content-Type", "application/json;charset=utf-8");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httppost.setEntity(new StringEntity(jsonString, ENCODING));

		HeaderPrinter.printPost(httppost);

		HttpResponse response = httpClient.execute(httppost);
		String responseString = IOUtils.toString(response.getEntity()
				.getContent(), ENCODING);

		System.out.println("    Response: " + responseString
				+ "\n--------------");
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
	 *             constructing the request.
	 */
	private static void deleteJSONData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret, final Collection<String> idList)
			throws Exception {

		String parameterString = StringUtil.toJsonArray(idList);

		String authorization = AuthorizationBuilder
				.createSaveDataRequestAuthorizationHeader(
						AuthorizationBuilder.createDeleteArrayUrl(moduleId),
						parameterString, token, secret, accessToken,
						accessSecret);
		HttpPost httppost = new HttpPost(
				AuthorizationBuilder.createDeleteArrayUrl(moduleId));
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		httppost.setHeader("Content-Type", "application/json");

		DefaultHttpClient httpClient = new DefaultHttpClient();

		httppost.setEntity(new StringEntity(parameterString));

		HeaderPrinter.printPost(httppost);
		HttpResponse response = httpClient.execute(httppost);
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
	 * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request.
	 */
	private static String loadData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {
		int start = 1;
		int max = 10;
		String dateSince = "0";
		DefaultHttpClient httpClient = new DefaultHttpClient();

		String authorization = AuthorizationBuilder
				.createLoadDataRequestAuthorizationHeader(
						AuthorizationBuilder.createRequestUrl(moduleId), start,
						max, dateSince, token, secret, accessToken,
						accessSecret);
		String requestUrl = AuthorizationBuilder.createRequestUrl(moduleId,
				start, max, dateSince);
		HttpGet httpget = new HttpGet(requestUrl);
		httpget.setHeader(AUTHORIZATION_STRING, authorization);

		HeaderPrinter.printPost(httpget);
		HttpResponse response = httpClient.execute(httpget);
		return IOUtils.toString(response.getEntity().getContent(), ENCODING);
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
	 * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request.
	 */
	private static String syncData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {
		int start = -1;
		int max = 100;
		String dateSince = "0";
		DefaultHttpClient httpClient = new DefaultHttpClient();

		String authorization = AuthorizationBuilder
				.createLoadDataRequestAuthorizationHeader(
						AuthorizationBuilder.createSyncUrl(moduleId), start,
						max, dateSince, token, secret, accessToken,
						accessSecret);
		String requestUrl = AuthorizationBuilder.createSyncUrl(moduleId, start,
				max, dateSince);
		HttpGet httpget = new HttpGet(requestUrl);
		httpget.setHeader(AUTHORIZATION_STRING, authorization);
		HeaderPrinter.printPost(httpget);

		HttpResponse response = httpClient.execute(httpget);
		InputStream testStream = response.getEntity().getContent();
		byte[] bytes = IOUtils.toByteArray(testStream);
		String responseString = new String(bytes, ENCODING);

		System.out.println("    Response: " + responseString
				+ "\n--------------");

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
	 * @return the number of deleted entries.
	 * @throws Exception
	 *             if there was an error communicating with the server or
	 *             constructing the request.
	 */
	public static int deleteAllData(final String token, final String secret,
			final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {

		String authorization = AuthorizationBuilder
				.createSaveDataRequestAuthorizationHeader(
						AuthorizationBuilder.createDeleteAllArrayUrl(moduleId),
						null, token, secret, accessToken, accessSecret);
		HttpPost httppost = new HttpPost(
				AuthorizationBuilder.createDeleteAllArrayUrl(moduleId));
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HeaderPrinter.printPost(httppost);

		HttpResponse response = httpClient.execute(httppost);
		String responseString = IOUtils.toString(response.getEntity()
				.getContent(), ENCODING);
		int number = 0;
		try {
			number = Integer.parseInt(responseString);
		} catch (NumberFormatException e) {
			throw new Exception(response.getStatusLine().getReasonPhrase());
		}
		return number;
	}

}
