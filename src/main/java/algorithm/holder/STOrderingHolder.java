package main.java.algorithm.holder;

import main.java.PrintColors;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class STOrderingHolder {

    private MultiDirectedGraph graph;
    private List<Vertex> stOrdering;
    private Queue<Vertex> vertexQueue;
    private Map<Vertex, Integer> incomingEdgesCounters;
    private int counter = 0;

    public STOrderingHolder(MultiDirectedGraph graph){
        this.graph = graph;
        this.stOrdering = new LinkedList<>();
        this.vertexQueue = new LinkedList<>();
        this.incomingEdgesCounters = new HashMap<>();
        for(Vertex vertex : graph.getVertices())
            incomingEdgesCounters.put(vertex, graph.getEdgesWithTarget(vertex).size());

        orderVertices(HolderProvider.getSourceTargetGraphHolder().getSourceNode());

        this.counter = 0;
        System.out.println(PrintColors.ANSI_PURPLE + "---------------------------");
        System.out.println(PrintColors.ANSI_PURPLE + "ST-Ordering");
        for(Vertex vertex : stOrdering)
            System.out.println(PrintColors.ANSI_PURPLE + "    Vertex " + vertex.getName() + ":\t" + counter++);
    }




    private void orderVertices(Vertex vertex){

        stOrdering.add(vertex);
        for(DirectedEdge outgoingEdge : HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex)){
            Vertex target = outgoingEdge.getTarget();
            int incomingEdgesCounter = incomingEdgesCounters.get(target);
            incomingEdgesCounters.remove(target);
            incomingEdgesCounters.put(target, --incomingEdgesCounter);
            if(incomingEdgesCounter < 1)
                orderVertices(target);
        }
    }


    public List<Vertex> getSTOrdering(){
        return stOrdering;
    }
}























