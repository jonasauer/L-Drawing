package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashSet;
import java.util.Set;

public class AugmentationHolder {

    private Vertex augmentedSource = null;
    private Set<DirectedEdge> augmentedEdges;

    public AugmentationHolder(){
        this.augmentedEdges = new HashSet<>();
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
}
