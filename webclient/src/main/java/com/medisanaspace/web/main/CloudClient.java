package com.medisanaspace.web.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.medisanaspace.model.User;
import com.medisanaspace.printer.AbstractPrinter;
import com.medisanaspace.printer.AbstractPrinter.LoggerAction;
import com.medisanaspace.printer.MailNotificationPrinter;
import com.medisanaspace.printer.WebPrinter;
import com.medisanaspace.web.datatask.ActivitydockTestData;
import com.medisanaspace.web.datatask.CardiodockTestData;
import com.medisanaspace.web.datatask.GluckodockTestData;
import com.medisanaspace.web.datatask.TargetscaleTestData;
import com.medisanaspace.web.datatask.ThermodockTestData;
import com.medisanaspace.web.datatask.TrackerActivityAndTrackerSleepTestData;
import com.medisanaspace.web.library.WebConstants;
import com.medisanaspace.web.testconfig.AuthorizationModule;
import com.medisanaspace.web.testconfig.OAuthData;
import com.medisanaspace.web.testconfig.ServerType;
import com.medisanaspace.web.testconfig.TestRunner;
import com.medisanaspace.web.testconfig.TestRunnerConfig;
import com.medisanaspace.web.testtask.AbstractTestTask;
import com.medisanaspace.web.testtask.ActivitydockTestTask;
import com.medisanaspace.web.testtask.CardiodockTestTask;
import com.medisanaspace.web.testtask.GluckodockTestTask;
import com.medisanaspace.web.testtask.TargetscaleTestTask;
import com.medisanaspace.web.testtask.ThermodockTestTask;
import com.medisanaspace.web.testtask.TrackerActivityAndTrackerSleepTestTask;
import com.medisanaspace.web.testtask.TrackerPhaseTestTask;
import com.medisanaspace.web.testtask.TrackerStatsTestTask;
import com.medisanaspace.web.testtask.UserSettingsTestTask;

/**
 * Main class for the test program, configure the TestRunner that runs the tests
 * here and add your TestTasks.
 * 
 * @author Jan Krause (c) Medisana Space Technologies GmbH, 2013
 * 
 * 
 * @version $Revision: 1.0 $
 */
@Component
@Scope("session")
public class CloudClient implements Serializable{
	private static final long serialVersionUID = 1L;

	// define EnumSet by hand: EnumSet.of(LoggerAction.LOG_ERROR, ...,);
	public static AbstractPrinter printer = new WebPrinter(
			EnumSet.allOf(LoggerAction.class));

	/*
	 * Set the serverType, user, mobile, maximal number of threads and printer
	 * parameter here. If user set to null, a new random user will be created on
	 * the server
	 */
	private static TestRunnerConfig newConfiguration = new TestRunnerConfig(
			ServerType.TEST_SERVER,
			new User("test.test", "test.test", "en_GB"),false, false, 1,
			CloudClient.printer);

	private ArrayList<AbstractTestTask> testsToRun = new ArrayList<AbstractTestTask>();

	@Resource(name = "authorizationModuleBean")
	private AuthorizationModule authorizationModule;

	
	/**************************************************
	 * Mapping of view strings to objects
	 **************************************************/
	private final Map<String,LoggerAction> loggerActions = new HashMap<String, LoggerAction>();
	private final Map<String,ServerType> servers = new HashMap<String, ServerType>();
	private final Map<String,AbstractTestTask> availableTests = new HashMap<String, AbstractTestTask>();
	private final Map<String,AbstractTestTask> modulesToAddRandomData = new HashMap<String, AbstractTestTask>();
	public CloudClient() {
		int numberOfEntries = 1;
		// available tests	
		availableTests.put(WebConstants.TRACKER_ACTIVITY_MODULE, new TrackerActivityAndTrackerSleepTestTask(numberOfEntries));
		availableTests.put(WebConstants.ACTIVITYDOCK_MODULE, new ActivitydockTestTask(numberOfEntries));
		availableTests.put(WebConstants.CARDIODOCK_MODULE, new CardiodockTestTask(numberOfEntries));
		availableTests.put(WebConstants.GLUCODOCK_GLUCOSE_MODULE, new GluckodockTestTask(numberOfEntries));
		availableTests.put(WebConstants.TARGETSCALE_MODULE,new TargetscaleTestTask(numberOfEntries));
		availableTests.put(WebConstants.THERMODOCK_MODULE,new ThermodockTestTask(numberOfEntries));
		availableTests.put(WebConstants.TRACKER_PHASE_MODULE,new TrackerPhaseTestTask(numberOfEntries));
		availableTests.put(WebConstants.USER_SETTINGS_MODULE, new UserSettingsTestTask(numberOfEntries));
		availableTests.put(WebConstants.TRACKER_STATS_MODULE, new TrackerStatsTestTask(numberOfEntries));	
		// random data fixtures
		int numberOfRandomData = 100;
		modulesToAddRandomData.put(WebConstants.TRACKER_ACTIVITY_MODULE, new TrackerActivityAndTrackerSleepTestData(numberOfRandomData));
		modulesToAddRandomData.put(WebConstants.ACTIVITYDOCK_MODULE,new ActivitydockTestData(numberOfRandomData));
		modulesToAddRandomData.put(WebConstants.CARDIODOCK_MODULE,new CardiodockTestData(numberOfRandomData));
		modulesToAddRandomData.put(WebConstants.GLUCODOCK_GLUCOSE_MODULE,new GluckodockTestData(numberOfRandomData));
		modulesToAddRandomData.put(WebConstants.TARGETSCALE_MODULE, new TargetscaleTestData(numberOfRandomData));
		modulesToAddRandomData.put(WebConstants.THERMODOCK_MODULE,new ThermodockTestData(numberOfRandomData));
//		modulesToAddRandomData.put("TRACKER_PHASE_MODULE","Tracker Phase");

		loggerActions.put(WebConstants.LOG_ERROR, LoggerAction.LOG_ERROR);
		loggerActions.put(WebConstants.LOG_JSON_DATA, LoggerAction.LOG_JSON_DATA);
		loggerActions.put(WebConstants.LOG_ACTIVITY, LoggerAction.LOG_ACTIVITY);
		loggerActions.put(WebConstants.LOG_PROTOCOL_MESSAGE, LoggerAction.LOG_PROTOCOL_MESSAGE);
		loggerActions.put(WebConstants.LOG_MESSAGE, LoggerAction.LOG_MESSAGE);
		
		servers.put(WebConstants.PRODUCTION_SERVER, ServerType.PRODUCTION_SERVER);
		servers.put(WebConstants.TEST_SERVER, ServerType.TEST_SERVER);
		servers.put(WebConstants.LOCALHOST, ServerType.LOCALHOST);
	}

	

	/**
	 * First part of the OAuth handshake.
	 * 
	 * @return url with unauthorizedaccess token to the login page
	 * @throws Exception
	 */
	public String authorize(String server, boolean createNewUser, String userEmail, String userPassword,
			Set<String> loggerActionStrings, String applicationToken, String applicationSecret) throws Exception {
		
		EnumSet<LoggerAction> selectedloggerLevel = EnumSet.noneOf(LoggerAction.class);
		for(String loggerAction: loggerActionStrings){
			selectedloggerLevel.add(loggerActions.get(loggerAction));
		}
		CloudClient.printer = new WebPrinter(selectedloggerLevel);
		ServerType serverType = servers.get(server);
		User user =  new User(userEmail, userPassword, "en_GB");
		newConfiguration =  new TestRunnerConfig(serverType, user, createNewUser, 
				false, 1, CloudClient.printer);
		
		if(applicationSecret!="" && applicationToken!=""){
			newConfiguration.setAPPLICATION_TOKEN(applicationToken);
			newConfiguration.setAPPLICATION_SECRET(applicationSecret);
		}
		authorizationModule = new AuthorizationModule(newConfiguration);
		return authorizationModule.authorize();
	}

	/**
	 * Second part of the OAuth handshake.
	 * 
	 * @param verifierToken
	 * @return OAuthData to communicate with the server.
	 * @throws Exception
	 */
	public OAuthData authorizeWithVerifierToken(String verifierToken)
			throws Exception {
		return authorizationModule.authorizeWithVerifierToken(verifierToken);
	}

	/**
	 * Method runTests with advanced settings
	 * @param testList
	 * @param numberOfTestdataEntries
	 * @param server
	 * @param oauthdata
	 * @param loggerActionStrings
	 */
	public void runTests(List<String> testList, int numberOfTestdataEntries, OAuthData oauthdata, String server,String loggerType,Set<String> loggerActionStrings) {
		EnumSet<LoggerAction> selectedloggerLevel = EnumSet.noneOf(LoggerAction.class);
		for(String loggerAction: loggerActionStrings){
			selectedloggerLevel.add(loggerActions.get(loggerAction));
		}
	
		if(loggerType.equals(WebConstants.WEB_PRINTER)){
			CloudClient.printer = new WebPrinter(selectedloggerLevel);
		}else if(loggerType.equals(WebConstants.MAIL_PRINTER)){
			CloudClient.printer = new MailNotificationPrinter(selectedloggerLevel); 
		}
		
		ServerType serverType = servers.get(server);		
		// user null, because not needed
		newConfiguration =  new TestRunnerConfig(serverType, null, false, 
				false, 1, CloudClient.printer);
		
		runTests(testList, numberOfTestdataEntries, oauthdata);
	}
	
	/**
	 * Method to run a list of test with a preconfigured TestRunnerConfiguration
	 * @param testList
	 * @param numberOfTestdataEntries
	 * @param oauthdata
	 */
	public void runTests(List<String> testList, int numberOfTestdataEntries,OAuthData oauthdata) {

		// maybe configuration of server etc. here
		// get selected tests to run
		testsToRun.clear();
		for (String index : testList) {
			AbstractTestTask test = availableTests.get(index);
			test.setNumberOfEntries(numberOfTestdataEntries);
			testsToRun.add(test);
		}

		TestRunner testRunner = new TestRunner(newConfiguration, oauthdata);
		testRunner.setTestTasks(testsToRun);
		testRunner.runTests();

		// latencies
		CloudClient.printer.logMessage("Latencies:");
		if(authorizationModule!=null){
			CloudClient.printer.logMessage("Authorization Module");
			for(String latency: authorizationModule.getLatency()){
				CloudClient.printer.logMessage(latency+"ms");
			}
		}
		for (AbstractTestTask task : testsToRun) {
			CloudClient.printer.logMessage("</ br>"+task.getClass().getSimpleName());			
			for(String latency: task.getLatency()){
				CloudClient.printer.logMessage(latency+"ms");
			}
		}
		CloudClient.printer.endLog("Finished");
	}
	
	public void addRandomData(List<String> moduleList, OAuthData oauthdata, String server,String loggerType, Set<String> loggerActionStrings){
		testsToRun.clear();
		for (String index : moduleList) {
			testsToRun.add(availableTests.get(index));
		}
		
		EnumSet<LoggerAction> selectedloggerLevel = EnumSet.noneOf(LoggerAction.class);
		for(String loggerAction: loggerActionStrings){
			selectedloggerLevel.add(loggerActions.get(loggerAction));
		}
		
		if(loggerType.equals(WebConstants.WEB_PRINTER)){
			CloudClient.printer = new WebPrinter(selectedloggerLevel);
		}else if(loggerType.equals(WebConstants.MAIL_PRINTER)){
			CloudClient.printer = new MailNotificationPrinter(selectedloggerLevel); 
		}
		
		ServerType serverType = servers.get(server);		
		// user null, because not needed
		newConfiguration =  new TestRunnerConfig(serverType, null, false, 
				false, 1, CloudClient.printer);

		
		TestRunner testRunner = new TestRunner(newConfiguration, oauthdata);
		testRunner.setTestTasks(testsToRun);
		testRunner.runTests();
		

	}
	
	public String getMessageLog() {
		return ((WebPrinter)CloudClient.printer).getMessages();
	}

}
