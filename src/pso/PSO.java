package pso;

import java.util.ArrayList;
import java.util.List;

public class PSO {
	
	double maxValue =  30;
	double minValue = -maxValue;
	static int numParticles = 8;
	static double momentum = 0.9;
	static double maxMomentum = momentum;
	static double minMomentum = 0.4;
	static double cognitiveInfluence = 1.0;
	static double socialInfluence = 1.0;
	static double maxSpeed = 0.7;
	static int numFitnessTests = 2;
	boolean echo = false;
	
	public List<Particle> particles;
	static Particle globalBest = null;
	double globalBestFitness = -1 * Double.MAX_VALUE;
	Fitness fitnessEvaluation;
	double avgFitness = 0.0;
	int convergenceCount = 0;
	double[] CDPosition;
	int timestep = 0;
	int maxIterations = 0;
	
	// NOTE: the influences can be local to the particle
	
	public PSO(int particleSize, Fitness fitnessEvaluation) {
		particles = new ArrayList<>(numParticles);
		this.fitnessEvaluation = fitnessEvaluation;
		for (int i = 0; i < numParticles; i++) {
			particles.add(new Particle(particleSize, minValue, maxValue));
		}
		CDPosition = new double[particleSize];
		evaluateParticles();
	}
	
	public void update() {
		timestep++;
		for (Particle particle : particles) {
			particle.runIteration();
		}
		evaluateParticles();
		//annealMomentumLinear();
	}
	
	public void setMaxIterations(int iterations) {
		maxIterations = iterations;
	}
	
	public void annealMomentumLinear() {
		if (maxIterations <= 0) {
			System.out.println("ERROR: Max iterations must be set.");
			System.exit(1);
		}
		int timeleft = maxIterations - timestep;
		momentum = minMomentum + (((double)timeleft/maxIterations) * (maxMomentum - minMomentum));
	}
	
	public void annealMomentumExponential() {
		if (maxIterations <= 0) {
			System.out.println("ERROR: Max iterations must be set.");
			System.exit(1);
		}
		int timeleft = maxIterations - timestep;
		double factor = Math.pow(((double)timeleft/maxIterations), 2);
		momentum = minMomentum + (factor * (maxMomentum - minMomentum));
	}
	
	public void evaluateParticles() {
		
		// evaluate all particles
		double minFitness = Double.MAX_VALUE;
		double avgFitness = 0.0;
		double maxFitness = -minFitness;
		for (Particle particle : particles) {
			double fitness = 0;
			for (int i = 0; i < numFitnessTests; i++)
				fitness += fitnessEvaluation.evaluate(particle);
			fitness /= numFitnessTests;
			if (fitness > globalBestFitness) {
				globalBestFitness = fitness;
				globalBest = particle;
			}
			avgFitness += fitness;
			minFitness = Math.min(fitness, minFitness);
			maxFitness = Math.max(fitness, maxFitness);
		}
		avgFitness /= particles.size();
		double range = globalBestFitness - minFitness;
		//System.out.format("FITNESS: (%10f , %10f)   %f\n", minFitness, globalBestFitness, avgFitness);
		if (echo) {
			System.out.format("RANGE: %10f    FITNESS: (%8f,%8f,%8f)    BEST: %10f    POS: %f,%f,%f\n", 
					range, 
					minFitness, avgFitness, maxFitness,
					globalBestFitness,
					globalBest.position[0], globalBest.position[1], globalBest.position[2]);
					//globalBest.personalBestPosition[0], globalBest.personalBestPosition[1], globalBest.personalBestPosition[2]);
		}
		if (this.avgFitness == avgFitness) {
			convergenceCount++;
		} else {
			this.avgFitness = avgFitness;
			convergenceCount = 0;
		}
		
	}
	
	public void setPositions(double[] position) {
		for (Particle particle : particles)
			for (int i = 0; i < position.length; i++)
				particle.position[i] = position[i];
		evaluateParticles();
	}
	
	public double[] getSolution() {
		return globalBest.personalBestPosition;
	}
	
	public double getSolutionFitness() {
		return globalBestFitness;
	}

}
