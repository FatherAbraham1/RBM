package rbm;

import data.Data;
import data.Datapoint;

public class ClassRBM {
	
	RBM rbm;
	Data data;
	double highestLabelProbability;
	
	public ClassRBM(RBM rbm, Data data) {
		this.rbm = rbm;
		this.data = data;
	}
	
	public int classify(Datapoint datapoint) {
		int[] points = new int[data.datapointSize()+data.numLabels()];
		for (int i = 0; i < datapoint.size(); i++)
			points[i] = datapoint.get(i);
		double[] probabilities = rbm.sampleProbabilities(points);
		int label = 0;
		highestLabelProbability = 0;
		for (int i = 0; i < data.numLabels(); i++) {
			if (probabilities[datapoint.size()+i] > highestLabelProbability) {
				highestLabelProbability = probabilities[datapoint.size()+i];
				label = i;
			}
			//System.out.print(probabilities[datapoint.size()+i]+" ");
		}
		//System.out.println();
		return label;
	}
	
	public int classifyMulti(Datapoint datapoint) {
		int[] points = new int[data.datapointSize()+data.numLabels()];
		for (int i = 0; i < datapoint.size(); i++)
			points[i] = datapoint.get(i);
		double[] probabilities = rbm.sampleProbabilities(points);
		int label = 0;
		highestLabelProbability = 0;
		for (int i = 0; i < data.numLabels(); i++) {
			if (probabilities[datapoint.size()+i] > highestLabelProbability) {
				highestLabelProbability = probabilities[datapoint.size()+i];
				label = i;
			}
			//System.out.print(probabilities[datapoint.size()+i]+" ");
		}
		//System.out.println();
		return label;
	}
	
	public double confidence(Datapoint datapoint) {
		classify(datapoint);
		// this looks like an error
		if (highestLabelProbability==0.5) {
			int[] points = new int[data.datapointSize()+data.numLabels()];
			for (int i = 0; i < datapoint.size(); i++)
				points[i] = datapoint.get(i);
			double[] probabilities = rbm.sampleProbabilities(points);
			if (probabilities[0]==probabilities[1] && probabilities[1]==probabilities[2]) {
				// this should be impossible
				return 0.0;
			}
		}
		return highestLabelProbability;
	}

}
