package parser;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MNISTParser {
	
	private static byte[] getData(File file) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		byte[] data = new byte[(int)file.length()];
		dis.readFully(data);
		dis.close();
		return data;
	}

	private static boolean checkMagic(byte[] data, int magic){
		int data_magic = ((data[3] & 0xFF) << 0) | ((data[2] & 0xFF) << 8) | ((data[1] & 0xFF) << 16) | ((data[0] & 0xFF) << 24);
		return magic == data_magic;
	}

	private static int getLength(byte[] data){
		int length = ((data[7] & 0xFF) << 0) | ((data[6] & 0xFF) << 8) | ((data[5] & 0xFF) << 16) | ((data[4] & 0xFF) << 24);
		return length;
	}

	private static int[] getLabels(byte[] data){
		int[] labels = new int[data.length - 8];

		for(int i = 8; i < data.length; i++){
			labels[i-8] = data[i] & 0xFF;
		}

		for(int i = 0; i < labels.length; i++){
			if(labels[i] > 9 || labels[i] < 0){
				System.out.println("Label greater than 9 or less than 0!");
				System.exit(0);
			}
		}
		return labels;
	}

	private static BufferedImage[] getImages(byte[] image_data){
		int byte_index = 8;
		int image_index = 0;

		BufferedImage[] images = new BufferedImage[getLength(image_data)];

		int rows = ((image_data[byte_index+3] & 0xFF) << 0) | ((image_data[byte_index+2] & 0xFF) << 8) | ((image_data[byte_index+1] & 0xFF) << 16) | ((image_data[byte_index+0] & 0xFF) << 24);
		byte_index += 4;
		int cols = ((image_data[byte_index+3] & 0xFF) << 0) | ((image_data[byte_index+2] & 0xFF) << 8) | ((image_data[byte_index+1] & 0xFF) << 16) | ((image_data[byte_index+0] & 0xFF) << 24);
		byte_index += 4;

		if(rows != 28 || cols != 28){
			System.out.println("Rows/Cols error! " + rows + "x" + cols + ", byte position: " + (byte_index-8));
			System.exit(0);
		}

		while(byte_index < image_data.length){
			BufferedImage image = new BufferedImage(cols, rows,  BufferedImage.TYPE_BYTE_GRAY);
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					int grey_value = image_data[byte_index] & 0xFF;
					if(grey_value < 0 || grey_value > 255) {
						System.out.println("Pixel value error!");
						System.exit(0);
					}
					int pixel_value = ((grey_value & 0xFF) << 0) | ((grey_value & 0xFF) << 8) | ((grey_value & 0xFF) << 16) | ((255 & 0xFF) << 24);
					image.setRGB(j, i, pixel_value);
					byte_index++;
				}
			}
			images[image_index] = image;
			image_index++;
		}

		return images;
	}

	private static void writeImages(int[] labels, BufferedImage[] images, File directory){
		for(int image_class = 0; image_class < 10; image_class++){
			int index = 0;
			for(int i = 0; i < labels.length; i++){
				if(labels[i] == image_class){
					File file = new File(directory, image_class + "_" + String.format("%04d", index) + ".png");
					try {
						ImageIO.write(images[i], "png", file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					index++;
				}
			}
		}
	}

	public static void parse(String label_filename, String image_filename, String output_dirname){

		File label_file = new File(label_filename);
		File image_file = new File(image_filename);
		File output_dir = new File(output_dirname);

		byte[] label = null;
		byte[] image = null;

		if(!label_file.exists() || !label_file.isFile()){
			System.out.println("Label file is either a directory or does not exist");
			System.exit(0);
		}
		if(!image_file.exists() || !image_file.isFile()){
			System.out.println("Image file is either a directory or does not exist");
			System.exit(0);
		}
		if(!output_dir.exists() || !output_dir.isDirectory()){
			System.out.println("Output directory is either a file or does not exist");
			System.exit(0);
		}

		try {
			label = getData(label_file);
			image = getData(image_file);
		} catch(IOException e) {
			e.printStackTrace();
		}

		if(!checkMagic(label, 2049)){
			System.out.println("Label magic failed");
		}

		if(!checkMagic(image, 2051)){
			System.out.println("Image magic failed");
		}

		if(getLength(label) != getLength(image)){
			System.out.println("Length mis-match");
		}

		int[] labels = getLabels(label);
		BufferedImage[] images = getImages(image);

		writeImages(labels, images, output_dir);
	}
}