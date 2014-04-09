package tools;

import java.util.Random;

public class Tools {

	// a seeded random object to be used by all
	public static Random random = new Random(11235);
	
	/**
	 * Returns a random value between any two double values.
	 * 
	 * @param minValue	The minimum value that can be produced.
	 * @param maxValue	The maximum value that can be produced.
	 * @return			A random value in the range (minValue,maxValue)
	 */
	public static double getRandomDouble(double minValue, double maxValue) {
		return getRandomDouble(minValue, maxValue, random);
	}
	
	/**
	 * Returns a random value between any two double values.
	 * 
	 * @param minValue	The minimum value that can be produced.
	 * @param maxValue	The maximum value that can be produced.
	 * @param random	The random generator to use for producing doubles.
	 * @return			A random value in the range (minValue,maxValue)
	 */
	public static double getRandomDouble(double minValue, double maxValue, Random random) {
		return minValue + (maxValue - minValue) * random.nextDouble();
	}
	
	/**
	 * Rounds a double to a specified number of decimal places.
	 * 
	 * @param value		The value to be rounded.
	 * @param decimals	The number of decimal places to round to.
	 * @return			The rounded value.
	 */
	public static double round(double value, int decimals) {
		int pow = (int) Math.pow(10, decimals);
		double val = Math.round(value * pow);
		return (double) Math.round(val) / pow;
	}
	
	public static int toInt(double value) {
		return (int) Math.round(value);
	}
	
	public static int[] oneVector(int val, int vectorSize) {
		int[] vector = new int[vectorSize];
		vector[val] = 1;
		return vector;
	}
	
}
