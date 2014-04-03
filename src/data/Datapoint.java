package data;

public class Datapoint {
	
	private int[] points;
	private int label;
	
	public Datapoint(int[] data, int label) {
		this.points = data;
		this.label = label;
	}
	
	public int label() {
		return label;
	}
	
	public int[] vector() {
		return points;
	}
	
	public int size() {
		return points.length;
	}
	
	public int get(int i) {
		return points[i];
	}

}
