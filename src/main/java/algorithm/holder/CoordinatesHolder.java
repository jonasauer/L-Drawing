package main.java.algorithm.holder;

import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatesHolder {

    private static final int xDifference = 50;
    private static final int yDifference = 50;

    private MultiDirectedGraph graph;
    private Map<Vertex, Integer> xCoordinates;
    private Map<Vertex, Integer> yCoordinates;

    public CoordinatesHolder(MultiDirectedGraph graph){
        this.graph = graph;
        this.xCoordinates = new HashMap<>();
        this.yCoordinates = new HashMap<>();
        calculateXCoordinates();
        calculateYCoordinates();
    }

    private void calculateXCoordinates(){
        //TODO: implement
    }

    private void calculateYCoordinates(){
        List<Vertex> stOrdering = HolderProvider.getStOrderingHolder().getSTOrdering();
        int counter = 0;

        for(Vertex vertex : stOrdering)
            yCoordinates.put(vertex, yDifference * counter++);
    }
}
