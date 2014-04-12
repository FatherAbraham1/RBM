package driver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
	static Data trainData;
	static Data testData;
	
	static boolean humanEcho = true;
	static boolean dataEcho = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//RBM rbm = new RBM(10);
		
//		trainImage(rbm, "data/train/7.txt", 7);
//		trainImages(rbm);
		
		collectData("results/XOR_30.txt");
		//test(25, "images");
		
		// 5 = 85
		// 15 = 125
		// 25 = 175

	}
	
	private static void collectData(String filename) {
		String str = "Hidden Nodes\tVisible Nodes\tConnections\tError (CD)\tError (PSO)\t";
		str += "\t";
		str += "Hidden Nodes\tVisible Nodes\tConnections\tIterations (CD)\tIterations (PSO)";
		str += "\n";
		System.out.println(str);
		for (int hidden = 5; hidden <= 25; hidden += 10) {
			for (int n = 1; n <= 25; n++) {
				String result = test(hidden, "xor", n);
				//String result = test(hidden, "images");
				System.out.println(result);
				str += result + "\n";
			}
		}
		
		try {
			 
			File file = new File(filename);
			FileOutputStream fop = new FileOutputStream(file);
			if (!file.exists())
				file.createNewFile();
			
			byte[] contentInBytes = str.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Results File: "+filename);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String test(int rbmSize, String type, int n) {
		
		String result = "";
		
		int trainCap = 30;
		int testCap = 100;
		
		int iterationsCD = 1500;
		int iterationsPSO = 1500;
		
		checkType(type, n);
		
		if (type.equals("xor"))
			readXORData(n, trainCap, testCap);
		else if (type.equals("images"))
			readImageData(trainCap, testCap);
		
		double accuracyPSO = 0;
		double accuracyCD = 0;
		int convergencePSO = 0;
		int convergenceCD = 0;
		long runtimePSO = 0;
		long runtimeCD = 0;
		
		if (iterationsPSO > 0) {
			RBM rbmPSO = new RBM(rbmSize);
			// Train
			long startTime = System.currentTimeMillis();
			Trainer trainerPSO = new TrainerPSO(rbmPSO, trainData);
			convergencePSO = trainerPSO.trainData(iterationsPSO);
			runtimePSO = System.currentTimeMillis() - startTime;
			// Test
			Tester testerPSO = new Tester(rbmPSO);
			accuracyPSO = testerPSO.testGenerative(testData);
		}
		
		if (iterationsCD > 0) {
			RBM rbmCD = new RBM(rbmSize);
			// Train
			long startTime = System.currentTimeMillis();
			Trainer trainerCD = new TrainerCD(rbmCD, trainData);
			convergenceCD = trainerCD.trainData(iterationsCD);
			runtimeCD = System.currentTimeMillis() - startTime;
			// Test
			Tester testerCD = new Tester(rbmCD);
			accuracyCD = testerCD.testGenerative(testData);
		}
		
		if (dataEcho) {
			int hidden = rbmSize;
			int visible = trainData.get(0).getVector().length;
			int connections = hidden * visible + hidden + visible;
			double errorCD = Tools.round((1 - accuracyCD), 5);
			double errorPSO = Tools.round((1 - accuracyPSO), 5);
			
			result += hidden+"\t";
			result += visible+"\t";
			result += connections+"\t";
			result += errorCD+"\t";
			result += errorPSO+"\t";
			result += "\t";
			result += hidden+"\t";
			result += visible+"\t";
			result += connections+"\t";
			result += convergenceCD+"\t";
			result += convergencePSO+"\t";
		} else if (humanEcho) {
			System.out.println("TRAIN = "+trainCap+"      TEST = "+testCap);
			System.out.format("%12s: %f     %d\n","CD ("+iterationsCD+")", accuracyCD, runtimeCD);
			System.out.format("%12s: %f     %d\n","PSO ("+iterationsPSO+")", accuracyPSO, runtimePSO);
		}
		
		// show network
		//Visualizer vis = new Visualizer(rbm);
		//vis.showStructure();
		
		return result;
		
	}
	
	private static String test(int rbmSize, String type) {
		return test(rbmSize, type, 0);
	}
	
	private static void readImageData(int trainCap, int testCap) {
		
		//System.out.println("Parsing Images Data");
		
		int minDigit = 0;
		int maxDigit = 9;
		
		// parse the 10 digits
		Parser parser;
		// train
		trainData = new Data();
		parser = new Parser(trainData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parseLabelFile("data/train/"+i+".txt", Tools.oneVector(i,10), trainCap);
		trainData.shuffle();
		// test
		testData = new Data();
		parser = new Parser(testData);
		for (int i = minDigit; i <= maxDigit; i++)
			parser.parseLabelFile("data/test/"+i+".txt", Tools.oneVector(i,10), testCap);
		testData.shuffle();
	}
	
	private static void readImageData() {
		readImageData(0,0);
	}
	
	private static void readXORData(int n, int trainCap, int testCap) {
		// Parse
		//System.out.println("Parsing XOR Data ("+n+")");
		trainData = new Data();
		testData = new Data();
		Parser parser = new Parser(trainData);
		parser.parseFile("data/xor/"+n+".txt");
		parser = new Parser(testData);
		parser.parseFile("data/xor/"+n+".txt");
		trainData.truncate(trainCap);
		testData.truncate(trainCap, testCap);
	}
	
	private static void readXORData(int n) {
		readXORData(n, 0, 0);
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

	private static void checkType(String type, int n) {
		if (type.equals("xor")) {
			if (n <= 0 || n > 25) {
				System.out.println("ERROR: N must be in [0,25]");
				System.exit(1);
			}
		} else if (!type.equals("images")) {
			System.out.println("ERROR: Type must be 'xor' or 'images'");
			System.exit(1);
		}
	}

}
