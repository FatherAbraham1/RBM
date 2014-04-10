package visualizer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import rbm.Connection;
import rbm.Layer;
import rbm.Neuron;
import rbm.RBM;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Visualizer {
	
	Graph<Node, Connection> graph;
	Map<Neuron, Node> nodes = new HashMap<>();
	int width = 1500;
	int height = 800;
	double minWeight = Double.MAX_VALUE;
	double maxWeight = Double.MIN_VALUE;
	Layer visible, hidden;
	boolean showBias = true;
	boolean showOn = true;
	
	// create a structure with "Node" objects as vertices and "Connection" objects as edges
	Layout<Node, Connection> layout;
 	BasicVisualizationServer<Node, Connection> vis;
 	
 	// these transformers are optional, only use if you want something besides default
 	private Transformer<Connection, Paint> edgePaint = new Transformer<Connection, Paint>() {
 	    public Paint transform(Connection c) {
 	    	float hue = 0.0f;
 	    	float saturation = (float) ((c.getWeight() - minWeight) / (maxWeight - minWeight));
 	    	float brightness = 0.8f;
 	        return Color.getHSBColor(hue, saturation, brightness);
 	    }
 	};
 	private Transformer<Connection, Stroke> edgeStroke = new Transformer<Connection, Stroke>() {
 	    public Stroke transform(Connection c) {
 	    	float weight = (float) (((c.getWeight() - minWeight) / (maxWeight - minWeight)) + 0.5);
 	    	return new BasicStroke(weight);
 	    }
 	};
 	private Transformer<Node,Paint> vertexPaint = new Transformer<Node,Paint>() {
		public Paint transform(Node node) {
			return node.getColor();
		}
	};
	
	// create a graph out of the rbm structure
	public Visualizer(RBM rbm) {
		graph = new SparseMultigraph<Node, Connection>();
		visible = rbm.getVisible();
		hidden = rbm.getHidden();
		// add all visible nodes as vertices
		for (int i = 0; i < visible.size(); i++) {
			Node node = new Node(visible.get(i));
			if (showOn && !visible.get(i).on())
				node.setColor(Color.BLACK);
			nodes.put(visible.get(i), node);
			graph.addVertex(node);
		}
		// add all hidden nodes as vertices and connections as edges
		for (int i = 0; i < hidden.size(); i++) {
			Node node = new Node(hidden.get(i));
			nodes.put(hidden.get(i), node);
			node.type = node.type.HIDDEN;
			node.setColor(Color.green);
			if (showOn && !hidden.get(i).on())
				node.setColor(Color.BLACK);
			graph.addVertex(node);
			List<Connection> connections = hidden.get(i).getConnections();
			for (Connection connection : connections) {
				if (nodes.get(connection.getNeuronA()) != null && nodes.get(connection.getNeuronB()) != null) {
					graph.addEdge(connection, nodes.get(connection.getNeuronA()), nodes.get(connection.getNeuronB()));
					double weight = connection.getWeight();
					if (weight < minWeight)
						minWeight = weight;
					if (weight > maxWeight)
						maxWeight = weight;
				}
			}
		}
		if (showBias) {
			// add bias node as vertex
			Node bias = new Node(rbm.bias);
			bias.type = bias.type.BIAS;
			bias.setColor(Color.BLUE);
			nodes.put(rbm.bias, bias);
			graph.addVertex(bias);
			// connect bias
			List<Connection> connections = rbm.bias.getConnections();
			for (Connection connection : connections) {
				graph.addEdge(connection, nodes.get(connection.getNeuronA()), nodes.get(connection.getNeuronB()));
				double weight = connection.getWeight();
				if (weight < minWeight)
					minWeight = weight;
				if (weight > maxWeight)
					maxWeight = weight;
			}
		}
		setGraph(graph);
	}
	
	private void setGraph(Graph<Node, Connection> graph) {
		
		// use a static layout to manually place nodes
		layout = new StaticLayout<Node, Connection>(graph);
		layout.setSize(new Dimension(width,height));
		vis = new BasicVisualizationServer<Node,Connection>(layout);
	 	vis.setPreferredSize(new Dimension(width,height));
	 	
	 	// this code is to manually place nodes (alternatively let jung handle this)
	 	double visibleStep = height / visible.size();
	 	double visiblePosition = visibleStep / 2;
	 	double hiddenStep = height / hidden.size();
	 	double hiddenPosition = hiddenStep / 2;
	 	for (Object n : graph.getVertices()) {
	 		Node node = (Node) n;
	 		if (node.type == node.type.BIAS) {
	 			layout.setLocation(node, new Point2D.Double(width/2, 20));
	 		} else if (node.type == node.type.HIDDEN) {
	 			layout.setLocation(node, new Point2D.Double(4*width/5, hiddenPosition));
	 			hiddenPosition += hiddenStep;
	 		} else if (node.type == node.type.VISIBLE) {
	 			layout.setLocation(node, new Point2D.Double(width/5, visiblePosition));
	 			visiblePosition += visibleStep;
	 		}
		}
	 	
	 	// apply transformers from above to change default colors and weights
	 	vis.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
	 	vis.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
	 	vis.getRenderContext().setEdgeStrokeTransformer(edgeStroke);
	}
	
	// draw the graph to a jframe
	public void showStructure() {
		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vis); 
		frame.pack();
		frame.setVisible(true); 
	}

}
