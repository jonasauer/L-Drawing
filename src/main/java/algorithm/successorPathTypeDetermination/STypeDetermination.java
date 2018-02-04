package main.java.algorithm.successorPathTypeDetermination;

import main.java.printer.PrintColors;
import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.types.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.algorithm.holder.HolderProvider;

public class STypeDetermination implements ITypeDetermination {

    public void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {

        System.out.println(PrintColors.ANSI_RED + "---------------------------");
        System.out.println(PrintColors.ANSI_RED + "SType Determination! Source Vertex is " + HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(tcTreeNode));
        System.out.println(PrintColors.ANSI_RED + "    Skeleton: " + tcTreeNode.getSkeleton());

        Vertex source = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(tcTreeNode);

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){

            Vertex childSource = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(child);
            if(source.equals(childSource)){
                SuccessorPathType type = HolderProvider.getSuccessorPathTypeHolder().getNodeType(child);
                HolderProvider.getSuccessorPathTypeHolder().setNodeType(tcTreeNode, type);
                System.out.println(PrintColors.ANSI_GREEN + "    SucessorPathType: " + type);
                return;
            }
        }
    }
}
