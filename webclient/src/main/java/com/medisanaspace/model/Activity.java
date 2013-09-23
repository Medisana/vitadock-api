package com.medisanaspace.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.medisanaspace.model.base.Entity;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 */
public class Activity extends Entity {

	private Integer activityId;
	private Integer activityParentId;
	private Integer calories;
	private String description;
	private Integer distance;
	private Integer duration;
	private boolean hasStartTime;
	private boolean isFavorite;
	private Integer logId;
	private String name;
	private Integer steps;
	private Date measurementDate;

	/**
	 * Method getActivityParentId.
	 * @return Integer
	 */
	public Integer getActivityParentId() {
		return activityParentId;
	}

	/**
	 * Method setActivityParentId.
	 * @param activityParentId Integer
	 */
	public void setActivityParentId(Integer activityParentId) {
		this.activityParentId = activityParentId;
	}

	/**
	 * Method getActivityId.
	 * @return Integer
	 */
	public Integer getActivityId() {
		return activityId;
	}

	/**
	 * Method setActivityId.
	 * @param activityId Integer
	 */
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

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
	 * Method getDescription.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Method setDescription.
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Method getDistance.
	 * @return Integer
	 */
	public Integer getDistance() {
		return distance;
	}

	/**
	 * Method setDistance.
	 * @param distance Integer
	 */
	public void setDistance(Integer distance) {
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
	 * Method isHasStartTime.
	 * @return boolean
	 */
	public boolean isHasStartTime() {
		return hasStartTime;
	}

	/**
	 * Method setHasStartTime.
	 * @param hasStartTime boolean
	 */
	public void setHasStartTime(boolean hasStartTime) {
		this.hasStartTime = hasStartTime;
	}

	/**
	 * Method isIsFavorite.
	 * @return boolean
	 */
	public boolean isIsFavorite() {
		return isFavorite;
	}

	/**
	 * Method setIsFavorite.
	 * @param isFavorite boolean
	 */
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	/**
	 * Method getLogId.
	 * @return Integer
	 */
	public Integer getLogId() {
		return logId;
	}

	/**
	 * Method setLogId.
	 * @param logId Integer
	 */
	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	/**
	 * Method getName.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method setName.
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Method toJsonArray.
	 * @param collection Collection<Activity>
	 * @return String
	 */
	public static final String toJsonArray(final Collection<Activity> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	/**
	 * Method fromJsonArrayToActivityLogs.
	 * @param json String
	 * @return Collection<Activity>
	 */
	public static final Collection<Activity> fromJsonArrayToActivityLogs(
			final String json) {
		return new JSONDeserializer<List<Activity>>()
				.use(null, ArrayList.class).use("values", Activitydock.class)
				.deserialize(json);
	}

	/**
	 * Method getMeasurementDate.
	 * @return Date
	 */
	public Date getMeasurementDate() {
		return measurementDate;
	}

	/**
	 * Method setMeasurementDate.
	 * @param measurementDate Date
	 */
	public void setMeasurementDate(Date measurementDate) {
		this.measurementDate = measurementDate;
	}
}
