package com.medisanaspace.web.datatask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.Thermodock;
import com.medisanaspace.model.fixture.ThermodockFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.testtask.AbstractTestTask;

/**
 */
public class ThermodockTestData extends AbstractTestTask {

	public ThermodockTestData(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {

		final List<Thermodock> thermodockList = new ArrayList<Thermodock>();
		int index = 0;
		for (int i = 0; i < numberOfEntries; i++) {
			index++;
			if (i == 0) {
				thermodockList.add(new ThermodockFixture(index,
						numberOfEntries, null).getThermodock());
			} else {
				thermodockList.add(new ThermodockFixture(index,
						numberOfEntries, thermodockList.get(i - 1))
						.getThermodock());
			}
		}
		printer.startDataSet("Fill Targetscale with "+numberOfEntries+" test data");		
		printer.logActivity("Writing data to the server");
		String responseThermodock = saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				Thermodock.toJsonArray(thermodockList),
				AuthorizationBuilder.THERMODOCK_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		// count the data

		int countThermodock = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.THERMODOCK_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		this.printer.logMessage("Data count" + countThermodock);
	}

}
