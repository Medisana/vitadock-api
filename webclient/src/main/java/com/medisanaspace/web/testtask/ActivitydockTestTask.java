package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.model.Activitydock;
import com.medisanaspace.model.fixture.ActivitydockFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class ActivitydockTestTask extends AbstractTestTask {

	public ActivitydockTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		printer.logMessage("Activitydoc Test");
		final List<Activitydock> activityList = new ArrayList<Activitydock>();

		for (int i = 0; i < numberOfEntries; i++) {
			activityList.add(new ActivitydockFixture().getActivity());
		}

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

		responseActivity = syncData(oauthData.getDeviceToken(),
				oauthData.getDeviceSecret(),
				AuthorizationBuilder.ACTIVITY_MODULE_ID,
				oauthData.getAccessToken(), oauthData.getAccessSecret());

		Collection<Activitydock> responseActivityList = Activitydock
				.fromJsonArrayToActivitys(responseActivity);

		ArrayList<String> idList = new ArrayList<String>();
		idList.add(activityList.get(activityList.size()-1).getId());
		
		this.deleteJSONData(oauthData.getDeviceToken(),
				oauthData.getDeviceSecret(),
				AuthorizationBuilder.ACTIVITY_MODULE_ID,
				oauthData.getAccessToken(), oauthData.getAccessSecret(),
				idList);

		// count data again
		int postCountActivity = countData(oauthData.getDeviceToken(),
				oauthData.getDeviceSecret(),
				AuthorizationBuilder.ACTIVITY_MODULE_ID,
				oauthData.getAccessToken(), oauthData.getAccessSecret());
		
		if (countActivity == postCountActivity){
			throw new Exception("Deletion of activity not successful!");
		}
		
		// clean up the rest
		this.deleteAllDataFromModule(oauthData.getDeviceToken(),
				oauthData.getDeviceSecret(),
				AuthorizationBuilder.ACTIVITY_MODULE_ID,
				oauthData.getAccessToken(), oauthData.getAccessSecret());
	}

}
