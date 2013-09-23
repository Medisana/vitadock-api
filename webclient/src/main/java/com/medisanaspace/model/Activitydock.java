package com.medisanaspace.model;

import com.medisanaspace.model.base.Versionable;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 */
public class Activitydock extends Versionable {

	private Set<Distance> distances;
	private Set<Activity> activities;

	private Integer activeScore;
	private Integer activityCalories;
	private Integer caloriesOut;
	private Float elevation;
	private Integer fairlyActiveMinutes;
	private Integer floors;
	private Integer lightlyActiveMinutes;
	private Integer marginalCalories;
	private Integer sedentaryMinutes;
	private Integer steps;
	private Integer veryActiveMinutes;
	private Integer goalActiveScore;
	private Integer goalCaloriesOut;
	private Integer goalFloors;
	private Float goalDistance;
	private Integer goalSteps;

	/**
	 * Method getDistances.
	 * @return Set<Distance>
	 */
	public Set<Distance> getDistances() {
		return distances;
	}

	/**
	 * Method setDistances.
	 * @param distances Set<Distance>
	 */
	public void setDistances(Set<Distance> distances) {
		this.distances = distances;
	}

	/**
	 * Method getActivities.
	 * @return Set<Activity>
	 */
	public Set<Activity> getActivities() {
		return activities;
	}

	/**
	 * Method setActivities.
	 * @param activities Set<Activity>
	 */
	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	/**
	 * Method getActiveScore.
	 * @return Integer
	 */
	public Integer getActiveScore() {
		return activeScore;
	}

	/**
	 * Method setActiveScore.
	 * @param activeScore Integer
	 */
	public void setActiveScore(Integer activeScore) {
		this.activeScore = activeScore;
	}

	/**
	 * Method getActivityCalories.
	 * @return Integer
	 */
	public Integer getActivityCalories() {
		return activityCalories;
	}

	/**
	 * Method setActivityCalories.
	 * @param activityCalories Integer
	 */
	public void setActivityCalories(Integer activityCalories) {
		this.activityCalories = activityCalories;
	}

	/**
	 * Method getCaloriesOut.
	 * @return Integer
	 */
	public Integer getCaloriesOut() {
		return caloriesOut;
	}

	/**
	 * Method setCaloriesOut.
	 * @param caloriesOut Integer
	 */
	public void setCaloriesOut(Integer caloriesOut) {
		this.caloriesOut = caloriesOut;
	}

	/**
	 * Method getElevation.
	 * @return Float
	 */
	public Float getElevation() {
		return elevation;
	}

	/**
	 * Method setElevation.
	 * @param elevation Float
	 */
	public void setElevation(Float elevation) {
		this.elevation = elevation;
	}

	/**
	 * Method getFairlyActiveMinutes.
	 * @return Integer
	 */
	public Integer getFairlyActiveMinutes() {
		return fairlyActiveMinutes;
	}

	/**
	 * Method setFairlyActiveMinutes.
	 * @param fairlyActiveMinutes Integer
	 */
	public void setFairlyActiveMinutes(Integer fairlyActiveMinutes) {
		this.fairlyActiveMinutes = fairlyActiveMinutes;
	}

	/**
	 * Method getFloors.
	 * @return Integer
	 */
	public Integer getFloors() {
		return floors;
	}

	/**
	 * Method setFloors.
	 * @param floors Integer
	 */
	public void setFloors(Integer floors) {
		this.floors = floors;
	}

	/**
	 * Method getLightlyActiveMinutes.
	 * @return Integer
	 */
	public Integer getLightlyActiveMinutes() {
		return lightlyActiveMinutes;
	}

	/**
	 * Method setLightlyActiveMinutes.
	 * @param lightlyActiveMinutes Integer
	 */
	public void setLightlyActiveMinutes(Integer lightlyActiveMinutes) {
		this.lightlyActiveMinutes = lightlyActiveMinutes;
	}

	/**
	 * Method getMarginalCalories.
	 * @return Integer
	 */
	public Integer getMarginalCalories() {
		return marginalCalories;
	}

	/**
	 * Method setMarginalCalories.
	 * @param marginalCalories Integer
	 */
	public void setMarginalCalories(Integer marginalCalories) {
		this.marginalCalories = marginalCalories;
	}

	/**
	 * Method getSedentaryMinutes.
	 * @return Integer
	 */
	public Integer getSedentaryMinutes() {
		return sedentaryMinutes;
	}

	/**
	 * Method setSedentaryMinutes.
	 * @param sedentaryMinutes Integer
	 */
	public void setSedentaryMinutes(Integer sedentaryMinutes) {
		this.sedentaryMinutes = sedentaryMinutes;
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
	 * Method getVeryActiveMinutes.
	 * @return Integer
	 */
	public Integer getVeryActiveMinutes() {
		return veryActiveMinutes;
	}

	/**
	 * Method setVeryActiveMinutes.
	 * @param veryActiveMinutes Integer
	 */
	public void setVeryActiveMinutes(Integer veryActiveMinutes) {
		this.veryActiveMinutes = veryActiveMinutes;
	}

	/**
	 * Method getGoalActiveScore.
	 * @return Integer
	 */
	public Integer getGoalActiveScore() {
		return goalActiveScore;
	}

	/**
	 * Method setGoalActiveScore.
	 * @param goalActiveScore Integer
	 */
	public void setGoalActiveScore(Integer goalActiveScore) {
		this.goalActiveScore = goalActiveScore;
	}

	/**
	 * Method getGoalCaloriesOut.
	 * @return Integer
	 */
	public Integer getGoalCaloriesOut() {
		return goalCaloriesOut;
	}

	/**
	 * Method setGoalCaloriesOut.
	 * @param goalCaloriesOut Integer
	 */
	public void setGoalCaloriesOut(Integer goalCaloriesOut) {
		this.goalCaloriesOut = goalCaloriesOut;
	}

	/**
	 * Method getGoalFloors.
	 * @return Integer
	 */
	public Integer getGoalFloors() {
		return goalFloors;
	}

	/**
	 * Method setGoalFloors.
	 * @param goalFloors Integer
	 */
	public void setGoalFloors(Integer goalFloors) {
		this.goalFloors = goalFloors;
	}

	/**
	 * Method getGoalDistance.
	 * @return Float
	 */
	public Float getGoalDistance() {
		return goalDistance;
	}

	/**
	 * Method setGoalDistance.
	 * @param goalDistance Float
	 */
	public void setGoalDistance(Float goalDistance) {
		this.goalDistance = goalDistance;
	}

	/**
	 * Method getGoalSteps.
	 * @return Integer
	 */
	public Integer getGoalSteps() {
		return goalSteps;
	}

	/**
	 * Method setGoalSteps.
	 * @param goalSteps Integer
	 */
	public void setGoalSteps(Integer goalSteps) {
		this.goalSteps = goalSteps;
	}

	/**
	 * Method toJson.
	 * @return String
	 */
	public final String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	/**
	 * Method fromJsonToActivity.
	 * @param json String
	 * @return Activitydock
	 */
	public static final Activitydock fromJsonToActivity(final String json) {
		return new JSONDeserializer<Activitydock>().use(null, Activitydock.class)
				.deserialize(json);
	}

	/**
	 * Method toJsonArray.
	 * @param collection Collection<Activitydock>
	 * @return String
	 */
	public static final String toJsonArray(final Collection<Activitydock> collection) {
		return new JSONSerializer().include("activities", "distances")
				.exclude("*.class").exclude("active").exclude("updatedDate")
				.exclude("version").exclude("id").serialize(collection);
	}

	/**
	 * Method fromJsonArrayToActivitys.
	 * @param json String
	 * @return Collection<Activitydock>
	 */
	public static final Collection<Activitydock> fromJsonArrayToActivitys(
			final String json) {
		return new JSONDeserializer<List<Activitydock>>()
				.use(null, ArrayList.class).use("values", Activitydock.class)
				.deserialize(json);
	}
}
