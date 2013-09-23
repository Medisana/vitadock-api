package com.medisanaspace.model.base;

import java.util.UUID;

import flexjson.JSON;

/**
 * Base class for all data entries. All data entries share ids, dates,
 * corresponding tokens and information concerning versioning. TODO
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 * @version $Revision: 1.0 $
 */
public abstract class Entity {

	protected String uuid;

	public Entity() {
		setUuid(UUID.randomUUID().toString());
	}

	/**
	 * Method setUuid.
	 * @param uuid String
	 */
	@JSON(include = false)
	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Method getUuid.
	 * @return String
	 */
	@JSON(include = false)
	public String getUuid() {
		return this.uuid;
	}
}
