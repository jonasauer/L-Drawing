package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.*;

public class SourceTargetPertinentGraphsHolder {

    private Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> sourceNodes;
    private Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> targetNodes;

    public SourceTargetPertinentGraphsHolder(){

        this.sourceNodes = new HashMap<>();
        this.targetNodes = new HashMap<>();

        determineSourcesAndTargets();
    }

    private void determineSourcesAndTargets(){

        List<TCTreeNode<DirectedEdge, Vertex>> postOrderNodes = HolderProvider.getPostOrderNodesHolder().getPostOrderNodes();

        for(TCTreeNode<DirectedEdge, Vertex> node : postOrderNodes){

            SourceTargetGraphHolder sourceTargetGraphHolder = new SourceTargetGraphHolder(HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(node));
            Set<Vertex> pertSourceNodes = sourceTargetGraphHolder.getSourceNodes();
            Set<Vertex> pertTargetNodes = sourceTargetGraphHolder.getTargetNodes();
            sourceNodes.put(node, pertSourceNodes.iterator().next());
            targetNodes.put(node, pertTargetNodes.iterator().next());
        }
    }



    public Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> getSourceNodes() {
        return sourceNodes;
    }

    public Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> getTargetNodes() {
        return targetNodes;
    }
}
