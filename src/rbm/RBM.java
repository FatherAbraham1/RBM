package rbm;

import java.util.ArrayList;
import java.util.List;

import tools.Tools;

public class RBM {
	
	public Layer visible, hidden;
	public List<Connection> connections;
	public static Neuron bias = new NeuronBias();
	
	// lazy evaluation (sets visible size when first datapoint comes in)
	public RBM(int hiddenSize) {
		hidden = new Layer(hiddenSize);
	}
	
	public RBM(int visibleSize, int hiddenSize) {
		visible = new Layer(visibleSize);
		hidden = new Layer(hiddenSize);
		connections = new ArrayList<>(visibleSize * hiddenSize);
	}
	
	public void setVisibleNodes(int[] data) {
		if (visible == null) {
			visible = new Layer(data.length);
			connections = new ArrayList<>(data.length * hidden.size());
			connectFully();
		}
		if (data.length != visible.size()) {
			System.out.println("ERROR: Datapoint size ("+data.length+") does not match RBM visible layer ("+visible.size()+").");
			System.exit(1);
		}
		for (int i = 0; i < data.length; i++) {
			if (data[i] == 1 || data[i] == 0) {
				visible.get(i).state = data[i];
			} else {
				System.out.println("ERROR: Network currently supports binary inputs only.");
				System.exit(1);
			}
		}
	}
	
	public Layer getVisible() {
		return visible;
	}
	
	public Layer getHidden() {
		return hidden;
	}
	
	private void connect(int visibleIndex, int hiddenIndex) {
		if (getConnection(visibleIndex, hiddenIndex) == null) {
			connections.add(new Connection(visible.get(visibleIndex), hidden.get(hiddenIndex)));
		}
	}
	
	public Connection getConnection(int visibleIndex, int hiddenIndex) {
		Neuron a = visible.get(visibleIndex);
		if (a != null)
			return a.getConnection(hidden.get(hiddenIndex));
		else
			return null;
	}
	
	private void connectFully() {
		// connect layers in bipartite fashion
		for (int i = 0; i < visible.size(); i++)
			for (int j = 0; j < hidden.size(); j++)
				connect(i, j);
		// connect all nodes in visible layer to bias unit
		for (int i = 0; i < visible.size(); i++)
			if (visible.get(i).getConnection(bias) == null)
				connections.add(new Connection(visible.get(i), bias));
		// connect all nodes in hidden layer to bias unit
		for (int i = 0; i < hidden.size(); i++)
			if (hidden.get(i).getConnection(bias) == null)
				connections.add(new Connection(hidden.get(i), bias));
		initializeWeights();
	}
	
	public void initializeWeights() {
		for (Connection connection : connections)
			connection.setWeight(Tools.random.nextGaussian() * 0.01);
	}
	
	public void setWeights(double[] weights) {
		if (weights.length != connections.size()) {
			System.out.println("ERROR: Weight vector size does not match number of connections");
			System.exit(1);
		}
		int i = 0;
		for (Connection connection : connections) {
			connection.setWeight(weights[i]);
			i++;
		}
	}
	
	public String toString() {
		return "["+visible.size()+","+hidden.size()+"]";
	}
	
	public int[] readVisible() {
		int[] result = new int[visible.size()];
		int i = 0;
		for (Neuron neuron : visible.neurons) {
			if (neuron.on())
				result[i] = 1;
			else
				result[i] = 0;
			i++;
		}
		return result;
	}
	
	public double[] readVisibleProbabilities() {
		double[] probabilities = new double[visible.size()];
		int i = 0;
		for (Neuron neuron : visible.neurons) {
			probabilities[i] = neuron.getProbability();
			i++;
		}
		return probabilities;
	}
	
	// sets visible nodes, samples back and for "steps" times, and returns visible nodes
	public int[] sample(int[] initial, int steps) {
		setVisibleNodes(initial);
		for (int i = 0; i < steps; i++) {
			hidden.sample();
			visible.sample();
		}
		return readVisible();
	}
	
	public int[] sample(int[] initial) {
		return sample(initial, 1);
	}
	
	public double[] sampleProbabilities(int[] initial, int steps) {
		setVisibleNodes(initial);
		for (int i = 0; i < steps; i++) {
			hidden.sample();
			visible.sample();
		}
		return readVisibleProbabilities();
	}
	
	public double[] sampleProbabilities(int[] initial) {
		return sampleProbabilities(initial, 1);
	}
	
	public int[] daydream(int steps) {
		int[] vector = new int[visible.size()];
		for (int i = 0; i < visible.size(); i++) {
			vector[i] = Tools.random.nextInt(1);
		}
		return sample(vector, steps);
	}
	
	public int[] daydream() {
		return daydream(1);
	}

}
