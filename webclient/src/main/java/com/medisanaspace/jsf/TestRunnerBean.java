package com.medisanaspace.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medisanaspace.printer.AbstractPrinter.LoggerAction;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.main.CloudClient;
import com.medisanaspace.web.testconfig.OAuthData;
import com.medisanaspace.web.testconfig.ServerType;

@Controller
@Scope("session")
@SessionScoped
@ManagedBean(name = "testRunnerBean")
public class TestRunnerBean implements Serializable {
	
	private CloudClient cloudClient;
	private OAuthData oauthdata;
	private String messageLog;

	// for the View
	private Map<String, String> tests;
	private Map<String, String> serverList;
	private Map<String, String> loggerList;
	private Map<String, String> roleList;

	private List<String> selectedTests = new ArrayList<String>();
	private String server;
	private Set<String> loggerLevel;
	private String role="MANAGER";
	
	// preconfigured logging level
	private Map<String, Set<String>> preConfiguredLoggerOptions;

	// temp
	private String oauth_token;
	private String oauth_verifier;
	private boolean deny;
	private String callbackMassage;

	
	public TestRunnerBean() {
		init();
	}

	public void init() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
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
			
			loggerLevel = preConfiguredLoggerOptions.get(role);
			
		}

	}
	
	public void updateLoggerLevel(AjaxBehaviorEvent e){
		setLoggerLevel(preConfiguredLoggerOptions.get(role));
	}
	
	@RequestMapping("/latency")
	public String getlatency(@RequestParam String server, Model model) {
		return "";
	}

	/**
	 * Authorize on a host
	 * 
	 * @return
	 */
	public String authorize() {
		cloudClient = new CloudClient();
		try {
			// redirect the user to the login page to authorize
			String url = cloudClient.authorize(server, loggerLevel);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
			return null;
		} catch (Exception e) {
			// refer to error page here, user does not need to know specifics
			System.out.println("error");
			return "error.jsf";
		}
	}

	/**
	 * Authorize verifier token
	 * 
	 * @return
	 */
	public String authorizeVerifierToken() {
		// we need the verifier token!
		if (!FacesContext.getCurrentInstance().isPostback()) {
			if (!isDeny() && oauth_verifier != null) {
				try {
					oauthdata = cloudClient
							.authorizeWithVerifierToken(oauth_verifier);
					messageLog = cloudClient.getMessageLog();
				} catch (Exception e) {
					// System.out.println("Error: Authorize verifier token error");
					return "error.jsf";
				}
			} else {
				// System.out.println("Error: denied or no Verifiertoken");
				return "error.jsf";
			}
		}
		return null;
	}

	/**
	 * Run tests after authorization process.
	 * 
	 * @return
	 */
	public String runTest() {
		try {
			if (!selectedTests.isEmpty()) {
				cloudClient.runTests(selectedTests, oauthdata);
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

	public List<String> getSelectedTests() {
		return selectedTests;
	}

	public void setSelectedTests(List<String> selectedTests) {
		this.selectedTests = selectedTests;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public String getOauth_verifier() {
		return oauth_verifier;
	}

	public void setOauth_verifier(String oauth_verifier) {
		this.oauth_verifier = oauth_verifier;
	}

	public boolean isDeny() {
		return deny;
	}

	public void setDeny(boolean deny) {
		this.deny = deny;
	}

	public String getCallbackMassage() {
		return callbackMassage;
	}

	public void setCallbackMassage(String callbackMassage) {
		this.callbackMassage = callbackMassage;
	}

	public OAuthData getOauthdata() {
		return oauthdata;
	}

	public void setOauthdata(OAuthData oauthdata) {
		this.oauthdata = oauthdata;
	}
	public Map<String, String> getServerList() {
		return serverList;
	}



	public Map<String, String> getRoleList() {
		return roleList;
	}

	public Set<String> getLoggerLevel() {
		return loggerLevel;
	}

	public void setLoggerLevel(Set<String> loggerLevel) {
		this.loggerLevel = loggerLevel;
	}

	public Map<String, String> getLoggerList() {
		return loggerList;
	}
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	

}
