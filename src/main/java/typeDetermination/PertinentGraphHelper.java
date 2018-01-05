package main.java.typeDetermination;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.*;

public class PertinentGraphHelper {

    private TCTree<DirectedEdge, Vertex> tcTree;
    private List<TCTreeNode<DirectedEdge, Vertex>> postOrderList;
    private Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs;

    public PertinentGraphHelper(TCTree<DirectedEdge, Vertex> tcTree){
        this.tcTree = tcTree;
        this.postOrderList = new ArrayList<>();
        this.pertinentGraphs = new HashMap<>();

        if(tcTree.getTCTreeNodes().size() <= 0)
            return;

        TCTreeNode<DirectedEdge, Vertex> root = tcTree.getRoot();
        postOrderNodes(root);

        for(TCTreeNode<DirectedEdge, Vertex> tcTreeNode : postOrderList)
            constructPertinentGraph(tcTreeNode);
    }


    private void postOrderNodes(TCTreeNode<DirectedEdge, Vertex> node){

        for(TCTreeNode<DirectedEdge, Vertex> child : this.tcTree.getChildren(node)){
            postOrderNodes(child);
        }
        postOrderList.add(node);
    }


    private void constructPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        MultiDirectedGraph pert = new MultiDirectedGraph();

        if(tcTreeNode.getType().equals(TCTreeNodeType.TYPE_Q)) {
            DirectedEdge e = tcTreeNode.getSkeleton().getOriginalEdges().iterator().next();
            pert.addEdge(e.getSource(), e.getTarget());
        }else{
            //kinder abfragen
            //deren pertinent graph abfragen
            //alle kanten der graphen zum neuen graphen hinzufügen.
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

    public List<TCTreeNode<DirectedEdge, Vertex>> getPostOrderList(){
        return postOrderList;
    }

    public Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> getPertinentGraphs(){
        return pertinentGraphs;
    }

}
