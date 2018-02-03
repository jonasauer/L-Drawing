package main.java.algorithm.holder;

import main.java.algorithm.exception.GraphConditionsException;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashSet;
import java.util.Set;

public class SourceTargetGraphHolder {

    private Vertex sourceNode;
    private Vertex targetNode;

    public SourceTargetGraphHolder(MultiDirectedGraph graph) throws GraphConditionsException {

        Set<Vertex> sourceNodes = new HashSet<>(graph.getVertices());
        Set<Vertex> targetNodes = new HashSet<>(graph.getVertices());

        for(DirectedEdge edge : graph.getEdges()){
            sourceNodes.remove(edge.getTarget());
            targetNodes.remove(edge.getSource());
        }

        if(sourceNodes.size() != 1)
            throw new GraphConditionsException("The input graph contains more than one source. Please add edges to the graph until it contains exactly one source.");
        if(targetNodes.size() != 1)
            throw new GraphConditionsException("The input graph contains more than one target. Please add edges to the graph until it contains exactly one target.");
        this.sourceNode = sourceNodes.iterator().next();
        this.targetNode = targetNodes.iterator().next();
    }




    public Vertex getSourceNode() {
        return sourceNode;
    }

    public Vertex getTargetNode() {
        return targetNode;
    }
}
