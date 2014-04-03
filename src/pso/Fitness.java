package pso;

import rbm.RBM;

public class Fitness {
	
	RBM rbm;
	int[][] datapoints;
	
	public Fitness(RBM rbm, int[][] datapoints) {
		this.rbm = rbm;
		this.datapoints = datapoints;
	}
	
	public double evaluate(Particle particle) {
		
		rbm.setWeights(particle.position);
		
		double fitness = 0.0;
		int i;
		for (i = 0; i < datapoints.length; i++) {
			int[] datapoint = datapoints[i];
			rbm.setVisibleNodes(datapoint);
			rbm.hidden.sample();
			rbm.visible.sample();
			rbm.hidden.sample();
			int[] generated = rbm.read();
			
			// calculate percent correct
			int correct = 0;
			for (int j = 0; j < datapoint.length; j++)
				if (datapoint[j] == generated[j])
					correct++;
			fitness += (double)correct / datapoint.length;
			
			if (i > 1) break;
			
		}
		fitness /= i;
		
		particle.setFitness(fitness);
		
		return fitness;
		
	}

}
