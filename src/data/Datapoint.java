package data;

public class Datapoint {
	
	private int[] points;
	private int[] label;
	
	public Datapoint(int[] data, int[] label) {
		this.points = data;
		this.label = label;
	}
	
	public int[] getLabel() {
		return label;
	}
	
	public int getSingleLabel() {
		int lbl = -Integer.MAX_VALUE;
		for (int l : label)
			lbl = Math.max(lbl, l);
		return lbl;
	}
	
	public int[] getFeatures() {
		return points;
	}
	
	public int[] getVector() {
		int[] vector = new int[label.length + points.length];
		for (int i = 0; i < points.length; i++)
			vector[i] = points[i];
		for (int i = 0; i < label.length; i++)
			vector[points.length+i] = label[i];
		return vector;
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
		if (label != null) {
			str += " : [";
			for (int i = 0; i < label.length; i++) {
				if (i > 0)
					str += ",";
				str += label[i];
			}
			str += "]";
		}
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
