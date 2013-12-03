package com.medisanaspace.model;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.base.Versionable;

import flexjson.JSONDeserializer;

/**
 */
public class TrackerStats extends Versionable {

	private Float calories;
	private Integer steps;
	private Integer runningSteps;
	private Float distance;
	private Integer durationActivity;
	private Integer durationSleep;
	private Integer sleepQuality;
	// added manually
	private Integer excellentSleepQualityDuration;
	private Integer badSleepQualityDuration;
	private Integer mediumSleepQualityDuration;
	private Integer goodSleepQualityDuration;
//	private String moduleSerialId;
	/**
	 * Method getCalories.
	 * 
	 * @return Float
	 */
	public Float getCalories() {
		return calories;
	}

	/**
	 * Method setCalories.
	 * 
	 * @param calories
	 *            Float
	 */
	public void setCalories(Float calories) {
		this.calories = calories;
	}

	/**
	 * Method getSteps.
	 * 
	 * @return Integer
	 */
	public Integer getSteps() {
		return steps;
	}

	/**
	 * Method setSteps.
	 * 
	 * @param steps
	 *            Integer
	 */
	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	/**
	 * Method getRunningSteps.
	 * 
	 * @return Integer
	 */
	public Integer getRunningSteps() {
		return runningSteps;
	}

	/**
	 * Method setRunningSteps.
	 * 
	 * @param runningSteps
	 *            Integer
	 */
	public void setRunningSteps(Integer runningSteps) {
		this.runningSteps = runningSteps;
	}

	/**
	 * Method getDistance.
	 * 
	 * @return Float
	 */
	public Float getDistance() {
		return distance;
	}

	/**
	 * Method setDistance.
	 * 
	 * @param distance
	 *            Float
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
	}

	/**
	 * Method getDurationActivity.
	 * 
	 * @return Integer
	 */
	public Integer getDurationActivity() {
		return durationActivity;
	}

	/**
	 * Method setDurationActivity.
	 * 
	 * @param durationActivity
	 *            Integer
	 */
	public void setDurationActivity(Integer durationActivity) {
		this.durationActivity = durationActivity;
	}

	/**
	 * Method getDurationSleep.
	 * 
	 * @return Integer
	 */
	public Integer getDurationSleep() {
		return durationSleep;
	}

	/**
	 * Method setDurationSleep.
	 * 
	 * @param durationSleep
	 *            Integer
	 */
	public void setDurationSleep(Integer durationSleep) {
		this.durationSleep = durationSleep;
	}

	/**
	 * Method getSleepQuality.
	 * 
	 * @return Integer
	 */
	public Integer getSleepQuality() {
		return sleepQuality;
	}

	/**
	 * Method setSleepQuality.
	 * 
	 * @param sleepQuality
	 *            Integer
	 */
	public void setSleepQuality(Integer sleepQuality) {
		this.sleepQuality = sleepQuality;
	}

	public static final TrackerStats fromJsonToTrackerStats(final String json) {
		List<TrackerStats> trackerStatsList = new JSONDeserializer<List<TrackerStats>>().use(null, ArrayList.class).use("values",
				TrackerStats.class).deserialize(json);
		if(trackerStatsList.size()>0){
			return trackerStatsList.get(0);
		}else{
			return null;
		}
	}
	
//	public static final TrackerStats fromJsonToTrackerStatsList(final String json) {
//		
//		List<TrackerStats> trackerStatsList = new JSONDeserializer<List<TrackerStats>>().use(null, ArrayList.class).use("values",
//				TrackerStats.class).deserialize(json);
//		return trackerStatsList.get(0);
//	}

	public Integer getExcellentSleepQualityDuration() {
		return excellentSleepQualityDuration;
	}

	public void setExcellentSleepQualityDuration(
			Integer excellentSleepQualityDuration) {
		this.excellentSleepQualityDuration = excellentSleepQualityDuration;
	}

	public Integer getBadSleepQualityDuration() {
		return badSleepQualityDuration;
	}

	public void setBadSleepQualityDuration(Integer badSleepQualityDuration) {
		this.badSleepQualityDuration = badSleepQualityDuration;
	}

	public Integer getMediumSleepQualityDuration() {
		return mediumSleepQualityDuration;
	}

	public void setMediumSleepQualityDuration(Integer mediumSleepQualityDuration) {
		this.mediumSleepQualityDuration = mediumSleepQualityDuration;
	}

	public Integer getGoodSleepQualityDuration() {
		return goodSleepQualityDuration;
	}

	public void setGoodSleepQualityDuration(Integer goodSleepQualityDuration) {
		this.goodSleepQualityDuration = goodSleepQualityDuration;
	}

//	public String getModuleSerialId() {
//		return moduleSerialId;
//	}
//
//	public void setModuleSerialId(String moduleSerialId) {
//		this.moduleSerialId = moduleSerialId;
//	}

}
