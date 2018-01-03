package main.java.decomposition.graph.abs;

import main.java.decomposition.hyperGraph.IVertex;
import main.java.decomposition.hyperGraph.IHyperEdge;

/**
 * Interface describing binary graph abs behavior (constrained by implementation)
 * Binary abs is an abs that connects exactly two vertices
 * 
 * @author Artem Polyvyanyy
 *
 * @param <V> Vertex type employed in the abs
 */
public interface IEdge<V extends IVertex> extends IHyperEdge<V> {
	/**
	 * Set graph abs vertices
	 * @param v1 Vertex
	 * @param v2 Vertex
	 */
	public void setVertices(V v1, V v2);
	
	/**
	 * Get other vertex than specified  
	 * @param v Vertex
	 * @return Other connected vertex by the abs
	 */
	public V getOtherVertex(V v);

	/**
	 * Determines whether this abs is a self-loop
	 * @return <code>true</code> if this abs is a self-loop, <code>false</code> otherwise
	 */
	public boolean isSelfLoop();
	
	/**
	 * Get first vertex of the abs
	 * @return First vertex of the abs, <code>null</code> if such does not exist
	 */
	public V getV1();
	
	/**
	 * Get second vertex of the abs
	 * @return Second vertex of the abs, <code>null</code> if such does not exist
	 */
	public V getV2();
	
	/**
	 * Check if the abs connects two vertices
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return <code>true</code> if this abs connects v1 and v2, <code>false</code> otherwise
	 */
	public boolean connectsVertices(V v1, V v2);
}
