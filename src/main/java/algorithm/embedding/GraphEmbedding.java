package main.java.algorithm.embedding;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class GraphEmbedding{

    private Map<Vertex, List<DirectedEdge>> outgoingEdges = new HashMap<>();
    private Map<Vertex, List<DirectedEdge>> incomingEdges = new HashMap<>();
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
            incomingEdges.put(vertex, new ArrayList<>());
        }
    }

    public List<DirectedEdge> getOutgoingEdges(Vertex vertex){
        return outgoingEdges.get(vertex);
    }

    public List<DirectedEdge> getIncomingEdges(Vertex vertex) {
        return incomingEdges.get(vertex);
    }

    public void printEmbedding(){
        for(Vertex vertex : outgoingEdges.keySet()){
            System.out.println(vertex.getName() + ":");
            System.out.print("    ");
            for(DirectedEdge edge : outgoingEdges.get(vertex)){
                System.out.print(edge + "  ");
            }
            System.out.println();
            System.out.print("    ");
            for(DirectedEdge edge : incomingEdges.get(vertex)){
                System.out.print(edge + "  ");
            }
            System.out.println();
        }
    }
}