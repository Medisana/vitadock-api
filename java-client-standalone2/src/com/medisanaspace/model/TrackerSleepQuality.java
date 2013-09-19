package com.medisanaspace.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.model.base.Entity;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 */
public class TrackerSleepQuality extends Entity {

	private Integer sleepQuality;
	private Integer duration;
	private Integer startTime;

	/**
	 * Method toJsonArray.
	 * @param collection Collection<TrackerSleepQuality>
	 * @return String
	 */
	public static final String toJsonArray(
			final Collection<TrackerSleepQuality> collection) {
		return new JSONSerializer().exclude("*.class").exclude("uuid")
				.serialize(collection);
	}

	/**
	 * Method fromJsonArrayToTrackerSleepQualities.
	 * @param json String
	 * @return Collection<TrackerSleepQuality>
	 */
	public static final Collection<TrackerSleepQuality> fromJsonArrayToTrackerSleepQualities(
			final String json) {
		return new JSONDeserializer<List<TrackerSleepQuality>>()
				.use(null, ArrayList.class).use("values", TrackerSleep.class)
				.deserialize(json);
	}

	/**
	 * Method getSleepQuality.
	 * @return Integer
	 */
	public Integer getSleepQuality() {
		return sleepQuality;
	}

	/**
	 * Method setSleepQuality.
	 * @param sleepQuality Integer
	 */
	public void setSleepQuality(Integer sleepQuality) {
		this.sleepQuality = sleepQuality;
	}

	/**
	 * Method getDuration.
	 * @return Integer
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * Method setDuration.
	 * @param duration Integer
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * Method getStartTime.
	 * @return Integer
	 */
	public Integer getStartTime() {
		return startTime;
	}

	/**
	 * Method setStartTime.
	 * @param startTime Integer
	 */
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
}
