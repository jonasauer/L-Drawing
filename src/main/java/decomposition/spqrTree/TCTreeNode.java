package main.java.decomposition.spqrTree;

import main.java.decomposition.graph.abs.IEdge;
import main.java.decomposition.hyperGraph.IVertex;
import main.java.decomposition.hyperGraph.Vertex;

/**
 * Implementation of the node of the tree of the triconnected components.
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> Edge template.
 * @param <V> Vertex template.
 */
public class TCTreeNode<E extends IEdge<V>, V extends IVertex> extends Vertex {
	// node type
	protected TCTreeNodeType type = TCTreeNodeType.UNDEFINED;
	// skeleton
	protected TCSkeleton<E,V> skeleton = new TCSkeleton<E,V>();
	// boundary vertices of the fragment

	public TCTreeNodeType getType() {
		return this.type;
	}
	
	public TCSkeleton<E,V> getSkeleton() {
		return this.skeleton;
	}
	
	@Override
	public String toString() {
		return this.getName() + " - skeleton: " + this.skeleton + " virtual: " + this.skeleton.virtualEdges;
	}
}
