package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.TrackerPhase;
import com.medisanaspace.model.fixture.TrackerPhaseFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class TrackerPhaseTestTask extends AbstractTestTask {

	public TrackerPhaseTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		printer.startDataSet("Trackerphase test.");
		final List<TrackerPhase> trackerPhaseList = new ArrayList<TrackerPhase>();

		for (int i = 0; i < numberOfEntries; i++) {
			trackerPhaseList.add(new TrackerPhaseFixture(i, numberOfEntries)
					.getTrackerPhase());
		}

		StandardCRUDTestTask crudtest = new StandardCRUDTestTask(
				numberOfEntries, AuthorizationBuilder.TRACKER_PHASE_MODULE_ID,
				TrackerPhase.toJsonArray(trackerPhaseList));

		crudtest.setPrinter(printer);
		crudtest.setServerConfig(this.getServerConfig());
		crudtest.setOauthData(this.getOauthData());
		crudtest.executeTask();
	}

}
