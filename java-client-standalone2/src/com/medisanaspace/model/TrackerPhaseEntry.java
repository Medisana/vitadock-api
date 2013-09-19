package com.medisanaspace.model;


import com.medisanaspace.model.base.Entity;

/**
 */
public class TrackerPhaseEntry extends Entity {

	private TrackerPhase trackerPhaseId;
	private Integer startTime;
	private Integer duration;
	private Float calories;
	private Integer steps;
	private Integer runningSteps;
	private Float distance;

	/**
	 * Method getTrackerPhaseId.
	 * @return TrackerPhase
	 */
	public TrackerPhase getTrackerPhaseId() {
		return trackerPhaseId;
	}

	/**
	 * Method setTrackerPhaseId.
	 * @param trackerPhaseId TrackerPhase
	 */
	public void setTrackerPhaseId(TrackerPhase trackerPhaseId) {
		this.trackerPhaseId = trackerPhaseId;
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
	 * Method getCalories.
	 * @return Float
	 */
	public Float getCalories() {
		return calories;
	}

	/**
	 * Method setCalories.
	 * @param calories Float
	 */
	public void setCalories(Float calories) {
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
}
