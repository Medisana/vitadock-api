package com.medisanaspace.printer;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Concrete Console Printer.
 * 
 */
public  class WebPrinter implements PrinterInterface {
	private LoggerStatus loggerStatus;
	private String messages = "";
	
	public String getMessages(){
		return messages;
	}
	
	/**
	 * Constructor for ConsolePrinter.
	 * @param loggerStatus LoggerStatus
	 */
	public WebPrinter(LoggerStatus loggerStatus) {
		this.loggerStatus = loggerStatus;
	}
	
	/**
	 * Method startLog.
	 * @param startMessage String
	 * @see com.medisanaspace.printer.PrinterInterface#startLog(String)
	 */
	@Override
	public void startLog(String startMessage) {
		if (loggerStatus.equals(LoggerStatus.LOG_ALL))
			messages+="Start logging: "+ startMessage +"\n -----------";
	}

	/**
	 * Method endLog.
	 * @param endMessage String
	 * @see com.medisanaspace.printer.PrinterInterface#endLog(String)
	 */
	@Override
	public void endLog(String endMessage) {
		if (loggerStatus.equals(LoggerStatus.LOG_ALL))
			messages+="End of log: "+ endMessage +"\n -----------";
	}

	/**
	 * This method is not used in the console printer.
	 * @param s String
	 * @see com.medisanaspace.printer.PrinterInterface#startDataSet(String)
	 */
	@Override
	public void startDataSet(String s) {
		// not needed in the console printer
	}

	/**
	 * Method logMessage.
	 * @param message String
	 * @see com.medisanaspace.printer.PrinterInterface#logMessage(String)
	 */
	@Override
	public void logMessage(String message){
	if (loggerStatus.equals(LoggerStatus.LOG_ALL))
		messages+=message+"\n";
	}

	/**
	 * Method logData.
	 * @param data String
	 * @see com.medisanaspace.printer.PrinterInterface#logData(String)
	 */
	@Override
	public void logData(String data) {
		if (loggerStatus.equals(LoggerStatus.LOG_ALL))
			messages+="    Response: " + data + "\n--------------";

	}

	/**
	 * Method logError.
	 * @param e String
	 * @see com.medisanaspace.printer.PrinterInterface#logError(String)
	 */
	@Override
	public void logError(String e) {
		if (!loggerStatus.equals(LoggerStatus.LOG_DISABLED))
			messages+=" ------------ \n An Error occured: "+ e +"\n -------------";
	}

	/**
	 * Method logError.
	 * @param e String
	 * @param exception Exception
	 * @see com.medisanaspace.printer.PrinterInterface#logError(String, Exception)
	 */
	@Override
	public void logError(String e, Exception exception) {
		if (!loggerStatus.equals(LoggerStatus.LOG_DISABLED)){
			messages+=" ------------ \n An Error occured: "+ e +
							"\n Message: "+ exception +"\n -------------";
			messages+=exception.getStackTrace().toString();
		}
	}

	/**
	 * Method logPost.
	 * @param httppost HttpRequestBase
	 * @see com.medisanaspace.printer.PrinterInterface#logPost(HttpRequestBase)
	 */
	@Override
	public void logPost(final HttpRequestBase httppost) {
		if (loggerStatus.equals(LoggerStatus.LOG_ALL)){		
			messages+="Request " + httppost.getMethod() + " URL:"
					+ httppost.getURI()+"\n";
			for (Header header : httppost.getAllHeaders()) {
				messages+="    " + header.getName() + " : "
						+ header.getValue()+"\n";
			}
			messages+='\n';
		}
	}

}
