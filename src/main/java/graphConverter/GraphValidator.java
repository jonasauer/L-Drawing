package main.java.graphConverter;

import com.yworks.yfiles.algorithms.GraphChecker;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.layout.YGraphAdapter;

public class GraphValidator {


    public static void checkIfLDrawingPossible(IGraph graph){

        YGraphAdapter graphAdapter = new YGraphAdapter(graph);
        if(!GraphChecker.isConnected(graphAdapter.getYGraph()))
            throw new RuntimeException("Graph is not connected!");
        if(GraphChecker.isCyclic(graphAdapter.getYGraph()))
            throw new RuntimeException("Graph is cyclic!");
        if(!GraphChecker.isBiconnected(graphAdapter.getYGraph()))
            throw new RuntimeException("Graph is not bi-connected!");
        if(!GraphChecker.isPlanar(graphAdapter.getYGraph()))
            throw new RuntimeException("Graph is not planar!");
    }
}
