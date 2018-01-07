package main.java.typeDetermination.utils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class SourceSinkGraphHelper {

    private MultiDirectedGraph graph;
    private List<Vertex> sourceNodes;
    private List<Vertex> sinkNodes;

    public SourceSinkGraphHelper(MultiDirectedGraph graph){

        this.graph = graph;

        this.sourceNodes = new ArrayList<>();
        this.sinkNodes = new ArrayList<>();

        determineSourcesAndSinks();
    }

    private void determineSourcesAndSinks(){

        sourceNodes.addAll(graph.getVertices());
        sinkNodes.addAll(graph.getVertices());

        for(DirectedEdge edge : graph.getEdges()){

            for(Vertex vertex : graph.getVertices()){

                if(edge.getSource().equals(vertex))
                    sinkNodes.remove(vertex);

                if(edge.getTarget().equals(vertex))
                    sourceNodes.remove(vertex);
            }
        }
    }

    public List<Vertex> getSourceNodes() {
        return sourceNodes;
    }

    public List<Vertex> getSinkNodes() {
        return sinkNodes;
    }
}
