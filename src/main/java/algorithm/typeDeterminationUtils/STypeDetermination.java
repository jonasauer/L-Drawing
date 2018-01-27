package main.java.algorithm.typeDeterminationUtils;

import main.java.PrintColors;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.algorithm.holder.HolderProvider;

public class STypeDetermination{

    public void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_S)) return;

        System.out.println(PrintColors.ANSI_RED + "---------------------------");
        System.out.println(PrintColors.ANSI_RED + "SType Determination! Source Vertex is " + HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNodes().get(tcTreeNode));
        System.out.println(PrintColors.ANSI_RED + "    Skeleton: " + tcTreeNode.getSkeleton());

        Vertex source = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){

            Vertex childSource = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNodes().get(child);
            if(source.equals(childSource)){
                SuccessorPathType type = HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(child);
                HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, type);
                System.out.println(PrintColors.ANSI_GREEN + "    SucessorPathType: " + type);
                return;
            }
        }
    }
}
