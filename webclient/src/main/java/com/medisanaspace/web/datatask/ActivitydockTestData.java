package com.medisanaspace.web.datatask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.Activitydock;
import com.medisanaspace.model.fixture.ActivitydockFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.testtask.AbstractTestTask;

/**
 */
public class ActivitydockTestData extends AbstractTestTask {

	public ActivitydockTestData(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		printer.startDataSet("Fill Activitydoc with "+numberOfEntries+" random entries");
		final List<Activitydock> activityList = new ArrayList<Activitydock>();

		for (int i = 0; i < numberOfEntries; i++) {
			activityList.add(new ActivitydockFixture().getActivity());
		}
		printer.logActivity("Writing data to the server");
		String responseActivity = saveJSONData(oauthData.getDeviceToken(),
				oauthData.getDeviceSecret(),
				Activitydock.toJsonArray(activityList),
				AuthorizationBuilder.ACTIVITY_MODULE_ID,
				oauthData.getAccessToken(), oauthData.getAccessSecret());

		int countActivity = countData(oauthData.getDeviceToken(),
				oauthData.getDeviceSecret(),
				AuthorizationBuilder.ACTIVITY_MODULE_ID,
				oauthData.getAccessToken(), oauthData.getAccessSecret());
		if (countActivity<=0){
			throw new Exception("Wrong data count!");
		}
		
		this.printer.logMessage("Data count " + countActivity);
		
	}

}
