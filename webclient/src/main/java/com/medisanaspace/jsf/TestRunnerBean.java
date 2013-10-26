package com.medisanaspace.jsf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.main.CloudClient;

@Controller
@Scope("request")
public class TestRunnerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/***********************************************
	 * Injected Beans
	 **********************************************/
	
	@Autowired private CloudClient cloudClient;
	/*
	 * Store session wide data as
	 * - oauth
	 * - selectedTests
	 * - modules to insert random data
	 */
	@Autowired private SessionDataBean sessionDataBean;
	

	/***********************************************
	 * View attributes and parameters
	 **********************************************/
	
	//output
	private Map<String, String> tests;
	private Map<String, String> serverList;
	private Map<String, String> loggerList;
	private Map<String, String> roleList;
	//  pre configured logging level regarding to the roles
	private Map<String, Set<String>> preConfiguredLoggerOptions;
	
	// input
	private String newUserEmail="";
	private String newUserPassword="";
	private String messageLog;
	private boolean createNewUser = false;	

	
	public TestRunnerBean() {
	}
	
	
	@PostConstruct
	public void init(){
		
			tests = new HashMap<String, String>();
			tests.put(String
					.valueOf(AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID),
					"Tracker Activity and Tracker Sleep Test");
			tests.put(String.valueOf(AuthorizationBuilder.ACTIVITY_MODULE_ID),
					"Activitydock Test");
			tests.put(
					String.valueOf(AuthorizationBuilder.CARDIODOCK_MODULE_ID),
					"Cardiodock Test");
			tests.put(String
					.valueOf(AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID),
					"Gluckodock Test");
			tests.put(
					String.valueOf(AuthorizationBuilder.TARGETSCALE_MODULE_ID),
					"Targetscale Test");
			tests.put(
					String.valueOf(AuthorizationBuilder.THERMODOCK_MODULE_ID),
					"Thermodock Test");
			tests.put(String
					.valueOf(AuthorizationBuilder.TRACKER_PHASE_MODULE_ID),
					"TrackerPhase Test");
			tests.put(String
					.valueOf(AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID),
					"TrackerSleep Test");
			tests.put(String
					.valueOf(AuthorizationBuilder.USER_SETTINGS_MODULE_ID),
					"UserSettings Test");
			
			serverList = new HashMap<String, String>();
			serverList.put("TEST_SERVER", "Test Server");
			serverList.put("PRODUCTION_SERVER", "Production Server");
			
			loggerList = new HashMap<String, String>();
			loggerList.put("LOG_ERROR", "Log Errors" );
			loggerList.put( "LOG_JSON_DATA","Log JSON data" );
			loggerList.put("LOG_ACTIVITY","Log Activities" );
			loggerList.put( "LOG_PROTOCOL_MESSAGE","Log Protocol Messages" );
			loggerList.put( "LOG_MESSAGE","Log Messages" );	

			roleList = new HashMap<String, String>();
			roleList.put("MANAGER", "Manager");
			roleList.put("TESTUSER", "Test user");
			roleList.put("DEVELOPER", "Developer");

			// pre configure logger options for the different roles

			preConfiguredLoggerOptions =  new HashMap<String, Set<String>>();
			Set<String> managerOptions = new HashSet<String>();
			Set<String> developerOptions = new HashSet<String>();
			Set<String> testUserOptions = new HashSet<String>();

			managerOptions.add("LOG_ERROR");
			managerOptions.add("LOG_ACTIVITY");
			developerOptions.add("LOG_ACTIVITY");
			developerOptions.add("LOG_ERROR");
			developerOptions.add("LOG_JSON_DATA");
			developerOptions.add("LOG_PROTOCOL_MESSAGE");
			developerOptions.add("LOG_MESSAGE");
			testUserOptions.add("LOG_ERROR");
			testUserOptions.add("LOG_ACTIVITY");
			testUserOptions.add("LOG_MESSAGE");

			preConfiguredLoggerOptions.put("MANAGER", managerOptions);
			preConfiguredLoggerOptions.put("DEVELOPER", developerOptions);
			preConfiguredLoggerOptions.put("TESTUSER", testUserOptions);
			sessionDataBean.setLoggerLevel(preConfiguredLoggerOptions.get(sessionDataBean.getRole()));

	}
	
	public void updateLoggerLevel(AjaxBehaviorEvent e){
		sessionDataBean.setLoggerLevel(preConfiguredLoggerOptions.get(sessionDataBean.getRole()));
	}
	
//	@RequestMapping("/latency")
//	public String getlatency(@RequestParam String server, Model model) {
//		return "";
//	}

	/**
	 * Authorize on a host
	 * 
	 * @return
	 */
	public String authorize() {
		try {
			
			// TODO: add createNewUser Parameter when ServerSide is repaired
			
			// redirect the user to the login page to authorize
			String url = cloudClient.authorize(sessionDataBean.getServer(), false ,newUserEmail, newUserPassword, sessionDataBean.getLoggerLevel());
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
			return null;
		} catch (Exception e) {
			// refer to error page here, user does not need to know specifics
			System.out.println("error");
			return "error.jsf";
		}
	}

//	/**
//	 * Authorize verifier token
//	 * 
//	 * @return
//	 */
//	public String authorizeVerifierToken() {
//		// we need the verifier token!
//		if (!FacesContext.getCurrentInstance().isPostback()) {
//			if (!isDeny() && oauth_verifier != null) {
//				try {
//					sessionDataBean.setOauthdata(cloudClient.authorizeWithVerifierToken(oauth_verifier));
//					messageLog = cloudClient.getMessageLog();
//				} catch (Exception e) {
//					// System.out.println("Error: Authorize verifier token error");
//					return "error.jsf";
//				}
//			} else {
//				// System.out.println("Error: denied or no Verifiertoken");
//				return "error.jsf";
//			}
//		}
//		return null;
//	}

	/**
	 * Run tests after authorization process.
	 * 
	 * @return
	 */
	public String runTest() {
		try {
			if (!sessionDataBean.getSelectedTests().isEmpty()) {
				cloudClient.runTests(sessionDataBean.getSelectedTests(), sessionDataBean.getOauthdata());
			}
			messageLog = cloudClient.getMessageLog();
		} catch (Exception e) {
			System.out.println("Error: Tests do not run correctly");
			return "error.jsf";
		}
		return null;
	}

	// getter only
	public Map<String, String> getTests() {
		return tests;
	}

	public String getMessageLog() {
		return messageLog;
	}



//	public String getOauth_verifier() {
//		return oauth_verifier;
//	}
//
//	public void setOauth_verifier(String oauth_verifier) {
//		this.oauth_verifier = oauth_verifier;
//	}
//
//	public boolean isDeny() {
//		return deny;
//	}
//
//	public void setDeny(boolean deny) {
//		this.deny = deny;
//	}


	public Map<String, String> getServerList() {
		return serverList;
	}



	public Map<String, String> getRoleList() {
		return roleList;
	}


	public Map<String, String> getLoggerList() {
		return loggerList;
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

	public SessionDataBean getSessionDataBean() {
		return sessionDataBean;
	}
	
	public void setSessionDataBean(SessionDataBean sessionDataBean) {
		this.sessionDataBean = sessionDataBean;
	}

	public boolean isCreateNewUser() {
		return createNewUser;
	}

	public void setCreateNewUser(boolean createNewUser) {
		this.createNewUser = createNewUser;
	}

//	public String getOauth_token() {
//		return oauth_token;
//	}
//
//	public void setOauth_token(String oauth_token) {
//		this.oauth_token = oauth_token;
//	}
//	

}
