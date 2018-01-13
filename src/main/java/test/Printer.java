package main.java.test;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.graph.abs.AbstractEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.tcTree.TCTree;
import main.java.decomposition.tcTree.TCTreeNode;
import main.java.decomposition.tcTree.TCTreeNodeType;
import main.java.test.graphProvider.ComplexGraphProvider;
import main.java.typeDetermination.holder.HolderProvider;
import main.java.typeDetermination.holder.PertinentGraphHolder;
import main.java.typeDetermination.holder.PostOrderNodesHolder;

import java.util.List;
import java.util.Map;

public class Printer {

    private static int dfsDepth;

    public static void printPertinentGraphsAndSkeletons(MultiDirectedGraph graph, DirectedEdge backEdge){

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(graph, backEdge);

        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tctree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tctree));
        List<TCTreeNode<DirectedEdge, Vertex>> postOrderList = HolderProvider.getPostOrderNodesHolder().getPostOrderNodes();
        Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs = HolderProvider.getPertinentGraphHolder().getPertinentGraphs();

        for(TCTreeNode<DirectedEdge, Vertex> node : postOrderList){

            System.out.println(node.getType());
            System.out.println("    Skeleton: " + node.getSkeleton().toString().replace("-", "->"));
            System.out.println("    Pert    : " + pertinentGraphs.get(node));
        }
    }

    public static void printTreePreOrder(MultiDirectedGraph graph, DirectedEdge backEdge){

        dfsDepth = -1;

        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(graph, backEdge);

        TCTreeNode root = tcTree.getRoot();
        dfs(tcTree, root);
    }

    public static void printTreePreOrder(TCTree<DirectedEdge, Vertex> tcTree){

        dfsDepth = -1;

        TCTreeNode root = tcTree.getRoot();
        dfs(tcTree, root);
    }

    private static void dfs(TCTree tcTree, TCTreeNode node){
        dfsDepth++;

        for(int i = 0; i < dfsDepth; i++) {
            if(i < dfsDepth-1)
                System.out.print("|   ");
            else
                System.out.print("|-> ");
        }
        System.out.println(node.getType() +  "    Skeleton: " + node.getSkeleton().toString().replace(",", "").replace("-", "->"));


        if(node.getSkeleton().getVirtualEdges().iterator().hasNext()) {
            for(int i = 0; i < dfsDepth; i++)
                System.out.print("|   ");
            if(node.getType().equals(TCTreeNodeType.TYPE_Q))
                System.out.print("  AbstractEdges:     ");
            else
                System.out.print("|  AbstractEdges:     ");
            for(Object o : node.getSkeleton().getVirtualEdges()){
                AbstractEdge e = (AbstractEdge)o;
                System.out.print(e.toString().replace("-", "->") + " ");
            }
            System.out.println();
        }

        if(node.getSkeleton().getOriginalEdges().iterator().hasNext()) {
            for(int i = 0; i < dfsDepth; i++)
                System.out.print("|   ");
            if(node.getType().equals(TCTreeNodeType.TYPE_Q))
                System.out.print("  OriginalEdges:     ");
            else
                System.out.print("|  OriginalEdges:     ");
            for(Object o : node.getSkeleton().getOriginalEdges()){
                DirectedEdge e = (DirectedEdge) o;
                System.out.print(e + " ");
            }
            System.out.println();
        }

        for(Object o : tcTree.getChildren(node)){
            TCTreeNode n = (TCTreeNode)o;
            dfs(tcTree, n);
        }
        dfsDepth--;
    }

    public static void main(String[] args){

        printTreePreOrder(ComplexGraphProvider.getComplexGraph(), ComplexGraphProvider.backEdge);
    }


}