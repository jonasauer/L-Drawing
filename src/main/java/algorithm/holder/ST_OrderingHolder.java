package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ST_OrderingHolder {

    private MultiDirectedGraph graph;
    private TCTree<DirectedEdge, Vertex> tcTree;
    private Map<Vertex, Integer> stOrdering;
    private int counter = 0;

    public ST_OrderingHolder(MultiDirectedGraph graph, DirectedEdge referenceEdge){
        this.graph = graph;
        this.stOrdering = new HashMap<>();
        this.tcTree = new TCTree<>(graph, referenceEdge);
        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tcTree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tcTree));
        HolderProvider.setSourceTargetPertinentGraphsHolder(new SourceTargetPertinentGraphsHolder());
        order(tcTree.getRoot());
        for(Vertex vertex : graph.getVertices())
            System.out.println(vertex.getName() + "    " + stOrdering.get(vertex));

    }




    private void order(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        switch (tcTreeNode.getType()){
            case TYPE_Q:
                orderQNode(tcTreeNode);
                break;
            case TYPE_S:
                orderSNode(tcTreeNode);
                break;
            case TYPE_P:
                orderPNode(tcTreeNode);
                break;
            case TYPE_R:
                orderRNode(tcTreeNode);
                break;
        }
    }


    private void orderQNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        //label the source node
        Vertex source = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);
        if(stOrdering.containsKey(source))
            return;
        stOrdering.put(source, counter++);
    }

    private void orderSNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        //label node1, iterate child1,
        //label node2, iterate child2,
        //...
        //label nodeN, iterate childN,
        //but DO NOT label nodeN+1
        Vertex source = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);

        Set<TCTreeNode<DirectedEdge, Vertex>> children = tcTree.getChildren(tcTreeNode);
        for(int i = 0; i < children.size(); i++){
            for(TCTreeNode<DirectedEdge, Vertex> child : children){
                Vertex childSource = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNodes().get(child);
                Vertex childTarget = HolderProvider.getSourceTargetPertinentGraphsHolder().getTargetNodes().get(child);
                if(source.equals(childSource)){
                    order(child);
                    source = childTarget;
                }
            }
        }
    }

    private void orderPNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        //sort virtual edges so that the edge (if present) is the first
        //then iterate from N to 1
        //TODO: maybe the children need to be ordered from right to left, because the sequence always needs to be monotonically decreasing.
        Set<TCTreeNode<DirectedEdge, Vertex>> children = tcTree.getChildren(tcTreeNode);
        TCTreeNode<DirectedEdge, Vertex> typeQChild = null;
        for(TCTreeNode<DirectedEdge, Vertex> child : children){
            if(child.getType().equals(TCTreeNodeType.TYPE_Q)) {
                typeQChild = child;
                continue;
            }
            order(child);
        }
        order(typeQChild);

    }

    private void orderRNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        //TODO: read
    }
}
