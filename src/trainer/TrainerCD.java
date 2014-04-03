package trainer;

import java.util.HashMap;
import java.util.Map;

import rbm.Connection;
import rbm.RBM;
import data.Data;
import data.Datapoint;

public class TrainerCD extends Trainer {
	
	// TODO: look into doing this with matrices and outer product instead of hashmap
	Map<Connection, Integer> positive;
	Map<Connection, Integer> negative;
	double learningRate = 0.5;
	
	public TrainerCD(RBM rbm, Data data) {
		super(rbm, data);
	}
	
	public void trainData(int epochs) {
		for (int epoch = 0; epoch < epochs; epoch++) {
			System.out.println("Epoch: "+epoch);
			for (int i = 0; i < data.size(); i++) {
				Datapoint datapoint = data.get(i);
				trainDataPoint(datapoint);
				if (img != null)
					img.showImage(rbm.read());
				//if (i > 0) break;
			}
		}
	}
	
	public void trainDataPoint(Datapoint datapoint) {
		rbm.setVisibleNodes(datapoint.vector());
		rbm.hidden.sample();
		positive = getGradient();
		rbm.visible.sample();
		rbm.hidden.sample();
		negative = getGradient();
		updateWeights();
	}
	
	private void updateWeights() {
		for (Connection connection : rbm.connections)
			connection.updateWeight(learningRate*(positive.get(connection) - negative.get(connection)));
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
