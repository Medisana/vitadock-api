package com.medisanaspace.web.datatask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.Targetscale;
import com.medisanaspace.model.fixture.TargetscaleFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.testtask.AbstractTestTask;

/**
 */
public class TargetscaleTestData extends AbstractTestTask {

	public TargetscaleTestData(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		printer.startDataSet("Fill Targetscale with "+numberOfEntries+" test data");
		final List<Targetscale> targetscaleList = new ArrayList<Targetscale>();
		int index = 0;
		for (int i = 0; i < numberOfEntries; i++) {
			index++;
			if (i == 0) {
				targetscaleList.add(new TargetscaleFixture(index,
						numberOfEntries, null).getTargetscale());
			} else {
				targetscaleList.add(new TargetscaleFixture(index,
						numberOfEntries, targetscaleList.get(i - 1))
						.getTargetscale());
			}
		}
		printer.logActivity("Writing Targetscale data to the server");
		String responseTargetscale = saveJSONData(oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				Targetscale.toJsonArray(targetscaleList),
				AuthorizationBuilder.TARGETSCALE_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		int countTargetscale = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.TARGETSCALE_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		this.printer.logMessage("Data count: " + countTargetscale);

	}

}
