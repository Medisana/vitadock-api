package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.library.StringUtil;
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
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {

		final List<TrackerActivity> trackerActivityList = new ArrayList<TrackerActivity>();
		final List<TrackerSleep> trackerSleepList = new ArrayList<TrackerSleep>();
		
		
		for (int i = 0; i < numberOfEntries; i++) {
			TrackerActivity currentActivity= new TrackerActivityFixture(i,numberOfEntries).getTrackerActivity();
			trackerActivityList.add(currentActivity);
			trackerSleepList.add(new TrackerSleepFixture(currentActivity).getTrackerSleep());
		}
		
		
		// clean up
//		deleteAllDataFromModule(this.oauthData.getDeviceToken(),
//				this.oauthData.getDeviceSecret(), 
//				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
//				//AuthorizationBuilder.T
//				this.oauthData.getAccessToken(),
//				this.oauthData.getAccessSecret());
//		
//		deleteAllDataFromModule(this.oauthData.getDeviceToken(),
//				this.oauthData.getDeviceSecret(), 
//				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
//				this.oauthData.getAccessToken(),
//				this.oauthData.getAccessSecret());
		 

		// save new data
		String responseTrackerActivity	= saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				TrackerActivity.toJsonArray(trackerActivityList),
				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		
		String responseTrackerSleep	= saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				TrackerSleep.toJsonArray(trackerSleepList),
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		
		// have a look at it?
		Collection<String> idTrackerActivityList = StringUtil
				.fromJsonArrayToStrings(responseTrackerActivity);
		String data = loadData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(), 
				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		printer.logData(data);
		
		data = loadData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(), 
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		printer.logData(data);
	
		
		int countTrackerActivity = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		this.printer.logMessage("Data count: " + countTrackerActivity);
		
//		responseTrackerActivity = syncData(this.oauthData.getDeviceToken(),
//				this.oauthData.getDeviceSecret(),
//				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
//				this.oauthData.getAccessToken(),
//				this.oauthData.getAccessSecret());
		
	}

}
