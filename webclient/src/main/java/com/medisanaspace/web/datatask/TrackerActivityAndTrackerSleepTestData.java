package com.medisanaspace.web.datatask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.TrackerActivity;
import com.medisanaspace.model.TrackerSleep;
import com.medisanaspace.model.fixture.TrackerActivityFixture;
import com.medisanaspace.model.fixture.TrackerSleepFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.testtask.AbstractTestTask;

/**
 */
public class TrackerActivityAndTrackerSleepTestData extends AbstractTestTask {

	
	
	public TrackerActivityAndTrackerSleepTestData(int numberOfEntries) {
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
		
		printer.startDataSet("Add ViFit test data");
		for (int i = 0; i < numberOfEntries; i++) {
			TrackerActivity currentActivity= new TrackerActivityFixture(i,numberOfEntries).getTrackerActivity();
			trackerActivityList.add(currentActivity);
			trackerSleepList.add(new TrackerSleepFixture(currentActivity).getTrackerSleep());
		}
		 
		printer.logActivity("Writing "+numberOfEntries+" Trackeractivity test data");
		// save new data
		String responseTrackerActivity	= saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				TrackerActivity.toJsonArray(trackerActivityList),
				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		printer.logActivity("Writing "+numberOfEntries+" Trackersleep test data");
		String responseTrackerSleep	= saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				TrackerSleep.toJsonArray(trackerSleepList),
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
	
		
		int countTrackerActivity = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		this.printer.logMessage("Data count: " + countTrackerActivity);
		
	
	}

}
