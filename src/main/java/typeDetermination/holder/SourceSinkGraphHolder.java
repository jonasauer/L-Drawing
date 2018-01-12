package main.java.typeDetermination.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashSet;
import java.util.Set;

public class SourceSinkGraphHolder {

    private MultiDirectedGraph graph;
    private Set<Vertex> sourceNodes;
    private Set<Vertex> sinkNodes;

    public SourceSinkGraphHolder(MultiDirectedGraph graph){

        this.graph = graph;

        this.sourceNodes = new HashSet<>();
        this.sinkNodes = new HashSet<>();

        determineSourcesAndSinks();
    }

    private void determineSourcesAndSinks(){

        for(DirectedEdge edge : graph.getEdges()){
            if(!sourceNodes.contains(edge.getSource()))
                sourceNodes.add(edge.getSource());
            if(!sinkNodes.contains(edge.getTarget()))
                sinkNodes.add(edge.getTarget());
        }

        for(DirectedEdge edge : graph.getEdges()){

            for(Vertex vertex : graph.getVertices()){

                if(edge.getSource().equals(vertex))
                    sinkNodes.remove(vertex);
                //TODO:graph.getEdgesWithTarget()

                if(edge.getTarget().equals(vertex))
                    sourceNodes.remove(vertex);
            }
        }
    }

    public Set<Vertex> getSourceNodes() {
        return sourceNodes;
    }

    public Set<Vertex> getSinkNodes() {
        return sinkNodes;
    }
}
