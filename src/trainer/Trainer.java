package trainer;

import rbm.RBM;
import visualizer.ImagePane;
import data.Data;

public abstract class Trainer {
	
	RBM rbm;
	ImagePane img = null;
	Data data;
	
	public Trainer(RBM rbm, Data data) {
		this.rbm = rbm;
		this.data = data;
	}
	
	public abstract void trainData(int epochs);
	
	public void drawProgress(ImagePane img) {
		this.img = img;
	}

}
