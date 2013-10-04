package com.medisanaspace.web.testconfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.medisanaspace.web.main.CloudClient;
import com.medisanaspace.web.testtask.AbstractTestTask;

/**
 * The TestRunner runs all the testTasks given to him. He also handles the the
 * token and secret handshakes.
 * 
 * @author Jan Krause (c) Medisana Space Technologies GmbH, 2013
 * 
 * @version $Revision: 1.0 $
 */
public class TestRunner {

	private ArrayList<AbstractTestTask> testTasks = new ArrayList<AbstractTestTask>();
	private TestRunnerConfig testRunnerConfig;
	private OAuthData oAuthData;

	/**
	 * Constructor for TestRunner.
	 * 
	 * @param testRunnerConfig
	 *            TestRunnerConfig
	 * @param oAuthData
	 *            OAuthData for the communication with the server
	 * @throws Exception
	 */
	public TestRunner(TestRunnerConfig testRunnerConfig, OAuthData oAuthData) {
		this.testRunnerConfig = testRunnerConfig;
		this.oAuthData = oAuthData;
	}

	/**
	 * runTests runs the test with the given TestRunnerConfig
	 */
	public void runTests() {
		CloudClient.printer.startLog("Start with tests");
		try {
			// OAuthData oAuthData = retrieveOAuthData();
			testRunnerConfig.setOauthData(oAuthData);
		} catch (Exception e) {
			CloudClient.printer.logError("Error when retrieving oAuth data: ",
					e);
		}

		ExecutorService executor = Executors
				.newFixedThreadPool(testRunnerConfig.getNumberOfThreads());
		// run tests
		for (AbstractTestTask task : testTasks) {
			task.setServerConfig(testRunnerConfig.getServerConfig());
			task.setOauthData(testRunnerConfig.getOauthData());
			task.setPrinter(testRunnerConfig.getPrinter());
			executor.execute(task);
	
		}

		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			CloudClient.printer.logError("thread executor error", e);

		}
		
		
		
		CloudClient.printer.endLog("Finished");
		
	}

	// /**
	// * Method readLine.
	// * @return String
	// */
	// protected static String readLine() {
	// String string = "";
	// try {
	// InputStreamReader converter = new InputStreamReader(System.in);
	// BufferedReader in = new BufferedReader(converter);
	// string = in.readLine();
	// } catch (Exception e) {
	// CloudClient.printer.logError("Error! Exception: ", e);
	// }
	// return string;
	// }
	private static String readLine() {
		String string = "";
		try {
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			string = in.readLine();
		} catch (Exception e) {
			System.out.println("Error! Exception: " + e);
		}
		return string;
	}

	/**
	 * Method setTestTasks.
	 * 
	 * @param testTasks
	 *            ArrayList<AbstractTestTask>
	 */
	public void setTestTasks(ArrayList<AbstractTestTask> testTasks) {
		this.testTasks = testTasks;
	}

}