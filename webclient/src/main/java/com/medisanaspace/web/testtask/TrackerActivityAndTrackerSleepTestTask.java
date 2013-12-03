package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.TrackerActivity;
import com.medisanaspace.model.TrackerSleep;
import com.medisanaspace.model.fixture.TrackerActivityFixture;
import com.medisanaspace.model.fixture.TrackerSleepFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class TrackerActivityAndTrackerSleepTestTask extends AbstractTestTask {

	public TrackerActivityAndTrackerSleepTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {

		final List<TrackerActivity> trackerActivityList = new ArrayList<TrackerActivity>();
		final List<TrackerSleep> trackerSleepList = new ArrayList<TrackerSleep>();

		for (int i = 0; i < numberOfEntries; i++) {
			TrackerActivity currentActivity = new TrackerActivityFixture(i,
					numberOfEntries).getTrackerActivity();
			trackerActivityList.add(currentActivity);
			trackerSleepList.add(new TrackerSleepFixture(currentActivity)
					.getTrackerSleep());
		}
		printer.startDataSet("Tracker activity test.");

		StandardCRUDTestTask crudtest = new StandardCRUDTestTask(
				numberOfEntries,
				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				TrackerActivity.toJsonArray(trackerActivityList));

		crudtest.setPrinter(printer);
		crudtest.setServerConfig(this.getServerConfig());
		crudtest.setOauthData(this.getOauthData());
		crudtest.executeTask();

		printer.startDataSet("Tracker sleep test.");

		crudtest = new StandardCRUDTestTask(numberOfEntries,
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				TrackerSleep.toJsonArray(trackerSleepList));

		crudtest.setPrinter(printer);
		crudtest.setServerConfig(this.getServerConfig());
		crudtest.setOauthData(this.getOauthData());
		crudtest.executeTask();

		// printer.logActivity("Counting data before the test.");
		//
		// int preTestcountTrackerActivity =
		// countData(this.oauthData.getDeviceToken(),
		// this.oauthData.getDeviceSecret(),
		// AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
		// this.oauthData.getAccessToken(),
		// this.oauthData.getAccessSecret());
		//
		// int preTestcountTrackerSleep =
		// countData(this.oauthData.getDeviceToken(),
		// this.oauthData.getDeviceSecret(),
		// AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
		// this.oauthData.getAccessToken(),
		// this.oauthData.getAccessSecret());
		//
		//
		// printer.logActivity("Saving "+numberOfEntries+" tracker activity test data on the server.");
		// String responseTrackerActivity = saveJSONData(
		// this.oauthData.getDeviceToken(),
		// this.oauthData.getDeviceSecret(),
		// TrackerActivity.toJsonArray(trackerActivityList),
		// AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
		// this.oauthData.getAccessToken(),
		// this.oauthData.getAccessSecret());
		//
		// printer.logActivity("Saving "+numberOfEntries+" tracker sleep test data on the server.");
		// String responseTrackerSleep = saveJSONData(
		// this.oauthData.getDeviceToken(),
		// this.oauthData.getDeviceSecret(),
		// TrackerSleep.toJsonArray(trackerSleepList),
		// AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
		// this.oauthData.getAccessToken(),
		// this.oauthData.getAccessSecret());
		//
		//
		// Collection<String> idTrackerActivityList = StringUtil
		// .fromJsonArrayToStrings(responseTrackerActivity);
		// Collection<String> idTrackerSleepList = StringUtil
		// .fromJsonArrayToStrings(responseTrackerSleep);
		//
		// if(idTrackerActivityList.size()-preTestcountTrackerActivity!=numberOfEntries){
		// printer.logError("Data count after writing to the server is wrong: "+idTrackerActivityList.size());
		// throw new Exception("Wrong data count after writing to the server!");
		// }
		//
		// if(idTrackerSleepList.size()-preTestcountTrackerSleep!=numberOfEntries){
		// printer.logError("Data count after writing to the server is wrong: "+idTrackerSleepList.size());
		// throw new Exception("Wrong data count after writing to the server!");
		// }
		//
		// // String data = loadData(this.oauthData.getDeviceToken(),
		// // this.oauthData.getDeviceSecret(),
		// // AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
		// // this.oauthData.getAccessToken(),
		// // this.oauthData.getAccessSecret());
		// // printer.logJSONData(data);
		// //
		// // data = loadData(this.oauthData.getDeviceToken(),
		// // this.oauthData.getDeviceSecret(),
		// // AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
		// // this.oauthData.getAccessToken(),
		// // this.oauthData.getAccessSecret());
		// // printer.logJSONData(data);
		//
		//
		//
		// printer.logActivity("Deleting test data from the server.");
		// deleteJSONData(this.oauthData.getDeviceToken(),
		// this.oauthData.getDeviceSecret(),
		// AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
		// this.oauthData.getAccessToken(),
		// this.oauthData.getAccessSecret(),
		// idTrackerActivityList);
		// deleteJSONData(this.oauthData.getDeviceToken(),
		// this.oauthData.getDeviceSecret(),
		// AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
		// this.oauthData.getAccessToken(),
		// this.oauthData.getAccessSecret(),
		// idTrackerSleepList);
		//
		// int countTrackerActivity = countData(this.oauthData.getDeviceToken(),
		// this.oauthData.getDeviceSecret(),
		// AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
		// this.oauthData.getAccessToken(),
		// this.oauthData.getAccessSecret());
		//
		// int countTrackerSleep = countData(this.oauthData.getDeviceToken(),
		// this.oauthData.getDeviceSecret(),
		// AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
		// this.oauthData.getAccessToken(),
		// this.oauthData.getAccessSecret());
		//
		// if(countTrackerActivity!=preTestcountTrackerActivity){
		// printer.logError("Wrong data count after deleting tracker activity data from the server: "+countTrackerActivity);
		// throw new
		// Exception("Tracker activity data are not successfully deleted from the server!");
		// }
		// if(countTrackerSleep!= preTestcountTrackerSleep){
		// printer.logError("Wrong data count after deleting tracker sleet data from the server: "+countTrackerSleep);
		// throw new
		// Exception("Tracker sleep data are not successfully deleted from the server!");
		// }
		// this.printer.logMessage("Data count: " + countTrackerActivity);

	}

}
