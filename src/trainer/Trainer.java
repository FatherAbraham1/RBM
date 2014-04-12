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
		rbm.setVisibleNodes(data.get(0).getVector());
	}
	
	public abstract int trainData(int epochs);
	
	public void drawProgress(ImagePane img) {
		this.img = img;
	}

}
