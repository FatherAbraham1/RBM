package pso;


import tools.Tools;

public class Particle {
	
	int size;
	double[] position, velocity, personalBestPosition;
	private double fitness, personalBestFitness;

	/**
	 * Create a new particle 
	 * 
	 * @param size		The search space for the particle.
	 * @param minValue	The minimum value accessible by the particle.
	 * @param maxValue	The maximum value accessible by the particle.
	 */
	public Particle(int size, double minValue, double maxValue) {
		this.size = size;
		position = new double[size];
		velocity = new double[size];
		for (int i = 0; i < size; i++) {
			position[i] = Tools.getRandomDouble(minValue, maxValue);
			velocity[i] = Tools.getRandomDouble(minValue, maxValue);
		}
		personalBestPosition = position;
	}
	
	public void runIteration() {
		velocityUpdate();
		move();
	}
	
	private void move() {
		for (int i = 0; i < size; i++)
			position[i] += velocity[i];
	}
	
	private void velocityUpdate() {
		// for each component in the vectors
		for (int i = 0; i < velocity.length; i++) {
			double term1 = PSO.momentum * velocity[i];
			double term2 = Tools.getRandomDouble(0, PSO.cognitiveInfluence) * (personalBestPosition[i] - position[i]);
			double term3 = Tools.getRandomDouble(0, PSO.socialInfluence) * (PSO.globalBest.personalBestPosition[i] - position[i]);;
			double update = term1 + term2 + term3;
			velocity[i] += update;
		}
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
		if (fitness > personalBestFitness) {
			personalBestFitness = fitness;
			personalBestPosition = position;
		}
	}

}
