package tester;

import rbm.ClassRBM;
import rbm.RBM;
import tools.Tools;
import data.Data;
import data.Datapoint;

public class Tester {
	
	RBM rbm;
	ConfusionMatrix confusionMatrix;
	double accuracy = 0.0;

	public Tester(RBM rbm) {
		this.rbm = rbm;
	}
	
	public ConfusionMatrix test(Data data) {
		ClassRBM crbm = new ClassRBM(rbm, data);
		confusionMatrix = new ConfusionMatrix(data.getLabels());
		for (int i = 0; i < data.numDatapoints(); i++) {
			Datapoint datapoint = data.get(i);
			int correctLabel = datapoint.getSingleLabel();
			int producedLabel = crbm.classify(datapoint);
			confusionMatrix.add(correctLabel, producedLabel);
		}
		return confusionMatrix;
	}
	
	public double testMulti(Data data) {
		ClassRBM crbm = new ClassRBM(rbm, data);
		int correct = 0;
		for (int i = 0; i < data.numDatapoints(); i++) {
			Datapoint datapoint = data.get(i);
			int[] correctLabel = datapoint.getLabel();
			int[] producedLabel = crbm.classifyMulti(datapoint);
			for (int j = 0; j < correctLabel.length; j++)
				if (correctLabel[j] == producedLabel[j])
					correct++;
		}
		double accuracy = (double) correct / (data.numDatapoints() * data.numLabels());
		return accuracy;
	}
	
	public double testGenerative(Data data) {
		int correct = 0;
		for (int i = 0; i < data.numDatapoints(); i++) {
			Datapoint datapoint = data.get(i);
			int[] result = rbm.sample(datapoint.getVector());
			for (int j = 0; j < result.length; j++)
				if (result[j] == datapoint.getVector()[j])
					correct++;
		}
		accuracy = (double) correct / (data.numDatapoints() * data.get(0).getVector().length);
		return accuracy;
	}
	
}
