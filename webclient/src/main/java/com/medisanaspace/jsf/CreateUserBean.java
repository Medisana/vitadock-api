package com.medisanaspace.jsf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.medisanaspace.web.main.CloudClient;

@Controller
@Scope("request")
public class CreateUserBean implements Serializable {
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
	
	private String server="TEST_SERVER";
	private String newUserEmail="";
	private String newUserPassword="";

	
	public CreateUserBean() {
	}
	
	@PostConstruct
	public void init() {
		
			serverList = new HashMap<String, String>();
			serverList.put("TEST_SERVER", "Test Server");
			serverList.put("PRODUCTION_SERVER", "Production Server");
			
			moduleList = new HashMap<String, String>();
			moduleList.put("TRACKER_ACTIVITY_MODULE","Tracker Activity and Tracker Sleep");
			moduleList.put("ACTIVITY_MODULE","Activitydock");
			moduleList.put("CARDIODOCK_MODULE","Cardiodock");
			moduleList.put("GLUCODOCK_GLUCOSE_MODULE","Gluckodock");
			moduleList.put("TARGETSCALE_MODULE","Targetscale");
			moduleList.put("THERMODOCK_MODULE","Thermodock");
			moduleList.put("TRACKER_PHASE_MODULE","Tracker Phase");

			
			loggerList = new HashMap<String, String>();
			loggerList.put("LOG_ERROR", "Log Errors" );
			loggerList.put( "LOG_JSON_DATA","Log JSON data" );
			loggerList.put("LOG_ACTIVITY","Log Activities" );
			loggerList.put( "LOG_PROTOCOL_MESSAGE","Log Protocol Messages" );
			loggerList.put( "LOG_MESSAGE","Log Messages" );
			sessionDataBean.getLoggerLevel().addAll(loggerList.keySet());
		
	}

	public String createUser(){
		try {
			String url = cloudClient.authorize(server, true ,newUserEmail, newUserPassword, sessionDataBean.getLoggerLevel());
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

	public String getNewUserEmail() {
		return newUserEmail;
	}

	public void setNewUserEmail(String newUserEmail) {
		this.newUserEmail = newUserEmail;
	}

	public String getNewUserPassword() {
		return newUserPassword;
	}

	public void setNewUserPassword(String newUserPassword) {
		this.newUserPassword = newUserPassword;
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

//	public Set<String> getModules() {
//		return selectedModules;
//	}
//
//	public void setModules(Set<String> modules) {
//		this.selectedModules = modules;
//	}



	public SessionDataBean getSessionDataBean() {
		return sessionDataBean;
	}

	public void setSessionDataBean(SessionDataBean sessionDataBean) {
		this.sessionDataBean = sessionDataBean;
	}



	



	

}
