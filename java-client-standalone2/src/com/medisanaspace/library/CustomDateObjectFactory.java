package com.medisanaspace.library;

import java.lang.reflect.Type;
import java.util.Date;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

/**
 * Helper class to properly format dates.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 * @version $Revision: 1.0 $
 */
public class CustomDateObjectFactory implements ObjectFactory {

	/**
	 * Method instantiate.
	 * @param context ObjectBinder
	 * @param value Object
	 * @param targetType Type
	 * @param targetClass Class
	 * @return Object
	 * @see flexjson.ObjectFactory#instantiate(ObjectBinder, Object, Type, Class)
	 */
	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {

		if (value instanceof Double) {
			return new Date(((Double) value).longValue());
		} else if (value instanceof Long) {
			return new Date((Long) value);
		} else if (value instanceof String) {
			try {
				return DateHelper.getDate((String) value);
			} catch (Exception e) {
				return null;
			}
		}
		return null;

	}
}
