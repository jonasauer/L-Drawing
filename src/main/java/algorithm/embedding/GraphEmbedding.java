package main.java.algorithm.embedding;

import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class GraphEmbedding extends AbstractEmbedding{

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
        super(graph);
    }


    public void clear(){
        for(Vertex vertex : originalGraph.getVertices()) {
            outgoingEdges.remove(vertex);
            outgoingEdges.put(vertex, new ArrayList<>());
        }
    }
}