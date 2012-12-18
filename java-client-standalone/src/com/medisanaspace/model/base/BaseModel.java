package com.medisanaspace.model.base;

public abstract class BaseModel extends BaseModelWithoutMeal {
	public static final int MIN_MEAL_STATUS = 0;
	public static final int MAX_MEAL_STATUS = 1;

	private Integer mealStatus;

	public final Integer getMealStatus() {
		return this.mealStatus;
	}

	public void setMealStatus(final Integer mealStatus) {
		this.mealStatus = mealStatus;
	}

}
