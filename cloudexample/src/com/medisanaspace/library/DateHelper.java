package com.medisanaspace.library;

import java.util.Date;
import java.util.Calendar;

/**
 * Helper class providing functions to generate and parse Dates/Strings.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public final class DateHelper {

	private DateHelper() {
	}

	public static Date generateCurrentDate() {
		long timeInMillis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		return cal.getTime();
	}
}
