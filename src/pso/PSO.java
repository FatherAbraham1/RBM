package pso;

import java.util.ArrayList;
import java.util.List;

public class PSO {
	
	double maxValue =  30;
	double minValue = -maxValue;
	static int numParticles = 10;
	static double momentum = 0.09;
	static double cognitiveInfluence = 1.0;
	static double socialInfluence = 1.0;
	static double maxSpeed = 0.01;
	static double velocityInfluence = 0.001;
	
	boolean echo = false;
	public List<Particle> particles;
	static Particle globalBest = null;
	double globalBestFitness = -1 * Double.MAX_VALUE;
	Fitness fitnessEvaluation;
	double avgFitness = 0.0;
	int convergenceCount = 0;
	double[] CDPosition;
	
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
		for (Particle particle : particles) {
			particle.runIteration();
		}
		evaluateParticles();
	}
	
	public void evaluateParticles() {
		
		// evaluate all particles
		double minFitness = Double.MAX_VALUE;
		double avgFitness = 0.0;
		double maxFitness = -minFitness;
		for (Particle particle : particles) {
			double fitness = fitnessEvaluation.evaluate(particle);
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

}
