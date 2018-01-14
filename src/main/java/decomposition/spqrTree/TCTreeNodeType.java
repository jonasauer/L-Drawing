package main.java.decomposition.spqrTree;

/**
 * Enumeration of structural types of the triconnected components.<br/><br/>
 * 
 * Note that every abs of a graph is a trivial component, but is not explicitly computed by {@link TCTree}.<br/><br/>
 * 
 * TYPE_Q - single abs.<br/>
 * TYPE_S - sequence of triconnected components.<br/>
 * TYPE_P - set of triconnected components that share a split pair.<br/>
 * TYPE_R - neither a trivial, nor polygon, nor bond component.<br/>
 * 
 * @author Artem Polyvyanyy
 */
public enum TCTreeNodeType {
	TYPE_Q,
	TYPE_S,
	TYPE_P,
	TYPE_R,
	UNDEFINED
}
