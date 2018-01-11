package main.java.typeDetermination.typeDeterminationUtils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.typeDetermination.holder.HolderProvider;

import java.util.ArrayList;
import java.util.List;

public class PTypeDetermination implements ITypeDetermination{


    @Override
    public void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_P)) return;
        TCTreeNode<DirectedEdge, Vertex> optTypeBNode = null;
        SuccessorPathType successorPathType = SuccessorPathType.TYPE_M;

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){

            if(HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(child).equals(SuccessorPathType.TYPE_B)){
                if(optTypeBNode != null)
                    throw new RuntimeException("Type M is occurring twice in P-Node!");
                optTypeBNode = child;
                successorPathType = SuccessorPathType.TYPE_B;
            }
        }

        HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, successorPathType);

        if(successorPathType.equals(SuccessorPathType.TYPE_M)){
            //TODO: connect all last successors with the following first successors of the source of the current pert
            //TODO: graph.
        }else{
            //TODO: place the type B node on the right, connect all last successors with the following successors of the
            //TODO: source of the current pert graph and the type B node with the last type M node.
        }
    }
}
