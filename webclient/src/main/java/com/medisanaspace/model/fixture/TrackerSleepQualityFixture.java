package com.medisanaspace.model.fixture;

import com.medisanaspace.model.TrackerSleepQuality;
import com.medisanaspace.library.RandomHelper;

/**
 * Generator for random TrackerSleepQuality entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class TrackerSleepQualityFixture {

	private final TrackerSleepQuality trackerSleepQuality;

	private Integer expectedSleepQuality;
	private Integer expectedDuration;
	private Integer expectedStartTime;

	/**
	 * Constructor for TrackerSleepQualityFixture.
	 * @param lastSleepEnd Integer
	 */
	public TrackerSleepQualityFixture(Integer lastSleepEnd) {
		this.trackerSleepQuality = new TrackerSleepQuality();

		this.expectedSleepQuality = RandomHelper.generateInt(22);
		this.expectedDuration = 2;

		if (lastSleepEnd == 0) {
			this.expectedStartTime = 0;
		} else {
			this.expectedStartTime = lastSleepEnd;
		}
		lastSleepEnd += this.expectedDuration;
		
		this.trackerSleepQuality.setDuration(expectedDuration);
		this.trackerSleepQuality.setSleepQuality(expectedSleepQuality);
		this.trackerSleepQuality.setStartTime(expectedStartTime);
	}

	/**
	 * Method getExpectedSleepQuality.
	 * @return Integer
	 */
	public Integer getExpectedSleepQuality() {
		return expectedSleepQuality;
	}

	/**
	 * Method setExpectedSleepQuality.
	 * @param expectedSleepQuality Integer
	 */
	public void setExpectedSleepQuality(Integer expectedSleepQuality) {
		this.expectedSleepQuality = expectedSleepQuality;
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
	 * Method getTrackerSleepQuality.
	 * @return TrackerSleepQuality
	 */
	public TrackerSleepQuality getTrackerSleepQuality() {
		return trackerSleepQuality;
	}

}
