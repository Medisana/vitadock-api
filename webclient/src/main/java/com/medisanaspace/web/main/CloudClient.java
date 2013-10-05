package com.medisanaspace.web.main;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.medisanaspace.printer.AbstractPrinter;
import com.medisanaspace.printer.AbstractPrinter.LoggerAction;
import com.medisanaspace.printer.WebPrinter;
import com.medisanaspace.web.library.AuthorizationBuilder;
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
import com.medisanaspace.web.testtask.TrackerSleepTestTask;
import com.medisanaspace.web.testtask.User;
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
public class CloudClient {

	// Webprinter
	// define EnumSet by hand: EnumSet.of(LoggerAction.LOG_ERROR, ...,);
	public static final AbstractPrinter printer = new WebPrinter(
			EnumSet.allOf(LoggerAction.class));

	/*
	 * Set the serverType, user, mobile, maximal number of threads and printer
	 * parameter here. If user set to null, a new random user will be created on
	 * the server
	 */
	private static TestRunnerConfig newConfiguration = new TestRunnerConfig(
			ServerType.TEST_SERVER,
			new User("test.test", "test.test", "en_GB"), false, 1,
			CloudClient.printer);

	private ArrayList<AbstractTestTask> testsToRun = new ArrayList<AbstractTestTask>();

	@Resource(name = "authorizationModuleBean")
	private AuthorizationModule authorizationModule;

//	private String messageLog = "";

	public CloudClient() {
	}

	/**
	 * First part of the OAuth handshake.
	 * 
	 * @return url with unauthorizedaccess token to the login page
	 * @throws Exception
	 */
	public String authorize() throws Exception {
		((WebPrinter)CloudClient.printer).clearLog();
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

	public void runTests(List<String> testList, OAuthData oauthdata) {

		// maybe configuration of server etc. here
		int numberOfEntries = 1;

		// available tests
		final Map<String,AbstractTestTask> tests = new HashMap<String, AbstractTestTask>();
		tests.put(String.valueOf(AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID), new TrackerActivityAndTrackerSleepTestTask(numberOfEntries));
		tests.put(String.valueOf(AuthorizationBuilder.ACTIVITY_MODULE_ID), new ActivitydockTestTask(numberOfEntries));
		tests.put(String.valueOf(AuthorizationBuilder.CARDIODOCK_MODULE_ID), new CardiodockTestTask(numberOfEntries));
		tests.put(String.valueOf(AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID), new GluckodockTestTask(numberOfEntries));
		tests.put(String.valueOf(AuthorizationBuilder.TARGETSCALE_MODULE_ID),new TargetscaleTestTask(numberOfEntries));
		tests.put(String.valueOf(AuthorizationBuilder.THERMODOCK_MODULE_ID),new ThermodockTestTask(numberOfEntries));
		tests.put(String.valueOf(AuthorizationBuilder.TRACKER_PHASE_MODULE_ID),new TrackerPhaseTestTask(numberOfEntries));
		tests.put(String.valueOf(AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID), new TrackerSleepTestTask(numberOfEntries));
		tests.put(String.valueOf(AuthorizationBuilder.USER_SETTINGS_MODULE_ID), new UserSettingsTestTask(numberOfEntries));
	
		// get selected tests to run
		testsToRun.clear();
		for (String index : testList) {
			testsToRun.add(tests.get(index));
		}

		TestRunner testRunner = new TestRunner(newConfiguration, oauthdata);
		testRunner.setTestTasks(testsToRun);
		testRunner.runTests();
		
		// latencies
		CloudClient.printer.logMessage("Latencies:");
		
		CloudClient.printer.logMessage("Authorization Module");
		for(String latency: authorizationModule.getLatency()){
			CloudClient.printer.logMessage(latency+"ms");
		}
		
		for (AbstractTestTask task : testsToRun) {
			CloudClient.printer.logMessage("</ br>"+task.getClass().getSimpleName());			
			for(String latency: task.getLatency()){
				CloudClient.printer.logMessage(latency+"ms");
			}
		}
			
		//messageLog = ((WebPrinter) printer).getMessages();
	

	}

	public String getMessageLog() {
		return ((WebPrinter)CloudClient.printer).getMessages();
	}

}
