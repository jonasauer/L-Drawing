package main.java.typeDetermination.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.abs.IGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class AugmentationHolder {

    private Map<DirectedEdge, Boolean> augmentedEdges;
    private Vertex augmentedSource = null;

    public AugmentationHolder(IGraph<DirectedEdge, Vertex> inititalGraph){

        this.augmentedEdges = new HashMap<>();
        for(DirectedEdge e : inititalGraph.getEdges())
            augmentedEdges.put(e, false);
    }

    public Map<DirectedEdge, Boolean> getAugmentedEdges(){
        return augmentedEdges;
    }

    public Vertex getAugmentedSource() {
        return augmentedSource;
    }
}
