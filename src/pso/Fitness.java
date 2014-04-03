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
		for (int[] datapoint : datapoints) {
			rbm.setVisibleNodes(datapoint);
			rbm.hidden.sample();
			rbm.visible.sample();
			rbm.hidden.sample();
			int[] generated = rbm.read();
			
			// calculate percent correct
			int correct = 0;
			for (int i = 0; i < datapoint.length; i++)
				if (datapoint[i] == generated[i])
					correct++;
			fitness += (double)correct / datapoint.length;
		}
		fitness /= datapoints.length;
		
		return fitness;
		
	}

}
