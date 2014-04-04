package rbm;

public class Error {
	
	public static double mse(int[] vector1, int[] vector2) {
		if (vector1.length != vector2.length) {
			System.out.println("ERROR: Vector lengths do not match.");
			System.exit(1);
		}
		double error = 0.0;
		for (int i = 0; i < vector1.length; i++)
			error += Math.pow((vector1[i] - vector2[i]), 2);
		error /= vector1.length;
		return error;
	}
	
	public static double logLikelihood() {
		return 0.0;
	}

}
