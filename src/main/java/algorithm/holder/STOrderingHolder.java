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
    private Map<Vertex, Integer> incomingEdgesCount;
    private int counter = 0;

    public STOrderingHolder(MultiDirectedGraph graph){
        this.graph = graph;
        this.stOrdering = new LinkedList<>();
        this.vertexQueue = new LinkedList<>();
        this.vertexQueue.offer(HolderProvider.getSourceTargetGraphHolder().getSourceNode());
        this.incomingEdgesCount = new HashMap<>();
        for(Vertex vertex : graph.getVertices())
            incomingEdgesCount.put(vertex, graph.getEdgesWithTarget(vertex).size());

        orderVertices();

        this.counter = 0;
        System.out.println(PrintColors.ANSI_PURPLE + "---------------------------");
        System.out.println(PrintColors.ANSI_PURPLE + "ST-Ordering");
        for(Vertex vertex : stOrdering)
            System.out.println(PrintColors.ANSI_PURPLE + "    Vertex " + vertex.getName() + ":\t" + counter++);
    }




    private void orderVertices(){

        while(!vertexQueue.isEmpty()){

            Vertex source = vertexQueue.poll();
            for(DirectedEdge edge : graph.getEdgesWithSource(source)){
                Vertex target = edge.getTarget();
                int incomingEdges = incomingEdgesCount.get(target) - 1;
                incomingEdgesCount.remove(target);
                incomingEdgesCount.put(target, incomingEdges);
                if(incomingEdges < 1)
                    vertexQueue.offer(target);
            }
            stOrdering.add(source);
        }
    }


    public List<Vertex> getSTOrdering(){
        return stOrdering;
    }
}























