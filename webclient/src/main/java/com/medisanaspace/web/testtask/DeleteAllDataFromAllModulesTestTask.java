package com.medisanaspace.web.testtask;

import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 * Execute deletion of all data from all modules. Note: works only for the test
 * server ---
 * 
 * @author Jan
 * 
 * @version $Revision: 1.0 $
 */

public class DeleteAllDataFromAllModulesTestTask extends AbstractTestTask {

	public DeleteAllDataFromAllModulesTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {

		String deviceToken = oauthData.getDeviceToken();
		String deviceSecret = oauthData.getDeviceSecret();
		String accessToken = oauthData.getAccessToken();
		String accessSecret = oauthData.getAccessSecret();
		printer.startDataSet("Delete all data test");
		
		deleteAllDataFromModule(deviceToken, deviceSecret,
				AuthorizationBuilder.CARDIODOCK_MODULE_ID, accessToken,
				accessSecret);
		deleteAllDataFromModule(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
				accessSecret);
		deleteAllDataFromModule(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
				accessSecret);
		deleteAllDataFromModule(deviceToken, deviceSecret,
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
				accessSecret);
		deleteAllDataFromModule(deviceToken, deviceSecret,
				AuthorizationBuilder.TARGETSCALE_MODULE_ID, accessToken,
				accessSecret);
		deleteAllDataFromModule(deviceToken, deviceSecret,
				AuthorizationBuilder.THERMODOCK_MODULE_ID, accessToken,
				accessSecret);

	}

}
