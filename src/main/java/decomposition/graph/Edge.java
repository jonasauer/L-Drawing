package main.java.decomposition.graph;

import main.java.decomposition.graph.abs.AbstractMultiGraph;
import main.java.decomposition.graph.abs.AbstractEdge;
import main.java.decomposition.hyperGraph.Vertex;

/**
 * Graph abs implementation
 * 
 * @author Artem Polyvyanyy
 */
public class Edge extends AbstractEdge<Vertex>
{
	protected Edge(AbstractMultiGraph<?, Vertex> g, Vertex v1, Vertex v2) {
		super(g, v1, v2);
	}	
}
