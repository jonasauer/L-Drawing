package main.java.typeDetermination.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.tcTree.TCTree;
import main.java.decomposition.tcTree.TCTreeNode;

import java.util.ArrayList;
import java.util.List;

public class PostOrderNodesHolder {

    private List<TCTreeNode<DirectedEdge, Vertex>> postOrderNodes;
    private TCTree<DirectedEdge, Vertex> tcTree;

    public PostOrderNodesHolder(TCTree<DirectedEdge, Vertex> tcTree){

        this.postOrderNodes = new ArrayList<>();
        this.tcTree = tcTree;

        TCTreeNode<DirectedEdge, Vertex> root = tcTree.getRoot();
        postOrderNodes(root);
    }

    private void postOrderNodes(TCTreeNode<DirectedEdge, Vertex> node){

        for(TCTreeNode<DirectedEdge, Vertex> child : this.tcTree.getChildren(node)){
            postOrderNodes(child);
        }
        postOrderNodes.add(node);
    }

    public List<TCTreeNode<DirectedEdge, Vertex>> getPostOrderNodes(){
        return postOrderNodes;
    }
}
