package com.medisanaspace.model.base;

import java.util.Date;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.medisanaspace.library.DateHelper;

import flexjson.JSON;

/**
 */
public abstract class Versionable {

	protected String id;
	protected Boolean active;
	protected Date measurementDate;
	protected Date updatedDate;
	private Integer version;

	public Versionable() {
		setId(UUID.randomUUID().toString());
		setActive(true);
		final Date date = DateHelper.generateCurrentDate();
		setUpdatedDate(date);
	}

	/**
	 * Method setId.
	 * @param id String
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Method getId.
	 * @return String
	 */
	@JSON(include = false)
	public final String getId() {
		return this.id;
	}

	/**
	 * Method setActive.
	 * @param active Boolean
	 */
	public void setActive(final Boolean active) {
		this.active = active;
	}

	/**
	 * Method getActive.
	 * @return Boolean
	 */
	@JSON(include = false)
	public final Boolean getActive() {
		return this.active;
	}

	/**
	 * Method getUpdatedDate.
	 * @return Date
	 */
	@JSON(include = false)
	@JsonIgnore
	public final Date getUpdatedDate() {
		return this.updatedDate;
	}

	/**
	 * Method setUpdatedDate.
	 * @param updatedDate Date
	 */
	@JsonIgnore
	public void setUpdatedDate(final Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * Method getMeasurementDate.
	 * @return Date
	 */
	public final Date getMeasurementDate() {
		return this.measurementDate;
	}

	/**
	 * Method setMeasurementDate.
	 * @param measurementDate Date
	 */
	public void setMeasurementDate(final Date measurementDate) {
		this.measurementDate = measurementDate;
	}

	/**
	 * Method getVersion.
	 * @return Integer
	 */
	@JSON(include = false)
	public final Integer getVersion() {
		return this.version;
	}

	/**
	 * Method setVersion.
	 * @param version Integer
	 */
	public void setVersion(final Integer version) {
		this.version = version;
	}
}
