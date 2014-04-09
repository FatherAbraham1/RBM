package driver;

import java.util.Random;

import rbm.RBM;
import tester.ConfusionMatrix;
import tester.Tester;
import tools.Tools;
import trainer.Trainer;
import trainer.TrainerCD;
import trainer.TrainerPSO;
import visualizer.ImagePane;
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
		
		for (int i = 1; i < 50; i++)
			createXOR(1).writeToFile("data/xor/"+i+".txt");
		
//		trainImage(rbm, "data/train/7.txt", 7);
//		trainImages(rbm);

	}
	
	private static Data createXOR(int n) {
		Data data = new Data();
		int examples = 100;
		Random random = new Random(11235);
		for (int i = 0; i < examples; i++) {
			int[] points = new int[n*2];
			int[] label = new int[n];
			for (int j = 0; j < n; j++) {
				points[j] = random.nextInt(2);
				points[n+j] = random.nextInt(2);
				label[j] = points[j]^points[n+j];
				data.add(points, label);
			}
		}
		return data;
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
			parser.parse("data/train/"+i+".txt", Tools.oneVector(i,10), trainCap);
		trainData.shuffle();
		// test
		Data testData = new Data();
		parser = new Parser(testData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parse("data/test/"+i+".txt", Tools.oneVector(i,10), testCap);
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
//			Trainer trainerPSO = new TrainerPSO(rbm, trainData);
//			trainerPSO.trainData(100);
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
	
	private static void trainImage(RBM rbm, String file, int label) {
		
		Data data = new Data();
		Parser parser = new Parser(data);
		parser.parse(file, Tools.oneVector(label,10), 100);
		
		
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
			parser.parse("data/train/"+i+".txt", Tools.oneVector(i,10), trainCap);
		trainData.shuffle();
		// test
		Data testData = new Data();
		parser = new Parser(testData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parse("data/test/"+i+".txt", Tools.oneVector(i,10), testCap);
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
