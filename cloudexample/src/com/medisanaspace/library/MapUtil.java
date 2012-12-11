package com.medisanaspace.library;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * see
 * http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the
 * -values-in-java/3420912#3420912
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public class MapUtil {
	public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(
			Map<K, V> map) {
		@SuppressWarnings("unchecked")
		Map.Entry<K, V>[] array = map.entrySet().toArray(
				new Map.Entry[map.size()]);

		Arrays.sort(array, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				return e1.getKey().compareTo(e2.getKey());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : array)
			result.put(entry.getKey(), entry.getValue());

		return result;
	}

}
