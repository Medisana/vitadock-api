package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.UserSettings;
import com.medisanaspace.model.fixture.UserSettingsFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class UserSettingsTestTask extends AbstractTestTask {

	public UserSettingsTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		final List<UserSettings> userSettingsList = new ArrayList<UserSettings>();

		for (int i = 0; i < numberOfEntries; i++) {
			userSettingsList.add(new UserSettingsFixture().getUserSettings());
		}
		String responseUserSettings = saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				UserSettings.toJsonArray(userSettingsList),
				AuthorizationBuilder.USER_SETTINGS_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		// retrieve the ids from the response

		int countUserSettings = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.USER_SETTINGS_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		this.printer.logMessage("Data count: " + countUserSettings);
		responseUserSettings = syncData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.USER_SETTINGS_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

	}

}
