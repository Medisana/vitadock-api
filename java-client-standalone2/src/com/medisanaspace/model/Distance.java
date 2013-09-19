package com.medisanaspace.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.medisanaspace.model.base.Entity;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 */
public class Distance extends Entity {

	private String activity;
	private Float distance;
	private Date measurementDate;

	/**
	 * Method getActivity.
	 * @return String
	 */
	public String getActivity() {
		return activity;
	}

	/**
	 * Method setActivity.
	 * @param activity String
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}

	/**
	 * Method getDistance.
	 * @return Float
	 */
	public Float getDistance() {
		return distance;
	}

	/**
	 * Method setDistance.
	 * @param distance Float
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
	}

	/**
	 * Method toJsonArray.
	 * @param collection Collection<Distance>
	 * @return String
	 */
	public static final String toJsonArray(final Collection<Distance> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	/**
	 * Method fromJsonArrayToActivityDistances.
	 * @param json String
	 * @return Collection<Distance>
	 */
	public static final Collection<Distance> fromJsonArrayToActivityDistances(
			final String json) {
		return new JSONDeserializer<List<Distance>>()
				.use(null, ArrayList.class).use("values", Activitydock.class)
				.deserialize(json);
	}

	/**
	 * Method getMeasurementDate.
	 * @return Date
	 */
	public Date getMeasurementDate() {
		return measurementDate;
	}

	/**
	 * Method setMeasurementDate.
	 * @param measurementDate Date
	 */
	public void setMeasurementDate(Date measurementDate) {
		this.measurementDate = measurementDate;
	}
}
