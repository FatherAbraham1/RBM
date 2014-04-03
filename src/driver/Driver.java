package driver;

import rbm.RBM;
import trainer.Trainer;
import trainer.TrainerCD;
import trainer.TrainerPSO;
import visualizer.ImagePane;
import data.Data;
import data.Parser;

public class Driver {
	
	static int width = 28;
	static int height = 28;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		RBM rbm = new RBM(width * height, 8);
		rbm.connectFully();
		
		trainImages(rbm, "data/train/0.txt", "zero");

	}
	
	private static void trainImages(RBM rbm, String file, String label) {
		
		Data data = new Data();
		Parser parser = new Parser(data);
		parser.parseLabelFile(file, label);
		
		
//		Trainer trainer = new TrainerCD(rbm, data);
//		trainer.trainData(3);
		
		data.truncate(3);
		Trainer trainer = new TrainerPSO(rbm, data);
		trainer.trainData(300);
		
		trainer.drawProgress(new ImagePane(width, 10.0));
		
		
	}

}
