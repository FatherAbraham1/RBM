package driver;

import rbm.RBM;
import tester.ConfusionMatrix;
import tester.Tester;
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
		
		RBM rbm = new RBM(8);
		
		trainImage(rbm, "data/train/7.txt", "null");
		//trainImages(rbm);

	}
	
	private static void trainImages(RBM rbm) {
		
		// parse the 10 digits
		System.out.println("Parsing...");
		Parser parser;
		// train
		Data trainData = new Data();
		parser = new Parser(trainData);
		for (int i = 0; i <= 9; i++)
			parser.parseLabelFile("data/train/"+i+".txt", String.valueOf(i), 100);
		trainData.shuffle();
		// test
		Data testData = new Data();
		parser = new Parser(testData);
		for (int i = 0; i <= 9; i++)
			parser.parseLabelFile("data/test/"+i+".txt", String.valueOf(i));
		testData.shuffle();
		
		System.out.println("Training...");
		Trainer trainer = new TrainerCD(rbm, trainData);
		trainer.drawProgress(new ImagePane(width, 10.0));
		trainer.trainData(1);
		
		System.out.println("Testing...");
		Tester tester = new Tester(rbm);
		ConfusionMatrix confusionMatrix = tester.test(testData);
		confusionMatrix.clearLabels();
		System.out.println(confusionMatrix);
		System.out.println();
		System.out.println("Accuracy: "+confusionMatrix.getAccuracy());
		
//		data.truncate(3);
//		Trainer trainer = new TrainerPSO(rbm, data);
//		trainer.drawProgress(new ImagePane(width, 10.0));
//		trainer.trainData(300);
		
		
	}
	
	private static void trainImage(RBM rbm, String file, String label) {
		
		Data data = new Data();
		Parser parser = new Parser(data);
		parser.parseLabelFile(file, label);
		
		Trainer trainer = new TrainerCD(rbm, data);
		trainer.drawProgress(new ImagePane(width, 10.0));
		trainer.trainData(5);
		
//		data.truncate(3);
//		Trainer trainer = new TrainerPSO(rbm, data);
//		trainer.trainData(300);
		
		
	}

}
