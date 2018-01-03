package main.java.decomposition.graph;

import main.java.decomposition.graph.abs.AbstractMultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

/**
 * Directed multi graph implementation
 *
 * @author Artem Polyvyanyy
 */
public class MultiDirectedGraph extends AbstractMultiDirectedGraph<DirectedEdge,Vertex>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public DirectedEdge addEdge(Vertex s, Vertex t) {
		DirectedEdge e = new DirectedEdge(this,s,t);
		return e;
	}
}
