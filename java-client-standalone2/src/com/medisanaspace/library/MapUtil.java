package com.medisanaspace.library;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.medisanaspace.web.library.SignatureHelper;

/**
 * see
 * http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the
 * -values-in-java/3420912#3420912
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 * @version $Revision: 1.0 $
 */
public final class MapUtil {

	private MapUtil() {
	}

	/**
	 * Method sortByKey.
	 * @param map Map<K,V>
	 * @return Map<K,V>
	 */
	public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(
			final Map<K, V> map) {
		@SuppressWarnings("unchecked")
		final Map.Entry<K, V>[] array = map.entrySet().toArray(
				new Map.Entry[map.size()]);

		Arrays.sort(array, new Comparator<Map.Entry<K, V>>() {
			
			public int compare(final Map.Entry<K, V> e1,
					final Map.Entry<K, V> e2) {
				return e1.getKey().compareTo(e2.getKey());
			}
		});

		final Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : array) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * Creates a sorted base parameter string using the given parameter string
	 * and signature map.
	 * 
	 * @param signatureMap
	 *            The elements of the authorization header in a map
	 * @param parameterString
	 *            The given parameter string of the access
	
	 * @return A connected and sorted Base Parameter String */
	public static String createBaseParameterString(
			final Map<String, String> signatureMap, final String parameterString) {

		if (StringUtil.isNotNullOrEmpty(parameterString)) {
			String[] parameterList = parameterString.split("&");

			for (String parameter : parameterList) {
				String[] pair = parameter.split("=");
				if (pair.length != 2) {
					Logger.getLogger(SignatureHelper.class.getName()).log(
							Level.WARNING,
							"Invalid parameter string (entry is missing \"=\":"
									+ parameterString + ").");
					return "Invalid parameter string (entry is missing \"=\")";
				}
				String key = pair[0];
				String value = pair[1];
				signatureMap.put(key, value);
			}
		}

		Map<String, String> sortedSignatureMap = MapUtil
				.sortByKey(signatureMap);

		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;

		for (Map.Entry<String, String> entry : sortedSignatureMap.entrySet()) {
			if (!first) {
				stringBuilder.append("&");
			}
			stringBuilder.append(entry.getKey() + "=" + entry.getValue());
			first = false;
		}

		return stringBuilder.toString();
	}

}
