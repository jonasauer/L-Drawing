package main.java.algorithm.utils.coordinates;

import main.java.algorithm.embedding.GraphEmbedding;
import main.java.algorithm.utils.STOrdering;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class XCoordinates extends AbstractCoordinates{

    private Map<Vertex, Map<Vertex, Boolean>> isLeftPlacementPossible = new HashMap<>();

    //Singleton
    private static XCoordinates singleton;

    public static XCoordinates getXCoordinates(){
        return singleton;
    }

    public static XCoordinates createCoordinates(MultiDirectedGraph graph){
        singleton = new XCoordinates(graph);
        return singleton;
    }

    private XCoordinates(MultiDirectedGraph graph){
        super(graph);
        calculateIsLeftPlacementPossible();
        calculateXCoordinates();
    }

    private void calculateXCoordinates(){

        List<Vertex> stOrderingList = STOrdering.getSTOrdering().getSTOrderingList();

        for(int i = 1; i < stOrderingList.size(); i++){
            Vertex vertex = stOrderingList.get(i);
            placeVertexInXDirection(vertex);
        }


        //find the leftmost vertex in the x-ordering
        Vertex leftmost = null;
        for(Vertex vertex : stOrderingList){
            if(vertex.getLeft() == null) {
                leftmost = vertex;
                break;
            }
        }

        //assign x-coordinates to the vertices
        Vertex currentVertex = leftmost;
        int index = stOrderingList.size()-1;
        while(currentVertex.getRight() != null){
            coordinates.put(currentVertex, index--*DISTANCE);
            currentVertex = currentVertex.getRight();
        }
    }


    private void placeVertexInXDirection(Vertex vertex){

        List<DirectedEdge> incomingEdgesOfVertex = GraphEmbedding.getEmbedding().getIncomingEdges(vertex);
        int sizeOfIncomingEdges = incomingEdgesOfVertex.size();
        if(sizeOfIncomingEdges == 1){

            Vertex source = incomingEdgesOfVertex.get(0).getSource();

            if(isLeftPlacementPossible.get(source).get(vertex)){
                Vertex left = source.getLeft();
                if(left != null){
                    left.setRight(vertex);
                    vertex.setLeft(left);
                }
                source.setLeft(vertex);
                vertex.setRight(source);
            }else{
                Vertex right = source.getRight();
                if(right != null){
                    right.setLeft(vertex);
                    vertex.setRight(right);
                }
                source.setRight(vertex);
                vertex.setLeft(source);
            }
        }else{
            DirectedEdge rightmostIncomingEdge = incomingEdgesOfVertex.get(sizeOfIncomingEdges-1);
            Vertex right = rightmostIncomingEdge.getSource();
            Vertex left = right.getLeft();

            if(left != null) {
                left.setRight(vertex);
                vertex.setLeft(left);
            }
            right.setLeft(vertex);
            vertex.setRight(right);
        }
    }


    private void calculateIsLeftPlacementPossible(){

        Map<Vertex, Integer> stOrdering = STOrdering.getSTOrdering().getSTOrderingMap();

        for(Vertex vertex : stOrdering.keySet()){
            isLeftPlacementPossible.put(vertex, new HashMap<>());

            List<DirectedEdge> outgoingEdges = GraphEmbedding.getEmbedding().getOutgoingEdges(vertex);
            int highestSTIndex = -1;

            for(DirectedEdge outgoingEdge : outgoingEdges){
                Vertex successor = outgoingEdge.getTarget();
                int stIndex = stOrdering.get(successor);
                if(stIndex > highestSTIndex){
                    highestSTIndex = stIndex;
                    isLeftPlacementPossible.get(vertex).put(successor, true);
                }else{
                    isLeftPlacementPossible.get(vertex).put(successor, false);
                }
            }
        }
    }
}
