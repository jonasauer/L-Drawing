package main.java.typeDetermination.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.tcTree.TCTree;
import main.java.decomposition.tcTree.TCTreeNode;
import main.java.decomposition.tcTree.TCTreeNodeType;

import java.util.*;

public class PertinentGraphHolder {

    private TCTree<DirectedEdge, Vertex> tcTree;
    private Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs;


    public PertinentGraphHolder(TCTree<DirectedEdge, Vertex> tcTree){
        this.tcTree = tcTree;
        this.pertinentGraphs = new HashMap<>();

        if(tcTree.getTCTreeNodes().size() <= 0)
            return;

        for(TCTreeNode<DirectedEdge, Vertex> tcTreeNode : HolderProvider.getPostOrderNodesHolder().getPostOrderNodes())
            constructPertinentGraph(tcTreeNode);
    }


    private void constructPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        MultiDirectedGraph pert = new MultiDirectedGraph();

        if(tcTreeNode.getType().equals(TCTreeNodeType.TYPE_Q)) {
            DirectedEdge e = tcTreeNode.getSkeleton().getOriginalEdges().iterator().next();
            pert.addEdge(e.getSource(), e.getTarget());
        }else{
            //getChildren
            //get children pertinent graph
            //add all edges of these to the pert graph
            Set<TCTreeNode<DirectedEdge, Vertex>> children = tcTree.getChildren(tcTreeNode);
            for(TCTreeNode<DirectedEdge, Vertex> child : children){

                MultiDirectedGraph childPert = pertinentGraphs.get(child);
                for(DirectedEdge e : childPert.getEdges()){
                    pert.addEdge(e.getSource(), e.getTarget());
                }
            }
        }
        pertinentGraphs.put(tcTreeNode, pert);
    }


    public Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> getPertinentGraphs(){
        return pertinentGraphs;
    }

}
