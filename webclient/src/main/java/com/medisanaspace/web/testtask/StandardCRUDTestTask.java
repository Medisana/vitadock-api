package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.medisanaspace.library.StringUtil;

/**
 * Tests standard methods:
 * - count
 * - save
 * - load
 * - delete
 */
public class StandardCRUDTestTask extends AbstractTestTask {
	
	protected int moduleID;
	protected String fixtureJSONString;
	/**
	 * 
	 * @param numberOfEntries
	 * @param moduleid
	 * @param fixtureJSONString are test date in JSON format. They will be written to the server.
	 */
	public StandardCRUDTestTask(int numberOfEntries, int moduleid, String fixtureJSONString) {
		super(numberOfEntries);
		this.moduleID = moduleid;
		this.fixtureJSONString = fixtureJSONString;
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		
		printer.logActivity("Counting data before the test.");	
		int preTestcount = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				moduleID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		printer.logMessage("There are now "+preTestcount+" datasets on the server.");
		
		printer.logActivity("Saving: "+numberOfEntries+" data on the server.");
		printer.logJSONData(fixtureJSONString);
		String response = saveJSONData(
				this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				fixtureJSONString,
				moduleID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		ArrayList<String> idList = StringUtil
				.fromJsonArrayToStrings(response);

		// count the data
		printer.logActivity("Counting data after the test.");
		int countData = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				moduleID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		
		if(countData-preTestcount!=numberOfEntries){
			printer.logError("Data count after writing to the server is wrong: "+countData);
			throw new Exception("Wrong data count after writing to the server!");
		}else{
			this.printer.logMessage("Data count " + countData+" correct.");
		}
		
		printer.logActivity("Loading data from the server.");
		response = loadData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				moduleID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		printer.logJSONData(response);
		Collection<Map<String, String>> result = StringUtil.fromJsonArrayToMap(response);
		int numberDataAfterSaving = result.size();
		printer.logMessage("Number  : "+numberDataAfterSaving);
		
		printer.logActivity("Verifying test data on the server.");

		// verify if the IDs are in the retrieved dataset
		boolean[] testDataFound = new boolean[numberOfEntries];
		Arrays.fill(testDataFound, Boolean.FALSE);
		Iterator<Map<String, String>> iterator = result.iterator();
		while(iterator.hasNext()){
			Map<String, String> jsonMap = iterator.next();
			for(int i=0; i < idList.size();i++){
				if(jsonMap.containsValue(idList.get(i))){
					testDataFound[i]=true;
				}
			}
		}
		boolean allComplete=true;
		for(int i=0; i<numberOfEntries;i++){
			if(testDataFound[i]!=true){
				allComplete=false;
			}
		}
		if(!allComplete){
			printer.logError("At least one test dataset was not found on the server.");
		}
		printer.logMessage("Test data verified.");
		
		printer.logActivity("Deleting data from the server.");
		deleteJSONData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				moduleID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret(), idList);
		
		printer.logActivity("Counting active data on the server after deletion.");

		response = loadData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				moduleID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		printer.logJSONData(response);
		result = StringUtil.fromJsonArrayToMap(response);
		int numberDataAfterDeletion = result.size();
		printer.logMessage("Data count : "+numberDataAfterDeletion);
		
		if(numberDataAfterDeletion != numberDataAfterSaving-numberOfEntries){
			printer.logError("Wrong data count after writing to the server: "+countData);
			throw new Exception("Data are not successfully saved to the server!");
		}
		
	}

}
