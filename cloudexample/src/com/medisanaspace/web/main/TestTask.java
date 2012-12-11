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

public class TestTask {

	public static final int DELETE_TIME_ID = 0;
	public static final int SYNC_TIME_ID = 1;
	public static final int SAVE_TIME_ID = 2;

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
		List<Cardiodock> cardiodockList = new ArrayList<Cardiodock>();
		List<Glucodockglucose> glucodockglucoseList = new ArrayList<Glucodockglucose>();
		List<Glucodockinsulin> glucodockinsulinList = new ArrayList<Glucodockinsulin>();
		List<Glucodockmeal> glucodockmealList = new ArrayList<Glucodockmeal>();
		List<Targetscale> targetscaleList = new ArrayList<Targetscale>();
		List<Thermodock> thermodockList = new ArrayList<Thermodock>();

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
		/*
		 * int countCardiodock = countData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.CARDIODOCK_MODULE_ID, this.accessToken,
		 * this.accessSecret); int countGlucodockglucose =
		 * countData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, this.accessToken,
		 * this.accessSecret); int countGlucodockinsulin =
		 * countData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, this.accessToken,
		 * this.accessSecret); int countGlucodockmeal =
		 * countData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, this.accessToken,
		 * this.accessSecret); int countTargetscale =
		 * countData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.TARGETSCALE_MODULE_ID, this.accessToken,
		 * this.accessSecret); int countThermodock = countData(this.deviceToken,
		 * this.deviceSecret, AuthorizationBuilder.THERMODOCK_MODULE_ID,
		 * this.accessToken, this.accessSecret);
		 * 
		 * Collection<String> idCardiodockList = StringUtil
		 * .fromJsonArrayToStrings(responseCardiodock); Collection<String>
		 * idGlucodockglucoseList = StringUtil
		 * .fromJsonArrayToStrings(responseGlucodockglucose); Collection<String>
		 * idGlucodockinsulinList = StringUtil
		 * .fromJsonArrayToStrings(responseGlucodockinsulin); Collection<String>
		 * idGlucodockmealList = StringUtil
		 * .fromJsonArrayToStrings(responseGlucodockmeal); Collection<String>
		 * idTargetscaleList = StringUtil
		 * .fromJsonArrayToStrings(responseTargetscale); Collection<String>
		 * idThermodockList = StringUtil
		 * .fromJsonArrayToStrings(responseThermodock);
		 * 
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
		 * 
		 * responseCardiodock = syncData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.CARDIODOCK_MODULE_ID, this.accessToken,
		 * this.accessSecret);
		 * 
		 * responseGlucodockglucose = syncData(this.deviceToken,
		 * this.deviceSecret, AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID,
		 * this.accessToken, this.accessSecret);
		 * 
		 * responseGlucodockinsulin = syncData(this.deviceToken,
		 * this.deviceSecret, AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID,
		 * this.accessToken, this.accessSecret);
		 * 
		 * responseGlucodockmeal = syncData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, this.accessToken,
		 * this.accessSecret);
		 * 
		 * responseTargetscale = syncData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.TARGETSCALE_MODULE_ID, this.accessToken,
		 * this.accessSecret);
		 * 
		 * responseThermodock = syncData(this.deviceToken, this.deviceSecret,
		 * AuthorizationBuilder.THERMODOCK_MODULE_ID, this.accessToken,
		 * this.accessSecret);
		 */
	}

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
				.getContent(), "UTF-8"));
	}

	private static String saveRandomArrayJSONData(final String token,
			final String secret, final String parameterString,
			final int moduleId, final String accessToken,
			final String accessSecret) throws Exception {

		String authorization = AuthorizationBuilder
				.createSaveDataRequestAuthorizationHeader(
						AuthorizationBuilder.createRequestArrayUrl(moduleId),
						parameterString, token, secret, accessToken,
						accessSecret);
		HttpPost httppost = new HttpPost(
				AuthorizationBuilder.createRequestArrayUrl(moduleId));
		httppost.setHeader(AUTHORIZATION_STRING, authorization);
		httppost.setHeader("Content-Type", "application/json;charset=utf-8");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httppost.setEntity(new StringEntity(parameterString, "UTF-8"));

		HeaderPrinter.printPost(httppost);

		HttpResponse response = httpClient.execute(httppost);
		String responseString = IOUtils.toString(response.getEntity()
				.getContent(), "UTF-8");

		System.out.println("    Response: " + responseString
				+ "\n--------------");
		return responseString;

	}

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
		return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
	}

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
		String responseString = new String(bytes, "UTF-8");

		System.out.println("    Response: " + responseString
				+ "\n--------------");

		return responseString;
	}

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
				.getContent(), "UTF-8");
		int number = 0;
		try {
			number = Integer.parseInt(responseString);
		} catch (NumberFormatException e) {
			throw new Exception(response.getStatusLine().getReasonPhrase());
		}
		return number;
	}

}
