package com.medisanaspace.web.main;

import java.util.ArrayList;

import com.medisanaspace.printer.ConsolePrinter;
import com.medisanaspace.printer.PrinterInterface;
import com.medisanaspace.printer.PrinterInterface.LoggerStatus;
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

	// Set your default printer here:
	public static final PrinterInterface printer = new ConsolePrinter(
			LoggerStatus.LOG_ALL);

	/*
	 * Set the serverType, user, mobile, maximal number of threads and printer
	 * parameter here. If user set to null, a new random user will be created on
	 * the server
	 */
	private static TestRunnerConfig newConfiguration = new TestRunnerConfig(
			ServerType.TEST_SERVER, new User("test.test", "test.test",
					"de_DE"), false, 1, CloudClient.printer);

	/**
	 * Method main.
	 * 
	 * @param args
	 *            String[]
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int numberOfEntries = 1;
		// Collection of tests
		TestRunner testRunner = new TestRunner(newConfiguration);
		ArrayList<AbstractTestTask> tests = new ArrayList<AbstractTestTask>();
		tests.add(new TrackerActivityAndTrackerSleepTestTask(numberOfEntries));
		tests.add(new ActivitydockTestTask(numberOfEntries));
		tests.add(new CardiodockTestTask(numberOfEntries));
		tests.add(new GluckodockTestTask(numberOfEntries));
		tests.add(new TargetscaleTestTask(numberOfEntries));
		tests.add(new ThermodockTestTask(numberOfEntries));
		tests.add(new TrackerPhaseTestTask(numberOfEntries));
		tests.add(new TrackerSleepTestTask(numberOfEntries));
		tests.add(new UserSettingsTestTask(numberOfEntries));
		
		testRunner.setTestTasks(tests);
		testRunner.runTests();
		
// retrieve latencies
//		for(AbstractTestTask task: tests){
//			task.getLatency()
//		}

	}

}
