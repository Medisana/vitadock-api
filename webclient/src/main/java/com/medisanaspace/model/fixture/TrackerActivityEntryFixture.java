package com.medisanaspace.model.fixture;

import com.medisanaspace.model.TrackerActivityEntry;
import com.medisanaspace.library.RandomHelper;

/**
 * Generator for random TrackerActivityEntries entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class TrackerActivityEntryFixture {

	private final TrackerActivityEntry trackerActivityEntry;

	private Integer expectedCalories;
	private Integer expectedSteps;
	private Integer expectedRunningSteps;
	private Float expectedDistance;
	private Integer expectedDuration;
	private Integer expectedStartTime;

	/**
	 * Constructor for TrackerActivityEntryFixture.
	 * @param lastActivityEnd Integer
	 */
	public TrackerActivityEntryFixture(Integer lastActivityEnd) {
		this.trackerActivityEntry = new TrackerActivityEntry();
		
		this.expectedSteps = RandomHelper.generateInt(10);
		this.expectedCalories = RandomHelper.generateInt(10);
		this.expectedRunningSteps = RandomHelper.generateInt(10);
		this.expectedDistance = expectedSteps*70f; // in cm //RandomHelper.generateFloat(0.0f, 10.0f);
		this.expectedDuration = RandomHelper.generateInt(1, 3) * 15;
		if(lastActivityEnd == 0) {
			this.expectedStartTime = 600 + RandomHelper.generateInt(60);
			lastActivityEnd += this.expectedStartTime + this.expectedDuration;
		} else {
			lastActivityEnd += RandomHelper.generateInt(60);
			this.expectedStartTime = lastActivityEnd;
		}
		

		this.trackerActivityEntry.setSteps(expectedSteps);
		this.trackerActivityEntry.setCalories(expectedCalories);
		this.trackerActivityEntry.setRunningSteps(expectedRunningSteps);
		this.trackerActivityEntry.setDistance(expectedDistance);
		this.trackerActivityEntry.setDuration(expectedDuration);
		this.trackerActivityEntry.setStartTime(expectedStartTime);
	}

	/**
	 * Method getExpectedCalories.
	 * @return Integer
	 */
	public Integer getExpectedCalories() {
		return expectedCalories;
	}

	/**
	 * Method setExpectedCalories.
	 * @param expectedCalories Integer
	 */
	public void setExpectedCalories(Integer expectedCalories) {
		this.expectedCalories = expectedCalories;
	}

	/**
	 * Method getExpectedSteps.
	 * @return Integer
	 */
	public Integer getExpectedSteps() {
		return expectedSteps;
	}

	/**
	 * Method setExpectedSteps.
	 * @param expectedSteps Integer
	 */
	public void setExpectedSteps(Integer expectedSteps) {
		this.expectedSteps = expectedSteps;
	}

	/**
	 * Method getExpectedRunningSteps.
	 * @return Integer
	 */
	public Integer getExpectedRunningSteps() {
		return expectedRunningSteps;
	}

	/**
	 * Method setExpectedRunningSteps.
	 * @param expectedRunningSteps Integer
	 */
	public void setExpectedRunningSteps(Integer expectedRunningSteps) {
		this.expectedRunningSteps = expectedRunningSteps;
	}

	/**
	 * Method getExpectedDistance.
	 * @return Float
	 */
	public Float getExpectedDistance() {
		return expectedDistance;
	}

	/**
	 * Method setExpectedDistance.
	 * @param expectedDistance Float
	 */
	public void setExpectedDistance(Float expectedDistance) {
		this.expectedDistance = expectedDistance;
	}

	/**
	 * Method getExpectedDuration.
	 * @return Integer
	 */
	public Integer getExpectedDuration() {
		return expectedDuration;
	}

	/**
	 * Method setExpectedDuration.
	 * @param expectedDuration Integer
	 */
	public void setExpectedDuration(Integer expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	/**
	 * Method getExpectedStartTime.
	 * @return Integer
	 */
	public Integer getExpectedStartTime() {
		return expectedStartTime;
	}

	/**
	 * Method setExpectedStartTime.
	 * @param expectedStartTime Integer
	 */
	public void setExpectedStartTime(Integer expectedStartTime) {
		this.expectedStartTime = expectedStartTime;
	}

	/**
	 * Method getTrackerActivityEntry.
	 * @return TrackerActivityEntry
	 */
	public TrackerActivityEntry getTrackerActivityEntry() {
		return trackerActivityEntry;
	}

}
