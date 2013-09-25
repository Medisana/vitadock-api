package com.medisanaspace.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.medisanaspace.web.main.CloudClient;
import com.medisanaspace.web.testconfig.OAuthData;

@Controller
@Scope("session")
@ManagedBean(name = "testRunnerBean")

public class TestRunnerBean {

	private List<Integer> selectedTests = new ArrayList<>();
	private Map<String, Integer> tests;
	
	@Resource(name="cloudClientBean")
	private CloudClient cloudClient = new CloudClient();
	
	private String messageLog;

	// temp
	private String oauth_token;
	private String oauth_verifier;
	private boolean deny;
	private String callbackMassage;
	
	public TestRunnerBean() {
		init();
	}
	
	public void init() {
		tests = new HashMap<String, Integer>();
		tests.put("Tracker Activity and Tracker Sleep Test", 1);
		tests.put("Activitydock Test", 2);
		tests.put("Cardiodock Test", 3);
		if (isDeny() && oauth_verifier == null) {
			setCallbackMassage("Access denied");
		} else if (!isDeny() && oauth_verifier != null){
			setCallbackMassage("Tests began");
			runTest();
		}
	}

	public String authorize() {
		//cloudClient = new CloudClient();
		try {
			
			// redirect the user to the login page
			if (cloudClient == null) {
				System.out.println("CloudCliente is null before");
			}
			String url = cloudClient.authorize() + "&faces-redirect=true";
			System.out.println("url: "+url);
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		    return null;
		} catch (Exception e) {
			// refer to error page here, user does not need to know specifics
			System.out.println("error");
			return "error.jsf";
		}
	}

	public String runTest() {
		try {
			System.out.println("runTest");
			if (cloudClient == null) {
				System.out.println("CloudCliente is null");
			}
			OAuthData oauthdata = cloudClient
					.authorizeWithVerifierToken(oauth_verifier);
			System.out.println("after authorize");
			System.out.println("oauth_data: "+oauthdata);
			System.out.println("selectTests: "+selectedTests);
			cloudClient.runTests(selectedTests, oauthdata);
			messageLog = cloudClient.getMessageLog();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return "completed";
	}

	// getter only
	public Map<String, Integer> getTests() {
		return tests;
	}

	public String getMessageLog() {
		return messageLog;
	}

	public List<Integer> getSelectedTests() {
		return selectedTests;
	}

	public void setSelectedTests(List<Integer> selectedTests) {
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
}
