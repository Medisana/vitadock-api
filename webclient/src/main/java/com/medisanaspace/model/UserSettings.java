package com.medisanaspace.model;

import com.medisanaspace.library.RandomHelper;
import com.medisanaspace.model.base.Versionable;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 */
public class UserSettings extends Versionable {

	private String sex;
	private Date birthday;
	private String lengthUnit;
	private float height;
	private float stride;
	private int goalSteps;
	private float bodyWeight;
	//goal_calories and goal_sleep
	private int goalCalories;
	private int goalSleep;
	/**
	 * Method generateRandomEntry.
	 * @return UserSettings
	 */
	public static final UserSettings generateRandomEntry() {
		final UserSettings userSettings = new UserSettings();
		userSettings.setSex("male");
		userSettings.setBirthday(new Date());
		userSettings.setLengthUnit("cm");
		userSettings.setStride(RandomHelper.generateFloat(40.0f, 100.0f));
		userSettings.setHeight(RandomHelper.generateFloat(61.0f, 249.0f));
		userSettings.setGoalSteps(RandomHelper.generateInt(500, 2000));
		userSettings.setMeasurementDate(new Date());
		userSettings.setBodyWeight(RandomHelper.generateFloat(50.0f, 199.0f));
		userSettings.setGoalCalories(2000);
		userSettings.setGoalSleep(480);
		return userSettings;
	}

	/**
	 * Method toJson.
	 * @return String
	 */
	public final String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	/**
	 * Method fromJsonToUserSettings.
	 * @param json String
	 * @return UserSettings
	 */
	public static final UserSettings fromJsonToUserSettings(final String json) {
		return new JSONDeserializer<UserSettings>().use(null,
				UserSettings.class).deserialize(json);
	}

	/**
	 * Method toJsonArray.
	 * @param collection Collection<UserSettings>
	 * @return String
	 */
	public static final String toJsonArray(
			final Collection<UserSettings> collection) {
		return new JSONSerializer().exclude("*.class").exclude("active")
				.exclude("updatedDate").exclude("version").exclude("id")
				.serialize(collection);
	}

	/**
	 * Method fromJsonArrayToUserSettings.
	 * @param json String
	 * @return Collection<UserSettings>
	 */
	public static final Collection<UserSettings> fromJsonArrayToUserSettings(
			final String json) {
		return new JSONDeserializer<List<UserSettings>>()
				.use(null, ArrayList.class).use("values", UserSettings.class)
				.deserialize(json);
	}

	/**
	 * Method getSex.
	 * @return String
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Method setSex.
	 * @param sex String
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * Method getBirthday.
	 * @return Date
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * Method setBirthday.
	 * @param birthday Date
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * Method getLengthUnit.
	 * @return String
	 */
	public String getLengthUnit() {
		return lengthUnit;
	}

	/**
	 * Method setLengthUnit.
	 * @param lengthUnit String
	 */
	public void setLengthUnit(String lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

	/**
	 * Method getHeight.
	 * @return float
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Method setHeight.
	 * @param height float
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * Method getStride.
	 * @return float
	 */
	public float getStride() {
		return stride;
	}

	/**
	 * Method setStride.
	 * @param stride float
	 */
	public void setStride(float stride) {
		this.stride = stride;
	}

	/**
	 * Method getGoalSteps.
	 * @return int
	 */
	public int getGoalSteps() {
		return goalSteps;
	}

	/**
	 * Method setGoalSteps.
	 * @param goalSteps int
	 */
	public void setGoalSteps(int goalSteps) {
		this.goalSteps = goalSteps;
	}

	/**
	 * Method getBodyWeight.
	 * @return float
	 */
	public float getBodyWeight() {
		return bodyWeight;
	}

	/**
	 * Method setBodyWeight.
	 * @param bodyWeight float
	 */
	public void setBodyWeight(float bodyWeight) {
		this.bodyWeight = bodyWeight;
	}

	public int getGoalCalories() {
		return goalCalories;
	}

	public void setGoalCalories(int goalCalories) {
		this.goalCalories = goalCalories;
	}

	public int getGoalSleep() {
		return goalSleep;
	}

	public void setGoalSleep(int goalSleep) {
		this.goalSleep = goalSleep;
	}
}
