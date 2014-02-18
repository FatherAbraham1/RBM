package rbm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RBM {
	
	Layer visible, hidden;
	List<Connection> connections;
	Random random = new Random(11235);
	
	public RBM(int visibleSize, int hiddenSize) {
		
		visible = new Layer(visibleSize);
		hidden = new Layer(hiddenSize);
		connections = new ArrayList<>(visibleSize * hiddenSize);
		
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
		for (Connection connection : connections)
			System.out.println(connection.getWeight());
	}
	
	public String toString() {
		return "["+visible.size()+","+hidden.size()+"]";
	}

}
