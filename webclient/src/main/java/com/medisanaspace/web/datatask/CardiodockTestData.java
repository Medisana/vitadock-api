package com.medisanaspace.web.datatask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.Cardiodock;
import com.medisanaspace.model.fixture.CardiodockFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.testtask.AbstractTestTask;

/**
 */
public class CardiodockTestData extends AbstractTestTask {

	public CardiodockTestData(int numberOfEntries) {
		super(numberOfEntries);
	}

	
	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception{
		printer.startDataSet("Fill Cardiodock with "+numberOfEntries+" test data");
		final List<Cardiodock> cardiodockList = new ArrayList<Cardiodock>();
		
		// generate random entries
		int index = 0;
		for (int i = 0; i < numberOfEntries; i++) {
			index++;
			if (i == 0) {
				cardiodockList.add(new CardiodockFixture(index,
						numberOfEntries, null).getCardiodock());
			} else {
				cardiodockList.add(new CardiodockFixture(index,
						numberOfEntries, cardiodockList.get(i - 1))
						.getCardiodock());
			}
		}
		
		printer.logActivity("Writing data to the server");
		// write data to the server
		String responseCardiodock = saveJSONData(oauthData.getDeviceToken(),
				oauthData.getDeviceSecret(), Cardiodock.toJsonArray(cardiodockList),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
				oauthData.getAccessSecret());
		// count the data
		int countCardiodock = countData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
				oauthData.getAccessSecret());
		this.printer.logMessage("Data count: "+countCardiodock);
		


	}

}
