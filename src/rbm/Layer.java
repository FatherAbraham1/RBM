package rbm;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	
	public List<Neuron> neurons;
	
	public Layer(int size) {
		neurons = new ArrayList<>(size);
		for (int i = 0; i < size; i++)
			neurons.add(new NeuronDefault());
	}
	
	public Neuron get(int index) {
		return neurons.get(index);
	}
	
	public int size() {
		return neurons.size();
	}
	
	public void sample() {
		for (Neuron neuron : neurons)
			neuron.update();
	}

}
