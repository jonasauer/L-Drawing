package main.java.algorithm.holder;

import main.java.algorithm.exception.GraphConditionsException;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;

import java.util.*;

public class SourceTargetPertinentGraphsHolder {

    private Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> sourceNodes;
    private Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> targetNodes;

    public SourceTargetPertinentGraphsHolder() throws GraphConditionsException {

        this.sourceNodes = new HashMap<>();
        this.targetNodes = new HashMap<>();

        determineSourcesAndTargets();
    }

    private void determineSourcesAndTargets() throws GraphConditionsException {

        List<TCTreeNode<DirectedEdge, Vertex>> postOrderNodes = HolderProvider.getPostOrderNodesHolder().getPostOrderNodes();

        for(TCTreeNode<DirectedEdge, Vertex> node : postOrderNodes){

            SourceTargetGraphHolder sourceTargetGraphHolder = new SourceTargetGraphHolder(HolderProvider.getPertinentGraphHolder().getPertinentGraph(node));
            Vertex pertSourceNode = sourceTargetGraphHolder.getSourceNode();
            Vertex pertTargetNode = sourceTargetGraphHolder.getTargetNode();
            sourceNodes.put(node, pertSourceNode);
            targetNodes.put(node, pertTargetNode);
        }
    }



    public Vertex getSourceNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {
        return sourceNodes.get(tcTreeNode);
    }

    public Vertex getTargetNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {
        return targetNodes.get(tcTreeNode);
    }
}
