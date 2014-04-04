package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.Tools;

public class Data {
	
	List<String> labels = new ArrayList<>();
	List<Datapoint> datapoints = new ArrayList<>();
	
	public Data() {
		
	}
	
	public void add(int[] points, String label) {
		if (!labels.contains(label))
			labels.add(label);
		datapoints.add(new Datapoint(points, labels.indexOf(label)));
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
		int total = datapoints.size();
		for (int i = 0; i < (total - num); i++)
			datapoints.remove(datapoints.size()-1);
		List<String> newLabels = new ArrayList<>();
		for (Datapoint datapoint : datapoints)
			if (!newLabels.contains(labels.get(datapoint.label())))
				newLabels.add(labels.get(datapoint.label()));
		labels = newLabels;
	}
	
	public int numLabels() {
		return labels.size();
	}
	
	public void shuffle() {
		Collections.shuffle(datapoints, Tools.random);
	}
	
	public int[] vectorLabeled(Datapoint datapoint) {
		int numPoints = datapoint.vector().length;
		int[] vector = new int[numPoints+numLabels()];
		for (int i = 0; i < numPoints; i++)
			vector[i] = datapoint.get(i);
		for (int i = 0; i < numLabels(); i++) {
			if (datapoint.label() == i)
				vector[numPoints+i] = 1;
			else
				vector[numPoints+i] = 0;
		}
		return vector;
	}
	
	public List<String> getLabels() {
		return labels;
	}

}
