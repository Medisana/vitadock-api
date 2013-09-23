package com.medisanaspace.model.fixture;

import com.medisanaspace.model.Activitydock;
import com.medisanaspace.model.Distance;
import com.medisanaspace.model.Activity;
import com.medisanaspace.library.RandomHelper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Generator for random Activitydock entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class ActivitydockFixture {

	private final Activitydock activity;

	private Date expectedMeasurementDate;

	private Set<Distance> expectedDistances;
	private Set<Activity> expectedActivities;

	private Integer expectedActiveScore;
	private Integer expectedActivityCalories;
	private Integer expectedCaloriesOut;
	private Float expectedElevation;
	private Integer expectedFairlyActiveMinutes;
	private Integer expectedFloors;
	private Integer expectedLightlyActiveMinutes;
	private Integer expectedMarginalCalories;
	private Integer expectedSedentaryMinutes;
	private Integer expectedSteps;
	private Integer expectedVeryActiveMinutes;
	private Integer expectedGoalActiveScore;
	private Integer expectedGoalCaloriesOut;
	private Integer expectedGoalFloors;
	private Float expectedGoalDistance;
	private Integer expectedGoalSteps;

	public ActivitydockFixture() {
		this.activity = new Activitydock();

		this.expectedDistances = new HashSet<Distance>();
		this.expectedActivities = new HashSet<Activity>();

		for (int i = 0; i < 2; i++) {
			this.expectedDistances.add(new DistanceFixture().getDistance());
			this.expectedActivities.add(new ActivityFixture().getActivity());
		}

		this.expectedMeasurementDate = new Date();
		this.expectedActiveScore = RandomHelper.generateInt(10);
		this.expectedActivityCalories = RandomHelper.generateInt(10);
		this.expectedCaloriesOut = RandomHelper.generateInt(10);
		this.expectedElevation = RandomHelper.generateFloat(0.0f, 10.0f);
		this.expectedFairlyActiveMinutes = RandomHelper.generateInt(10);
		this.expectedFloors = RandomHelper.generateInt(10);
		this.expectedLightlyActiveMinutes = RandomHelper.generateInt(10);
		this.expectedMarginalCalories = RandomHelper.generateInt(10);
		this.expectedSedentaryMinutes = RandomHelper.generateInt(10);
		this.expectedSteps = RandomHelper.generateInt(10);
		this.expectedVeryActiveMinutes = RandomHelper.generateInt(10);
		this.expectedGoalActiveScore = RandomHelper.generateInt(10);
		this.expectedGoalCaloriesOut = RandomHelper.generateInt(10);
		this.expectedGoalFloors = RandomHelper.generateInt(10);
		this.expectedGoalDistance = RandomHelper.generateFloat(0.0f, 10.0f);
		this.expectedGoalSteps = RandomHelper.generateInt(10);

		this.activity.setMeasurementDate(expectedMeasurementDate);
		this.activity.setDistances(expectedDistances);
		this.activity.setActivities(expectedActivities);
		this.activity.setActiveScore(expectedActiveScore);
		this.activity.setActivityCalories(expectedActivityCalories);
		this.activity.setCaloriesOut(expectedCaloriesOut);
		this.activity.setElevation(expectedElevation);
		this.activity.setFairlyActiveMinutes(expectedFairlyActiveMinutes);
		this.activity.setFloors(expectedFloors);
		this.activity.setLightlyActiveMinutes(expectedLightlyActiveMinutes);
		this.activity.setMarginalCalories(expectedMarginalCalories);
		this.activity.setSedentaryMinutes(expectedSedentaryMinutes);
		this.activity.setSteps(expectedSteps);
		this.activity.setVeryActiveMinutes(expectedVeryActiveMinutes);
		this.activity.setGoalActiveScore(expectedGoalActiveScore);
		this.activity.setGoalCaloriesOut(expectedGoalCaloriesOut);
		this.activity.setGoalFloors(expectedGoalFloors);
		this.activity.setGoalDistance(expectedGoalDistance);
		this.activity.setGoalSteps(expectedGoalSteps);

	}

	/**
	 * Method getActivity.
	 * @return Activitydock
	 */
	public Activitydock getActivity() {
		return activity;
	}

	/**
	 * Method getExpectedDistances.
	 * @return Set<Distance>
	 */
	public Set<Distance> getExpectedDistances() {
		return expectedDistances;
	}

	/**
	 * Method getExpectedActivities.
	 * @return Set<Activity>
	 */
	public Set<Activity> getExpectedActivities() {
		return expectedActivities;
	}

	/**
	 * Method getExpectedActiveScore.
	 * @return Integer
	 */
	public Integer getExpectedActiveScore() {
		return expectedActiveScore;
	}

	/**
	 * Method getExpectedActivityCalories.
	 * @return Integer
	 */
	public Integer getExpectedActivityCalories() {
		return expectedActivityCalories;
	}

	/**
	 * Method getExpectedCaloriesOut.
	 * @return Integer
	 */
	public Integer getExpectedCaloriesOut() {
		return expectedCaloriesOut;
	}

	/**
	 * Method getExpectedElevation.
	 * @return Float
	 */
	public Float getExpectedElevation() {
		return expectedElevation;
	}

	/**
	 * Method getExpectedFairlyActiveMinutes.
	 * @return Integer
	 */
	public Integer getExpectedFairlyActiveMinutes() {
		return expectedFairlyActiveMinutes;
	}

	/**
	 * Method getExpectedFloors.
	 * @return Integer
	 */
	public Integer getExpectedFloors() {
		return expectedFloors;
	}

	/**
	 * Method getExpectedLightlyActiveMinutes.
	 * @return Integer
	 */
	public Integer getExpectedLightlyActiveMinutes() {
		return expectedLightlyActiveMinutes;
	}

	/**
	 * Method getExpectedMarginalCalories.
	 * @return Integer
	 */
	public Integer getExpectedMarginalCalories() {
		return expectedMarginalCalories;
	}

	/**
	 * Method getExpectedSedentaryMinutes.
	 * @return Integer
	 */
	public Integer getExpectedSedentaryMinutes() {
		return expectedSedentaryMinutes;
	}

	/**
	 * Method getExpectedSteps.
	 * @return Integer
	 */
	public Integer getExpectedSteps() {
		return expectedSteps;
	}

	/**
	 * Method getExpectedVeryActiveMinutes.
	 * @return Integer
	 */
	public Integer getExpectedVeryActiveMinutes() {
		return expectedVeryActiveMinutes;
	}

	/**
	 * Method getExpectedGoalActiveScore.
	 * @return Integer
	 */
	public Integer getExpectedGoalActiveScore() {
		return expectedGoalActiveScore;
	}

	/**
	 * Method getExpectedGoalCaloriesOut.
	 * @return Integer
	 */
	public Integer getExpectedGoalCaloriesOut() {
		return expectedGoalCaloriesOut;
	}

	/**
	 * Method getExpectedGoalFloors.
	 * @return Integer
	 */
	public Integer getExpectedGoalFloors() {
		return expectedGoalFloors;
	}

	/**
	 * Method getExpectedGoalDistance.
	 * @return Float
	 */
	public Float getExpectedGoalDistance() {
		return expectedGoalDistance;
	}

	/**
	 * Method getExpectedGoalSteps.
	 * @return Integer
	 */
	public Integer getExpectedGoalSteps() {
		return expectedGoalSteps;
	}

	/**
	 * Method setExpectedDistances.
	 * @param expectedDistances Set<Distance>
	 */
	public void setExpectedDistances(Set<Distance> expectedDistances) {
		this.expectedDistances = expectedDistances;
	}

	/**
	 * Method setExpectedActivities.
	 * @param expectedActivities Set<Activity>
	 */
	public void setExpectedActivities(Set<Activity> expectedActivities) {
		this.expectedActivities = expectedActivities;
	}

	/**
	 * Method setExpectedActiveScore.
	 * @param expectedActiveScore Integer
	 */
	public void setExpectedActiveScore(Integer expectedActiveScore) {
		this.expectedActiveScore = expectedActiveScore;
	}

	/**
	 * Method setExpectedActivityCalories.
	 * @param expectedActivityCalories Integer
	 */
	public void setExpectedActivityCalories(Integer expectedActivityCalories) {
		this.expectedActivityCalories = expectedActivityCalories;
	}

	/**
	 * Method setExpectedCaloriesOut.
	 * @param expectedCaloriesOut Integer
	 */
	public void setExpectedCaloriesOut(Integer expectedCaloriesOut) {
		this.expectedCaloriesOut = expectedCaloriesOut;
	}

	/**
	 * Method setExpectedElevation.
	 * @param expectedElevation Float
	 */
	public void setExpectedElevation(Float expectedElevation) {
		this.expectedElevation = expectedElevation;
	}

	/**
	 * Method setExpectedFairlyActiveMinutes.
	 * @param expectedFairlyActiveMinutes Integer
	 */
	public void setExpectedFairlyActiveMinutes(
			Integer expectedFairlyActiveMinutes) {
		this.expectedFairlyActiveMinutes = expectedFairlyActiveMinutes;
	}

	/**
	 * Method setExpectedFloors.
	 * @param expectedFloors Integer
	 */
	public void setExpectedFloors(Integer expectedFloors) {
		this.expectedFloors = expectedFloors;
	}

	/**
	 * Method setExpectedLightlyActiveMinutes.
	 * @param expectedLightlyActiveMinutes Integer
	 */
	public void setExpectedLightlyActiveMinutes(
			Integer expectedLightlyActiveMinutes) {
		this.expectedLightlyActiveMinutes = expectedLightlyActiveMinutes;
	}

	/**
	 * Method setExpectedMarginalCalories.
	 * @param expectedMarginalCalories Integer
	 */
	public void setExpectedMarginalCalories(Integer expectedMarginalCalories) {
		this.expectedMarginalCalories = expectedMarginalCalories;
	}

	/**
	 * Method setExpectedSedentaryMinutes.
	 * @param expectedSedentaryMinutes Integer
	 */
	public void setExpectedSedentaryMinutes(Integer expectedSedentaryMinutes) {
		this.expectedSedentaryMinutes = expectedSedentaryMinutes;
	}

	/**
	 * Method setExpectedSteps.
	 * @param expectedSteps Integer
	 */
	public void setExpectedSteps(Integer expectedSteps) {
		this.expectedSteps = expectedSteps;
	}

	/**
	 * Method setExpectedVeryActiveMinutes.
	 * @param expectedVeryActiveMinutes Integer
	 */
	public void setExpectedVeryActiveMinutes(Integer expectedVeryActiveMinutes) {
		this.expectedVeryActiveMinutes = expectedVeryActiveMinutes;
	}

	/**
	 * Method setExpectedGoalActiveScore.
	 * @param expectedGoalActiveScore Integer
	 */
	public void setExpectedGoalActiveScore(Integer expectedGoalActiveScore) {
		this.expectedGoalActiveScore = expectedGoalActiveScore;
	}

	/**
	 * Method setExpectedGoalCaloriesOut.
	 * @param expectedGoalCaloriesOut Integer
	 */
	public void setExpectedGoalCaloriesOut(Integer expectedGoalCaloriesOut) {
		this.expectedGoalCaloriesOut = expectedGoalCaloriesOut;
	}

	/**
	 * Method setExpectedGoalFloors.
	 * @param expectedGoalFloors Integer
	 */
	public void setExpectedGoalFloors(Integer expectedGoalFloors) {
		this.expectedGoalFloors = expectedGoalFloors;
	}

	/**
	 * Method setExpectedGoalDistance.
	 * @param expectedGoalDistance Float
	 */
	public void setExpectedGoalDistance(Float expectedGoalDistance) {
		this.expectedGoalDistance = expectedGoalDistance;
	}

	/**
	 * Method setExpectedGoalSteps.
	 * @param expectedGoalSteps Integer
	 */
	public void setExpectedGoalSteps(Integer expectedGoalSteps) {
		this.expectedGoalSteps = expectedGoalSteps;
	}

}
