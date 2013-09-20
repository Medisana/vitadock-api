package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.Glucodockglucose;
import com.medisanaspace.model.Glucodockinsulin;
import com.medisanaspace.model.Glucodockmeal;
import com.medisanaspace.model.fixture.GlucodockglucoseFixture;
import com.medisanaspace.model.fixture.GlucodockinsulinFixture;
import com.medisanaspace.model.fixture.GlucodockmealFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class GluckodockTestTask extends AbstractTestTask {

	public GluckodockTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		final List<Glucodockglucose> glucodockglucoseList = new ArrayList<Glucodockglucose>();
		final List<Glucodockinsulin> glucodockinsulinList = new ArrayList<Glucodockinsulin>();
		final List<Glucodockmeal> glucodockmealList = new ArrayList<Glucodockmeal>();
		String deviceToken = oauthData.getDeviceToken();
		String deviceSecret = oauthData.getDeviceSecret();
		String accessToken = oauthData.getAccessToken();
		String accessSecret = oauthData.getAccessSecret();
		
		printer.logMessage("Gluckodock test");
		
		// fill with "maxEntries" random entries
		int index = 0;
		for (int i = 0; i < numberOfEntries; i++) {
			index++;
			if (i == 0) {
				glucodockglucoseList.add(new GlucodockglucoseFixture(index,
						numberOfEntries, null).getGlucodockglucose());

				glucodockinsulinList.add(new GlucodockinsulinFixture(index,
						numberOfEntries).getGlucodockinsulin());

				glucodockmealList.add(new GlucodockmealFixture(index,
						numberOfEntries).getGlucodockmeal());

			} else {
				glucodockglucoseList.add(new GlucodockglucoseFixture(index,
						numberOfEntries, glucodockglucoseList
								.get(i - 1)).getGlucodockglucose());

				glucodockinsulinList.add(new GlucodockinsulinFixture(index,
						numberOfEntries).getGlucodockinsulin());

				glucodockmealList.add(new GlucodockmealFixture(index,
						numberOfEntries).getGlucodockmeal());
			}
		}

		String responseGlucodockglucose = saveJSONData(deviceToken,
				deviceSecret,
				Glucodockglucose.toJsonArray(glucodockglucoseList),
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
				accessSecret);
		String responseGlucodockinsulin = saveJSONData(deviceToken,
				deviceSecret,
				Glucodockinsulin.toJsonArray(glucodockinsulinList),
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
				accessSecret);
		String responseGlucodockmeal = saveJSONData(deviceToken, deviceSecret,
				Glucodockmeal.toJsonArray(glucodockmealList),
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
				accessSecret);

		// retrieve the ids from the response
		Collection<String> idGlucodockglucoseList = StringUtil
				.fromJsonArrayToStrings(responseGlucodockglucose);
		Collection<String> idGlucodockinsulinList = StringUtil
				.fromJsonArrayToStrings(responseGlucodockinsulin);
		Collection<String> idGlucodockmealList = StringUtil
				.fromJsonArrayToStrings(responseGlucodockmeal);

		// count data
		int countGlucodockglucose = countData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
				accessSecret);
		int countGlucodockinsulin = countData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
				accessSecret);
		int countGlucodockmeal = countData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
				accessSecret);

		this.printer.logMessage("Data count: " + countGlucodockglucose + ", "
				+ countGlucodockinsulin + ", " + countGlucodockmeal);

		// delete the data from the server
		deleteJSONData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
				accessSecret, idGlucodockglucoseList);
		deleteJSONData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
				accessSecret, idGlucodockinsulinList);
		deleteJSONData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
				accessSecret, idGlucodockmealList);

		// retrieve the data back from the server (note that the active flag is
		// set to zero if you uncomment the deletion process above)

		responseGlucodockglucose = syncData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
				accessSecret);
		responseGlucodockinsulin = syncData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
				accessSecret);
		responseGlucodockmeal = syncData(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
				accessSecret);

	}

}
