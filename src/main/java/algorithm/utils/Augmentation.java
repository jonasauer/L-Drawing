package main.java.algorithm.utils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashSet;
import java.util.Set;

public class Augmentation {

    private Set<DirectedEdge> augmentedEdges;
    private MultiDirectedGraph augmentedGraph;
    private Vertex augmentedSource;
    //Singleton
    private static Augmentation singleton;


    public static Augmentation getAugmentation(){
        return singleton;
    }

    public static Augmentation createAugmentation(MultiDirectedGraph augmentedGraph){
        singleton = new Augmentation(augmentedGraph);
        return singleton;
    }


    private Augmentation(MultiDirectedGraph augmentedGraph){
        this.augmentedEdges = new HashSet<>();
        this.augmentedGraph = augmentedGraph;
    }

    public Set<DirectedEdge> getAugmentedEdges(){
        return augmentedEdges;
    }

    public MultiDirectedGraph getAugmentedGraph() {
        return augmentedGraph;
    }

    public void setAugmentedSource(Vertex augmentedSource){
        this.augmentedSource = augmentedSource;
    }

    public Vertex getAugmentedSource() {
        return augmentedSource;
    }

    public void removeAugmentedParts(){

        for(DirectedEdge augmentedEdge : augmentedEdges)
            augmentedGraph.removeEdge(augmentedEdge);
        augmentedGraph.removeVertex(augmentedSource);

    }
}
