package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	
	Data data;
	String delim = " ";
	
	public Parser(Data data) {
		this.data = data;
	}
	
	/**
	 * Parses a file that contains datapoints for a single label.
	 * @return	A list of datapoints
	 */
	public void parseLabelFile(String filePath, String label) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] elements = line.split(delim);
				int[] points = new int[elements.length];
				for (int i = 0; i < elements.length; i++)
					points[i] = Integer.valueOf(elements[i]);
				data.add(points, label);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
