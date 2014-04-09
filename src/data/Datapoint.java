package data;

public class Datapoint {
	
	private int[] points;
	private int label;
	
	public Datapoint(int[] data, int label) {
		this.points = data;
		this.label = label;
	}
	
	public int getLabel() {
		return label;
	}
	
	public int[] getFeatures() {
		return points;
	}
	
	public int size() {
		return points.length;
	}
	
	public int get(int i) {
		return points[i];
	}
	
	public String toString() {
		String str = "[";
		for (int i = 0; i < points.length; i++) {
			if (i > 0)
				str += ",";
			str += points[i];
		}
		str += "]";
		return str;
	}
	
	public void convertToBinary() {
		int max = -Integer.MAX_VALUE;
		for (int i = 0; i < points.length; i++) {
			max = Math.max(max, points[i]);
		}
		int threshold = max/2;
		for (int i = 0; i < points.length; i++) {
			if (points[i] > threshold)
				points[i] = 1;
			else
				points[i] = 0;
		}
	}

}
