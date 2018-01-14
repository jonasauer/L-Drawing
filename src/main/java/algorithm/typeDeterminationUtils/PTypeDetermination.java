package main.java.algorithm.typeDeterminationUtils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.algorithm.holder.HolderProvider;

import java.util.List;

public class PTypeDetermination{

    public static void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

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



        Vertex source = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);
        Vertex target = HolderProvider.getSourceSinkPertinentGraphsHolder().getSinkNodes().get(tcTreeNode);
        DirectedEdge sourceSinkEdge = HolderProvider.getAugmentationHolder().getAugmentedGraph().getEdge(source, target);
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

            //connect pertinentGraph of all children
            int targetPosition = 0;
            for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){

                int amountOfSuccessorVertices = HolderProvider.getPertinentGraphHolder()
                        .getPertinentGraphs().get(child).getEdges(source).size();
                targetPosition += amountOfSuccessorVertices;

                //add the connection between the two children
                if(targetPosition < outgoingEdgesSource.size()){
                    Vertex connectionSource = outgoingEdgesSource.get(targetPosition-1).getTarget();
                    Vertex connectionTarget = outgoingEdgesSource.get(targetPosition  ).getTarget();
                    DirectedEdge augmentedEdge = HolderProvider.getAugmentationHolder().getAugmentedGraph().addEdge(connectionSource, connectionTarget);
                    HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                }
            }

        }else{
            //TODO: place the type B node on the right, connect all last successors with the following successors of the
            //TODO: source of the current pert graph and the type B node with the last type M node.
        }
    }
}
























