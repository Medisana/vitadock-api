package com.medisanaspace.printer;

import java.io.UnsupportedEncodingException;
import java.util.EnumSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

import com.medisanaspace.web.library.EmailHelper;

/**
 * Printer that send´s an report via Mail, if an error occures
 * 
 */
public  class MailNotificationPrinter extends AbstractPrinter{

	private String messages = "";
	private boolean errorOccured=false;
	
	public String getMessages(){
		return messages;
	}
	
	/**
	 * Constructor for ConsolePrinter.
	 * @param loggerStatus LoggerStatus
	 */
	public MailNotificationPrinter(EnumSet<LoggerAction> set) {
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
		messages+="<!DOCTYPE html> <html> <head> <title>Error report</title> </head> <body>";
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
		messages+="End of log: "+ endMessage +"<br /> -----------<br /> ";
		messages+="</body> </html>";
		if(errorOccured){
			try {
				EmailHelper.sendMail("Jan.Krause@medisanaspace.com", "Error while running API tests", this.getMessages());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		errorOccured=true;
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
		errorOccured=true;
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
