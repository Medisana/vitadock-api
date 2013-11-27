package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.List;

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
	 * 
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
		printer.startDataSet("Thermodock test.");
		
		StandardCRUDTestTask crudtest = new StandardCRUDTestTask(
				numberOfEntries, AuthorizationBuilder.THERMODOCK_MODULE_ID,
				Thermodock.toJsonArray(thermodockList));
		crudtest.setPrinter(printer);
		crudtest.setServerConfig(this.getServerConfig());
		crudtest.setOauthData(this.getOauthData());
		crudtest.executeTask();

	}

}
