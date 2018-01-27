package main.java.algorithm.typeDeterminationUtils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.algorithm.holder.HolderProvider;

public class QTypeDetermination{

    public void determineType(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_Q)) return;
        HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, SuccessorPathType.TYPE_M);
    }
}
