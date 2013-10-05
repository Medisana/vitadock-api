package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.TrackerPhase;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class TrackerPhaseTestTask extends AbstractTestTask {

	public TrackerPhaseTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		printer.startDataSet("Trackerphase test");
		final List<TrackerPhase> trackerPhaseList = new ArrayList<TrackerPhase>();
		// read only
		String responseTrackerPhase = syncData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TRACKER_PHASE_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

	}

}
