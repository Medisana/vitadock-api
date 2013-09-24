package com.medisanaspace.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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
	
	private List<Integer> selectedTests = new ArrayList<Integer>();  
	private Map<String,Integer> tests;
	private CloudClient cloudClient;		
	private String messageLog;
	
	// temp
	private String oauth_token;
	private String oauth_verifier;
	

   public void init() {
	   if (!FacesContext.getCurrentInstance().isPostback()) {   
		   tests = new HashMap<String, Integer>();
		   tests.put("Tracker Activity and Tracker Sleep Test", 1);
		   tests.put("Activitydock Test", 2);
		   tests.put("Cardiodock Test", 3);
	   }
    }
   
   
   public void authorize(){
	   cloudClient = new CloudClient();
	   try {
		   // redirect the user to the login page
		   ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		   externalContext.redirect(cloudClient.authorize());
		   
		} catch (Exception e) {
			// refer to error page here, user does not need to know specifics
			//return "error.jsf";
		}
   }
   
   
   public String runTest(){
	   try{
		   OAuthData oauthdata = cloudClient.authorizeWithVerifierToken(oauth_verifier);
		   cloudClient.runTests(selectedTests, oauthdata);
		   messageLog = cloudClient.getMessageLog();
	   }catch(Exception e){
		   //
	   }
		return "completed";
	}
	
	// getter only
	public Map<String, Integer>getTests(){
		return tests;
	}
	
	public String getMessageLog(){
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


}
