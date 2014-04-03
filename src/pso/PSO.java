package pso;

import java.util.ArrayList;
import java.util.List;

public class PSO {
	
	List<Particle> particles;
	double minValue = -2;
	double maxValue =  2;
	static double momentum = 0.1;
	// NOTE: these influences can be local to the particle
	static double cognitiveInfluence = 0.7;
	static double socialInfluence = 0.3;
	static Particle globalBest = null;
	double globalBestFitness = 0.0;
	Fitness fitnessEvaluation;
	
	public PSO(int numParticles, int particleSize, Fitness fitnessEvaluation) {
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
		for (Particle particle : particles) {
			double fitness = fitnessEvaluation.evaluate(particle);
			if (fitness > globalBestFitness) {
				globalBestFitness = fitness;
				globalBest = particle;
			}
		}
		System.out.println("FITNESS: "+globalBestFitness);
	}

}
