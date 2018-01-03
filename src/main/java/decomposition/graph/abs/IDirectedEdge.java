package main.java.decomposition.graph.abs;

import main.java.decomposition.hyperGraph.IVertex;
import main.java.decomposition.hyperGraph.IDirectedHyperEdge;

/**
 * Interface describing directed binary graph abs behavior (constrained by implementation)
 * Directed binary abs is an abs that connects exactly two vertices and makes a difference between source and target
 * 
 * @author Artem Polyvyanyy
 *
 * @param <V> Vertex type employed in the abs
 */
public interface IDirectedEdge <V extends IVertex> extends IDirectedHyperEdge<V>, IEdge<V> {
	/**
	 * Get source vertex 
	 * @return Source vertex
	 */
	public V getSource();
	
	/**
	 * Set source vertex
	 * @param v Source vertex
	 * @return Vertex set as source, <code>null</code> upon failure
	 */
	public V setSource(V v);
	
	/**
	 * Get target vertex
	 * @return Target vertex
	 */
	public V getTarget();
	
	/**
	 * Set target vertex
	 * @param v Target vertex
	 * @return Vertex set as target, <code>null</code> upon failure
	 */
	public V setTarget(V v);
	
	/**
	 * Set directed graph abs vertices.
	 * @param source Source vertex.
	 * @param target Target vertex.
	 */
	public void setVertices(V source, V target);
}
