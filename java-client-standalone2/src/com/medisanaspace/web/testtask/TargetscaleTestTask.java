package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.Targetscale;
import com.medisanaspace.model.fixture.TargetscaleFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class TargetscaleTestTask extends AbstractTestTask {

	public TargetscaleTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {

		final List<Targetscale> targetscaleList = new ArrayList<Targetscale>();
		int index = 0;
		for (int i = 0; i < TestConstants.MAX_ENTRIES; i++) {
			index++;
			if (i == 0) {
				targetscaleList.add(new TargetscaleFixture(index,
						TestConstants.MAX_ENTRIES, null).getTargetscale());
			} else {
				targetscaleList.add(new TargetscaleFixture(index,
						TestConstants.MAX_ENTRIES, targetscaleList.get(i - 1))
						.getTargetscale());
			}
		}
		String responseTargetscale = saveJSONData(oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				Targetscale.toJsonArray(targetscaleList),
				AuthorizationBuilder.TARGETSCALE_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		Collection<String> idTargetscaleList = StringUtil
				.fromJsonArrayToStrings(responseTargetscale);
		int countTargetscale = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TARGETSCALE_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		this.printer.logMessage("Data count: " + countTargetscale);

		deleteJSONData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TARGETSCALE_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret(), idTargetscaleList);
		responseTargetscale = syncData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TARGETSCALE_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
	}

}
