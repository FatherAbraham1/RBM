package rbm;

import java.util.HashMap;
import java.util.Map;

public class Trainer {
	
	RBM rbm;
	double learningRate = 0.5;
	Map<Connection, Integer> positive;
	Map<Connection, Integer> negative;
	
	public Trainer(RBM rbm) {
		this.rbm = rbm;
	}
	
	public void trainingIteration() {
		// reset positive and negative counting maps
		positive = new HashMap<>();
		negative = new HashMap<>();
		// single Gibbs sampling step
		updateHiddenUnits();
		countMatches(positive);
		updateVisibleUnits();
		updateHiddenUnits();
		countMatches(negative);
	}
	
	private void updateWeights() {
		for (Connection connection : rbm.connections)
			connection.updateWeight(learningRate*(positive.get(connection) - negative.get(connection)));
	}
	
	/**
	 * Update all units in the hidden layer.
	 */
	public void updateHiddenUnits() {
		for (Neuron neuron : rbm.hidden.neurons)
			neuron.update();
	}
	
	/**
	 * Update all units in the visible layer.
	 */
	public void updateVisibleUnits() {
		for (Neuron neuron : rbm.hidden.neurons)
			neuron.update();
	}
	
	/**
	 * Counts the number of instances where connected units are both on and increments in map
	 */
	public void countMatches(Map<Connection, Integer> map) {
		for (Connection connection : rbm.connections) {
			if (connection.getNeuronA().on() && connection.getNeuronB().on()) {
				int val = 1;
				if (map.containsKey(connection))
					val += map.get(connection);
				map.put(connection, val);
			}
		}
	}

}
