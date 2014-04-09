package trainer;

import pso.Fitness;
import pso.PSO;
import rbm.RBM;
import data.Data;

public class TrainerPSO extends Trainer {
	
	PSO pso = null;
	Fitness fitness;
	
	public TrainerPSO(RBM rbm, Data data) {
		super(rbm, data);
		double[] weights = rbm.getWeights(); // test
		fitness = new Fitness(rbm, data);
		pso = new PSO(rbm.connections.size(), fitness);
		
		pso.setPositions(weights);
	}
	
	public void trainData(int epochs) {
		for (int i = 0; i < epochs; i++) {
			pso.update();
			if (img != null)
				img.showImage(rbm.readVisible());
		}
		rbm.setWeights(pso.getSolution());
	}

}
