package rbm;

import java.util.regex.Matcher;

public class Error {
	
	public static double mse(int[] vector1, int[] vector2) {
		matchError(vector1, vector2);
		double error = 0.0;
		for (int i = 0; i < vector1.length; i++)
			error += Math.pow((vector1[i] - vector2[i]), 2);
		error /= vector1.length;
		return error;
	}
	
	/**
	 * Mean Error. Same as MSE but no squaring.
	 * Note: for binary values, this is identical to MSE but less expensive.
	 * @return	Mean Error
	 */
	public static double me(int[] vector1, int[] vector2) {
		matchError(vector1, vector2);
		double error = 0.0;
		for (int i = 0; i < vector1.length; i++)
			error += Math.abs(vector1[i] - vector2[i]);
		error /= vector1.length;
		return error;
	}
	
	public static double logLikelihood() {
		return 0.0;
	}
	
	private static void matchError(int[] vector1, int[] vector2) {
		if (vector1.length != vector2.length) {
			System.out.println("ERROR: Vector lengths do not match.");
			System.exit(1);
		}
	}

}
