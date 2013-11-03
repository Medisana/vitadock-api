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
	@Override
	protected void executeTask() throws Exception{
		printer.startDataSet("Cardiodock test");
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
		
		printer.logActivity("Counting data on the server before the test.");
		int preTestcountCardiodock = countData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
					AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
					oauthData.getAccessSecret());
		
		printer.logActivity("Saving the JSON data on the server.");
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
		if(countCardiodock-preTestcountCardiodock!= numberOfEntries){
			printer.logError("Data count after writing to the server is wrong: "+countCardiodock);
			throw new Exception("Wrong data count after writing to the server!");
		}
		this.printer.logMessage("Data count: "+countCardiodock);
		
		
		printer.logActivity("Deleting the Cardiodock JSON data on the server.");
		deleteJSONData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
				oauthData.getAccessSecret(), idCardiodockList);

		// retrieve the data back from the server (note that the active flag is
		// set to zero because of the deletion process above)

		responseCardiodock = syncData(oauthData.getDeviceToken(), oauthData.getDeviceSecret(),
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, oauthData.getAccessToken(),
				oauthData.getAccessSecret());
		idCardiodockList = StringUtil.fromJsonArrayToStrings(responseCardiodock);
		if(idCardiodockList.size()!=preTestcountCardiodock){
			throw new Exception("Not all Cardiodock data deleted from the server");
		}
	}

}
