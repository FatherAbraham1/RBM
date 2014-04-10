package visualizer;

import java.awt.Color;

import rbm.Neuron;

public class Node {
	
	public enum TYPE { VISIBLE, HIDDEN, BIAS };
	
	Neuron neuron;
	Color color = Color.red;
	TYPE type = TYPE.VISIBLE;
	
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
