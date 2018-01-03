package main.java.decomposition.graph;

import main.java.decomposition.graph.abs.AbstractMultiDirectedGraph;
import main.java.decomposition.graph.abs.AbstractDirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;

/**
 * Directed abs implementation
 * 
 * @author Artem Polyvyanyy
 */
public class DirectedEdge extends AbstractDirectedEdge<Vertex>
{
	protected DirectedEdge(AbstractMultiDirectedGraph<?, Vertex> g, Vertex source, Vertex target) {
		super(g, source, target);
	}
}
