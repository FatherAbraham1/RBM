package rbm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.Tools;

public abstract class Neuron {
	
	protected Map<Neuron, Connection> connections = new HashMap<>();
	protected double state;
	private double energy;
	
	public Neuron() {
		
	}
	
	/**
	 * Adds a connection that involves the current Neuron.
	 * NOTE: This method should only be used by the Connection class itself.
	 * @param connection	The connection object linking the current Neuron to another.
	 */
	protected void addConnection(Connection connection, Neuron neuron) {
		connections.put(neuron, connection);
	}
	
	
	public Connection getConnection(Neuron neuron) {
		return connections.get(neuron);
	}
	
	public List<Connection> getConnections() {
		return new ArrayList<>(connections.values());
	}
	
	private double activationEnergy() {
		double energy = 0.0;
		for (Map.Entry<Neuron, Connection > conn : connections.entrySet()) {
			energy += conn.getKey().state() * conn.getValue().getWeight();
		}
		this.energy = energy;
		return energy;
	}
	
	private double logistic(double energy) {
		return 1.0 / (1 + Math.exp(-1 * energy));
	}
	
	private void stochasticSwitch(double probability) {
		if (Tools.random.nextDouble() < probability)
			state = 1.0;
		else
			state = 0.0;
	}
	
	public void update() {
		double energy = activationEnergy();
		double probability = logistic(energy);
		stochasticSwitch(probability);
	}
	
	public double state() {
		return state;
	}
	
	public boolean on() {
		return (state != 0.0);
	}
	
	public double getProbability() {
		return logistic(activationEnergy());
	}

}
