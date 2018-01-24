package main.java.algorithm.typeDeterminationUtils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.algorithm.holder.HolderProvider;

import java.util.*;

public class PTypeDetermination{

    public static void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_P)) return;

        TCTreeNode<DirectedEdge, Vertex> optTypeBNode = null;
        SuccessorPathType successorPathType = SuccessorPathType.TYPE_M;

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){

            if(HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(child).equals(SuccessorPathType.TYPE_B)){
                if(optTypeBNode != null)
                    throw new RuntimeException("Type B is occurring twice in P-Node!");
                optTypeBNode = child;
                successorPathType = SuccessorPathType.TYPE_B;
            }
        }

        HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, successorPathType);
        MultiDirectedGraph augmentedGraph = HolderProvider.getAugmentationHolder().getAugmentedGraph();

        Vertex source = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);
        Vertex target = HolderProvider.getSourceSinkPertinentGraphsHolder().getSinkNodes().get(tcTreeNode);
        DirectedEdge sourceSinkEdge = augmentedGraph.getEdge(source, target);
        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(source);
        List<DirectedEdge> incomingEdgesTarget = HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(target);



        if(successorPathType.equals(SuccessorPathType.TYPE_M)){

            //shift edge from source to sink to the right path
            if(sourceSinkEdge != null){
                //remove edge and add it at the last index so that it lies on the right path
                outgoingEdgesSource.remove(sourceSinkEdge);
                outgoingEdgesSource.add(sourceSinkEdge);

                //remove edge and add it on the first index so that it lies on the right path
                incomingEdgesTarget.remove(sourceSinkEdge);
                incomingEdgesTarget.add(0, sourceSinkEdge);
            }

            SuccessorPathUtils.connectSuccessorsLeftToRight(augmentedGraph, outgoingEdgesSource, tcTree, tcTreeNode, outgoingEdgesSource.size()-1);

        }else{

            if(sourceSinkEdge != null)
                throw new RuntimeException("Type B and edge from source to sink is occurring on P-Node!");

            MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(optTypeBNode);

            List<DirectedEdge> pertOutgoingEdges = new LinkedList<>(pert.getEdgesWithSource(source));
            List<DirectedEdge> pertIncomingEdges = new LinkedList<>(pert.getEdgesWithTarget(target));

            //iterate over all outgoing edges and if it is part of the typeB pertGraph, remove it and add it again.
            List<DirectedEdge> outgoingEdgesCopy = new ArrayList<>(outgoingEdgesSource);
            for(DirectedEdge edge : outgoingEdgesCopy){
                if(pertOutgoingEdges.contains(edge)) {
                    outgoingEdgesSource.remove(edge);
                    outgoingEdgesSource.add(edge);
                }
            }

            //iterate over all incoming edges from right to left and if it is part of the typeB pertGraph, remove it and
            //add it again on the insertIndex, which starts at zero (right path) and increases
            int insertIndex = 0;
            List<DirectedEdge> incomingEdgesCopy = new ArrayList<>(incomingEdgesTarget);
            for(DirectedEdge edge : incomingEdgesCopy){
                if(pertIncomingEdges.contains(edge)){
                    incomingEdgesTarget.remove(edge);
                    incomingEdgesTarget.add(insertIndex++, edge);
                }
            }



            //get Index of the apex
            int apexIndex = -1;
            for(int i = 1; i < outgoingEdgesSource.size()-1; i++){
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

            SuccessorPathUtils.connectSuccessorsLeftToRight(augmentedGraph, outgoingEdgesSource, tcTree, tcTreeNode, apexIndex);

        }
    }
}
























