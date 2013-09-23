package com.medisanaspace.model.fixture;

import java.util.Date;

import com.medisanaspace.model.UserSettings;

/**
 * Generator for random UserSettings. 
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class UserSettingsFixture {

	private final UserSettings userSettings;
	private final String expectedSex;
	private final Date expectedBirthday;
	private final Float expectedHeight;
	private final Float expectedStride;
	private final String expectedLengthUnit;
	private final Integer expectedGoalSteps;
	private final Float expectedBodyWeight;

	public UserSettingsFixture() {
		this.userSettings = UserSettings.generateRandomEntry();
		this.expectedSex = userSettings.getSex();
		this.expectedBirthday = userSettings.getBirthday();
		this.expectedHeight = userSettings.getHeight();
		this.expectedStride = userSettings.getStride();
		this.expectedLengthUnit = userSettings.getLengthUnit();
		this.expectedGoalSteps = userSettings.getGoalSteps();
		this.expectedBodyWeight = userSettings.getBodyWeight();
	}

	/**
	 * Method getUserSettings.
	 * @return UserSettings
	 */
	public final UserSettings getUserSettings() {
		return this.userSettings;
	}

	/**
	 * Method getExpectedSex.
	 * @return String
	 */
	public String getExpectedSex() {
		return expectedSex;
	}

	/**
	 * Method getExpectedBirthday.
	 * @return Date
	 */
	public Date getExpectedBirthday() {
		return expectedBirthday;
	}

	/**
	 * Method getExpectedHeight.
	 * @return Float
	 */
	public Float getExpectedHeight() {
		return expectedHeight;
	}

	/**
	 * Method getExpectedStride.
	 * @return Float
	 */
	public Float getExpectedStride() {
		return expectedStride;
	}

	/**
	 * Method getExpectedLengthUnit.
	 * @return String
	 */
	public String getExpectedLengthUnit() {
		return expectedLengthUnit;
	}

	/**
	 * Method getExpectedGoalSteps.
	 * @return Integer
	 */
	public Integer getExpectedGoalSteps() {
		return expectedGoalSteps;
	}

	/**
	 * Method getExpectedBodyWeight.
	 * @return Float
	 */
	public Float getExpectedBodyWeight() {
		return expectedBodyWeight;
	}
}
