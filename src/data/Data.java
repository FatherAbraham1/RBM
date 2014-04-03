package data;

import java.util.ArrayList;
import java.util.List;

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
	
	public int size() {
		return datapoints.size();
	}

}
