package com.medisanaspace.model.fixture;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.medisanaspace.library.RandomHelper;
import com.medisanaspace.model.TrackerActivity;
import com.medisanaspace.model.TrackerActivityEntry;
import com.medisanaspace.model.TrackerSleep;
import com.medisanaspace.model.TrackerSleepQuality;

/**
 * Generator for random TrackerSleep entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class TrackerSleepFixture {

	private final TrackerSleep trackerSleep;
	private Date expectedMeasurementDate;
	private Set<TrackerSleepQuality> expectedTrackerSleepQualities;

	/**
	 * Constructor for TrackerSleepFixture.
	 * @param index int
	 * @param maxEntries int
	 */
	public TrackerSleepFixture(final int index, final int maxEntries) {

		this.expectedMeasurementDate = new Date(new Date().getTime()
				- (long) maxEntries * 3600L * 24L * 1000L + (long) index
				* 3600L * 1000L * 24L);
		this.trackerSleep = new TrackerSleep();

		this.expectedTrackerSleepQualities = new HashSet<TrackerSleepQuality>();

		Integer lastSleepEnd = 0;
		int entries = RandomHelper.generateInt(180, 270);
		for (int i = 0; i < entries; i++) {
			this.expectedTrackerSleepQualities
					.add(new TrackerSleepQualityFixture(lastSleepEnd)
							.getTrackerSleepQuality());
		}

		this.trackerSleep.setMeasurementDate(expectedMeasurementDate);
		this.trackerSleep
				.setTrackerSleepQualities(expectedTrackerSleepQualities);
	}
	
	/**
	 * Constructor for TrackerSleepFixture. Generates TrackerSleepEntries
	 * from a given TrackerActivity.
	 * @param trackerActivity
	 */
	public TrackerSleepFixture(final TrackerActivity trackerActivity) {
		this.trackerSleep = new TrackerSleep();
		this.expectedMeasurementDate = trackerActivity.getMeasurementDate();
		this.expectedTrackerSleepQualities = new HashSet<TrackerSleepQuality>();

		// calculate possible sleep intervals in correlation to a given activity
		int earliestActivityStart =1439;
		int latestActivityEnd = 0; 
		for( TrackerActivityEntry trackerActivityEntry: trackerActivity.getTrackerActivityEntries()){
			if( trackerActivityEntry.getStartTime()< earliestActivityStart)
				earliestActivityStart = trackerActivityEntry.getStartTime();
			if(trackerActivityEntry.getStartTime()+trackerActivityEntry.getDuration() > latestActivityEnd )
				latestActivityEnd = trackerActivityEntry.getStartTime()+trackerActivityEntry.getDuration();
		}
		
		// 2 minutes ^= 1 entry
		// sleep in the morning
		int entries = (earliestActivityStart/2)-1; 
		Integer lastSleepEnd = 0;
		for (int i = 0; i < entries; i++) {
			this.expectedTrackerSleepQualities
					.add(new TrackerSleepQualityFixture(lastSleepEnd)
							.getTrackerSleepQuality());
			lastSleepEnd+=2;
		}
		// sleep at night
		entries = (1439-latestActivityEnd)/2; 
		lastSleepEnd = latestActivityEnd+1;
		for (int i = 0; i < entries; i++) {
			this.expectedTrackerSleepQualities
					.add(new TrackerSleepQualityFixture(lastSleepEnd)
							.getTrackerSleepQuality());
			lastSleepEnd+=2;
		}

		this.trackerSleep.setMeasurementDate(expectedMeasurementDate);
		this.trackerSleep
				.setTrackerSleepQualities(expectedTrackerSleepQualities);
	}
		

	/**
	 * Method getExpectedMeasurementDate.
	 * @return Date
	 */
	public Date getExpectedMeasurementDate() {
		return expectedMeasurementDate;
	}

	/**
	 * Method setExpectedMeasurementDate.
	 * @param expectedMeasurementDate Date
	 */
	public void setExpectedMeasurementDate(Date expectedMeasurementDate) {
		this.expectedMeasurementDate = expectedMeasurementDate;
	}

	/**
	 * Method getExpectedTrackerSleepQualities.
	 * @return Set<TrackerSleepQuality>
	 */
	public Set<TrackerSleepQuality> getExpectedTrackerSleepQualities() {
		return expectedTrackerSleepQualities;
	}

	/**
	 * Method setExpectedTrackerSleepQualities.
	 * @param expectedTrackerSleepQualities Set<TrackerSleepQuality>
	 */
	public void setExpectedTrackerSleepQualities(
			Set<TrackerSleepQuality> expectedTrackerSleepQualities) {
		this.expectedTrackerSleepQualities = expectedTrackerSleepQualities;
	}

	/**
	 * Method getTrackerSleep.
	 * @return TrackerSleep
	 */
	public TrackerSleep getTrackerSleep() {
		return trackerSleep;
	}

}
