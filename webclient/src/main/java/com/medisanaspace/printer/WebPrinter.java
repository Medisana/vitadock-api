package com.medisanaspace.printer;

import java.util.EnumSet;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Concrete Console Printer.
 * 
 */
public  class WebPrinter extends AbstractPrinter{

	private String messages = "";
	
	public String getMessages(){
		return messages;
	}
	
	/**
	 * Constructor for ConsolePrinter.
	 * @param loggerStatus LoggerStatus
	 */
	public WebPrinter(EnumSet<LoggerAction> set) {
		super(set);
	}
	
	public void clearLog(){
		messages="";
	}
	
	/**
	 * Method startLog.
	 * @param startMessage String
	 * @see com.medisanaspace.printer.PrinterInterface#startLog(String)
	 */
	@Override
	public void startLog(String startMessage) {
		clearLog();
		if (loggerActionSet.contains(LoggerAction.LOG_MESSAGE))
			messages+="Start logging: "+ startMessage +" <br /> -----------<br /> ";
	}

	/**
	 * Method endLog.
	 * @param endMessage String
	 * @see com.medisanaspace.printer.PrinterInterface#endLog(String)
	 */
	@Override
	public void endLog(String endMessage) {
		if (loggerActionSet.contains(LoggerAction.LOG_MESSAGE))
			messages+="End of log: "+ endMessage +"<br /> -----------<br /> ";
	}

	/**
	 * This method is not used in the console printer.
	 * @param s String
	 * @see com.medisanaspace.printer.PrinterInterface#startDataSet(String)
	 */
	@Override
	public void startDataSet(String testName) {
		messages+="<br /> <b> Start test: "+testName+" </b> <br />";
	}

	/**
	 * Method logMessage.
	 * @param message String
	 * @see com.medisanaspace.printer.PrinterInterface#logMessage(String)
	 */
	@Override
	public void logMessage(String message){
	if (loggerActionSet.contains(LoggerAction.LOG_MESSAGE))
		messages+=message+"<br />";
	}

	/**
	 * Method logData.
	 * @param data String
	 * @see com.medisanaspace.printer.PrinterInterface#logJSONData(String)
	 */
	@Override
	public void logJSONData(String data) {
		if (loggerActionSet.contains(LoggerAction.LOG_JSON_DATA))
			messages+="Response: " + data + "<br /> ------------- <br />";

	}

	/**
	 * Method logError.
	 * @param e String
	 * @see com.medisanaspace.printer.PrinterInterface#logError(String)
	 */
	@Override
	public void logError(String e) {
		if (loggerActionSet.contains(LoggerAction.LOG_ERROR))
			messages+=" ------------ <br /> An Error occured: "+ e +"<br /> -------------<br /> ";
	}

	/**
	 * Method logError.
	 * @param e String
	 * @param exception Exception
	 * @see com.medisanaspace.printer.PrinterInterface#logError(String, Exception)
	 */
	@Override
	public void logError(String e, Exception exception) {
		if (loggerActionSet.contains(LoggerAction.LOG_ERROR)){
			messages+=" ------------ <br /> An Error occured: "+ e +
							"<br /> Message: "+ exception +"<br /> -------------<br /> ";
			messages+=exception.getStackTrace().toString()+"<br /> ";
		}
	}

	/**
	 * Method logPost.
	 * @param httppost HttpRequestBase
	 * @see com.medisanaspace.printer.PrinterInterface#logPost(HttpRequestBase)
	 */
	@Override
	public void logPost(final HttpRequestBase httppost) {
		if (loggerActionSet.contains(LoggerAction.LOG_PROTOCOL_MESSAGE)){		
			messages+="Request " + httppost.getMethod() + " URL:"
					+ httppost.getURI()+"<br />";
			for (Header header : httppost.getAllHeaders()) {
				messages+="    " + header.getName() + " : "
						+ header.getValue()+"<br />";
			}
			messages+="<br />";
		}
	}

	@Override
	public void logActivity(String activity) {
		if(loggerActionSet.contains(LoggerAction.LOG_ACTIVITY)){
			messages+="<b>"+activity+"</b> <br />";
		}
	}

	@Override
	public void logProtocolMessages(String s) {
		if (loggerActionSet.contains(LoggerAction.LOG_PROTOCOL_MESSAGE)){
			messages+=s+"<br />";
		}
		
	}

}
