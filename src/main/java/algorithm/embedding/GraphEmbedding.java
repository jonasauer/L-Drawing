package main.java.algorithm.embedding;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class GraphEmbedding{

    private Map<Vertex, List<DirectedEdge>> outgoingEdges = new HashMap<>();
    //Singleton
    private static GraphEmbedding singleton;

    public static GraphEmbedding getEmbedding(){
        return singleton;
    }

    public static GraphEmbedding createEmbedding(MultiDirectedGraph graph){
        singleton = new GraphEmbedding(graph);
        return singleton;
    }


    private GraphEmbedding(MultiDirectedGraph graph){
        for(Vertex vertex : graph.getVertices()){
            outgoingEdges.put(vertex, new ArrayList<>());
        }
    }

    public List<DirectedEdge> getOutgoingEdges(Vertex vertex){
        return outgoingEdges.get(vertex);
    }
}