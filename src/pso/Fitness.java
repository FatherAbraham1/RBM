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
			rbm.setVisibleNodes(data.vectorLabeled(datapoint));
			rbm.hidden.sample();
			rbm.visible.sample();
			rbm.hidden.sample();
			
			//int[] generated = rbm.readVisible();
			
			int[] vector = data.vectorLabeled(datapoint);
			error += Error.mse(vector, rbm.sample(vector));
			
			/*
			// calculate percent correct
			int correct = 0;
			for (int j = 0; j < datapoint.size(); j++)
				if (datapoint.get(j) == generated[j])
					correct++;
			fitness += (double)correct / datapoint.size();
			*/
			
		}
		error /= data.numDatapoints();
		//fitness /= (i+1);
		
		fitness = 1 - error;
		
		particle.setFitness(fitness);
		
		return fitness;
		
	}

}
