package main.java.algorithm.successorPathTypeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.types.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.algorithm.holder.HolderProvider;

public class QTypeDetermination implements ITypeDetermination {

    public void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {

        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_Q)) return;
        HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, SuccessorPathType.TYPE_M);
    }
}
