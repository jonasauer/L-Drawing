package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class STOrderingHolder {

    private List<Vertex> stOrderingList;
    private Map<Vertex, Integer> stOrderingMap;

    public STOrderingHolder(MultiDirectedGraph graph){

        this.stOrderingList = new LinkedList<>();
        this.stOrderingMap = new HashMap<>();
        Map<Vertex, Integer> incomingEdgesCounters = new HashMap<>();
        for(Vertex vertex : graph.getVertices())
            incomingEdgesCounters.put(vertex, HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(vertex).size());

        orderVertices(HolderProvider.getSourceTargetGraphHolder().getSourceNode(), incomingEdgesCounters);
    }




    private void orderVertices(Vertex vertex, Map<Vertex, Integer> incomingEdgesCounters){

        stOrderingMap.put(vertex, stOrderingMap.size());
        stOrderingList.add(vertex);

        for(DirectedEdge outgoingEdge : HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex)){
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























