package com.medisanaspace.model.fixture;

import com.medisanaspace.model.Glucodockinsulin;

import com.medisanaspace.library.RandomHelper;
import java.util.Date;

/**
 * Generator for random Glucodockinsulin entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class GlucodockinsulinFixture {

	private final Glucodockinsulin glucodockInsulin;
	private final Date expectedMeasurementDate;
	private final Float expectedInsulin;
	private final Integer expectedInsulinTypeIndex;
	private final String expectedInsulinTypeName;

	/**
	 * Constructor for GlucodockinsulinFixture.
	 * @param index int
	 * @param maxEntries int
	 */
	public GlucodockinsulinFixture(final int index, final int maxEntries) {
		this.glucodockInsulin = new Glucodockinsulin();

		this.expectedMeasurementDate = new Date(new Date().getTime()
				- maxEntries * 3600L * 24L * 1000L + index
				* 3600L * 24L * 1000L);

		this.expectedInsulin = RandomHelper.generateFloat(
				Glucodockinsulin.MIN_INSULIN, Glucodockinsulin.MAX_INSULIN);
		this.expectedInsulinTypeIndex = RandomHelper.generateInt(
				Glucodockinsulin.MIN_INSULIN_TYPE_INDEX,
				Glucodockinsulin.MAX_INSULIN_TYPE_INDEX);
		switch (this.expectedInsulinTypeIndex) {
		case 1:
			this.expectedInsulinTypeName = "Insulin glulisin";
			break;
		case 2:
			this.expectedInsulinTypeName = "Insulin detemir";
			break;
		default:
			this.expectedInsulinTypeName = "Insulinaspart";
			break;
		}

		this.glucodockInsulin.setMeasurementDate(this.expectedMeasurementDate);
		this.glucodockInsulin.setInsulin(this.expectedInsulin);
		this.glucodockInsulin
				.setInsulinTypeIndex(this.expectedInsulinTypeIndex);
		this.glucodockInsulin.setInsulinTypeName(this.expectedInsulinTypeName);
	}

	/**
	 * Method getExpectedMeasurementDate.
	 * @return Date
	 */
	public final Date getExpectedMeasurementDate() {
		return new Date(this.expectedMeasurementDate.getTime());
	}

	/**
	 * Method getExpectedInsulin.
	 * @return Float
	 */
	public final Float getExpectedInsulin() {
		return this.expectedInsulin;
	}

	/**
	 * Method getExpectedInsulinTypeIndex.
	 * @return Integer
	 */
	public final Integer getExpectedInsulinTypeIndex() {
		return this.expectedInsulinTypeIndex;
	}

	/**
	 * Method getExpectedInsulinTypeName.
	 * @return String
	 */
	public final String getExpectedInsulinTypeName() {
		return this.expectedInsulinTypeName;
	}

	/**
	 * Method getGlucodockinsulin.
	 * @return Glucodockinsulin
	 */
	public final Glucodockinsulin getGlucodockinsulin() {
		return this.glucodockInsulin;
	}
}
