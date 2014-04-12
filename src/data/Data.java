package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.Tools;

public class Data {
	
	List<String> labels = new ArrayList<>();
	List<Datapoint> datapoints = new ArrayList<>();
	
	public Data() {
		
	}
	
	public void add(int[] points, int[] label) {
		String lbl = null;
		for (int l : label)
			lbl += l+" ";
		if (!labels.contains(lbl))
			labels.add(lbl);
		datapoints.add(new Datapoint(points, label));
	}
	
	public Datapoint get(int i) {
		return datapoints.get(i);
	}
	
	public int datapointSize() {
		return datapoints.get(0).size();
	}
	
	public int datapointSizeLabeled() {
		return datapoints.get(0).size() + labels.size();
	}
	
	public int numDatapoints() {
		return datapoints.size();
	}
	
	public void truncate(int num) {
		truncate(0, num);
	}
	
	public void truncate(int start, int num) {
		int total = datapoints.size();
		for (int i = start; i < (total - num) + start; i++)
			datapoints.remove(datapoints.size()-1);
	}
	
	public int numLabels() {
		return datapoints.get(0).getLabel().length;
	}
	
	public void shuffle() {
		Collections.shuffle(datapoints, Tools.random);
	}
	
	public List<String> getLabels() {
		return labels;
	}
	
	public String getLabel(int index) {
		return labels.get(index);
	}
	
	public void convertToBinary() {
		for (Datapoint datapoint : datapoints) {
			datapoint.convertToBinary();
		}
	}
	
	public void writeToFile(String filename) {
		
		try {
 
			File file = new File(filename);
			FileOutputStream fop = new FileOutputStream(file);
			if (!file.exists())
				file.createNewFile();
 
			String content = "";
			for (Datapoint datapoint : datapoints) {
				int[] features = datapoint.getFeatures();
				int[] labels = datapoint.getLabel();
				for (int feature : features)
					content += feature+" ";
				content+=": ";
				for (int label : labels)
					content += label+" ";
				content = content.substring(0,content.length() - 1);
				content += "\n";
			}
			
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Data written to "+filename);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		String str = "";
		for (Datapoint datapoint : datapoints) {
			str += datapoint.toString()+"\n";
		}
		return str;
	}

}
