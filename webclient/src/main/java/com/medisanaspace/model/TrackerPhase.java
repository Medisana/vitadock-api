package com.medisanaspace.model;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.medisanaspace.library.CustomDateObjectFactory;
import com.medisanaspace.model.base.Versionable;

/**
 */
public class TrackerPhase extends Versionable {

	/**
	 * Method toJsonArray.
	 * @param collection Collection<com.medisanaspace.model.TrackerPhase>
	 * @return String
	 */
	public static String toJsonArray(
			Collection<com.medisanaspace.model.TrackerPhase> collection) {
		return new JSONSerializer().exclude("*.class").exclude("active")
				.exclude("updatedDate").exclude("version").exclude("id")
				.serialize(collection);
	}

	/**
	 * Method fromJsonArrayToTrackerPhases.
	 * @param json String
	 * @return Collection<com.medisanaspace.model.TrackerPhase>
	 */
	public static Collection<com.medisanaspace.model.TrackerPhase> fromJsonArrayToTrackerPhases(
			String json) {
		return new JSONDeserializer<List<TrackerPhase>>()
				.use(null, ArrayList.class).use("values", TrackerPhase.class)
				.use(Date.class, new CustomDateObjectFactory())
				.deserialize(json);
	}

}
