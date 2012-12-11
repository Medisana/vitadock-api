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
		Date date = DateHelper.generateCurrentDate();
		setUpdatedDate(date);
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Boolean getActive() {
		return this.active;
	}

	@JsonIgnore
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@JsonIgnore
	public void setUpdatedDate(final Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getMeasurementDate() {
		return this.measurementDate;
	}

	public void setMeasurementDate(final Date measurementDate) {
		this.measurementDate = measurementDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
