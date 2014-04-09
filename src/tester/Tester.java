package tester;

import rbm.ClassRBM;
import rbm.RBM;
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
			int correctLabel = datapoint.getLabel();
			int producedLabel = crbm.classify(datapoint);
			confusionMatrix.add(correctLabel, producedLabel);
		}
		return confusionMatrix;
	}
	
}
