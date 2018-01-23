package main.java.algorithm.typeDeterminationUtils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.algorithm.holder.HolderProvider;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

            //flip node if necessary.
            for(int i = 0; i < outgoingEdgesSource.size()-1; i++){
                Vertex v1 = outgoingEdgesSource.get(i+0).getTarget();
                Vertex v2 = outgoingEdgesSource.get(i+1).getTarget();
                DirectedEdge edge = augmentedGraph.getEdge(v1, v2);
                if(edge != null && edge.getSource().equals(v2) && edge.getTarget().equals(v1)){
                    for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                        MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(child);
                        if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                            flipNodeInEmbedding(child);
                    }
                }
            }


            //connect pertinentGraph of all children
            for(int i = 0; i < outgoingEdgesSource.size()-1; i++){
                Vertex successor1 = outgoingEdgesSource.get(i+0).getTarget();
                Vertex successor2 = outgoingEdgesSource.get(i+1).getTarget();
                if(augmentedGraph.getEdge(successor1, successor2) == null){
                    DirectedEdge augmentedEdge = augmentedGraph.addEdge(successor1, successor2);
                    HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(successor1).add(augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(successor2).add(augmentedEdge);
                }
            }

        }else{

            if(sourceSinkEdge != null)
                throw new RuntimeException("Type B and edge from source to sink is occurring on P-Node!");

            MultiDirectedGraph pertTypeB = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(optTypeBNode);

            //get successors/predecessors of the pert graph that causes the type B
            List<Vertex> typeBSuccessorVertices = new LinkedList<>();
            for(DirectedEdge edge : pertTypeB.getEdgesWithSource(source))
                typeBSuccessorVertices.add(edge.getTarget());
            List<Vertex> typeBPredecessorVertices = new LinkedList<>();
            for(DirectedEdge edge : pertTypeB.getEdgesWithTarget(target)){
                typeBPredecessorVertices.add(edge.getSource());
            }

            //get the corresponding edges
            List<DirectedEdge> typeBSuccessorEdges = new LinkedList<>();
            List<DirectedEdge> typeBPredecessorEdges = new LinkedList<>();
            for(DirectedEdge edge : augmentedGraph.getEdgesWithTargets(typeBSuccessorVertices)){
                if(edge.getSource().equals(source)) typeBSuccessorEdges.add(edge);
            }
            for(DirectedEdge edge : augmentedGraph.getEdgesWithSources(typeBPredecessorVertices)){
                if(edge.getTarget().equals(target)) typeBPredecessorEdges.add(edge);
            }

            //iterate over all outgoing edges and if it is part of the typeB pertGraph, remove it and add it again.
            Iterator<DirectedEdge> edgeIterator = outgoingEdgesSource.iterator();
            while (edgeIterator.hasNext()){
                DirectedEdge edge = edgeIterator.next();
                if(typeBSuccessorEdges.contains(edge)) {
                    edgeIterator.remove();
                    outgoingEdgesSource.add(edge);
                }
            }

            //iterate over all incoming edges from right to left and if it is part of the typeB pertGraph, remove it and
            //add it again on the insertIndex, which starts at zero (right path) and increases
            int insertIndex = 0;
            edgeIterator = incomingEdgesTarget.iterator();
            while (edgeIterator.hasNext()){
                DirectedEdge edge = edgeIterator.next();
                if(typeBPredecessorEdges.contains(edge)) {
                    edgeIterator.remove();
                    incomingEdgesTarget.add(insertIndex++, edge);
                }
            }

            //get Index of the apex
            int apexIndex = -1;
            for(int i = 1; i < outgoingEdgesSource.size()-1; i++){
                Vertex leftVertex = outgoingEdgesSource.get(i-1).getTarget();
                Vertex middleVertex = outgoingEdgesSource.get(i).getTarget();
                Vertex rightVertex = outgoingEdgesSource.get(i+1).getTarget();
                DirectedEdge leftMiddle = augmentedGraph.getEdge(leftVertex, middleVertex);
                DirectedEdge rightMiddle = augmentedGraph.getEdge(rightVertex, middleVertex);

                if(leftMiddle != null && rightMiddle != null){
                    if(leftMiddle.getTarget().equals(middleVertex) && rightMiddle.getTarget().equals(middleVertex)){
                        apexIndex = i;
                        break;
                    }
                }
            }


            //flip node if necessary.
            for(int i = 0; i < outgoingEdgesSource.size()-1; i++){
                Vertex v1 = outgoingEdgesSource.get(i+0).getTarget();
                Vertex v2 = outgoingEdgesSource.get(i+1).getTarget();
                DirectedEdge edge = augmentedGraph.getEdge(v1, v2);
                if(edge != null && edge.getSource().equals(v2) && edge.getTarget().equals(v1)){
                    for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                        MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(child);
                        if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                            flipNodeInEmbedding(child);
                    }
                }
            }


            //connect vertices before apex position with an edge to the right.
            for(int i = 0; i < apexIndex-1; i++){
                Vertex firstSuccessor = outgoingEdgesSource.get(i).getTarget();
                Vertex secondSuccessor = outgoingEdgesSource.get(i+1).getTarget();
                if(augmentedGraph.getEdge(firstSuccessor, secondSuccessor) == null){
                    DirectedEdge augmentedEdge = augmentedGraph.addEdge(firstSuccessor, secondSuccessor);
                    HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(firstSuccessor).add(augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(secondSuccessor).add(augmentedEdge);
                }
            }

            //connect vertices after apex position with an edge to the left.
            for(int i = apexIndex+1; i < outgoingEdgesSource.size()-1; i++){
                Vertex firstSuccessor = outgoingEdgesSource.get(i).getTarget();
                Vertex secondSuccessor = outgoingEdgesSource.get(i+1).getTarget();
                if(augmentedGraph.getEdge(firstSuccessor, secondSuccessor) == null){
                    DirectedEdge augmentedEdge = augmentedGraph.addEdge(secondSuccessor, firstSuccessor);
                    HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(secondSuccessor).add(0, augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(firstSuccessor).add(0 ,augmentedEdge);
                }
            }

        }
    }





    private static void flipNodeInEmbedding(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(tcTreeNode);
        Vertex pertSource = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);
        Vertex pertTarget = HolderProvider.getSourceSinkPertinentGraphsHolder().getSinkNodes().get(tcTreeNode);
        MultiDirectedGraph augmentedGraph = HolderProvider.getAugmentationHolder().getAugmentedGraph();

        List<Vertex> successorVertices = new LinkedList<>();
        for(DirectedEdge edge : pert.getEdgesWithSource(pertSource))
            successorVertices.add(edge.getTarget());
        List<Vertex> predecessorVertices = new LinkedList<>();
        for(DirectedEdge edge : pert.getEdgesWithTarget(pertTarget)){
            predecessorVertices.add(edge.getSource());
        }

        //get the corresponding edges
        List<DirectedEdge> successorEdges = new LinkedList<>();
        for(DirectedEdge edge : augmentedGraph.getEdgesWithTargets(successorVertices)){
            if(edge.getSource().equals(pertSource))
                successorEdges.add(edge);
        }
        List<DirectedEdge> predecessorEdges = new LinkedList<>();
        for(DirectedEdge edge : augmentedGraph.getEdgesWithSources(predecessorVertices)){
            if(edge.getTarget().equals(pertTarget))
                predecessorEdges.add(edge);
        }

        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(pertSource);
        List<DirectedEdge> incomingEdgesTarget = HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(pertTarget);

        //switch edges in skeletonEmbedding of sourceNode of the pertinent graph
        if(successorEdges.size() > 1){
            int insertIndex = 0;
            for(DirectedEdge outgoingEdge : outgoingEdgesSource){
                if(successorEdges.contains(outgoingEdge)){
                    outgoingEdgesSource.removeAll(successorEdges);
                    break;
                }
                insertIndex++;
            }

            for(int i = successorEdges.size()-1; i >= 0; i--){
                outgoingEdgesSource.add(insertIndex++, successorEdges.get(i));
            }
        }

        //switch edges in skeletonEmbedding of targetNode of the pertinent graph
        if(predecessorEdges.size() > 1){
            int insertIndex = 0;
            for(DirectedEdge incomingEdge : incomingEdgesTarget){
                if(predecessorEdges.contains(incomingEdge)){
                    incomingEdgesTarget.removeAll(predecessorEdges);
                    break;
                }
                insertIndex++;
            }

            for(int i = predecessorEdges.size()-1; i >= 0; i--){
                incomingEdgesTarget.add(insertIndex++, predecessorEdges.get(i));
            }
        }

        //switch all other edges in the skeletonEmbedding.
        Collection<Vertex> vertices = pert.getVertices();
        vertices.remove(pertSource);
        vertices.remove(pertTarget);

        for(Vertex vertex : vertices){

            List<DirectedEdge> outgoingEdges = new LinkedList<>(HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex));
            HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex).clear();
            for(int i = outgoingEdges.size()-1; i >= 0; i--){
                HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex).add(outgoingEdges.get(i));
            }

            List<DirectedEdge> incomingEdges = new LinkedList<>(HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(vertex));
            HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(vertex).clear();
            for(int i = incomingEdges.size()-1; i >= 0; i--){
                HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(vertex).add(incomingEdges.get(i));
            }
        }
    }
}
























