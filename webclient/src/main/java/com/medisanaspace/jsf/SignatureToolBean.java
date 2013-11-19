package com.medisanaspace.jsf;

import java.io.BufferedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.printer.AbstractPrinter.LoggerAction;
import com.medisanaspace.printer.WebPrinter;
import com.medisanaspace.web.library.AuthorizationBuilder;
import com.medisanaspace.web.library.WebConstants;
import com.medisanaspace.web.main.CloudClient;
import com.medisanaspace.web.testconfig.ServerConfig;

@Controller
@Scope("session")
public class SignatureToolBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/***********************************************
	 * Injected Beans
	 **********************************************/

	@Autowired
	private CloudClient cloudClient;

	// @Autowired private SessionDataBean sessionDataBean;

	/***********************************************
	 * View attributes and parameters
	 **********************************************/

	// output
	private Map<String, String> serverList;
	private Map<String, String> actionList;
	private Map<Integer, String> moduleList;
	private String messageLog;

	// input
	private String action;
	private String server;

	private String applicationToken;
	private String applicationSecret;
	private String accessToken;
	private String accessSecret;
	private String unauthorizedAccessToken;
	private String unauthorizedAccessSecret;

	private String verifierToken;

	private Integer module;
	private String dataSince = "0";

	public SignatureToolBean() {
	}

	@PostConstruct
	public void init() {
		serverList = new HashMap<String, String>();
		serverList.put(WebConstants.TEST_SERVER, "Test Server");
		serverList.put(WebConstants.PRODUCTION_SERVER, "Production Server");
		actionList = new HashMap<String, String>();
		actionList.put(WebConstants.DELTE_ACTION, "Delete data");
		actionList.put(WebConstants.SAVE_ACTION, "Save data");
		actionList.put(WebConstants.LOAD_DATA_ACTION, "Load data");
		actionList.put(WebConstants.COUNT_DATA_ACTION, "Count data");
		actionList.put(WebConstants.RETRIEVE_ACCESS_TOKEN, "Get access token");
		actionList.put(WebConstants.RETRIEVE_UNAUTHORIZED_ACCESS_TOKEN,
				"Get unauthorized access token");

		moduleList = new HashMap<Integer, String>();

		moduleList.put(AuthorizationBuilder.ACTIVITY_MODULE_ID, "Activitydock");
		moduleList.put(AuthorizationBuilder.CARDIODOCK_MODULE_ID, "Cardiodock");
		moduleList.put(AuthorizationBuilder.CARDIODOCK_SETTINGS_MODULE_ID,
				"Cardiodock Settings");
		moduleList.put(AuthorizationBuilder.GLUCODOCK_GLUCOSE_MODULE_ID,
				"Gluckodock Glucose");
		moduleList.put(
				AuthorizationBuilder.GLUCODOCK_GLUCOSE_SETTINGS_MODULE_ID,
				"Gluckodock Glucose Settings");
		moduleList.put(AuthorizationBuilder.GLUCODOCK_INSULIN_MODULE_ID,
				"Gluckodock Insulin");
		moduleList.put(
				AuthorizationBuilder.GLUCODOCK_INSULIN_SETTINGS_MODULE_ID,
				"Gluckodock Insulin Settings");
		moduleList.put(AuthorizationBuilder.GLUCODOCK_MEAL_MODULE_ID,
				"Gluckodock Meal");
		moduleList.put(AuthorizationBuilder.GLUCODOCK_MEAL_SETTINGS_MODULE_ID,
				"Gluckodock Meal Settings");
		moduleList.put(AuthorizationBuilder.TARGETSCALE_MODULE_ID,
				"Targetscale");
		moduleList.put(AuthorizationBuilder.TARGETSCALE_SETTINGS_MODULE_ID,
				"Targetscale Settings");
		moduleList.put(AuthorizationBuilder.THERMODOCK_MODULE_ID, "Thermodock");
		moduleList.put(AuthorizationBuilder.THERMODOCK_SETTINGS_MODULE_ID,
				"Thermodock Settings");
		moduleList.put(AuthorizationBuilder.TRACKER_ACTIVITY_MODULE_ID,
				"Tracker Activity");
		moduleList.put(AuthorizationBuilder.TRACKER_STATS_MODULE_ID,
				"Tracker Stats");
		moduleList.put(AuthorizationBuilder.TRACKER_PHASE_MODULE_ID,
				"Tracker Phase");
		moduleList.put(AuthorizationBuilder.TRACKER_SLEEP_MODULE_ID,
				"Tracker Sleep");
		moduleList.put(AuthorizationBuilder.USER_SETTINGS_MODULE_ID,
				"User Settings");

	}

	/**
	 * Calculate
	 * 
	 * @return
	 */
	public String calculate() {
		String result = "";
		CloudClient.printer = new WebPrinter(EnumSet.allOf(LoggerAction.class));
		CloudClient.printer.startLog("Calculating signature:");
		// set server url according to the chosen server
		ServerConfig serverConfig = new ServerConfig();
		switch (server) {
		case WebConstants.TEST_SERVER:
			serverConfig
					.setEXTERNAL_AUTH_URL("vitacloud.medisanaspace.com/auth");
			serverConfig.setEXTERNAL_LOGIN_URL("vitacloud.medisanaspace.com");
			serverConfig
					.setEXTERNAL_DATA_URL("vitacloud.medisanaspace.com/data");
			break;
		case WebConstants.PRODUCTION_SERVER:
			serverConfig.setEXTERNAL_AUTH_URL("cloud.vitadock.com/auth");
			serverConfig.setEXTERNAL_LOGIN_URL("cloud.vitadock.com");
			serverConfig.setEXTERNAL_DATA_URL("cloud.vitadock.com/data");
			break;
		}

		if (WebConstants.COUNT_DATA_ACTION.equals(action)) {
			String requestUrl = AuthorizationBuilder.createCountUrl(
					serverConfig, module);
			try {
				result = AuthorizationBuilder
						.createCountDataRequestAuthorizationHeader(dataSince,
								requestUrl, applicationToken,
								applicationSecret, accessToken, accessSecret);
				CloudClient.printer.logMessage("Complete Auth header <br />"
						+ result);
			} catch (Exception e) {
				CloudClient.printer
						.logError(
								"Error when calculating count data request authorization header",
								e);
				messageLog = ((WebPrinter) CloudClient.printer).getMessages();
			}
		} else if (WebConstants.DELTE_ACTION.equals(action)
				|| WebConstants.SAVE_ACTION.equals(action)) {
			Collection<String> json_data = new ArrayList<String>();

			json_data.add("Your");
			json_data.add("JSON");
			json_data.add("data");

			String parameterString = StringUtil.toJsonArray(json_data);
			String authorization = "";
			try {
				String url = "";
				if (WebConstants.DELTE_ACTION.equals(action)) {
					url = AuthorizationBuilder.createDeleteArrayUrl(
							serverConfig, module);
				} else {
					url = AuthorizationBuilder.createRequestArrayUrl(
							serverConfig, module);
				}
				authorization = AuthorizationBuilder
						.createSaveDataRequestAuthorizationHeader(url,
								parameterString, applicationToken,
								applicationSecret, accessToken, accessSecret);
				HttpPost httppost = new HttpPost(
						AuthorizationBuilder.createDeleteArrayUrl(serverConfig,
								module));
				httppost.setHeader("Authorization", authorization);
				httppost.setHeader("Content-Type", "application/json");
				httppost.setEntity(new StringEntity(parameterString));
				CloudClient.printer.logPost(httppost);
			} catch (Exception e) {
				CloudClient.printer
						.logError(
								"Error when calculating delete request authorization header",
								e);
				messageLog = ((WebPrinter) CloudClient.printer).getMessages();
			}
		} else if (WebConstants.LOAD_DATA_ACTION.equals(action)) {
			int start = 1;
			int max = 10;

			String authorization;
			try {
				authorization = AuthorizationBuilder
						.createLoadDataRequestAuthorizationHeader(
								AuthorizationBuilder.createRequestUrl(
										serverConfig, module), start, max,
								dataSince, applicationToken, applicationSecret,
								accessToken, accessSecret);
				String requestUrl = AuthorizationBuilder.createRequestUrl(
						serverConfig, module, start, max, dataSince);
				HttpGet httpget = new HttpGet(requestUrl);
				httpget.setHeader("Authorization", authorization);
				CloudClient.printer.logPost(httpget);
			} catch (Exception e) {
				CloudClient.printer
						.logError(
								"Error when calculating load data request authorization header",
								e);
				messageLog = ((WebPrinter) CloudClient.printer).getMessages();
			}

		} else if (WebConstants.RETRIEVE_UNAUTHORIZED_ACCESS_TOKEN.equals(action)) {

			try {
				String uriOauthRequest = serverConfig.getHTTPS_AUTH_URL()
						+ "/unauthorizedaccesses";
				String authorization = AuthorizationBuilder
						.createUnauthorizedAccessRequestAuthorizationHeader(
								uriOauthRequest, applicationToken,
								applicationSecret);
				HttpPost httppost = new HttpPost(uriOauthRequest);
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httppost.setHeader("Authorization", authorization);
				CloudClient.printer.logPost(httppost);
			} catch (Exception e) {
				CloudClient.printer
						.logError(
								"Error when calculating unauthorized request header",
								e);
				messageLog = ((WebPrinter) CloudClient.printer).getMessages();
			}
		} else if (WebConstants.RETRIEVE_ACCESS_TOKEN.equals(action)) {
//			String verifierToken, String unauthorizedAccessToken,
//			String unauthorizedAccessSecret) throws Exception {


	
			String uriOauthRequest = serverConfig.getHTTPS_AUTH_URL()
					+ "/accesses/verify";
			try {
				String authorization = AuthorizationBuilder
						.createAccessRequestAuthorizationHeader(
								serverConfig,
								applicationToken,
								unauthorizedAccessToken, unauthorizedAccessSecret,
								verifierToken,
								applicationSecret);
				HttpPost httppost = new HttpPost(uriOauthRequest);
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httppost.setHeader("Authorization", authorization);
				CloudClient.printer.logPost(httppost);
			}catch(Exception e){
				CloudClient.printer
				.logError(
						"Error when calculating access request header",
						e);
				messageLog = ((WebPrinter) CloudClient.printer).getMessages();
			}
		}

		messageLog = ((WebPrinter) CloudClient.printer).getMessages();
		return "";
	}

	/**
	 * Helper method to identify which field has to be rendered in the view
	 * 
	 * @param field
	 * @return
	 */
	public boolean toRender(String field) {
		if (field.equals("verifier_token_panel")
				&& WebConstants.RETRIEVE_ACCESS_TOKEN.equals(action)) {
			return true;
		} else if (field.equals("datasince_panel")
				&& (WebConstants.LOAD_DATA_ACTION.equals(action) || WebConstants.COUNT_DATA_ACTION.equals(action))) {
			return true;
		} else if (field.equals("access_token_panel")
				&& !WebConstants.RETRIEVE_UNAUTHORIZED_ACCESS_TOKEN.equals(action)
				&& !WebConstants.RETRIEVE_ACCESS_TOKEN.equals(action)) {
			return true;
		} else if (field.equals("access_secret_panel")
				&& !WebConstants.RETRIEVE_UNAUTHORIZED_ACCESS_TOKEN.equals(action)
				&& !WebConstants.RETRIEVE_ACCESS_TOKEN.equals(action)) {
			return true;
		} else if (field.equals("module_panel")
				&& !WebConstants.RETRIEVE_UNAUTHORIZED_ACCESS_TOKEN.equals(action)
				&& !WebConstants.RETRIEVE_ACCESS_TOKEN.equals(action)) {
			return true;
		}else if(field.equals("verifier_token_panel") && WebConstants.RETRIEVE_ACCESS_TOKEN.equals(action)){
			return true;
		}else if(field.equals("unauthorized_access_token_panel") && WebConstants.RETRIEVE_ACCESS_TOKEN.equals(action)){
			return true;
		}else if(field.equals("unauthorized_access_secret_panel") && WebConstants.RETRIEVE_ACCESS_TOKEN.equals(action)){
			return true;
		}
		return false;
	}

	public String getMessageLog() {
		return messageLog;
	}

	public Map<String, String> getServerList() {
		return serverList;
	}

	public String getApplicationToken() {
		return applicationToken;
	}

	public void setApplicationToken(String applicationToken) {
		this.applicationToken = applicationToken;
	}

	public String getApplicationSecret() {
		return applicationSecret;
	}

	public void setApplicationSecret(String applicationSecret) {
		this.applicationSecret = applicationSecret;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, String> getActionList() {
		return actionList;
	}

	public void setActionList(Map<String, String> actionList) {
		this.actionList = actionList;
	}

	public String getVerifierToken() {
		return verifierToken;
	}

	public void setVerifierToken(String verifierToken) {
		this.verifierToken = verifierToken;
	}

	public Map<Integer, String> getModuleList() {
		return moduleList;
	}

	public void setModuleList(Map<Integer, String> moduleList) {
		this.moduleList = moduleList;
	}

	public Integer getModule() {
		return module;
	}

	public void setModule(Integer module) {
		this.module = module;
	}

	public String getDataSince() {
		return dataSince;
	}

	public void setDataSince(String dataSince) {
		this.dataSince = dataSince;
	}

	public String getUnauthorizedAccessToken() {
		return unauthorizedAccessToken;
	}

	public void setUnauthorizedAccessToken(String unauthorizedAccessToken) {
		this.unauthorizedAccessToken = unauthorizedAccessToken;
	}

	public String getUnauthorizedAccessSecret() {
		return unauthorizedAccessSecret;
	}

	public void setUnauthorizedAccessSecret(String unauthorizedAccessSecret) {
		this.unauthorizedAccessSecret = unauthorizedAccessSecret;
	}

}
