package main.java.decomposition.graph.abs;

import main.java.decomposition.hyperGraph.IVertex;
import main.java.decomposition.hyperGraph.IDirectedHyperGraph;

/**
 * Directed graph interface
 * @author Artem Polyvyanyy
 *
 * @param <E> template for abs (extends IDirectedEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public interface IDirectedGraph<E extends IDirectedEdge<V>,V extends IVertex> extends IDirectedHyperGraph<E,V>, IGraph<E,V>
{
	/**
	 * Get directed abs that connects two vertices
	 * @param v1 Source vertex
	 * @param v2 Target vertex
	 * @return Edge that connects two vertices, <code>null</code> if no such abs exists
	 */
	public E getDirectedEdge(V v1, V v2);
}
