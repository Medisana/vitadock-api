package com.medisanaspace.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.medisanaspace.library.CustomDateObjectFactory;
import com.medisanaspace.model.base.Versionable;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 */
public class TrackerPhase extends Versionable {

	private Set<TrackerPhaseEntry> trackerPhaseEntries;

	/**
	 * Method toJsonArray.
	 * 
	 * @param collection
	 *            Collection<com.medisanaspace.model.TrackerPhase>
	 * @return String
	 */
	public static String toJsonArray(
			Collection<com.medisanaspace.model.TrackerPhase> collection) {
		return new JSONSerializer().include("trackerPhaseEntries")
				.exclude("*.class").exclude("active").exclude("updatedDate")
				.exclude("version").exclude("id").serialize(collection);
	
	}

	/**
	 * Method fromJsonArrayToTrackerPhases.
	 * 
	 * @param json
	 *            String
	 * @return Collection<com.medisanaspace.model.TrackerPhase>
	 */
	public static Collection<com.medisanaspace.model.TrackerPhase> fromJsonArrayToTrackerPhases(
			String json) {
		return new JSONDeserializer<List<TrackerPhase>>()
				.use(null, ArrayList.class).use("values", TrackerPhase.class)
				.use(Date.class, new CustomDateObjectFactory())
				.deserialize(json);
	}

	public Set<TrackerPhaseEntry> getTrackerPhaseEntries() {
		return trackerPhaseEntries;
	}

	public void setTrackerPhaseEntries(
			Set<TrackerPhaseEntry> trackerPhaseEntries) {
		this.trackerPhaseEntries = trackerPhaseEntries;
	}

}
