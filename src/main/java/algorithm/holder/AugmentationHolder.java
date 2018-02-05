package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AugmentationHolder {

    private Set<DirectedEdge> augmentedEdges;
    private MultiDirectedGraph augmentedGraph;
    private Vertex augmentedSource;

    public AugmentationHolder(MultiDirectedGraph augmentedGraph){
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

    public void removeAugmentedParts(){

        for(DirectedEdge augmentedEdge : augmentedEdges) {
            augmentedGraph.removeEdge(augmentedEdge);
            Vertex source = augmentedEdge.getSource();
            List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(source);
            outgoingEdgesSource.remove(augmentedEdge);
        }
        augmentedGraph.removeVertex(augmentedSource);

    }
}
