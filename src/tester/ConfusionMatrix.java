package tester;

import java.util.List;

public class ConfusionMatrix {
	
	int[][] matrix;
	int size;
	String[] labels;
	int maxLabelSize = -1;
	int[] columnPadding;
	int correct = 0;
	int incorrect = 0;
	
	public ConfusionMatrix(List<String> labels) {
		setLabels(labels.toArray(new String[labels.size()]));
		initialize(labels.size());
	}
	
	public ConfusionMatrix(int size) {
		initialize(size);
	}
	
	private void initialize(int size) {
		this.size = size;
		matrix = new int[size][size];
		columnPadding = new int[size];
	}
	
	private void setLabels(String[] labels) {
		this.labels = labels;
		for (int i = 0; i < labels.length; i++)
			maxLabelSize = Math.max(maxLabelSize, labels[i].length());
	}
	
	public void add(int correctLabel, int producedLabel) {
		matrix[correctLabel][producedLabel]++;
		if (correctLabel == producedLabel)
			correct++;
		else
			incorrect++;
		columnPadding[producedLabel] = Math.max(columnPadding[producedLabel], String.valueOf(matrix[correctLabel][producedLabel]).length());
	}
	
	public double getAccuracy() {
		return (double)correct / (correct + incorrect);
	}
	
	// TODO: getPrecision
	// TODO: getRecall
	
	public String toString() {
		String str = "";
		int padding = size + 4;
		str += String.format("%"+(maxLabelSize+5)+"s", "");
		for (int i = 0; i < size; i++) {
			columnPadding[i] = Math.max(columnPadding[i]+3, 5+String.valueOf(i).length());
			String lbl = String.format("|%"+columnPadding[i]+"s", " ("+i+") ");
			str += lbl;
		}
		str += "\n";
		for (int i = 0; i < size; i++) {
			if (labels != null)
				str += String.format("%1$-"+maxLabelSize+"s ", labels[i]);
			str += "("+i+") ";
			for (int j = 0; j < size; j++) {
				str += String.format("|%"+(columnPadding[j]-1)+"d ", matrix[i][j]);
			}
			str += "\n";
		}
		return str;
	}
	
	public void clearLabels() {
		labels = null;
		maxLabelSize = -1;
	}

}
