package com.medisanaspace.model.base;

/**
 */
public abstract class BaseModelWithoutMeal extends Versionable {
	public static final int MIN_ACTIVITY_STATUS = 0;
	public static final int MAX_ACTIVITY_STATUS = 3;
	public static final int MIN_MOOD = 0;
	public static final int MAX_MOOD = 2;
	public static final int MAX_NOTE_LENGTH = 512;

	private Integer activityStatus;
	private Integer mood;
	private String note;

	/**
	 * Method getActivityStatus.
	 * @return Integer
	 */
	public final Integer getActivityStatus() {
		return this.activityStatus;
	}

	/**
	 * Method setActivityStatus.
	 * @param activityStatus Integer
	 */
	public void setActivityStatus(final Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	/**
	 * Method getMood.
	 * @return Integer
	 */
	public final Integer getMood() {
		return this.mood;
	}

	/**
	 * Method setMood.
	 * @param mood Integer
	 */
	public void setMood(final Integer mood) {
		this.mood = mood;
	}

	/**
	 * Method getNote.
	 * @return String
	 */
	public final String getNote() {
		return this.note;
	}

	/**
	 * Method setNote.
	 * @param note String
	 */
	public void setNote(final String note) {
		this.note = note;
	}

}
