package pso;

import rbm.RBM;
import data.Data;
import data.Datapoint;

public class Fitness {
	
	RBM rbm;
	Data datapoints;
	
	public Fitness(RBM rbm, Data data) {
		this.rbm = rbm;
		this.datapoints = data;
	}
	
	public double evaluate(Particle particle) {
		
		rbm.setWeights(particle.position);
		
		double fitness = 0.0;
		int i;
		for (i = 0; i < datapoints.size(); i++) {
			Datapoint datapoint = datapoints.get(i);
			rbm.setVisibleNodes(datapoint.vector());
			rbm.hidden.sample();
			rbm.visible.sample();
			rbm.hidden.sample();
			int[] generated = rbm.read();
			
			// calculate percent correct
			int correct = 0;
			for (int j = 0; j < datapoint.size(); j++)
				if (datapoint.get(j) == generated[j])
					correct++;
			fitness += (double)correct / datapoint.size();
			
		}
		fitness /= (i+1);
		
		particle.setFitness(fitness);
		
		return fitness;
		
	}

}
