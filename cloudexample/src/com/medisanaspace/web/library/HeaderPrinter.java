package com.medisanaspace.web.library;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Helper class to print HTTP headers.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public class HeaderPrinter {
	public static void printPost(HttpRequestBase httppost) {
		System.out.println("Request " + httppost.getMethod() + " URL:"
				+ httppost.getURI());
		for (Header header : httppost.getAllHeaders()) {
			System.out.println("    " + header.getName() + " : "
					+ header.getValue());
		}
		System.out.println();
	}
}
