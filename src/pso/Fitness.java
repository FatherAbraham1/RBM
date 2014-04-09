package pso;

import rbm.Error;
import rbm.RBM;
import data.Data;
import data.Datapoint;

public class Fitness {
	
	RBM rbm;
	Data data;
	
	public Fitness(RBM rbm, Data data) {
		this.rbm = rbm;
		this.data = data;
	}
	
	public double evaluate(Particle particle) {
		
		rbm.setWeights(particle.position);
		
		double fitness = 0.0;
		double error = 0.0;
		int i;
		for (i = 0; i < data.numDatapoints(); i++) {
			Datapoint datapoint = data.get(i);
			
			int[] vector = data.vectorLabeled(datapoint);
			error += Error.mse(vector, rbm.sample(vector));
			
		}
		error /= data.numDatapoints();
		
		fitness = 1 - error;
		particle.setFitness(fitness);
		return fitness;
		
	}

}
