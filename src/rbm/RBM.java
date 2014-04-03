package rbm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RBM {
	
	public Layer visible, hidden;
	public List<Connection> connections;
	Random random = new Random(11235);
	
	public RBM(int visibleSize, int hiddenSize) {
		
		visible = new Layer(visibleSize);
		hidden = new Layer(hiddenSize);
		connections = new ArrayList<>(visibleSize * hiddenSize);
		
	}
	
	public void setVisibleNodes(int[] data) {
		if (data.length != visible.size()) {
			System.out.println("ERROR: Datapoint size does not match RBM visible layer.");
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
	
	public void connect(int visibleIndex, int hiddenIndex) {
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
	
	public void connectFully() {
		for (int i = 0; i < visible.size(); i++) {
			for (int j = 0; j < hidden.size(); j++) {
				connect(i, j);
			}
		}
		initializeWeights();
	}
	
	public void initializeWeights() {
		for (Connection connection : connections)
			connection.setWeight(random.nextGaussian() * 0.01);
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
	
	public int[] read() {
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

}
