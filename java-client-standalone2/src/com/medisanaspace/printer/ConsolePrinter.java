package com.medisanaspace.printer;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Concrete Console Printer.
 * 
 */
public  class ConsolePrinter implements PrinterInterface {
	private LoggerStatus loggerStatus;
	
	/**
	 * Constructor for ConsolePrinter.
	 * @param loggerStatus LoggerStatus
	 */
	public ConsolePrinter(LoggerStatus loggerStatus) {
		this.loggerStatus = loggerStatus;
	}
	
	/**
	 * Method startLog.
	 * @param startMessage String
	 * @see com.medisanaspace.printer.PrinterInterface#startLog(String)
	 */
	public void startLog(String startMessage) {
		if (loggerStatus.equals(LoggerStatus.LOG_ALL))
			System.out.println("Start logging: "+ startMessage +"\n -----------");
	}

	/**
	 * Method endLog.
	 * @param endMessage String
	 * @see com.medisanaspace.printer.PrinterInterface#endLog(String)
	 */

	public void endLog(String endMessage) {
		if (loggerStatus.equals(LoggerStatus.LOG_ALL))
			System.out.println("End of log: "+ endMessage +"\n -----------");
	}

	/**
	 * This method is not used in the console printer.
	 * @param s String
	 * @see com.medisanaspace.printer.PrinterInterface#startDataSet(String)
	 */

	public void startDataSet(String s) {
		// not needed in the console printer
	}

	/**
	 * Method logMessage.
	 * @param message String
	 * @see com.medisanaspace.printer.PrinterInterface#logMessage(String)
	 */
	public void logMessage(String message){
	if (loggerStatus.equals(LoggerStatus.LOG_ALL))
		System.out.println(message);
	}

	/**
	 * Method logData.
	 * @param data String
	 * @see com.medisanaspace.printer.PrinterInterface#logData(String)
	 */
	public void logData(String data) {
		if (loggerStatus.equals(LoggerStatus.LOG_ALL))
			System.out.println("    Response: " + data + "\n--------------");

	}

	/**
	 * Method logError.
	 * @param e String
	 * @see com.medisanaspace.printer.PrinterInterface#logError(String)
	 */
	public void logError(String e) {
		if (!loggerStatus.equals(LoggerStatus.LOG_DISABLED))
			System.out.println(" ------------ \n An Error occured: "+ e +"\n -------------");
	}

	/**
	 * Method logError.
	 * @param e String
	 * @param exception Exception
	 * @see com.medisanaspace.printer.PrinterInterface#logError(String, Exception)
	 */
	public void logError(String e, Exception exception) {
		if (!loggerStatus.equals(LoggerStatus.LOG_DISABLED)){
			System.out.println(" ------------ \n An Error occured: "+ e +
							"\n Message: "+ exception +"\n -------------");
			exception.printStackTrace();
		}
	}

	/**
	 * Method logPost.
	 * @param httppost HttpRequestBase
	 * @see com.medisanaspace.printer.PrinterInterface#logPost(HttpRequestBase)
	 */
	public void logPost(final HttpRequestBase httppost) {
		if (loggerStatus.equals(LoggerStatus.LOG_ALL)){		
			System.out.println("Request " + httppost.getMethod() + " URL:"
					+ httppost.getURI());
			for (Header header : httppost.getAllHeaders()) {
				System.out.println("    " + header.getName() + " : "
						+ header.getValue());
			}
			System.out.println('\n');
		}
	}

}
