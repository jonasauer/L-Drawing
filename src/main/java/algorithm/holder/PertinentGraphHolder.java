package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.*;

public class PertinentGraphHolder {

    private TCTree<DirectedEdge, Vertex> tcTree;
    private Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs;


    public PertinentGraphHolder(TCTree<DirectedEdge, Vertex> tcTree){

        this.tcTree = tcTree;
        this.pertinentGraphs = new HashMap<>();
        constructPertinentGraph();
    }


    private void constructPertinentGraph(){

        for(TCTreeNode<DirectedEdge, Vertex> tcTreeNode : HolderProvider.getPostOrderNodesHolder().getPostOrderNodes()) {

            MultiDirectedGraph pert = new MultiDirectedGraph();
            if(tcTreeNode.getType().equals(TCTreeNodeType.TYPE_Q)){
                DirectedEdge e = tcTreeNode.getSkeleton().getOriginalEdges().iterator().next();
                pert.addEdge(e.getSource(), e.getTarget());
            }else{
                for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)) {
                    for(DirectedEdge edge : pertinentGraphs.get(child).getEdges())
                        pert.addEdge(edge.getSource(), edge.getTarget());
                }
            }
            pertinentGraphs.put(tcTreeNode, pert);
        }
    }


    public MultiDirectedGraph getPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){
        return pertinentGraphs.get(tcTreeNode);
    }

}
