package com.medisanaspace.model.fixture;

import com.medisanaspace.model.Glucodockmeal;
import com.medisanaspace.library.RandomHelper;
import java.util.Date;

/**
 * Generator for random Glucodockmeal entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 */
public class GlucodockmealFixture {

	private Glucodockmeal glucodockMeal = null;
	private Date expectedMeasurementDate;
	private Integer expectedCarbohydrates;

	public GlucodockmealFixture(final int index) {
		this.glucodockMeal = new Glucodockmeal();

		this.expectedMeasurementDate = new Date(new Date().getTime()
				- (long) index * 3600L * 24L * 1000L);

		this.expectedCarbohydrates = RandomHelper.generateLong(
				Glucodockmeal.MIN_CARBOHYDRATES,
				Glucodockmeal.MAX_CARBOHYDRATES);

		this.glucodockMeal.setMeasurementDate(this.expectedMeasurementDate);

		this.glucodockMeal.setCarbohydrates(this.expectedCarbohydrates);
	}

	public final Date getExpectedMeasurementDate() {
		return new Date(this.expectedMeasurementDate.getTime());
	}

	public final Integer getExpectedCarbohydrates() {
		return this.expectedCarbohydrates;
	}

	public final Glucodockmeal getGlucodockmeal() {
		return this.glucodockMeal;
	}
}
