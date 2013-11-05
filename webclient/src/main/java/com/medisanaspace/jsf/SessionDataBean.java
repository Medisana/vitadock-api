package com.medisanaspace.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.medisanaspace.web.testconfig.OAuthData;

@Component
@Scope("session")
public class SessionDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/***********************************************
	 * Stored data during one session
	 **********************************************/
	private OAuthData oauthdata;
	private List<String> selectedTests = new ArrayList<String>();
	private List<String> selectedModulesToAddRandomData = new ArrayList<String>();
	private String server = "TEST_SERVER";
	private Set<String> loggerLevel = new HashSet<String>();
	private String role = "MANAGER";
	private boolean addRandomData=false;
	private int testRunIndex=-1;
	private int testProgress=0;
				
	public OAuthData getOauthdata() {
		return oauthdata;
	}

	public void setOauthdata(OAuthData oauthdata) {
		this.oauthdata = oauthdata;
	}

	public List<String> getSelectedTests() {
		return selectedTests;
	}

	public void setSelectedTests(List<String> selectedTests) {
		this.selectedTests = selectedTests;
	}

	public List<String> getSelectedModulesToAddRandomData() {
		return selectedModulesToAddRandomData;
	}

	public void setSelectedModulesToAddRandomData(
			List<String> selectedModulesToAddRandomData) {
		this.selectedModulesToAddRandomData = selectedModulesToAddRandomData;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Set<String> getLoggerLevel() {
		return loggerLevel;
	}

	public void setLoggerLevel(Set<String> loggerLevel) {
		this.loggerLevel = loggerLevel;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isAddRandomData() {
		return addRandomData;
	}

	public void setAddRandomData(boolean addRandomData) {
		this.addRandomData = addRandomData;
	}

	public int getTestRunIndex() {
		return testRunIndex;
	}

	public void setTestRunIndex(int testRunIndex) {
		this.testRunIndex = testRunIndex;
	}

	public int getTestProgress() {
		return testProgress;
	}

	public void setTestProgress(int testProgress) {
		this.testProgress = testProgress;
	}



}
