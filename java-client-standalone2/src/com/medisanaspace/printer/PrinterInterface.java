package com.medisanaspace.printer;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Used to print logs and protocols.
 * Derive your printer classes to log in csv format,  etc.
 * 
 * @author Jan Krause 
 *
 * @version $Revision: 1.0 $
 */
public interface PrinterInterface {
	/**
	 * Indicate the granularity of the log.
	 * Your printer should add a local attribute to set the setting.
	 */
	public enum LoggerStatus {
		LOG_ALL, LOG_ERROR_ONLY,  LOG_DISABLED;
	}

	/**
	 * Called to indicate that a new log/testrun is started.
	 * @param s Message to start the log with
	 */
	public void startLog(String s);
	/**
	 Called to indicate that a log/testrun has finished.
	 * @param s Message to indicate the end of a log.
	 */
	public void endLog(String s);
	
	/**
	 * Start a new data set in the same log (e.g. data from another person).
	 * @param s Separator message.
	 */
	public void startDataSet(String s);
	
	//  
	/**
	 * Messages during the tests.
	 * @param message String
	 */
	public void logMessage(String message);
	
	
	/**
	 * Log arriving data. 
	 * @param data String
	 */
	public void logData(String data);
	
	// 
	/**
	 * Log protocol based HttpRequestBase message (post/get requests).
	 * @param httppost HttpRequestBase
	 */
	public void logPost(final HttpRequestBase httppost);

	/**
	 * Log an error message.
	 * @param e String
	 */
	public void logError(String e);
	/**
	 * Log an error message and the thrown exception.
	 * @param e String
	 * @param exception Exception
	 */
	public void logError(String e, Exception exception);
	
}
