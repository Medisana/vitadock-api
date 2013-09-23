package com.medisanaspace.library;

import java.util.Random;

/**
 * Helper class to generate random values (especially for testing).
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 * @version $Revision: 1.0 $
 */
public final class RandomHelper {

	private static Random random = new Random();
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";



	private RandomHelper() {
	}

	/**
	 * Method randomString.
	 * @param len int
	 * @return String
	 */
	public static String randomString( int len ) 
	{
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( random.nextInt(AB.length()) ) );
	   return sb.toString();
	}

	
	/**
	 * Method generateInt.
	 * @param min int
	 * @param max int
	 * @return int
	 */
	public static int generateInt(final int min, final int max) {
		if (max < min) {
			throw new RuntimeException(
					"Could not generate random number, max value is smaller than min value ("
							+ max + " < " + min + ").");
		}
		return random.nextInt(1 + max - min) + min;
	}

	// FIXME
	/**
	 * Method generateLong.
	 * @param min long
	 * @param max long
	 * @return int
	 */
	public static int generateLong(final long min, final long max) {
		if (max < min) {
			throw new RuntimeException(
					"Could not generate random number, max value is smaller than min value ("
							+ max + " < " + min + ").");
		}
		return random.nextInt((int) (1 + max - min)) + (int) min;
	}

	/**
	 * Method generateInt.
	 * @param max int
	 * @return int
	 */
	public static int generateInt(final int max) {
		return random.nextInt(max);
	}

	/**
	 * Method generateFloat.
	 * @param min float
	 * @param max float
	 * @return Float
	 */
	public static Float generateFloat(final float min, final float max) {
		return random.nextFloat() * (max - min) + min;
	}

	/**
	 * Method generateBoolean.
	 * @return Boolean
	 */
	public static Boolean generateBoolean() {
		return random.nextBoolean();
	}

	/**
	 * Method generateFloat.
	 * @return Float
	 */
	public static Float generateFloat() {
		return random.nextFloat();
	}
}
