package main.java.algorithm.utils;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;

import java.util.ArrayList;
import java.util.List;

public class NodesPostOrder extends ArrayList<TCTreeNode<DirectedEdge, Vertex>> {

    private TCTree<DirectedEdge, Vertex> tcTree;
    //Singleton
    private static NodesPostOrder singleton;


    public static NodesPostOrder getNodesPostOrder(){
        return singleton;
    }

    public static NodesPostOrder createNodesPostOrder(TCTree<DirectedEdge, Vertex> tcTree){
        singleton = new NodesPostOrder(tcTree);
        return singleton;
    }


    private NodesPostOrder(TCTree<DirectedEdge, Vertex> tcTree){
        super(tcTree.countVertices());
        this.tcTree = tcTree;
        orderNodes(tcTree.getRoot());
    }

    private void orderNodes(TCTreeNode<DirectedEdge, Vertex> node){

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(node))
            orderNodes(child);

        add(node);
    }
}
