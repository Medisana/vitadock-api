package com.medisanaspace.jsf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.medisanaspace.web.library.WebConstants;
import com.medisanaspace.web.main.CloudClient;
import com.medisanaspace.web.testconfig.OAuthData;

@Controller
@RequestMapping("/runtests")
public class RestInterfaceController {
	
	private final String OAUTH_DEVICE_TOKEN ="NjyM7rkSvEGUDzbnzUsfY1PCLsdLkUBkFRJquiRoor9UdoPJLskW6bWGhXWoyzFf";
	private final String OAUTH_DEVICE_SECRET ="h9UBgiRJ0vgCVNvu7jovpycENZSMmSdy1BpepizVrJtUsBuwmKya3ZMeurXKbw9R";
	private final String OAUTH_ACCESS_TOKEN ="wnKR4cLqgz21kbm48Vzmect1y8Ebdd4LcdY1N0K1nDN1p4Ayz3tClotlGSztrYnt";
	private final String OAUTH_ACCESS_SECRET ="a9D25mCsbXjuRSFAc9FzvnmZpZPpj3phxmLqvaagjK990RuUq51wxfdXyflc8Zaz";

	
	
	@RequestMapping( method = RequestMethod.GET)
	public void runTest(){
		int numberOfEntries = 20;
		String server="TEST_SERVER";
		List<String> runAllTests = new ArrayList<String>();
		runAllTests.add(WebConstants.TRACKER_ACTIVITY_MODULE);
		runAllTests.add(WebConstants.ACTIVITYDOCK_MODULE);
		runAllTests.add(WebConstants.CARDIODOCK_MODULE);
		runAllTests.add(WebConstants.GLUCODOCK_GLUCOSE_MODULE);
		runAllTests.add(WebConstants.TARGETSCALE_MODULE);
		runAllTests.add(WebConstants.THERMODOCK_MODULE);
//		runAllTests.add(new TrackerPhaseTestTask(numberOfEntries)); not yet working
		
		Set<String>loggerActionList= new HashSet<String>();
		loggerActionList.add(WebConstants.LOG_ERROR);
		loggerActionList.add(WebConstants.LOG_JSON_DATA);
		loggerActionList.add(WebConstants.LOG_ACTIVITY);
		loggerActionList.add(WebConstants.LOG_PROTOCOL_MESSAGE);
		loggerActionList.add(WebConstants.LOG_MESSAGE);

		CloudClient cloudClient = new CloudClient();
		OAuthData oauthdata = new OAuthData();
		oauthdata.setDeviceToken(OAUTH_DEVICE_TOKEN);
		oauthdata.setDeviceSecret(OAUTH_DEVICE_SECRET);
		oauthdata.setAccessToken(OAUTH_ACCESS_TOKEN);
		oauthdata.setAccessSecret(OAUTH_ACCESS_SECRET);
		
		cloudClient.runTests(runAllTests, numberOfEntries, oauthdata, server, WebConstants.MAIL_PRINTER, loggerActionList);
		
	}
	
}
