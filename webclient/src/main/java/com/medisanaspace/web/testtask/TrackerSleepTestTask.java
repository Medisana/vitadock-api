package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.TrackerSleep;
import com.medisanaspace.model.fixture.TrackerSleepFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class TrackerSleepTestTask extends AbstractTestTask {

	public TrackerSleepTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {

		// fill the lists with generated random entries
		final List<TrackerSleep> trackerSleepList = new ArrayList<TrackerSleep>();
		for (int i = 0; i < numberOfEntries; i++) {
			trackerSleepList.add(new TrackerSleepFixture(i,
					numberOfEntries).getTrackerSleep());
		}
		printer.startDataSet("Trackersleep test");
		printer.logActivity("Counting data before the test.");	
		
		String responseTrackerSleep = saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				TrackerSleep.toJsonArray(trackerSleepList),
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		int countTrackerSleep = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		this.printer.logMessage("Data count: " + countTrackerSleep);
		responseTrackerSleep = syncData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
	}

}
