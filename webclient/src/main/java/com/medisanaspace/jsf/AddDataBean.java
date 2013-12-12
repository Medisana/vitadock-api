package com.medisanaspace.jsf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.medisanaspace.web.library.WebConstants;
import com.medisanaspace.web.main.CloudClient;

@Controller
@Scope("request")
public class AddDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/***********************************************
	 * Injected Beans
	 **********************************************/

	@Autowired private CloudClient cloudClient;
	@Autowired private SessionDataBean sessionDataBean;

	/***********************************************
	 * View attributes and parameters
	 **********************************************/

	private Map<String, String> serverList;
	private Map<String, String> loggerList;
	private Map<String, String> moduleList;
	
	private String server=WebConstants.TEST_SERVER;
	private int numberOfEntries=0;

	
	public AddDataBean() {
	}
	
	@PostConstruct
	public void init() {
		
			serverList = new HashMap<String, String>();
			serverList.put(WebConstants.TEST_SERVER, "Test Server");
			serverList.put(WebConstants.PRODUCTION_SERVER, "Production Server");
			
			moduleList = new HashMap<String, String>();
			moduleList.put(WebConstants.TRACKER_ACTIVITY_MODULE,"Tracker Activity and Tracker Sleep");
			moduleList.put(WebConstants.ACTIVITYDOCK_MODULE,"Activitydock");
			moduleList.put(WebConstants.CARDIODOCK_MODULE,"Cardiodock");
			moduleList.put(WebConstants.GLUCODOCK_GLUCOSE_MODULE,"Gluckodock");
			moduleList.put(WebConstants.TARGETSCALE_MODULE,"Targetscale");
			moduleList.put(WebConstants.THERMODOCK_MODULE,"Thermodock");
			//moduleList.put("TRACKER_PHASE_MODULE","Tracker Phase");

			loggerList = new HashMap<String, String>();
			loggerList.put(WebConstants.LOG_ERROR, "Log Errors" );
			loggerList.put(WebConstants.LOG_JSON_DATA,"Log JSON data" );
			loggerList.put(WebConstants.LOG_ACTIVITY,"Log Activities" );
			loggerList.put(WebConstants.LOG_PROTOCOL_MESSAGE,"Log Protocol Messages" );
			loggerList.put(WebConstants.LOG_MESSAGE,"Log Messages" );
			sessionDataBean.getLoggerLevel().addAll(loggerList.keySet());
		
	}

	public String addData(){
		try {
			sessionDataBean.setAddRandomData(true);
			sessionDataBean.setNumberOfEntries(numberOfEntries);
			String url = cloudClient.authorize(server, false,null, null, sessionDataBean.getLoggerLevel(), "", "");
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
			return null;
		} catch (Exception e) {
			// refer to error page here, user does not need to know specifics
			System.out.println("error when creating  new user");
			return "error.jsf";
		}

	}

	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}


	public Map<String, String> getServerList() {
		return serverList;
	}

	public void setServerList(Map<String, String> serverList) {
		this.serverList = serverList;
	}

	public Map<String, String> getModuleList() {
		return moduleList;
	}



	public SessionDataBean getSessionDataBean() {
		return sessionDataBean;
	}

	public void setSessionDataBean(SessionDataBean sessionDataBean) {
		this.sessionDataBean = sessionDataBean;
	}

	public Map<String, String> getLoggerList() {
		return loggerList;
	}

	public void setLoggerList(Map<String, String> loggerList) {
		this.loggerList = loggerList;
	}

	public int getNumberOfEntries() {
		return numberOfEntries;
	}

	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}



	



	

}
