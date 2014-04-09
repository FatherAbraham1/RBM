package visualizer;

import java.awt.FlowLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImagePane {
	
	JFrame frame;
	BufferedImage scaledImage;
	int width;
	double scale = 1.0;
	
	public ImagePane(int width, double scale) {
		this.width = width;
		this.scale = scale;
		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void showImage(BufferedImage image) {
		
		int w = image.getWidth();
		int h = image.getHeight();
		scaledImage = new BufferedImage((int)(w*scale), (int)(h*scale), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		scaledImage = scaleOp.filter(image, scaledImage);
		
		frame.getContentPane().removeAll();
		frame.getContentPane().add(new JLabel(new ImageIcon(scaledImage)));
		frame.pack();
		frame.setVisible(true);
	}
	
	public void showImage(int[][] image) {
		showImage(ImagePane.createImage(image));
	}
	
	public void showImage(int[] image) {
		int[][] generated = assemble(image, width);
		showImage(generated);
	}
	
	public static BufferedImage createImage(int[][] data) {
		int width = data.length;
		int height = data[0].length;
		int[] pixels = new int[width*height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixels[i*height + j] = data[i][j]*16777215;
			}
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, width, height, pixels);
		return image;
	}
	
	public static int[][] assemble(int[] data, int width) {
		int[][] newData = new int[width][data.length/width];
		for (int i = 0; i < newData.length; i++)
			for (int j = 0; j < newData[i].length; j++)
				newData[i][j] = data[i*newData[i].length+j];
		return newData;
	}

}
