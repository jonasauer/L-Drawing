package main.java.algorithm.typeDeterminationUtils.typeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibeExceptionException;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;

public interface TypeDetermination {

    void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibeExceptionException;
}
