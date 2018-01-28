package main.java.algorithm.typeDeterminationUtils.typeDetermination;

import main.java.PrintColors;
import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.typeDeterminationUtils.SuccessorConnector;
import main.java.algorithm.typeDeterminationUtils.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.algorithm.holder.HolderProvider;

import java.util.*;

public class PTypeDetermination implements TypeDetermination{

    public  void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {

        System.out.println(PrintColors.ANSI_RED + "---------------------------");
        System.out.println(PrintColors.ANSI_RED + "PType Determination! Source Vertex is " + HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNodes().get(tcTreeNode));
        System.out.println(PrintColors.ANSI_RED + "    Skeleton: " + tcTreeNode.getSkeleton());

        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_P)) return;

        TCTreeNode<DirectedEdge, Vertex> optTypeBNode = null;
        SuccessorPathType successorPathType = SuccessorPathType.TYPE_M;


        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){

            if(HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(child).equals(SuccessorPathType.TYPE_B)){
                if(optTypeBNode != null)
                    throw new LDrawingNotPossibleException("P-Node contains two children assigned with Type-B.");
                optTypeBNode = child;
                successorPathType = SuccessorPathType.TYPE_B;
            }
        }

        HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, successorPathType);
        System.out.println(PrintColors.ANSI_GREEN + "    SucessorPathType: " + successorPathType);
        MultiDirectedGraph augmentedGraph = HolderProvider.getAugmentationHolder().getAugmentedGraph();

        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(tcTreeNode);
        Vertex source = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);
        Vertex target = HolderProvider.getSourceTargetPertinentGraphsHolder().getTargetNodes().get(tcTreeNode);
        DirectedEdge sourceSinkEdge = augmentedGraph.getEdge(source, target);
        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(source);
        List<DirectedEdge> incomingEdgesTarget = HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(target);


        boolean lastEdgeWasInPert = false;
        int sourcePertStart = outgoingEdgesSource.size()-1;
        int sourcePertEnd = 0;


        for (int i = sourcePertEnd; i < outgoingEdgesSource.size(); i++) {
            DirectedEdge outgoingEdge = outgoingEdgesSource.get(i);
            if (lastEdgeWasInPert && !pert.getEdges().contains(outgoingEdge))
                sourcePertEnd = i;
            if (!lastEdgeWasInPert && pert.getEdges().contains(outgoingEdge))
                sourcePertStart = i;
            lastEdgeWasInPert = pert.getEdges().contains(outgoingEdge);
        }

        if(sourcePertEnd == 0)
            sourcePertEnd = 1;
        if(pert.getEdges().contains(outgoingEdgesSource.get(outgoingEdgesSource.size()-1)))
            sourcePertEnd = outgoingEdgesSource.size();
        if(pert.getEdges().contains(outgoingEdgesSource.get(0))){
            sourcePertStart = 0;
        }


        lastEdgeWasInPert = false;
        int targetPertStart = incomingEdgesTarget.size()-1;
        int targetPertEnd = 0;

        for (int i = targetPertStart; i >= 0; i--) {
            DirectedEdge incomingEdge = incomingEdgesTarget.get(i);
            if (lastEdgeWasInPert && !pert.getEdges().contains(incomingEdge))
                targetPertStart = i;
            if (!lastEdgeWasInPert && pert.getEdges().contains(incomingEdge))
                targetPertEnd = i;
            lastEdgeWasInPert = pert.getEdges().contains(incomingEdge);
        }
        if(targetPertStart == incomingEdgesTarget.size()-1)
            targetPertStart = incomingEdgesTarget.size()-2;
        if(pert.getEdges().contains(incomingEdgesTarget.get(0)))
            targetPertStart = -1;
        if(pert.getEdges().contains(incomingEdgesTarget.get(incomingEdgesTarget.size()-1))){
            targetPertEnd = 0;
        }





        if(successorPathType.equals(SuccessorPathType.TYPE_M)){

            //shift edge from source to sink to the right path
            if(sourceSinkEdge != null){
                //remove edge and add it at the last index so that it lies on the right path
                outgoingEdgesSource.remove(sourceSinkEdge);
                outgoingEdgesSource.add(sourcePertEnd-1, sourceSinkEdge);

                //remove edge and add it on the first index so that it lies on the right path
                incomingEdgesTarget.remove(sourceSinkEdge);
                incomingEdgesTarget.add(targetPertStart + 1, sourceSinkEdge);
            }

            SuccessorConnector.connectSuccessorsLeftToRight(augmentedGraph, source, tcTree, tcTreeNode, outgoingEdgesSource.size()-1);

        }else{

            if(sourceSinkEdge != null)
                throw new LDrawingNotPossibleException("P-Node contains one child assigned with Type-B and an edge reaching from the source to the target.");

            MultiDirectedGraph typeBPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(optTypeBNode);

            List<DirectedEdge> typeBPertOutgoingEdges = new LinkedList<>(typeBPert.getEdgesWithSource(source));
            List<DirectedEdge> typeBPertIncomingEdges = new LinkedList<>(typeBPert.getEdgesWithTarget(target));

            //iterate over all outgoing edges and if it is part of the typeB pertGraph, remove it and add it again.
            List<DirectedEdge> outgoingEdgesCopy = new ArrayList<>(outgoingEdgesSource);
            for(DirectedEdge edge : outgoingEdgesCopy){
                if(typeBPertOutgoingEdges.contains(edge)) {
                    outgoingEdgesSource.remove(edge);
                    outgoingEdgesSource.add(sourcePertEnd-1, edge);
                }
            }

            //iterate over all incoming edges from right to left and if it is part of the typeB pertGraph, remove it and
            //add it again on the insertIndex, which starts at zero (right path) and increases
            int insertIndex = 0;
            List<DirectedEdge> incomingEdgesCopy = new ArrayList<>(incomingEdgesTarget);
            for(DirectedEdge edge : incomingEdgesCopy){
                if(typeBPertIncomingEdges.contains(edge)){
                    incomingEdgesTarget.remove(edge);
                    incomingEdgesTarget.add(targetPertStart + 1 + insertIndex++, edge);
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

            for(DirectedEdge edge : HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(source)){
                System.out.println(edge);
            }

            SuccessorConnector.connectSuccessorsLeftToRight(augmentedGraph, source, tcTree, tcTreeNode, apexIndex);

        }
    }
}
























