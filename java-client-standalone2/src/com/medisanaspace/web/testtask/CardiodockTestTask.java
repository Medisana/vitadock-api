package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.Cardiodock;
import com.medisanaspace.model.fixture.CardiodockFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class CardiodockTestTask extends AbstractTestTask {

	public CardiodockTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	
	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	protected void executeTask() throws Exception{
		printer.logMessage("Cardiodock test");
		final List<Cardiodock> cardiodockList = new ArrayList<Cardiodock>();
		
		// generate random entries
		int index = 0;
		for (int i = 0; i < TestConstants.MAX_ENTRIES; i++) {
			index++;
			if (i == 0) {
				cardiodockList.add(new CardiodockFixture(index,
						TestConstants.MAX_ENTRIES, null).getCardiodock());
			} else {
				cardiodockList.add(new CardiodockFixture(index,
						TestConstants.MAX_ENTRIES, cardiodockList.get(i - 1))
						.getCardiodock());
			}
		}
		
		// write data to the server
		String responseCardiodock = saveJSONData(oauthData.getDeviceToken(),
				oauthData.getDeviceSecret(), Cardiodock.toJsonArray(cardiodockList),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
				oauthData.getAccessSecret());
		
		// retrieve the ids from the response

		Collection<String> idCardiodockList = StringUtil
				.fromJsonArrayToStrings(responseCardiodock);
		
		// count the data
		int countCardiodock = countData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
				oauthData.getAccessSecret());
		this.printer.logMessage("Data count: "+countCardiodock);
		
		// delete the data from the server
		deleteJSONData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
				oauthData.getAccessSecret(), idCardiodockList);

		// retrieve the data back from the server (note that the active flag is
		// set to zero because of the deletion process above)

		responseCardiodock = syncData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
				oauthData.getAccessSecret());

	}

}
