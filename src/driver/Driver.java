package driver;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import rbm.RBM;
import visualizer.Visualizer;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int[][] data = readImage("data/train/0/0_0000.png");
		
		RBM rbm = new RBM(data.length * data[0].length, 8);
		rbm.connectFully();
		System.out.println(rbm);
		
//		Visualizer visualizer = new Visualizer(rbm);
//		visualizer.showStructure();
		
//		MNISTParser parser = new MNISTParser();
//		parser.parse("data/t10k-labels-idx1-ubyte", "data/t10k-images-idx3-ubyte", "data/output");

	}
	
	private static int[][] readImage(String file) {
		try {
			BufferedImage image = ImageIO.read(new File(file));
			int width = image.getWidth();
			int height = image.getHeight();
			int[][] data = new int[height][width];
			int black = new Color(255, 255, 255).getRGB();
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					if (image.getRGB(col, row) == black)
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

}
