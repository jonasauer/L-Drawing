package main.java.algorithm.successorPathTypeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.types.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.algorithm.holder.HolderProvider;

public class QTypeDetermination implements ITypeDetermination {

    public void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {

        HolderProvider.getSuccessorPathTypeHolder().setNodeType(tcTreeNode, SuccessorPathType.TYPE_M);
    }
}
