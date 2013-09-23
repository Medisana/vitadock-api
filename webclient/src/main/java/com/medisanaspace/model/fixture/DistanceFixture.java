package com.medisanaspace.model.fixture;

import com.medisanaspace.library.RandomHelper;
import com.medisanaspace.model.Distance;

import java.util.Date;

/**
 * Generator for random Distance entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class DistanceFixture {

	private final Distance distance;

	private Date expectedMeasurementDate;

	private String expectedActivity;
	private Float expectedDistance;

	public DistanceFixture() {
		this.distance = new Distance();

		this.expectedMeasurementDate = new Date();
		this.expectedActivity = "[auto generated]";
		this.expectedDistance = RandomHelper.generateFloat(0.0f, 10.0f);

		this.distance.setMeasurementDate(expectedMeasurementDate);
		this.distance.setActivity(expectedActivity);
		this.distance.setDistance(expectedDistance);
	}

	/**
	 * Method getDistance.
	 * @return Distance
	 */
	public Distance getDistance() {
		return distance;
	}

	/**
	 * Method getExpectedMeasurementDate.
	 * @return Date
	 */
	public Date getExpectedMeasurementDate() {
		return expectedMeasurementDate;
	}

	/**
	 * Method getExpectedActivity.
	 * @return String
	 */
	public String getExpectedActivity() {
		return expectedActivity;
	}

	/**
	 * Method getExpectedDistance.
	 * @return Float
	 */
	public Float getExpectedDistance() {
		return expectedDistance;
	}

	/**
	 * Method setExpectedMeasurementDate.
	 * @param expectedMeasurementDate Date
	 */
	public void setExpectedMeasurementDate(Date expectedMeasurementDate) {
		this.expectedMeasurementDate = expectedMeasurementDate;
	}
	
	/**
	 * Method setExpectedActivity.
	 * @param expectedActivity String
	 */
	public void setExpectedActivity(String expectedActivity) {
		this.expectedActivity = expectedActivity;
	}

	/**
	 * Method setExpectedDistance.
	 * @param expectedDistance Float
	 */
	public void setExpectedDistance(Float expectedDistance) {
		this.expectedDistance = expectedDistance;
	}

}
