package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.TrackerActivity;
import com.medisanaspace.model.TrackerActivityEntry;
import com.medisanaspace.model.TrackerSleep;
import com.medisanaspace.model.TrackerSleepQuality;
import com.medisanaspace.model.TrackerStats;
import com.medisanaspace.model.fixture.TrackerActivityFixture;
import com.medisanaspace.model.fixture.TrackerSleepFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 * Tests tracker/stats
 */
public class TrackerStatsTestTask extends AbstractTestTask {

	public TrackerStatsTestTask(int numberOfEntries) {
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

		// data retrieved from server before test
		int sumActivityStepsBeforeTest = 0;
		int sumActivityRunningStepsBeforeTest = 0; // not in the fixture yet
		Float sumActivityDistanceBeforeTest = 0f;
		Float sumActivityCaloriesBeforeTest = 0f;
		int sumSleepDurationBeforeTest = 0;

		// Data written to the server
		int sumActivityDuration = 0;
		int sumActivitySteps = 0;
		int sumActivityRunningSteps = 0; // not in the fixture yet
		Float sumActivityDistance = 0f;
		Float sumActivityCalories = 0f;

		printer.startDataSet("Tracker stats test");

		printer.logActivity("Loading current stats from the server.");

		// first retrieve current stats from the server
		String response = loadData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_STATS_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		printer.logJSONData(response);
		int activityDurationBeforeSaving = 0;

		/*
		 * Save current values from the server
		 */
		if (!response.equals("[ ]") && !response.equals("[]")) {
			TrackerStats trackerStatsBeforeSaving = TrackerStats
					.fromJsonToTrackerStats(response);
			activityDurationBeforeSaving = trackerStatsBeforeSaving
					.getDurationActivity();
			sumActivityStepsBeforeTest = trackerStatsBeforeSaving.getSteps();
			sumActivityRunningStepsBeforeTest = trackerStatsBeforeSaving
					.getRunningSteps();
			sumActivityDistanceBeforeTest = trackerStatsBeforeSaving
					.getDistance();
			sumActivityCaloriesBeforeTest = trackerStatsBeforeSaving
					.getCalories();
			sumSleepDurationBeforeTest = trackerStatsBeforeSaving
					.getDurationSleep();
		}

		// create dummy data
		for (int i = 0; i < numberOfEntries; i++) {
			TrackerActivity currentActivity = new TrackerActivityFixture(i,
					numberOfEntries).getTrackerActivity();
			trackerActivityList.add(currentActivity);
			trackerSleepList.add(new TrackerSleepFixture(currentActivity)
					.getTrackerSleep());
		}

		// save data from the test data
		for (TrackerActivity activity : trackerActivityList) {
			for (TrackerActivityEntry entry : activity
					.getTrackerActivityEntries()) {
				sumActivityDuration += entry.getDuration();
				sumActivityCalories += entry.getCalories();
				sumActivityDistance += entry.getDistance();
				sumActivityRunningSteps += entry.getRunningSteps();
				sumActivitySteps += entry.getSteps();
			}
		}

		int sumSleepDuration = 0;
		for (TrackerSleep trackerSleep : trackerSleepList) {
			for (TrackerSleepQuality trackerSleepQuality : trackerSleep
					.getTrackerSleepQualities()) {
				sumSleepDuration += trackerSleepQuality.getDuration();

			}

		}

		printer.logActivity("Saving test activities on the server");
		// save on the server
		response = saveJSONData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				TrackerActivity.toJsonArray(trackerActivityList),
				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		ArrayList<String> idListActivities = StringUtil
				.fromJsonArrayToStrings(response);

		printer.logActivity("Saving test sleep data on the server.");
		response = saveJSONData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				TrackerSleep.toJsonArray(trackerSleepList),
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		ArrayList<String> idListSleep = StringUtil
				.fromJsonArrayToStrings(response);

		printer.logActivity("Loading statistics from the server.");
		// retrieve stats from server
		response = loadData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_STATS_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		printer.logJSONData(response);
		TrackerStats trackerStatsAfterSaving = TrackerStats
				.fromJsonToTrackerStats(response);

		/*
		 * Check if stats are updated correctly
		 */
		printer.logActivity("Validate data on the server.");
		// duration
		if (activityDurationBeforeSaving + sumActivityDuration != trackerStatsAfterSaving
				.getDurationActivity()) {
			// error
			throw new Exception("Wrong statistics! Duration Before Test: "
					+ activityDurationBeforeSaving
					+ " Duration written to server: " + sumActivityDuration
					+ " After test: "
					+ trackerStatsAfterSaving.getDurationActivity());
		}

		printer.logMessage("Duration Before Test: "
				+ activityDurationBeforeSaving
				+ " Duration written to server: " + sumActivityDuration
				+ " After test: "
				+ trackerStatsAfterSaving.getDurationActivity());
		// distance

		if (!StringUtil.floatToTwoDigits(
				sumActivityDistanceBeforeTest + sumActivityDistance).equals(
				StringUtil.floatToTwoDigits(trackerStatsAfterSaving
						.getDistance()))) {
			throw new Exception("Wrong statistics! Distance before test: "
					+ sumActivityDistanceBeforeTest
					+ " summed distance written to server: "
					+ sumActivityDistance + " summed distance after saving: "
					+ trackerStatsAfterSaving.getDistance());
		}

		printer.logMessage("Distance before test: "
				+ sumActivityDistanceBeforeTest
				+ " summed distance written to server: " + sumActivityDistance
				+ " summed distance after saving: "
				+ trackerStatsAfterSaving.getDistance());

		// steps
		if (sumActivityStepsBeforeTest + sumActivitySteps != trackerStatsAfterSaving
				.getSteps()) {
			throw new Exception("Wrong statistics! Steps before test: "
					+ sumActivityStepsBeforeTest
					+ " summed steps written to server: " + sumActivitySteps
					+ " summed steps after saving: "
					+ trackerStatsAfterSaving.getSteps());
		}
		printer.logMessage("Steps before test: " + sumActivityStepsBeforeTest
				+ " summed steps written to server: " + sumActivitySteps
				+ " summed steps after saving: "
				+ trackerStatsAfterSaving.getSteps());

		// calories
		if (sumActivityCaloriesBeforeTest + sumActivityCalories != trackerStatsAfterSaving
				.getCalories()) {
			throw new Exception("Wrong statistics! Calories before test: "
					+ sumActivityCaloriesBeforeTest
					+ " summed calories written to server: "
					+ sumActivityCalories + " summed distance after saving: "
					+ trackerStatsAfterSaving.getCalories());
		}
		printer.logMessage("Calories before test: "
				+ sumActivityCaloriesBeforeTest
				+ " summed calories written to server: " + sumActivityCalories
				+ " summed distance after saving: "
				+ trackerStatsAfterSaving.getCalories());
		// duration sleep
		if (sumSleepDurationBeforeTest + sumSleepDuration != trackerStatsAfterSaving
				.getDurationSleep()) {
			throw new Exception(
					"Wrong statistics! Sleep duration before test: "
							+ sumSleepDurationBeforeTest
							+ " sleep duration written to server: "
							+ sumSleepDuration
							+ " sleep duration after saving: "
							+ trackerStatsAfterSaving.getDurationSleep());
		}
		printer.logMessage("Sleep duration before test: "
				+ sumSleepDurationBeforeTest
				+ " sleep duration written to server: " + sumSleepDuration
				+ " sleep duration after saving: "
				+ trackerStatsAfterSaving.getDurationSleep());
		// running steps
		if (sumActivityRunningStepsBeforeTest + sumActivityRunningSteps != trackerStatsAfterSaving
				.getRunningSteps()) {
			throw new Exception("Wrong statistics! Running steps before test: "
					+ sumActivityRunningStepsBeforeTest
					+ " running steps written to server: "
					+ sumActivityRunningSteps
					+ " sleep duration after saving: "
					+ trackerStatsAfterSaving.getRunningSteps());
		}
		printer.logMessage("Running steps before test: "
				+ sumActivityRunningStepsBeforeTest
				+ " running steps written to server: "
				+ sumActivityRunningSteps + " sleep duration after saving: "
				+ trackerStatsAfterSaving.getRunningSteps());
		printer.logMessage("Statistics are correct.");

		printer.logActivity("Deleting activities from the server.");
		deleteJSONData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				oauthData.getAccessToken(), oauthData.getAccessSecret(),
				idListActivities);

		printer.logActivity("Deleting sleep entries from the server.");
		deleteJSONData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				oauthData.getAccessToken(), oauthData.getAccessSecret(),
				idListSleep);

	}

}
