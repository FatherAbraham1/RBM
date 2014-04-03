package pso;

import java.util.ArrayList;
import java.util.List;

public class PSO {
	
	List<Particle> particles;
	double minValue = -4;
	double maxValue =  4;
	static int numParticles = 75;
	static double momentum = 4;
	static double cognitiveInfluence = 2.5;
	static double socialInfluence = 2.5;
	static Particle globalBest = null;
	double globalBestFitness = 0.0;
	Fitness fitnessEvaluation;
	double avgFitness = 0.0;
	int convergenceCount = 0;
	
	// NOTE: the influences can be local to the particle
	
	public PSO(int particleSize, Fitness fitnessEvaluation) {
		particles = new ArrayList<>(numParticles);
		this.fitnessEvaluation = fitnessEvaluation;
		for (int i = 0; i < numParticles; i++) {
			particles.add(new Particle(particleSize, minValue, maxValue));
		}
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
		System.out.format("RANGE: %10f    AVG: %10f    BEST: %10f\n", 
				range, 
				avgFitness,
				globalBestFitness);
		
		if (this.avgFitness == avgFitness) {
			convergenceCount++;
		} else {
			this.avgFitness = avgFitness;
			convergenceCount = 0;
		}
		
		if (range < 0.01 || convergenceCount > 5) {
			System.out.println("REINITIALIZING");
			for (Particle particle : particles)
				particle.reinitialize();
		}
	}

}
