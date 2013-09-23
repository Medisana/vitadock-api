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
public class TrackerActivity extends Versionable {

	private Set<TrackerActivityEntry> trackerActivityEntries;

	/**
	 * Method toJson.
	 * @return String
	 */
	public final String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	/**
	 * Method fromJsonToTrackerActivity.
	 * @param json String
	 * @return TrackerActivity
	 */
	public static final TrackerActivity fromJsonToTrackerActivity(
			final String json) {
		return new JSONDeserializer<TrackerActivity>().use(null,
				TrackerActivity.class).deserialize(json);
	}

	/**
	 * Method toJsonArray.
	 * @param collection Collection<TrackerActivity>
	 * @return String
	 */
	public static final String toJsonArray(
			final Collection<TrackerActivity> collection) {
		return new JSONSerializer().include("trackerActivityEntries")
				.exclude("*.class").exclude("active").exclude("updatedDate")
				.exclude("version").exclude("uuid").serialize(collection);
	}

	/**
	 * Method fromJsonArrayToTrackerActivities.
	 * @param json String
	 * @return Collection<TrackerActivity>
	 */
	public static final Collection<TrackerActivity> fromJsonArrayToTrackerActivities(
			final String json) {
		return new JSONDeserializer<List<TrackerActivity>>()
				.use(null, ArrayList.class)
				.use("values", TrackerActivityEntry.class).deserialize(json);
	}

	/**
	 * Method getTrackerActivityEntries.
	 * @return Set<TrackerActivityEntry>
	 */
	public Set<TrackerActivityEntry> getTrackerActivityEntries() {
		return trackerActivityEntries;
	}

	/**
	 * Method setTrackerActivityEntries.
	 * @param trackerActivityEntries Set<TrackerActivityEntry>
	 */
	public void setTrackerActivityEntries(
			Set<TrackerActivityEntry> trackerActivityEntries) {
		this.trackerActivityEntries = trackerActivityEntries;
	}
}
