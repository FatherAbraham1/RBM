package visualizer;

import java.awt.Color;

import rbm.Neuron;

public class Node {
	
	Neuron neuron;
	Color color = Color.red;
	boolean hidden = false;
	
	public Node(Neuron neuron) {
		this.neuron = neuron;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

}
