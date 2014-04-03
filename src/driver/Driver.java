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
		
		//trainImages(rbm, "data/train/9/");
		//trainImages(rbm, "data/train/0.txt", "zero");
		
		convertImagesToText("data/images/test/9/", "data/test/9.txt");
		
//		ImagePane img = new ImagePane();
//		img.showImage(createImage(data), 5.0);
		
//		Visualizer visualizer = new Visualizer(rbm);
//		visualizer.showStructure();

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
//		trainer.trainData(3);
		
	}
	
	private static void convertImagesToText(String filePath, String output) {
		File dir = new File(filePath);
		File[] images = dir.listFiles();
		int[][] datapoints = new int[images.length][]; 
		
		// read in data from images
		int i = 0;
		for (File image : images) {
			int[][] data = readImage(image.getAbsolutePath());
			if (data.length != width || data[0].length != height) {
				System.out.println("ERROR: Invalid image dimension");
				System.exit(0);
			}
			datapoints[i] = serialize(data);
			i++;
		}
	 
		try {
			
			File file = new File(output);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int[] datapoint : datapoints) {
				String line = "";
				for (int val : datapoint) {
					line += val+" ";
				}
				line = line.substring(0,line.length()-1)+"\n";
				bw.write(line);
			}
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int[] serialize(int[][] data) {
		int[] newData = new int[data.length * data[0].length];
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				newData[i*data[i].length+j] = data[i][j];
		return newData;
	}
	
	private static int[][] readImage(String file) {
		try {
			BufferedImage image = ImageIO.read(new File(file));
			int width = image.getWidth();
			int height = image.getHeight();
			int[][] data = new int[height][width];
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					Color c = new Color(image.getRGB(col, row));
					int rgb = c.getRed() + c.getGreen() + c.getBlue();
					if (rgb < 200)
						data[row][col] = 0;
					else
						data[row][col] = 1;
				}
			}
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void print(int[][] data) {
		for (int i = 0; i < data.length; i++)
			print(data[i]);
	}
	
	private static void print(int[] data) {
		for (int i = 0; i < data.length; i++)
			System.out.print(data[i]+" ");
		System.out.println();
	}

}
