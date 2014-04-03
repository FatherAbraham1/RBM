package trainer;

import java.util.HashMap;
import java.util.Map;

import rbm.Connection;
import rbm.Neuron;
import rbm.RBM;
import visualizer.ImagePane;

public abstract class Trainer {
	
	RBM rbm;
	ImagePane img = null;
	int[][] datapoints;
	
	public Trainer(RBM rbm, int[][] datapoints) {
		this.rbm = rbm;
		this.datapoints = datapoints;
	}
	
	public abstract void trainData(int epochs);
	
	public void drawProgress(ImagePane img) {
		this.img = img;
	}

}
