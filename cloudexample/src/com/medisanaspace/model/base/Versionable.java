package com.medisanaspace.model.base;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.medisanaspace.library.DateHelper;

public abstract class Versionable {

	protected String id;
	protected Boolean active;
	protected Date measurementDate;
	protected Date updatedDate;
	private Integer version;

	public Versionable() {
		setId(null);
		setActive(true);
		final Date date = DateHelper.generateCurrentDate();
		setUpdatedDate(date);
	}

	public void setId(final String id) {
		this.id = id;
	}

	public final String getId() {
		return this.id;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public final Boolean getActive() {
		return this.active;
	}

	@JsonIgnore
	public final Date getUpdatedDate() {
		return this.updatedDate;
	}

	@JsonIgnore
	public void setUpdatedDate(final Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public final Date getMeasurementDate() {
		return this.measurementDate;
	}

	public void setMeasurementDate(final Date measurementDate) {
		this.measurementDate = measurementDate;
	}

	public final Integer getVersion() {
		return this.version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}
}
