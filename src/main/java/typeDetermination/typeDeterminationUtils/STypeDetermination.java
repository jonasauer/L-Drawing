package main.java.typeDetermination.typeDeterminationUtils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.typeDetermination.holder.HolderProvider;

public class STypeDetermination{

    public static void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_S)) return;

        Vertex source = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){

            Vertex childSource = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(child);
            if(source.equals(childSource)){
                SuccessorPathType type = HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(child);
                HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, type);
                return;
            }
        }
    }
}
