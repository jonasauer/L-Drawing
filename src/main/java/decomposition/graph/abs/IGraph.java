package main.java.decomposition.graph.abs;

import main.java.decomposition.hyperGraph.IVertex;
import main.java.decomposition.hyperGraph.IHyperGraph;

import java.util.Collection;


/**
 * Graph interface
 * Graph consists of IEdge(s) and IVertex(ies)
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for abs (extends IEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public interface IGraph<E extends IEdge<V>,V extends IVertex> extends IHyperGraph<E,V>
{
	/**
	 * Check if two vertices are adjacent (connected by a single abs in a graph)
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return <code>true</code> if vertices are adjacent, <code>false</code> otherwise
	 */
	public boolean areAdjacent(V v1, V v2);
	
	/**
	 * Get abs that connects two vertices
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return Edge that connects two vertices, <code>null</code> if no such abs exists
	 */
	public E getEdge(V v1, V v2);
	
	/**
	 * Get collection of edges that connect two vertices
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return Collection of edges that connect two vertices
	 */
	public Collection<E> getEdges(V v1, V v2);
	
	/**
	 * Add abs to the graph
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return Edge added to the graph, <code>null</code> upon failure
	 */
	// This method is evil!
	//public E addEdge(V v1, V v2);
	
	/**
	 * Serialize graph to GraphViz DOT language
	 * http://www.graphviz.org/
	 * 
	 * @return DOT string
	 */
	public String toDOT();
}
