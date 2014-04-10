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
		parseLabelFile(filePath, label, Integer.MAX_VALUE);
	}
	
	/**
	 * Parses a file that contains datapoints for a single label.
	 * @return	A list of datapoints
	 */
	public void parseLabelFile(String filePath, int[] label, int numExamples) {
		parse(filePath, label, numExamples);
	}
	
	private void parse(String filePath, int[] label, int numExamples) {
		if (numExamples < 1)
			numExamples = Integer.MAX_VALUE;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			int example = 0;
			while ((line = reader.readLine()) != null && example < numExamples) {
				String[] elements = line.split(delim);
				int[] points;
				
				if (label != null) {
					points = new int[elements.length];
					for (int i = 0; i < points.length; i++) {
						points[i] = Integer.valueOf(elements[i]);
					}
					data.add(points, label);
				} else {
					int sepIndex = 0;
					for (int i = 0; i < elements.length; i++) {
						if (elements[i].equals(seperator)) {
							sepIndex = i;
							break;
						}
					}
					points = new int[sepIndex];
					int[] lbl = new int[elements.length - sepIndex - 1];
					// verify the seperator is in the correct location
					if (!elements[points.length].equals(seperator)) {
						System.out.println("ERROR: invalid data format");
						System.exit(1);
					}
					for (int i = 0; i < points.length; i++) {
						points[i] = Integer.valueOf(elements[i]);
					}
					for (int i = 0; i < lbl.length; i++) {
						lbl[i] = Integer.valueOf(elements[points.length+1+i]);
					}
					data.add(points, lbl);
				}
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
