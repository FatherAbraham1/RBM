package pso;

import java.util.ArrayList;
import java.util.List;

public class PSO {
	
	List<Particle> particles;
	double minValue = -0.3;
	double maxValue =  0.3;
	static double momentum = 0.3;
	// NOTE: these influences can be local to the particle
	static double cognitiveInfluence = 0.3;
	static double socialInfluence = 0.3;
	static Particle globalBest = null;
	double globalBestFitness = 0.0;
	
	public PSO(int numParticles, int particleSize, Fitness fitnessEvaluation) {
		particles = new ArrayList<>(numParticles);
		for (int i = 0; i < numParticles; i++) {
			particles.add(new Particle(particleSize, minValue, maxValue));
		}
		// evaluate all particles
		for (Particle particle : particles) {
			double fitness = fitnessEvaluation.evaluate(particle);
			if (fitness > globalBestFitness) {
				globalBestFitness = fitness;
				globalBest = particle;
			}
		}
		
	}
	
	public void update() {
		for (int i = 0; i < particles.size(); i++) {
			Particle particle = particles.get(i);
			particle.runIteration();
			double fitness = particle.fitness();
			if (fitness > globalBestFitness) {
				globalBestFitness = fitness;
				globalBest = particle;
			}
		}
	}

}
