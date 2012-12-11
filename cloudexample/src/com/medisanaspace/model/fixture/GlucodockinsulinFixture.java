package com.medisanaspace.model.fixture;

import com.medisanaspace.model.Glucodockinsulin;

import com.medisanaspace.library.RandomHelper;
import java.util.Date;

/**
 * Generator for random Glucodockinsulin entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 */
public class GlucodockinsulinFixture {

	private Glucodockinsulin glucodockInsulin = null;
	private Date expectedMeasurementDate;
	private Float expectedInsulin;
	private Integer expectedInsulinTypeIndex;
	private String expectedInsulinTypeName;

	public GlucodockinsulinFixture(final int index) {
		this.glucodockInsulin = new Glucodockinsulin();

		this.expectedMeasurementDate = new Date(new Date().getTime()
				- (long) index * 3600L * 24L * 1000L);

		this.expectedInsulin = RandomHelper.generateFloat(
				Glucodockinsulin.MIN_INSULIN, Glucodockinsulin.MAX_INSULIN);
		this.expectedInsulinTypeIndex = RandomHelper.generateLong(
				Glucodockinsulin.MIN_INSULIN_TYPE_INDEX,
				Glucodockinsulin.MAX_INSULIN_TYPE_INDEX);
		switch (this.expectedInsulinTypeIndex) {
		case 0:
			this.expectedInsulinTypeName = "Insulinaspart";
			break;
		case 1:
			this.expectedInsulinTypeName = "Insulin glulisin";
			break;
		case 2:
			this.expectedInsulinTypeName = "Insulin detemir";
			break;
		}

		this.glucodockInsulin.setMeasurementDate(this.expectedMeasurementDate);
		this.glucodockInsulin.setInsulin(this.expectedInsulin);
		this.glucodockInsulin
				.setInsulinTypeIndex(this.expectedInsulinTypeIndex);
		this.glucodockInsulin.setInsulinTypeName(this.expectedInsulinTypeName);
	}

	public final Date getExpectedMeasurementDate() {
		return new Date(this.expectedMeasurementDate.getTime());
	}

	public final Float getExpectedInsulin() {
		return this.expectedInsulin;
	}

	public final Integer getExpectedInsulinTypeIndex() {
		return this.expectedInsulinTypeIndex;
	}

	public final String getExpectedInsulinTypeName() {
		return this.expectedInsulinTypeName;
	}

	public final Glucodockinsulin getGlucodockinsulin() {
		return this.glucodockInsulin;
	}
}
