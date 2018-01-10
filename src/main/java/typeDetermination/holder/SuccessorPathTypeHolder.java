package main.java.typeDetermination.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.typeDetermination.typeDeterminationUtils.Type;

import java.util.HashMap;
import java.util.Map;

public class SuccessorPathTypeHolder {

    private Map<TCTreeNode<DirectedEdge, Vertex>,Type> nodeTypes;

    private SuccessorPathTypeHolder(){
        this.nodeTypes = new HashMap<>();
    }

    public Map<TCTreeNode<DirectedEdge, Vertex>, Type> getNodeTypes(){
        return nodeTypes;
    }
}
