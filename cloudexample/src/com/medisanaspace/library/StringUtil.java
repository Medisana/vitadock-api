package com.medisanaspace.library;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * Helper class for string comparisons.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public final class StringUtil {

	private StringUtil() {
	}

	public static boolean isNotNullOrEmpty(final String string) {
		if (string == null || "".equals(string)) {
			return false;
		}
		return true;
	}

	public static boolean isNullOrEmpty(final String string) {
		return !isNotNullOrEmpty(string);
	}

	public static String getStackTraceAsString(final Throwable t) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		t.printStackTrace(out);
		return writer.getBuffer().toString();
	}

	public static String toJsonArray(final Collection<String> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<String> fromJsonArrayToStrings(final String json) {
		return new JSONDeserializer<List<String>>().use(null, ArrayList.class)
				.use("values", String.class).deserialize(json);
	}

}
