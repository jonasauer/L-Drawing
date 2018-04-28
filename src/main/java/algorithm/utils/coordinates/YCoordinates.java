package main.java.algorithm.utils.coordinates;

import main.java.algorithm.utils.STOrdering;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.List;

public class YCoordinates extends AbstractCoordinates{

    //Singleton
    private static YCoordinates singleton;

    public static YCoordinates getYCoordinates(){
        return singleton;
    }

    public static YCoordinates createCoordinates(MultiDirectedGraph graph){
        singleton = new YCoordinates(graph);
        return singleton;
    }

    public YCoordinates(MultiDirectedGraph graph) {
        super(graph);
        calculateYCoordinates();
    }

    private void calculateYCoordinates(){

        List<Vertex> stOrdering = STOrdering.getSTOrdering().getSTOrderingList();
        int counter = 0;

        for(Vertex vertex : stOrdering)
            coordinates.put(vertex, DISTANCE * counter++);
    }
}
