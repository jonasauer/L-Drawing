package main.java.algorithm.successorPathTypeDetermination;

import main.java.printer.PrintColors;
import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.types.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.algorithm.holder.HolderProvider;

import java.util.*;

public class PTypeDetermination implements ITypeDetermination {

    public  void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {

        System.out.println(PrintColors.ANSI_RED + "---------------------------");
        System.out.println(PrintColors.ANSI_RED + "PType Determination! Source Vertex is " + HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(tcTreeNode));
        System.out.println(PrintColors.ANSI_RED + "    Skeleton: " + tcTreeNode.getSkeleton());


        TCTreeNode<DirectedEdge, Vertex> optTypeBNode = null;
        SuccessorPathType successorPathType = SuccessorPathType.TYPE_M;

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
            if(HolderProvider.getSuccessorPathTypeHolder().getNodeType(child).equals(SuccessorPathType.TYPE_B)){
                if(optTypeBNode != null)
                    throw new LDrawingNotPossibleException("P-Node contains two children assigned with Type-B.");
                optTypeBNode = child;
                successorPathType = SuccessorPathType.TYPE_B;
            }
        }

        HolderProvider.getSuccessorPathTypeHolder().setNodeType(tcTreeNode, successorPathType);
        System.out.println(PrintColors.ANSI_GREEN + "    SucessorPathType: " + successorPathType);
        MultiDirectedGraph augmentedGraph = HolderProvider.getAugmentationHolder().getAugmentedGraph();

        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(tcTreeNode);
        Vertex source = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(tcTreeNode);
        Vertex target = HolderProvider.getSourceTargetPertinentGraphsHolder().getTargetNode(tcTreeNode);
        DirectedEdge sourceSinkEdge = augmentedGraph.getEdge(source, target);
        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(source);

        //get index of start and end of the pert graph in the outgoing edges.
        int sourcePertStart = 0;
        int sourcePertEnd = outgoingEdgesSource.size();
        boolean lastEdgeWasInPert = false;
        int edgeCount = 0;
        for (DirectedEdge edge : outgoingEdgesSource) {
            boolean thisEdgeIsInPert = pert.getEdge(edge.getSource(), edge.getTarget()) != null; //TODO: change with hashset and make hashcode for edge.
            if (lastEdgeWasInPert && !thisEdgeIsInPert) {
                sourcePertEnd = edgeCount;
                break;
            }
            if (!lastEdgeWasInPert && thisEdgeIsInPert) {
                sourcePertStart = edgeCount;
            }
            lastEdgeWasInPert = thisEdgeIsInPert;
            edgeCount++;
        }


        if(successorPathType.equals(SuccessorPathType.TYPE_M)){

            //shift edge from source to sink to the right path
            if(sourceSinkEdge != null){
                //remove edge and add it at the last index so that it lies on the right path
                outgoingEdgesSource.remove(sourceSinkEdge);
                outgoingEdgesSource.add(sourcePertEnd-1, sourceSinkEdge);
            }

            SuccessorConnector.connectSuccessorsLeftToRight(augmentedGraph, source, tcTree, tcTreeNode, outgoingEdgesSource.size()-1);

        }else{

            if(sourceSinkEdge != null)
                throw new LDrawingNotPossibleException("P-Node contains one child assigned with Type-B and an edge reaching from the source to the target.");

            MultiDirectedGraph typeBPert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(optTypeBNode);

            List<DirectedEdge> typeBPertOutgoingEdges = new LinkedList<>(typeBPert.getEdgesWithSource(source));

            //iterate over all outgoing edges and if it is part of the typeB pertGraph, remove it and add it again.
            List<DirectedEdge> outgoingEdgesCopy = new ArrayList<>(outgoingEdgesSource);
            for(DirectedEdge edge : outgoingEdgesCopy){
                if(typeBPertOutgoingEdges.contains(edge)) {
                    outgoingEdgesSource.remove(edge);
                    outgoingEdgesSource.add(sourcePertEnd-1, edge);
                }
            }


            //get Index of the apex
            int apexIndex = -1;

            for(int i = 1; i < outgoingEdgesSource.size()-1; i++){
                if(sourcePertStart > i || i > sourcePertEnd)
                    continue;
                Vertex v1 = outgoingEdgesSource.get(i-1).getTarget();
                Vertex v2 = outgoingEdgesSource.get(i).getTarget();
                Vertex v3 = outgoingEdgesSource.get(i+1).getTarget();
                DirectedEdge leftMiddle = augmentedGraph.getEdge(v1, v2);
                DirectedEdge rightMiddle = augmentedGraph.getEdge(v3, v2);

                if(leftMiddle != null && rightMiddle != null){
                    if(leftMiddle.getTarget().equals(v2) && rightMiddle.getTarget().equals(v2)){
                        apexIndex = i;
                        break;
                    }
                }
            }

            SuccessorConnector.connectSuccessorsLeftToRight(augmentedGraph, source, tcTree, tcTreeNode, apexIndex);
        }
    }
}
























