package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.List;

import com.medisanaspace.model.Glucodockglucose;
import com.medisanaspace.model.Glucodockinsulin;
import com.medisanaspace.model.Glucodockmeal;
import com.medisanaspace.model.fixture.GlucodockglucoseFixture;
import com.medisanaspace.model.fixture.GlucodockinsulinFixture;
import com.medisanaspace.model.fixture.GlucodockmealFixture;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class GluckodockTestTask extends AbstractTestTask {

	public GluckodockTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		final List<Glucodockglucose> glucodockglucoseList = new ArrayList<Glucodockglucose>();
		final List<Glucodockinsulin> glucodockinsulinList = new ArrayList<Glucodockinsulin>();
		final List<Glucodockmeal> glucodockmealList = new ArrayList<Glucodockmeal>();

		// fill with "maxEntries" random entries
		int index = 0;
		for (int i = 0; i < numberOfEntries; i++) {
			index++;
			if (i == 0) {
				glucodockglucoseList.add(new GlucodockglucoseFixture(index,
						numberOfEntries, null).getGlucodockglucose());

				glucodockinsulinList.add(new GlucodockinsulinFixture(index,
						numberOfEntries).getGlucodockinsulin());

				glucodockmealList.add(new GlucodockmealFixture(index,
						numberOfEntries).getGlucodockmeal());

			} else {
				glucodockglucoseList.add(new GlucodockglucoseFixture(index,
						numberOfEntries, glucodockglucoseList.get(i - 1))
						.getGlucodockglucose());

				glucodockinsulinList.add(new GlucodockinsulinFixture(index,
						numberOfEntries).getGlucodockinsulin());

				glucodockmealList.add(new GlucodockmealFixture(index,
						numberOfEntries).getGlucodockmeal());
			}
		}
		printer.startDataSet("Gluckodock Glucose test");
		StandardCRUDTestTask crudtest = new StandardCRUDTestTask(
				numberOfEntries,
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID,
				Glucodockglucose.toJsonArray(glucodockglucoseList));

		crudtest.setPrinter(printer);
		crudtest.setServerConfig(this.getServerConfig());
		crudtest.setOauthData(this.getOauthData());
		crudtest.executeTask();

		printer.startDataSet("Gluckodock Insulin test");
		crudtest = new StandardCRUDTestTask(numberOfEntries,
				AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID,
				Glucodockinsulin.toJsonArray(glucodockinsulinList));

		crudtest.setPrinter(printer);
		crudtest.setServerConfig(this.getServerConfig());
		crudtest.setOauthData(this.getOauthData());
		crudtest.executeTask();
		
		printer.startDataSet("Gluckodock Meal test");
		crudtest = new StandardCRUDTestTask(numberOfEntries,
				AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID,
				Glucodockmeal.toJsonArray(glucodockmealList));

		crudtest.setPrinter(printer);
		crudtest.setServerConfig(this.getServerConfig());
		crudtest.setOauthData(this.getOauthData());
		crudtest.executeTask();

		// printer.startDataSet("Gluckodock test");
		// printer.logActivity("Counting data before the test.");
		// // count data
		// int preTestcountGlucodockglucose = countData(deviceToken,
		// deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
		// accessSecret);
		// int preTestcountGlucodockinsulin = countData(deviceToken,
		// deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
		// accessSecret);
		// int preTestcountGlucodockmeal = countData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
		// accessSecret);
		//

		// printer.logActivity("Saving Gluckodock glucose data on the server.");
		// String responseGlucodockglucose = saveJSONData(deviceToken,
		// deviceSecret,
		// Glucodockglucose.toJsonArray(glucodockglucoseList),
		// AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
		// accessSecret);
		// printer.logActivity("Saving Gluckodock insulin data on the server.");
		// String responseGlucodockinsulin = saveJSONData(deviceToken,
		// deviceSecret,
		// Glucodockinsulin.toJsonArray(glucodockinsulinList),
		// AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
		// accessSecret);
		// printer.logActivity("Saving Gluckodock meal data on the server.");
		// String responseGlucodockmeal = saveJSONData(deviceToken,
		// deviceSecret,
		// Glucodockmeal.toJsonArray(glucodockmealList),
		// AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
		// accessSecret);
		//
		// // retrieve the ids from the response
		// Collection<String> idGlucodockglucoseList = StringUtil
		// .fromJsonArrayToStrings(responseGlucodockglucose);
		// Collection<String> idGlucodockinsulinList = StringUtil
		// .fromJsonArrayToStrings(responseGlucodockinsulin);
		// Collection<String> idGlucodockmealList = StringUtil
		// .fromJsonArrayToStrings(responseGlucodockmeal);
		//
		// // count data
		// int countGlucodockglucose = countData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
		// accessSecret);
		// int countGlucodockinsulin = countData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
		// accessSecret);
		// int countGlucodockmeal = countData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
		// accessSecret);
		//
		// if(countGlucodockglucose-preTestcountGlucodockglucose !=
		// numberOfEntries){
		// printer.logError("Wrong data count after writing to the server: "+countGlucodockglucose);
		// throw new
		// Exception("Glucodock glucose data are not successfully saved on the server!");
		// }
		//
		// if(countGlucodockinsulin-preTestcountGlucodockinsulin !=
		// numberOfEntries){
		// printer.logError("Wrong data count after writing to the server: "+countGlucodockinsulin);
		// throw new
		// Exception("Glucodock insulin data are not successfully saved on the server!");
		// }
		//
		// if(countGlucodockmeal-preTestcountGlucodockmeal != numberOfEntries){
		// printer.logError("Wrong data count after writing to the server: "+countGlucodockmeal);
		// throw new
		// Exception("Glucodock meal data are not successfully saved on the server!");
		// }
		//
		// this.printer.logMessage("Data count: " + countGlucodockglucose + ", "
		// + countGlucodockinsulin + ", " + countGlucodockmeal);
		//
		// // delete the data from the server
		// printer.logActivity("Deleting Gluckodock glucose data from the server.");
		// deleteJSONData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
		// accessSecret, idGlucodockglucoseList);
		// printer.logActivity("Deleting Gluckodock insulin data from the server.");
		// deleteJSONData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
		// accessSecret, idGlucodockinsulinList);
		// printer.logActivity("Deleting Gluckodock meal data from the server.");
		// deleteJSONData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
		// accessSecret, idGlucodockmealList);
		//
		// // retrieve the data back from the server (note that the active flag
		// is
		// // set to zero if you uncomment the deletion process above)
		//
		// responseGlucodockglucose = syncData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID, accessToken,
		// accessSecret);
		// responseGlucodockinsulin = syncData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID, accessToken,
		// accessSecret);
		// responseGlucodockmeal = syncData(deviceToken, deviceSecret,
		// AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID, accessToken,
		// accessSecret);
		//
		// idGlucodockglucoseList = StringUtil
		// .fromJsonArrayToStrings(responseGlucodockglucose);
		// idGlucodockinsulinList = StringUtil
		// .fromJsonArrayToStrings(responseGlucodockinsulin);
		// idGlucodockmealList = StringUtil
		// .fromJsonArrayToStrings(responseGlucodockmeal);
		//
		// if(idGlucodockglucoseList.size() == countGlucodockglucose){
		// printer.logError("Wrong data count after deleting the glucose test data from the server: "+idGlucodockglucoseList.size());
		// throw new
		// Exception("Glucodock glucose data are not deleted successfully from the server");
		// }
		//
		// if(idGlucodockinsulinList.size() == countGlucodockinsulin){
		// printer.logError("Wrong data count after deleting the insulin test data from the server: "+idGlucodockinsulinList.size());
		// throw new
		// Exception("Glucodock insulin data are not deleted successfully from the server");
		// }
		//
		// if(idGlucodockmealList.size() == countGlucodockmeal){
		// printer.logError("Wrong data count after deleting the meal test data from the server: "+idGlucodockmealList.size());
		// throw new
		// Exception("Glucodock meal data are not deleted successfully from the server");
		// }

	}

}
