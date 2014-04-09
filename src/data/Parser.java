package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	
	Data data;
	String delim = " ";		// delimiter for attributes / labels
	String seperator = ":"; // separates attributes and labels
	
	public Parser(Data data) {
		this.data = data;
	}
	
	public void parseLabelFile(String filePath, int[] label) {
		parse(filePath, label, Integer.MAX_VALUE);
	}
	
	/**
	 * Parses a file that contains datapoints for a single label.
	 * @return	A list of datapoints
	 */
	public void parse(String filePath, int[] label, int numExamples) {
		if (numExamples < 1)
			numExamples = Integer.MAX_VALUE;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			int example = 0;
			while ((line = reader.readLine()) != null && example < numExamples) {
				String[] elements = line.split(delim);
				int[] points = new int[elements.length];
				
				for (int i = 0; i < elements.length; i++) {
					points[i] = Integer.valueOf(elements[i]);
				}
				data.add(points, label);
				example++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parseFile(String filePath, String seperator) {
		this.seperator = seperator;
		parseFile(filePath);
		
	}
	
	public void parseFile(String filePath) {
		parse(filePath, null, Integer.MAX_VALUE);
	}

}
