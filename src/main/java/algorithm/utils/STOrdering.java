package main.java.algorithm.utils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class STOrdering {

    private List<Vertex> stOrderingList = new ArrayList<>();
    private Map<Vertex, Integer> stOrderingMap = new HashMap<>();
    private MultiDirectedGraph graph;

    //Singleton
    private static STOrdering singleton;


    public static STOrdering getSTOrdering(){
        return singleton;
    }

    public static STOrdering createSTOrdering(MultiDirectedGraph graph, Vertex source){
        singleton = new STOrdering(graph, source);
        return singleton;
    }

    private STOrdering(MultiDirectedGraph graph, Vertex source){

        this.graph = graph;
        Map<Vertex, Integer> incomingEdgesCounters = new HashMap<>();
        for(Vertex vertex : graph.getVertices())
            incomingEdgesCounters.put(vertex, graph.getEdgesWithTarget(vertex).size());

        orderVertices(source, incomingEdgesCounters);
    }




    private void orderVertices(Vertex vertex, Map<Vertex, Integer> incomingEdgesCounters){

        stOrderingMap.put(vertex, stOrderingMap.size());
        stOrderingList.add(vertex);

        for(DirectedEdge outgoingEdge : graph.getEdgesWithSource(vertex)){
            Vertex target = outgoingEdge.getTarget();
            int incomingEdgesCounter = incomingEdgesCounters.get(target);
            incomingEdgesCounters.replace(target, incomingEdgesCounter, incomingEdgesCounter-1);
            if(incomingEdgesCounter-1 < 1)
                orderVertices(target, incomingEdgesCounters);
        }
    }


    public List<Vertex> getSTOrderingList(){
        return stOrderingList;
    }

    Map<Vertex, Integer> getSTOrderingMap() {
        return stOrderingMap;
    }
}























