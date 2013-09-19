package com.medisanaspace.web.testtask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.medisanaspace.library.StringUtil;
import com.medisanaspace.model.Order;
import com.medisanaspace.web.library.AuthorizationBuilder;

/**
 */
public class OrderTestTask extends AbstractTestTask {

	public OrderTestTask(int numberOfEntries) {
		super(numberOfEntries);
	}

	/**
	 * Method executeTask.
	 * @throws Exception
	 */
	@Override
	protected void executeTask() throws Exception {
		final List<Order> orderList = new ArrayList<Order>();
		
		printer.logMessage("Order test");
		
		for (int i = 0; i < TestConstants.MAX_ENTRIES; i++) {
			orderList.add(Order.generateRandomEntry());
		}
		String responseOrder = saveJSONData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(), Order.toJsonArray(orderList),
				AuthorizationBuilder.ORDER_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());
		
		Collection<String> idOrderList = StringUtil
				.fromJsonArrayToStrings(responseOrder);
		
		int countOrder = countData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.ORDER_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret());

		this.printer.logMessage("Data count: " + countOrder);
		
		this.deleteJSONData(this.oauthData.getDeviceToken(),
				this.oauthData.getDeviceSecret(),
				AuthorizationBuilder.ORDER_MODULE_ID,
				this.oauthData.getAccessToken(),
				this.oauthData.getAccessSecret(),
				idOrderList);
		
	}

}
