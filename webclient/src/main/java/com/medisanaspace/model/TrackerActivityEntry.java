package com.medisanaspace.model;

import com.medisanaspace.model.base.Entity;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 */
public class TrackerActivityEntry extends Entity {

	private Integer calories;
	private Integer steps;
	private Integer runningSteps;
	private Float distance;
	private Integer duration;
	private Integer startTime;

	/**
	 * Method getCalories.
	 * @return Integer
	 */
	public Integer getCalories() {
		return calories;
	}

	/**
	 * Method setCalories.
	 * @param calories Integer
	 */
	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	/**
	 * Method getSteps.
	 * @return Integer
	 */
	public Integer getSteps() {
		return steps;
	}

	/**
	 * Method setSteps.
	 * @param steps Integer
	 */
	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	/**
	 * Method getRunningSteps.
	 * @return Integer
	 */
	public Integer getRunningSteps() {
		return runningSteps;
	}

	/**
	 * Method setRunningSteps.
	 * @param runningSteps Integer
	 */
	public void setRunningSteps(Integer runningSteps) {
		this.runningSteps = runningSteps;
	}

	/**
	 * Method getDistance.
	 * @return Float
	 */
	public Float getDistance() {
		return distance;
	}

	/**
	 * Method setDistance.
	 * @param distance Float
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
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

	/**
	 * Method toJsonArray.
	 * @param collection Collection<TrackerActivityEntry>
	 * @return String
	 */
	public static final String toJsonArray(
			final Collection<TrackerActivityEntry> collection) {
		return new JSONSerializer().exclude("*.class").exclude("uuid")
				.serialize(collection);
	}

	/**
	 * Method fromJsonArrayToTrackerActivityEntries.
	 * @param json String
	 * @return Collection<TrackerActivityEntry>
	 */
	public static final Collection<TrackerActivityEntry> fromJsonArrayToTrackerActivityEntries(
			final String json) {
		return new JSONDeserializer<List<TrackerActivityEntry>>()
				.use(null, ArrayList.class)
				.use("values", TrackerActivityEntry.class).deserialize(json);
	}
}
