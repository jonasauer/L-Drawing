package main.java.algorithm.utils;

import main.java.algorithm.embedding.GraphEmbedding;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class Coordinates {

    private static final int xDifference = 50;
    private static final int yDifference = 50;
    private MultiDirectedGraph graph;
    private Map<Vertex, Integer> xCoordinates;
    private Map<Vertex, Integer> yCoordinates;
    //Singleton
    private static Coordinates singleton;


    public static Coordinates getCoordinates(){
        return singleton;
    }

    public static Coordinates createCoordinates(MultiDirectedGraph graph){
        singleton = new Coordinates(graph);
        return singleton;
    }

    private Coordinates(MultiDirectedGraph graph){
        this.graph = graph;
        this.xCoordinates = new HashMap<>();
        this.yCoordinates = new HashMap<>();
        calculateXCoordinates();
        calculateYCoordinates();
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
        int index = 0;
        while(currentVertex.getRight() != null){
            xCoordinates.put(currentVertex, index++*xDifference);
            currentVertex = currentVertex.getRight();
        }
    }

    //TODO: make constant runtime
    private void placeVertexInXDirection(Vertex vertex){

        List<DirectedEdge> incomingEdgesOfVertex = GraphEmbedding.getEmbedding().getIncomingEdges(vertex);
        int sizeOfIncomingEdges = incomingEdgesOfVertex.size();
        System.out.println(vertex.getName());
        if(sizeOfIncomingEdges == 1){

            Vertex source = incomingEdgesOfVertex.get(0).getSource();
            List<DirectedEdge> siblingEdges = GraphEmbedding.getEmbedding().getOutgoingEdges(source);
            boolean placeLeft = true;
            for(DirectedEdge siblingEdge : siblingEdges){
                Vertex sibling = siblingEdge.getTarget();
                if(sibling == vertex)
                    break;
                if(STOrdering.getSTOrdering().getSTOrderingMap().get(vertex) < STOrdering.getSTOrdering().getSTOrderingMap().get(sibling)){
                    placeLeft = false;
                    break;
                }
            }

            if(placeLeft){
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

    private void calculateYCoordinates(){

        List<Vertex> stOrdering = STOrdering.getSTOrdering().getSTOrderingList();
        int counter = 0;

        for(Vertex vertex : stOrdering)
            yCoordinates.put(vertex, yDifference * counter++);
    }




    public Map<Vertex, Integer> getXCoordinates() {
        return xCoordinates;
    }

    public Map<Vertex, Integer> getYCoordinates() {
        return yCoordinates;
    }
}
