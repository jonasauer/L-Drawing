package main.java.algorithm.successorPathTypeDetermination;

import main.java.printer.PrintColors;
import main.java.algorithm.holder.HolderProvider;
import main.java.algorithm.types.FaceType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SuccessorConnector {


    private static void mirrorNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(tcTreeNode);
        Vertex pertSource = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(tcTreeNode);
        Vertex pertTarget = HolderProvider.getSourceTargetPertinentGraphsHolder().getTargetNode(tcTreeNode);
        List<DirectedEdge> pertEdges = null;
        List<DirectedEdge> allEdges = null;


        for(int loopCount = 0; loopCount < 2; loopCount++){

            switch (loopCount){
                case 0:
                    pertEdges = new LinkedList<>(pert.getEdgesWithSource(pertSource));
                    allEdges = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(pertSource);
                    break;
                case 1:
                    pertEdges = new ArrayList<>(pert.getEdgesWithTarget(pertTarget));
                    allEdges = HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(pertTarget);
                    break;
            }

            if(pertEdges.size() > 1){
                int insertIndex = 0;
                for(DirectedEdge edge : allEdges){
                    if(pertEdges.contains(edge))
                        break;
                    insertIndex++;
                }

                List<DirectedEdge> temp = new ArrayList<>();
                for(DirectedEdge edge : allEdges){
                    if(pertEdges.contains(edge))
                        temp.add(edge);
                }
                allEdges.removeAll(temp);

                for(int i = temp.size()-1; i >= 0; i--)
                    allEdges.add(insertIndex++, temp.get(i));
            }
        }


        //flip all incoming and outgoing edges of all other vertices of pert.
        List<Vertex> vertices = new ArrayList<>(pert.getVertices());
        vertices.remove(pertSource);
        vertices.remove(pertTarget);
        for(Vertex vertex : vertices){

            List<DirectedEdge> outgoingEdges = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
            List<DirectedEdge> outgoingEdgesCopy = new ArrayList<>(outgoingEdges);
            outgoingEdges.clear();
            for(DirectedEdge edge : outgoingEdgesCopy){
                outgoingEdges.add(0, edge);
            }

            List<DirectedEdge> incomingEdges = HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(vertex);
            List<DirectedEdge> incomingEdgesCopy = new ArrayList<>(incomingEdges);
            incomingEdges.clear();
            for(DirectedEdge edge : incomingEdgesCopy){
                incomingEdges.add(0, edge);
            }
        }
    }





    public static void connectWithTypeB(MultiDirectedGraph graph,
                                  TCTree<DirectedEdge, Vertex> tcTree,
                                  TCTreeNode<DirectedEdge, Vertex> tcTreeNode,
                                  Vertex vertex){

        List<DirectedEdge> outgoingEdges = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
        if(outgoingEdges.size() <= 1)
            return;
        int apexIndex = -1;

        for(int i = 1; i < outgoingEdges.size()-1; i++){
            Vertex v1 = outgoingEdges.get(i-1).getTarget();
            Vertex v2 = outgoingEdges.get(i).getTarget();
            Vertex v3 = outgoingEdges.get(i+1).getTarget();
            DirectedEdge v1_v2 = graph.getEdge(v1, v2);
            DirectedEdge v2_v3 = graph.getEdge(v2, v3);

            if(v1_v2 != null && v2_v3 != null){
                if(v1_v2.getTarget().equals(v2_v3.getTarget())){
                    apexIndex = i;
                    break;
                }
            }
        }

        connectSuccessorsLeftToRight(graph, vertex, tcTree, tcTreeNode, apexIndex);
        connectSuccessorsRightToLeft(graph, vertex, tcTree, tcTreeNode, apexIndex);
    }



    public static void connectWithBothTypes(MultiDirectedGraph graph,
                                      TCTree<DirectedEdge, Vertex> tcTree,
                                      TCTreeNode<DirectedEdge, Vertex> tcTreeNode,
                                      TCTreeNode<DirectedEdge, Vertex> rl_Divider,
                                      Vertex vertex){

        int apexIndex = 0;
        List<DirectedEdge> outgoingEdges = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(rl_Divider);
        for(DirectedEdge edge : outgoingEdges){
            if(pert.getEdges().contains(edge))
                break;
            apexIndex++;
        }

        connectSuccessorsLeftToRight(graph, vertex, tcTree, tcTreeNode, apexIndex);
        connectSuccessorsRightToLeft(graph, vertex, tcTree, tcTreeNode, apexIndex);
    }



    public static void connectWithOnlyOneType(MultiDirectedGraph graph,
                                        TCTree<DirectedEdge, Vertex> tcTree,
                                        TCTreeNode<DirectedEdge, Vertex> tcTreeNode,
                                        Map<Vertex, List<List<DirectedEdge>>> facesOfSource,
                                        Map<List<DirectedEdge>, FaceType> faceTypes,
                                        Vertex vertex){

        List<DirectedEdge> outgoingEdges = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
        if(outgoingEdges.size() <= 1)
            return;
        if(facesOfSource.get(vertex).size() <= 0)
            return;

        FaceType faceType = FaceType.UNDEFINED;
        for(List<DirectedEdge> face : facesOfSource.get(vertex)){
            if(!faceTypes.get(face).equals(FaceType.UNDEFINED)){
                faceType = faceTypes.get(face);
                break;
            }
        }

        if(faceType.equals(FaceType.TYPE_R))
            connectSuccessorsLeftToRight(graph, vertex, tcTree, tcTreeNode, outgoingEdges.size()-1);
        else
            connectSuccessorsRightToLeft(graph, vertex, tcTree, tcTreeNode, 0);

    }


    public static void connectSuccessorsLeftToRight(MultiDirectedGraph graph,
                                              Vertex vertex,
                                              TCTree<DirectedEdge, Vertex> tcTree,
                                              TCTreeNode<DirectedEdge, Vertex> tcTreeNode,
                                              int apexIndex){

        List<DirectedEdge> outgoingEdges = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(tcTreeNode);

        //flip nodes preceding the apex if they are not from left to right.
        for(int i = 0; i < apexIndex; i++){
            Vertex v1 = outgoingEdges.get(i+0).getTarget();
            Vertex v2 = outgoingEdges.get(i+1).getTarget();
            DirectedEdge v1_v2 = graph.getEdge(v1, v2);

            if(v1_v2 != null && v1_v2.getSource().equals(v2) && v1_v2.getTarget().equals(v1)){
                for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                    MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(child);
                    if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                        mirrorNode(child);
                }
            }
        }

        //augment the graph with missing edges between nodes preceding the apex.
        for(int i = 0; i < apexIndex; i++){
            Vertex v1 = outgoingEdges.get(i+0).getTarget();
            Vertex v2 = outgoingEdges.get(i+1).getTarget();

            if(pert.getVertices().contains(v1) && pert.getVertices().contains(v2) && graph.getEdge(v1, v2) == null){
                System.out.println(PrintColors.ANSI_BLUE + "        Insert edge LR: " + v1 + "->" + v2);
                DirectedEdge augmentedEdge = graph.addEdge(v1, v2);
                HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(v1).add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(v2).add(augmentedEdge);
            }
        }
    }

    public static void connectSuccessorsRightToLeft(MultiDirectedGraph graph,
                                              Vertex vertex,
                                              TCTree<DirectedEdge, Vertex> tcTree,
                                              TCTreeNode<DirectedEdge, Vertex> tcTreeNode,
                                              int apexIndex){

        List<DirectedEdge> outgoingEdges = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(tcTreeNode);

        //flip nodes following the apex if they are not from right to left.
        for(int i = apexIndex; i < outgoingEdges.size()-1; i++){
            Vertex v1 = outgoingEdges.get(i+0).getTarget();
            Vertex v2 = outgoingEdges.get(i+1).getTarget();
            DirectedEdge v1_v2 = graph.getEdge(v1, v2);

            if(v1_v2 != null && v1_v2.getSource().equals(v1) && v1_v2.getTarget().equals(v2)){
                for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                    MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(child);
                    if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                        mirrorNode(child);
                }
            }
        }

        //augment the graph with missing edges between nodes following the apex.
        for(int i = apexIndex; i < outgoingEdges.size()-1; i++){
            Vertex v1 = outgoingEdges.get(i+0).getTarget();
            Vertex v2 = outgoingEdges.get(i+1).getTarget();

            if(pert.getVertices().contains(v1) && pert.getVertices().contains(v2) && graph.getEdge(v1, v2) == null){
                System.out.println(PrintColors.ANSI_BLUE + "        Insert edge RL: " + v2 + "->" + v1);
                DirectedEdge augmentedEdge = graph.addEdge(v2, v1);
                HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(v2).add(0, augmentedEdge);
                HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(v1).add(0 ,augmentedEdge);
            }
        }
    }
}
