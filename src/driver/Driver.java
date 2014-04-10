package driver;

import rbm.RBM;
import tester.ConfusionMatrix;
import tester.Tester;
import tools.Tools;
import trainer.Trainer;
import trainer.TrainerCD;
import trainer.TrainerPSO;
import visualizer.ImagePane;
import visualizer.Visualizer;
import data.Data;
import data.Datapoint;
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
		
		//RBM rbm = new RBM(10);
		RBM rbm = new RBM(25);
		
//		trainImage(rbm, "data/train/7.txt", 7);
//		trainImages(rbm);
		testXOR(rbm, 2);

	}
	
	private static void testXOR(RBM rbm, int n) {
		
		// parse the 10 digits
		System.out.println("Parsing...");
		Data trainData = new Data();
		Data testData = new Data();
		Parser parser = new Parser(trainData);
		parser.parseFile("data/xor/"+n+".txt");
		parser = new Parser(testData);
		parser.parseFile("data/xor/"+n+".txt");
		trainData.shuffle();
		testData.shuffle();
		trainData.truncate(50);
		testData.truncate(50);
		
		// train
//		Trainer trainerCD = new TrainerCD(rbm, trainData);
//		trainerCD.trainData(200);
		
		Trainer trainerPSO = new TrainerPSO(rbm, trainData);
		trainerPSO.trainData(20000);
		
		// show network
		Visualizer vis = new Visualizer(rbm);
		//vis.showStructure();
		
		System.out.println();
		System.out.println("TESTS");
		double accuracy;
		
		Tester tester = new Tester(rbm);
		accuracy = tester.testGenerative(testData);
		System.out.println("Accuracy: "+accuracy);
		
		/*
		Tester tester = new Tester(rbm);
		accuracy = tester.testMulti(testData);
		System.out.println("Accuracy: "+accuracy);
		*/
		/*
		int correct = 0;
		for (int i = 0; i < testData.numDatapoints(); i++) {
			Datapoint datapoint = testData.get(i);
			int[] vector = new int[datapoint.getFeatures().length+datapoint.getLabel().length];
			for (int j = 0; j < datapoint.getFeatures().length; j++)
				vector[j] = datapoint.getFeatures()[j];
			int[] result = rbm.sample(vector);
			//Tools.printVector(result);
			if (result.length == 3 && ((result[0]^result[1])==result[2]))
				correct++;
		}
		accuracy = (double) correct / testData.numDatapoints();
		System.out.println("ACCURACY: "+accuracy);
		*/
		
		/*
		System.out.println();
		System.out.println("TESTS");
		int[][] tests = new int[][] { {0,0,0}, {0,1,0}, {1,0,0}, {1,1,0} };
		for (int i = 0; i < tests.length; i++) {
			for (int j = 0; j < 5; j++) {
				int[] result = rbm.sample(tests[i], 1);
				for (int k = 0; k < result.length; k++)
					System.out.print(result[k]+" ");
				System.out.println();
			}
		}
		*/
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
			parser.parseLabelFile("data/train/"+i+".txt", Tools.oneVector(i,10), trainCap);
		trainData.shuffle();
		// test
		Data testData = new Data();
		parser = new Parser(testData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parseLabelFile("data/test/"+i+".txt", Tools.oneVector(i,10), testCap);
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
		parser.parseLabelFile(file, Tools.oneVector(label,10), 100);
		
		
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
			parser.parseLabelFile("data/train/"+i+".txt", Tools.oneVector(i,10), trainCap);
		trainData.shuffle();
		// test
		Data testData = new Data();
		parser = new Parser(testData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parseLabelFile("data/test/"+i+".txt", Tools.oneVector(i,10), testCap);
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
