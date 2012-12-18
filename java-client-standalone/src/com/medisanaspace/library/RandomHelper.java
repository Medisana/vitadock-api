package com.medisanaspace.library;

import java.util.Random;

/**
 * Helper class to generate random values (especially for testing).
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 */
public final class RandomHelper {

	private static Random random = new Random();

	private RandomHelper() {
	}

	public static int generateInt(final int min, final int max) {
		if (max < min) {
			throw new RuntimeException(
					"Could not generate random number, max value is smaller than min value ("
							+ max + " < " + min + ").");
		}
		return random.nextInt(1 + max - min) + min;
	}

	// FIXME
	public static int generateLong(final long min, final long max) {
		if (max < min) {
			throw new RuntimeException(
					"Could not generate random number, max value is smaller than min value ("
							+ max + " < " + min + ").");
		}
		return random.nextInt((int) (1 + max - min)) + (int) min;
	}

	public static int generateInt(final int max) {
		return random.nextInt(max);
	}

	public static Float generateFloat(final float min, final float max) {
		return random.nextFloat() * (max - min) + min;
	}

	public static Boolean generateBoolean() {
		return random.nextBoolean();
	}

	public static Float generateFloat() {
		return random.nextFloat();
	}
}
