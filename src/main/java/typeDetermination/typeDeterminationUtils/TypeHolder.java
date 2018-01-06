package main.java.typeDetermination.typeDeterminationUtils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;

import java.util.HashMap;
import java.util.Map;

public class TypeHolder {

    private Map<TCTreeNode<DirectedEdge, Vertex>,Type> nodeTypes;

    private TypeHolder(){
        this.nodeTypes = new HashMap<>();
    }

    public Map<TCTreeNode<DirectedEdge, Vertex>, Type> getNodeTypes(){
        return nodeTypes;
    }






    private static TypeHolder instance = null;

    public static TypeHolder getInstance(){
        if(instance == null)
            instance = new TypeHolder();
        return instance;
    }
}
