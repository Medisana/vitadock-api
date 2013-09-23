package com.medisanaspace.model.fixture;

import com.medisanaspace.model.Activity;
import com.medisanaspace.library.RandomHelper;

import java.util.Date;

/**
 * Generator for random Activity entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class ActivityFixture {

	private final Activity activity;

	private Date expectedMeasurementDate;

	private Integer expectedActivityId;
	private Integer expectedActivityParentId;
	private Integer expectedCalories;
	private String expectedDescription;
	private Integer expectedDistance;
	private Integer expectedDuration;
	private boolean expectedHasStartTime;
	private boolean expectedIsFavorite;
	private Integer expectedLogId;
	private String expectedName;
	private Integer expectedSteps;

	public ActivityFixture() {
		this.activity = new Activity();

		this.expectedMeasurementDate = new Date();
		

		this.expectedActivityId = 5678;
		this.expectedActivityParentId = 1234;
		this.expectedCalories = RandomHelper.generateInt(10);
		this.expectedDescription = "[auto generated]";
		this.expectedDistance = RandomHelper.generateInt(10);
		this.expectedDuration = RandomHelper.generateInt(10);
		this.expectedHasStartTime = true;
		this.expectedIsFavorite = RandomHelper.generateBoolean();
		this.expectedLogId = 1234;
		this.expectedName = "[auto generated]";
		this.expectedSteps = RandomHelper.generateInt(10);

		this.activity.setMeasurementDate(expectedMeasurementDate);
		this.activity.setActivityId(expectedActivityId);
		this.activity.setActivityParentId(expectedActivityParentId);
		this.activity.setCalories(expectedCalories);
		this.activity.setDescription(expectedDescription);
		this.activity.setDistance(expectedDistance);
		this.activity.setDuration(expectedDuration);
		this.activity.setHasStartTime(expectedHasStartTime);
		this.activity.setIsFavorite(expectedIsFavorite);
		this.activity.setLogId(expectedLogId);
		this.activity.setName(expectedName);
		this.activity.setSteps(expectedSteps);
		
	}

	/**
	 * Method getActivity.
	 * @return Activity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * Method getExpectedMeasurementDate.
	 * @return Date
	 */
	public Date getExpectedMeasurementDate() {
		return expectedMeasurementDate;
	}

	/**
	 * Method getExpectedActivityParentId.
	 * @return Integer
	 */
	public Integer getExpectedActivityParentId() {
		return expectedActivityParentId;
	}

	/**
	 * Method getExpectedActivityId.
	 * @return Integer
	 */
	public Integer getExpectedActivityId() {
		return expectedActivityId;
	}

	/**
	 * Method getExpectedCalories.
	 * @return Integer
	 */
	public Integer getExpectedCalories() {
		return expectedCalories;
	}

	/**
	 * Method getExpectedDescription.
	 * @return String
	 */
	public String getExpectedDescription() {
		return expectedDescription;
	}

	/**
	 * Method getExpectedDistance.
	 * @return Integer
	 */
	public Integer getExpectedDistance() {
		return expectedDistance;
	}

	/**
	 * Method getExpectedDuration.
	 * @return Integer
	 */
	public Integer getExpectedDuration() {
		return expectedDuration;
	}

	/**
	 * Method isExpectedHasStartTime.
	 * @return boolean
	 */
	public boolean isExpectedHasStartTime() {
		return expectedHasStartTime;
	}

	/**
	 * Method isExpectedIsFavorite.
	 * @return boolean
	 */
	public boolean isExpectedIsFavorite() {
		return expectedIsFavorite;
	}

	/**
	 * Method getExpectedLogId.
	 * @return Integer
	 */
	public Integer getExpectedLogId() {
		return expectedLogId;
	}

	/**
	 * Method getExpectedName.
	 * @return String
	 */
	public String getExpectedName() {
		return expectedName;
	}

	/**
	 * Method getExpectedSteps.
	 * @return Integer
	 */
	public Integer getExpectedSteps() {
		return expectedSteps;
	}

	/**
	 * Method setExpectedMeasurementDate.
	 * @param expectedMeasurementDate Date
	 */
	public void setExpectedMeasurementDate(Date expectedMeasurementDate) {
		this.expectedMeasurementDate = expectedMeasurementDate;
	}

	/**
	 * Method setExpectedActivityParentId.
	 * @param expectedActivityParentId Integer
	 */
	public void setExpectedActivityParentId(Integer expectedActivityParentId) {
		this.expectedActivityParentId = expectedActivityParentId;
	}

	/**
	 * Method setExpectedActivityId.
	 * @param expectedActivityId Integer
	 */
	public void setExpectedActivityId(Integer expectedActivityId) {
		this.expectedActivityId = expectedActivityId;
	}

	/**
	 * Method setExpectedCalories.
	 * @param expectedCalories Integer
	 */
	public void setExpectedCalories(Integer expectedCalories) {
		this.expectedCalories = expectedCalories;
	}

	/**
	 * Method setExpectedDescription.
	 * @param expectedDescription String
	 */
	public void setExpectedDescription(String expectedDescription) {
		this.expectedDescription = expectedDescription;
	}

	/**
	 * Method setExpectedDistance.
	 * @param expectedDistance Integer
	 */
	public void setExpectedDistance(Integer expectedDistance) {
		this.expectedDistance = expectedDistance;
	}

	/**
	 * Method setExpectedDuration.
	 * @param expectedDuration Integer
	 */
	public void setExpectedDuration(Integer expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	/**
	 * Method setExpectedHasStartTime.
	 * @param expectedHasStartTime boolean
	 */
	public void setExpectedHasStartTime(boolean expectedHasStartTime) {
		this.expectedHasStartTime = expectedHasStartTime;
	}

	/**
	 * Method setExpectedIsFavorite.
	 * @param expectedIsFavorite boolean
	 */
	public void setExpectedIsFavorite(boolean expectedIsFavorite) {
		this.expectedIsFavorite = expectedIsFavorite;
	}

	/**
	 * Method setExpectedLogId.
	 * @param expectedLogId Integer
	 */
	public void setExpectedLogId(Integer expectedLogId) {
		this.expectedLogId = expectedLogId;
	}

	/**
	 * Method setExpectedName.
	 * @param expectedName String
	 */
	public void setExpectedName(String expectedName) {
		this.expectedName = expectedName;
	}

	/**
	 * Method setExpectedSteps.
	 * @param expectedSteps Integer
	 */
	public void setExpectedSteps(Integer expectedSteps) {
		this.expectedSteps = expectedSteps;
	}

}
