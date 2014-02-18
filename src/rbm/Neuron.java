package rbm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neuron {
	
	private Map<Neuron, Connection> connections = new HashMap<>();
	
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

}
