package main.java.typeDetermination.test;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.graph.abs.AbstractEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.typeDetermination.utils.PertinentGraphHelper;

import java.util.List;
import java.util.Map;

public class Printer {

    private static MultiDirectedGraph simpleGraph;
    private static DirectedEdge simpleGraphBackEdge;
    private static int dfsDepth;


    private static void simpleGraphSetup(){

        //		  --- t3 --- t4 ---
        //		  |				  |
        // t1 -- s2 ------------ j5 -- t9
        //	.	  |				  |		.
        //	.	  |_ s6 ---- j7 __|		.
        // 	.		  |_ t8 _|			.
        //	.............................

        simpleGraph = new MultiDirectedGraph();

        Vertex t1 = new Vertex("1");
        Vertex t3 = new Vertex("3");
        Vertex t4 = new Vertex("4");
        Vertex t8 = new Vertex("8");
        Vertex t9 = new Vertex("9");

        Vertex s2 = new Vertex("2");
        Vertex s6 = new Vertex("6");
        Vertex j7 = new Vertex("7");
        Vertex j5 = new Vertex("5");

        simpleGraph.addEdge(t1, s2);
        simpleGraph.addEdge(s2, t3);
        simpleGraph.addEdge(s2, s6);
        simpleGraph.addEdge(s2, j5);
        simpleGraph.addEdge(t3, t4);
        simpleGraph.addEdge(t4, j5);
        simpleGraph.addEdge(s6, j7);
        simpleGraph.addEdge(s6, t8);
        simpleGraph.addEdge(t8, j7);
        simpleGraph.addEdge(j7, j5);
        simpleGraph.addEdge(j5, t9);
        simpleGraphBackEdge = simpleGraph.addEdge(t1, t9);
    }

    private static void printPertinentGraphsAndSkeletons(){

        simpleGraphSetup();

        //		  --- t2 --- t3 ---
        //		  |				  |
        //  t1 -- t6 ------------ t9 -- t5
        //	.	  |				  |		.
        //	.	  |_ t7 ---- t8 __|		.
        // 	.		  |_ t4 _|			.
        //	.............................

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(simpleGraph, simpleGraphBackEdge);

        PertinentGraphHelper pertinentGraphHelper = new PertinentGraphHelper(tctree);
        List<TCTreeNode<DirectedEdge, Vertex>> postOrderList = pertinentGraphHelper.getPostOrderList();
        Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs = pertinentGraphHelper.getPertinentGraphs();

        for(TCTreeNode<DirectedEdge, Vertex> node : postOrderList){

            System.out.println(node.getType());
            System.out.println("    Skeleton: " + node.getSkeleton().toString().replace("-", "->"));
            System.out.println("    Pert    : " + pertinentGraphs.get(node));
        }
    }

    private static void printTreePreOrder(){

        dfsDepth = -1;

        simpleGraphSetup();

        //		  --- t2 --- t3 ---
        //		  |				  |
        //  t1 -- t6 ------------ t9 -- t5
        //	.	  |				  |		.
        //	.	  |_ t7 ---- t8 __|		.
        // 	.		  |_ t4 _|			.
        //	.............................

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(simpleGraph, simpleGraphBackEdge);

        TCTreeNode root = tctree.getRoot();
        dfs(tctree, root);
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
        printTreePreOrder();
    }


}
