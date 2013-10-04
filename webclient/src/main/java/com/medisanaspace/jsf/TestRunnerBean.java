package com.medisanaspace.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.medisanaspace.web.main.CloudClient;

import com.medisanaspace.web.testconfig.OAuthData;

@Controller
@Scope("session")
@SessionScoped
@ManagedBean(name="testRunnerBean")
public class TestRunnerBean implements Serializable{

	private List<String> selectedTests = new ArrayList<String>();  
	private Map<String,String> tests;
	private CloudClient cloudClient;
	private String messageLog;

	// temp
	private String oauth_token;
	private String oauth_verifier;
	private boolean deny;
	private String callbackMassage;
	private OAuthData oauthdata;
	
	public TestRunnerBean() {
		init();
	}
	
	public void init() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			tests = new HashMap<String, String>();
			tests.put("Tracker Activity and Tracker Sleep Test", "0");
			tests.put("Activitydock Test", "1");
			tests.put("Cardiodock Test", "2");
			tests.put("Targetscale Test", "3");
			tests.put("Thermodock Test", "4");
			tests.put("TrackerPhase Test", "5");
			tests.put("TrackerSleep Test", "6");
			tests.put("UserSettings Test", "7");
		}

	}
	
	/**
	 * Authorize on a host
	 * @return
	 */
	public String authorize() {
		cloudClient = new CloudClient();
		try {
			// redirect the user to the login page
			String url = cloudClient.authorize();
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		    return null;
		} catch (Exception e) {
			// refer to error page here, user does not need to know specifics
			System.out.println("error");
			return "error.jsf";
		}
	}
	
	/**
	 * Authorize verifier token
	 * @return
	 */
	public String authorizeVerifierToken(){
		// we need the verifier token!
		if(!FacesContext.getCurrentInstance().isPostback()){
			if (!isDeny() && oauth_verifier != null){
				try {
					oauthdata = cloudClient
							.authorizeWithVerifierToken(oauth_verifier);
				} catch (Exception e) {
					System.out.println("Error: Authorize verifier token error");
					return "error.jsf";
				}
			}else{
				System.out.println("Error: denied or no Verifiertoken");
				return "error.jsf";			
			}
		}
		return null;
	}
	
	/**
	 * Run tests after authorization process.
	 * @return
	 */
	public String runTest() {
		try {
			cloudClient.runTests(selectedTests, oauthdata);
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
}
