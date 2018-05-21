package main.java.algorithm.utils.coordinates;

import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCoordinates {

    public static int DISTANCE = 50;

    protected Map<Vertex, Integer> coordinates;
    protected MultiDirectedGraph graph;


    public AbstractCoordinates(MultiDirectedGraph graph){
        this.graph = graph;
        this.coordinates = new HashMap<>();
    }

    public Map<Vertex, Integer> getCoordinates() {
        return coordinates;
    }
}
