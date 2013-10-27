package com.medisanaspace.web.datatask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.Glucodockglucose;
import com.medisanaspace.model.Glucodockinsulin;
import com.medisanaspace.model.Glucodockmeal;
import com.medisanaspace.model.fixture.GlucodockglucoseFixture;
import com.medisanaspace.model.fixture.GlucodockinsulinFixture;
import com.medisanaspace.model.fixture.GlucodockmealFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.testtask.AbstractTestTask;

/**
 */
public class GluckodockTestData extends AbstractTestTask {

	public GluckodockTestData(int numberOfEntries) {
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
		
		printer.startDataSet("Fill Gluckodock with "+numberOfEntries+" test data.");
		
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
		printer.logActivity("Writing glucose data to the server");
		String responseGlucodockglucose = saveJSONData(deviceToken,
				deviceSecret,
				Glucodockglucose.toJsonArray(glucodockglucoseList),
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
				accessSecret);
		
		printer.logActivity("Writing insulin data to the server");
		String responseGlucodockinsulin = saveJSONData(deviceToken,
				deviceSecret,
				Glucodockinsulin.toJsonArray(glucodockinsulinList),
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
				accessSecret);
		
		printer.logActivity("Writing meal data to the server");
		String responseGlucodockmeal = saveJSONData(deviceToken, deviceSecret,
				Glucodockmeal.toJsonArray(glucodockmealList),
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
				accessSecret);


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

	}

}
