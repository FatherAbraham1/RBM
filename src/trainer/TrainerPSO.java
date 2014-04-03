package trainer;

import pso.Fitness;
import pso.PSO;
import rbm.RBM;

public class TrainerPSO extends Trainer {
	
	PSO pso = null;
	int numParticles = 1;
	double learningRate = 0.5;
	Fitness fitness;
	
	public TrainerPSO(RBM rbm, int[][] datapoints) {
		super(rbm, datapoints);
		fitness = new Fitness(rbm, datapoints);
		pso = new PSO(numParticles, rbm.connections.size(), fitness);
	}
	
	public void trainData(int epochs) {
		for (int i = 0; i < epochs; i++) {
			pso.update();
		}
	}

}
