package driver;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		
		// choose the trainer
		Trainer trainerCD = new TrainerCD(rbm, data);
		Trainer trainerPSO = new TrainerPSO(rbm, data);
		
		Trainer trainer = trainerCD;
		trainer.drawProgress(new ImagePane(width, 10.0));
		
		// train on all data
		trainer.trainData(3);
		
	}

}
