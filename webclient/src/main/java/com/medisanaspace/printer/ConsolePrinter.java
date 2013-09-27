package com.medisanaspace.printer;

import java.util.EnumSet;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Concrete Console Printer.
 * 
 */
public class ConsolePrinter extends AbstractPrinter {
	private EnumSet<LoggerAction> loggerActionSet;

	/**
	 * Constructor for ConsolePrinter.
	 * 
	 * @param loggerStatus
	 *            LoggerStatus
	 */
	public ConsolePrinter(EnumSet<LoggerAction> set) {
		super(set);
	}

	/**
	 * Method startLog.
	 * 
	 * @param startMessage
	 *            String
	 * @see com.medisanaspace.printer.PrinterInterface#startLog(String)
	 */
	@Override
	public void startLog(String startMessage) {
		if (loggerActionSet.contains(LoggerAction.LOG_MESSAGE))
			System.out.println("Start logging: " + startMessage
					+ "\n -----------");
	}

	/**
	 * Method endLog.
	 * 
	 * @param endMessage
	 *            String
	 * @see com.medisanaspace.printer.PrinterInterface#endLog(String)
	 */
	@Override
	public void endLog(String endMessage) {
		if (loggerActionSet.contains(LoggerAction.LOG_MESSAGE))
			System.out.println("End of log: " + endMessage + "\n -----------");
	}

	/**
	 * This method is not used in the console printer.
	 * 
	 * @param s
	 *            String
	 * @see com.medisanaspace.printer.PrinterInterface#startDataSet(String)
	 */
	@Override
	public void startDataSet(String s) {
		// not needed in the console printer
	}

	/**
	 * Method logMessage.
	 * 
	 * @param message
	 *            String
	 * @see com.medisanaspace.printer.PrinterInterface#logMessage(String)
	 */
	@Override
	public void logMessage(String message) {
		if (loggerActionSet.contains(LoggerAction.LOG_MESSAGE))
			System.out.println(message);
	}

	/**
	 * Method logData.
	 * 
	 * @param data
	 *            String
	 * @see com.medisanaspace.printer.PrinterInterface#logJSONData(String)
	 */
	@Override
	public void logJSONData(String data) {
		if (loggerActionSet.contains(LoggerAction.LOG_JSON_DATA))
			System.out.println("    Response: " + data + "\n--------------");

	}

	/**
	 * Method logError.
	 * 
	 * @param e
	 *            String
	 * @see com.medisanaspace.printer.PrinterInterface#logError(String)
	 */
	@Override
	public void logError(String e) {
		if (loggerActionSet.contains(LoggerAction.LOG_ERROR))
			System.out.println(" ------------ \n An Error occured: " + e
					+ "\n -------------");
	}

	/**
	 * Method logError.
	 * 
	 * @param e
	 *            String
	 * @param exception
	 *            Exception
	 * @see com.medisanaspace.printer.PrinterInterface#logError(String,
	 *      Exception)
	 */
	@Override
	public void logError(String e, Exception exception) {
		if (loggerActionSet.contains(LoggerAction.LOG_ERROR)) {
			System.out.println(" ------------ \n An Error occured: " + e
					+ "\n Message: " + exception + "\n -------------");
			exception.printStackTrace();
		}
	}

	/**
	 * Method logPost.
	 * 
	 * @param httppost
	 *            HttpRequestBase
	 * @see com.medisanaspace.printer.PrinterInterface#logPost(HttpRequestBase)
	 */
	@Override
	public void logPost(final HttpRequestBase httppost) {
		if (loggerActionSet.contains(LoggerAction.LOG_PROTOCOL_MESSAGE)) {
			System.out.println("Request " + httppost.getMethod() + " URL:"
					+ httppost.getURI());
			for (Header header : httppost.getAllHeaders()) {
				System.out.println("    " + header.getName() + " : "
						+ header.getValue());
			}
			System.out.println('\n');
		}
	}

	public void logProtocolMessages(final String s) {
		if (loggerActionSet.contains(LoggerAction.LOG_PROTOCOL_MESSAGE)) {
			System.out.println(s);
		}
	}

	@Override
	public void logActivity(String activity) {
		if (loggerActionSet.contains(LoggerAction.LOG_ACTIVITY)) {
			System.out.println(activity);
		}

	}

}
