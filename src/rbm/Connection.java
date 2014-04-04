package rbm;

public class Connection {
	
	private Neuron a, b;
	private double weight;
	
	public Connection(Neuron a, Neuron b) {
		a.addConnection(this, b);
		b.addConnection(this, a);
		this.a = a;
		this.b = b;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double newWeight) {
		this.weight = newWeight;
	}
	
	public void updateWeight(double weightChange) {
		this.weight += weightChange;
	}
	
	public Neuron getConnected(Neuron neuron) {
		if (neuron == a) {
			return b;
		} else if (neuron == b) {
			return a;
		} else {
			return null;
		}
	}
	
	public Neuron getNeuronA() {
		return a;
	}
	
	public Neuron getNeuronB() {
		return b;
	}
	
	public String toString() {
		return "<-"+weight+"->";
	}

}
