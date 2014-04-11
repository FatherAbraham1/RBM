package trainer;

import pso.Fitness;
import pso.PSO;
import rbm.RBM;
import data.Data;

public class TrainerPSO extends Trainer {
	
	PSO pso = null;
	Fitness fitness;
	int convergenceNumber = 300;
	// .9544
	
	public TrainerPSO(RBM rbm, Data data) {
		super(rbm, data);
		fitness = new Fitness(rbm, data);
		pso = new PSO(rbm.connections.size(), fitness);
	}
	
	public void setToExistingRBM() {
		pso.setPositions(rbm.getWeights());
	}
	
	public void trainData(int epochs) {
		pso.setMaxIterations(epochs);
		double lastFitness = 0;
		int sameCount = 0;
		int i;
		for (i = 0; i < epochs; i++) {
			pso.update();
			double fitness = pso.getSolutionFitness();
			if (fitness == lastFitness) {
				sameCount++;
			} else {
				lastFitness = fitness;
				sameCount = 0;
			}
			if (sameCount >= convergenceNumber || fitness == 1)
				break;
			if (img != null)
				img.showImage(rbm.readVisible());
		}
		if (i < epochs - 1) {
			System.out.println("CONVERGED AT TIMESTEP "+i);
		}
		rbm.setWeights(pso.getSolution());
	}

}
