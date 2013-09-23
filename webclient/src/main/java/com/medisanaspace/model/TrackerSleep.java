package com.medisanaspace.model;

import com.medisanaspace.model.base.Versionable;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 */
public class TrackerSleep extends Versionable {

	private Set<TrackerSleepQuality> trackerSleepQualities;

	/**
	 * Method toJson.
	 * @return String
	 */
	public final String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	/**
	 * Method fromJsonToTrackerSleep.
	 * @param json String
	 * @return TrackerSleep
	 */
	public static final TrackerSleep fromJsonToTrackerSleep(final String json) {
		return new JSONDeserializer<TrackerSleep>().use(null,
				TrackerSleep.class).deserialize(json);
	}

	/**
	 * Method toJsonArray.
	 * @param collection Collection<TrackerSleep>
	 * @return String
	 */
	public static final String toJsonArray(
			final Collection<TrackerSleep> collection) {
		return new JSONSerializer().include("trackerSleepQualities").exclude("*.class")
				.exclude("active").exclude("updatedDate").exclude("version")
				.exclude("uuid").serialize(collection);
	}

	/**
	 * Method fromJsonArrayToTrackerSleeps.
	 * @param json String
	 * @return Collection<TrackerSleep>
	 */
	public static final Collection<TrackerSleep> fromJsonArrayToTrackerSleeps(
			final String json) {
		return new JSONDeserializer<List<TrackerSleep>>()
				.use(null, ArrayList.class).use("values", TrackerSleepQuality.class)
				.deserialize(json);
	}

	/**
	 * Method getTrackerSleepQualities.
	 * @return Set<TrackerSleepQuality>
	 */
	public Set<TrackerSleepQuality> getTrackerSleepQualities() {
		return trackerSleepQualities;
	}

	/**
	 * Method setTrackerSleepQualities.
	 * @param trackerSleepQualities Set<TrackerSleepQuality>
	 */
	public void setTrackerSleepQualities(
			Set<TrackerSleepQuality> trackerSleepQualities) {
		this.trackerSleepQualities = trackerSleepQualities;
	}
}
