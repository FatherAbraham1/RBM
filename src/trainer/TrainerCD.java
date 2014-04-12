package trainer;

import java.util.HashMap;
import java.util.Map;

import rbm.ClassRBM;
import rbm.Connection;
import rbm.RBM;
import data.Data;
import data.Datapoint;
import rbm.Error;

public class TrainerCD extends Trainer {
	
	Map<Connection, Integer> positive;
	Map<Connection, Integer> negative;
	double learningRate = 0.5;
	double errorThreshold = 0.1;
	int samplingSteps = 5;
	int convergenceNumber = 200;
	public boolean echo = false;
	
	public TrainerCD(RBM rbm, Data data) {
		super(rbm, data);
	}
	
	public int trainData(int epochs) {
		double lastError = 0;
		int sameCount = 0;
		int epoch;
		for (epoch = 0; epoch < epochs; epoch++) {
			double error = 0.0;
			for (int i = 0; i < data.numDatapoints(); i++) {
				Datapoint datapoint = data.get(i);
				int[] vector = datapoint.getVector();
				trainDataPoint(datapoint);
				updateWeights();
				error += Error.mse(vector, rbm.sample(vector));
				if (img != null)
					img.showImage(rbm.readVisible());
			}
			error /= data.numDatapoints();
			if (error >= lastError) {
				sameCount++;
			} else {
				lastError = error;
				sameCount = 0;
			}
			if (sameCount >= convergenceNumber || error == 0)
				break;
			if (echo)
				System.out.println("EPOCH "+epoch+" ERROR: "+error);
			//  .11 .12
		}
		return epoch;
	}
	
	public void trainDataPoint(Datapoint datapoint) {
		rbm.setVisibleNodes(datapoint.getVector());
		rbm.hidden.sample();
		positive = getGradient();
		rbm.visible.sample();
		rbm.hidden.sample();
		negative = getGradient();
	}
	
	public void updateWeights() {
		for (Connection connection : rbm.connections)
			connection.updateWeight(learningRate*(positive.get(connection) - negative.get(connection)));
	}
	
	public double[] getNextWeights() {
		double[] nextWeights = new double[rbm.connections.size()];
		for (int i = 0; i < rbm.connections.size(); i++) {
			Connection connection = rbm.connections.get(i);
			nextWeights[i] = connection.getWeight() + learningRate*(positive.get(connection) - negative.get(connection));
		}
		return nextWeights;
	}
	
	/**
	 * Counts the number of instances where connected units are both on and increments in map
	 */
	public Map<Connection, Integer> getGradient() {
		Map<Connection, Integer> map = new HashMap<>();
		for (Connection connection : rbm.connections) {
			if (connection.getNeuronA().on() && connection.getNeuronB().on()) {
				map.put(connection, 1);
			} else {
				map.put(connection, 0);
			}
		}
		return map;
	}

}
