package main.java.typeDetermination.typeDeterminationUtils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.tcTree.TCTreeNode;
import main.java.decomposition.tcTree.TCTreeNodeType;
import main.java.typeDetermination.holder.HolderProvider;

public class QTypeDetermination{

    public static void determineType(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_Q)) return;
        HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, SuccessorPathType.TYPE_M);
    }
}