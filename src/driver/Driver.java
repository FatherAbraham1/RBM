package driver;

import rbm.RBM;
import tester.ConfusionMatrix;
import tester.Tester;
import trainer.Trainer;
import trainer.TrainerCD;
import trainer.TrainerPSO;
import visualizer.ImagePane;
import visualizer.Visualizer;
import data.Data;
import data.PCA;
import data.Parser;

public class Driver {
	
	//static PCA pca = new PCA(783);
	static PCA pca = null;
	static int width = 28;
	static int height = 28;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		RBM rbm = new RBM(10);
		
//		trainImage(rbm, "data/train/7.txt", "null");
		trainImages(rbm);

	}
	
	private static void trainImages(RBM rbm) {
		
		int minDigit = 0;
		int maxDigit = 9;
		int trainCap = 10;
		int testCap = 0;
		
		// parse the 10 digits
		System.out.println("Parsing...");
		Parser parser;
		// train
		Data trainData = new Data();
		parser = new Parser(trainData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parseLabelFile("data/train/"+i+".txt", String.valueOf(i), trainCap);
		trainData.shuffle();
		// test
		Data testData = new Data();
		parser = new Parser(testData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parseLabelFile("data/test/"+i+".txt", String.valueOf(i), testCap);
		testData.shuffle();
		
		System.out.println("Training...");
		/*
		// Just CD
		Trainer trainer = new TrainerCD(rbm, trainData);
		//trainer.drawProgress(new ImagePane(width, 10.0));
		trainer.trainData(20);
		*/
		/*
		// CD then PSO
		Trainer trainerCD = new TrainerCD(rbm, trainData);
		trainerCD.trainData(20);
		Trainer trainerPSO = new TrainerPSO(rbm, trainData);
		trainerPSO.trainData(20);
		*/
		
		// AlternatingCD and PSO
		// BUG: right now PSO is making this worse, but at a minimum it should be the same
		// since it uses the answer to CD as a starting point, it can just return that when its done
		for (int i = 0; i < 5; i++) {
			Trainer trainerCD = new TrainerCD(rbm, trainData);
			trainerCD.trainData(3);
			Trainer trainerPSO = new TrainerPSO(rbm, trainData);
			trainerPSO.trainData(100);
		}
		
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
		parser.parseLabelFile(file, label, 100);
		
		
		Trainer trainerCD = new TrainerCD(rbm, data);
		trainerCD.trainData(3);
		
		Trainer trainerPSO = new TrainerPSO(rbm, data);
		trainerPSO.drawProgress(new ImagePane(width, 10.0));
		trainerPSO.trainData(30);
		
		
		/*
		Trainer trainer = new TrainerPSO(rbm, data);
		trainer.drawProgress(new ImagePane(width, 10.0));
		//trainer.trainData(50000);
		trainer.trainData(1000);
		
		ImagePane img = new ImagePane(width, 10.0);
		for (int i = 0; i < 1; i++)
			img.showImage(rbm.daydream(1));
		*/
		
	}
	
private static void trainImagesPCA(RBM rbm) {
		
		int minDigit = 0;
		int maxDigit = 9;
		int trainCap = 10;
		int testCap = 100;
		
		// parse the 10 digits
		System.out.println("Parsing...");
		Parser parser;
		// train
		Data trainData = new Data();
		parser = new Parser(trainData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parseLabelFile("data/train/"+i+".txt", String.valueOf(i), trainCap);
		trainData.shuffle();
		// test
		Data testData = new Data();
		parser = new Parser(testData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parseLabelFile("data/test/"+i+".txt", String.valueOf(i), testCap);
		testData.shuffle();
		
		if (pca != null) {
			System.out.println("REDUCING "+trainData.datapointSize()+" TO "+pca.numPrincipleComponents());
			trainData = pca.runPCA(trainData);
			testData = pca.runPCA(testData);
			trainData.convertToBinary();
			testData.convertToBinary();
			System.out.println(trainData.get(0));
		}
		
		System.out.println("Training...");
		Trainer trainer = new TrainerCD(rbm, trainData);
		//trainer.drawProgress(new ImagePane(width, 10.0));
		trainer.trainData(20);
		
		System.out.println("Testing...");
		Tester tester = new Tester(rbm);
		ConfusionMatrix confusionMatrix = tester.test(testData);
		confusionMatrix.clearLabels();
		System.out.println(confusionMatrix);
		System.out.println();
		System.out.println("Accuracy: "+confusionMatrix.getAccuracy());
		
		
	}

}
