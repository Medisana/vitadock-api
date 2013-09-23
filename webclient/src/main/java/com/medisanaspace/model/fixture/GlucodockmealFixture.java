package com.medisanaspace.model.fixture;

import com.medisanaspace.model.Glucodockmeal;
import com.medisanaspace.library.RandomHelper;
import java.util.Date;

/**
 * Generator for random Glucodockmeal entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class GlucodockmealFixture {

	private final Glucodockmeal glucodockMeal;
	private final Date expectedMeasurementDate;
	private final Integer expectedCarbohydrates;

	/**
	 * Constructor for GlucodockmealFixture.
	 * @param index int
	 * @param maxEntries int
	 */
	public GlucodockmealFixture(final int index, final int maxEntries) {
		this.glucodockMeal = new Glucodockmeal();

		this.expectedMeasurementDate = new Date(new Date().getTime()
				- maxEntries * 3600L * 24L * 1000L + index
				* 3600L * 24L * 1000L);

		this.expectedCarbohydrates = RandomHelper.generateInt(
				Glucodockmeal.MIN_CARBOHYDRATES,
				Glucodockmeal.MAX_CARBOHYDRATES);

		this.glucodockMeal.setMeasurementDate(this.expectedMeasurementDate);

		this.glucodockMeal.setCarbohydrates(this.expectedCarbohydrates);
	}

	/**
	 * Method getExpectedMeasurementDate.
	 * @return Date
	 */
	public final Date getExpectedMeasurementDate() {
		return new Date(this.expectedMeasurementDate.getTime());
	}

	/**
	 * Method getExpectedCarbohydrates.
	 * @return Integer
	 */
	public final Integer getExpectedCarbohydrates() {
		return this.expectedCarbohydrates;
	}

	/**
	 * Method getGlucodockmeal.
	 * @return Glucodockmeal
	 */
	public final Glucodockmeal getGlucodockmeal() {
		return this.glucodockMeal;
	}
}
