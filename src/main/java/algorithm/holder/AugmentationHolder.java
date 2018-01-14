package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashSet;
import java.util.Set;

public class AugmentationHolder {

    private Vertex augmentedSource = null;
    private Set<DirectedEdge> augmentedEdges;
    private MultiDirectedGraph augmentedGraph;

    public AugmentationHolder(MultiDirectedGraph augmentedGraph){
        this.augmentedEdges = new HashSet<>();
        this.augmentedGraph = augmentedGraph;
    }

    public void setAugmentedSource(Vertex augmentedSource){
        this.augmentedSource = augmentedSource;
    }

    public Vertex getAugmentedSource() {
        return augmentedSource;
    }

    public Set<DirectedEdge> getAugmentedEdges(){
        return augmentedEdges;
    }

    public MultiDirectedGraph getAugmentedGraph() {
        return augmentedGraph;
    }
}
