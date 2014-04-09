package pso;


import tools.Tools;

public class Particle {
	
	int size;
	double[] position, velocity, personalBestPosition;
	private double personalBestFitness;
	private double minValue, maxValue;
	private double maxSpeed = 0.01;
	//private double velocityInfluence = 0.0000001;
	private double velocityInfluence = 0.001;

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
		this.minValue = minValue;
		this.maxValue = maxValue;
		initialize();
		updatePersonalBestPosition(position);
	}
	
	public void reinitialize() {
		initialize();
	}
	
	private void initialize() {
		for (int i = 0; i < size; i++) {
			position[i] = Tools.getRandomDouble(minValue, maxValue);
			velocity[i] = Tools.getRandomDouble(-maxSpeed, maxSpeed);
		}
	}
	
	public void runIteration() {
		velocityUpdate();
		move();
	}
	
	private void move() {
		int margin = 2;
		for (int i = 0; i < size; i++) {
			// TODO: this is causing problems
			position[i] += velocity[i] * velocityInfluence;
			if (position[i] < minValue) {
				position[i] = minValue + margin;
				velocity[i] = 0;
				//velocity[i] *= -0.5;
			}
			if (position[i] > maxValue) {
				position[i] = maxValue - margin;
				velocity[i] = 0;
				//velocity[i] *= -0.5;
			}
		}
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
		clampSpeed();
	}
	
	private void clampSpeed() {
		for (int i = 0; i < velocity.length; i++) {
			velocity[i] = Math.max(Math.min(velocity[i], maxSpeed), -maxSpeed);
		}
	}
	
	public void setFitness(double fitness) {
		if (fitness > personalBestFitness) {
			personalBestFitness = fitness;
			updatePersonalBestPosition(position);
		}
	}
	
	private void updatePersonalBestPosition(double[] position) {
		if (personalBestPosition == null)
			personalBestPosition = new double[position.length];
		for (int i = 0; i < position.length; i++)
			personalBestPosition[i] = position[i];
	}
	
	public void setPosition(double[] position) {
		this.position = position;
	}

}
