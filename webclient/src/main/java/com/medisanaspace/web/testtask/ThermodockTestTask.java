package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.Thermodock;
import com.medisanaspace.model.fixture.ThermodockFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class ThermodockTestTask extends AbstractTestTask {

	public ThermodockTestTask(int numberOfEntries) {
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
		printer.startDataSet("Thermodock test");
		String responseThermodock = saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				Thermodock.toJsonArray(thermodockList),
				AuthorizationBuilder.THERMODOCK_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		Collection<String> idThermodockList = StringUtil
				.fromJsonArrayToStrings(responseThermodock);

		// count the data

		int countThermodock = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.THERMODOCK_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		this.printer.logMessage("Data count" + countThermodock);

		deleteJSONData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.THERMODOCK_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret(), idThermodockList);

		responseThermodock = syncData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.THERMODOCK_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
	}

}
