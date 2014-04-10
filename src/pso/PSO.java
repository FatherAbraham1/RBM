package pso;

import java.util.ArrayList;
import java.util.List;

public class PSO {
	
	public List<Particle> particles;
	double maxValue =  5;
	double minValue = -maxValue;
	static int numParticles = 1;
	static double momentum = 0.9;
//	static double cognitiveInfluence = 2.5;
//	static double socialInfluence = 0.05;
	static double cognitiveInfluence = 0.00;
	static double socialInfluence = 0.005;
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
		for (Particle particle : particles) {
			double fitness = fitnessEvaluation.evaluate(particle);
			if (fitness > globalBestFitness) {
				globalBestFitness = fitness;
				globalBest = particle;
			}
			avgFitness += fitness;
			minFitness = Math.min(fitness, minFitness);
		}
		avgFitness /= particles.size();
		double range = globalBestFitness - minFitness;
		//System.out.format("FITNESS: (%10f , %10f)   %f\n", minFitness, globalBestFitness, avgFitness);
		/*
		System.out.format("RANGE: %10f    AVG: %10f    BEST: %10f    POS: %f,%f,%f\n", 
				range, 
				avgFitness,
				globalBestFitness,
				globalBest.position[0], globalBest.position[1], globalBest.position[2]);
				//globalBest.personalBestPosition[0], globalBest.personalBestPosition[1], globalBest.personalBestPosition[2]);
		*/
		
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
