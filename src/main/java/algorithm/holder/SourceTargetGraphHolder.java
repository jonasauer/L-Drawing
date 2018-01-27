package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashSet;
import java.util.Set;

public class SourceTargetGraphHolder {

    private MultiDirectedGraph graph;
    private Set<Vertex> sourceNodes;
    private Set<Vertex> targetNodes;

    public SourceTargetGraphHolder(MultiDirectedGraph graph){

        this.graph = graph;

        this.sourceNodes = new HashSet<>(graph.getVertices());
        this.targetNodes = new HashSet<>(graph.getVertices());

        for(DirectedEdge edge : graph.getEdges()){
            sourceNodes.remove(edge.getTarget());
            targetNodes.remove(edge.getSource());
        }
    }




    public Set<Vertex> getSourceNodes() {
        return sourceNodes;
    }

    public Set<Vertex> getTargetNodes() {
        return targetNodes;
    }
}
