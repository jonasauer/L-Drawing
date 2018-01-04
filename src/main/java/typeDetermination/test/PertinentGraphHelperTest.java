package main.java.typeDetermination.test;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.typeDetermination.PertinentGraphHelper;

import java.util.List;
import java.util.Map;

public class PertinentGraphHelperTest {

    public static void main(String[] args){
        //		  --- t2 --- t3 ---
        //		  |				  |
        //  t1 -- t6 ------------ t9 -- t5
        //	.	  |				  |		.
        //	.	  |_ t7 ---- t8 __|		.
        // 	.		  |_ t4 _|			.
        //	.............................

        MultiDirectedGraph g = new MultiDirectedGraph();

        Vertex t1 = new Vertex("t1");
        Vertex t2 = new Vertex("t2");
        Vertex t3 = new Vertex("t3");
        Vertex t4 = new Vertex("t4");
        Vertex t5 = new Vertex("t5");

        Vertex t6 = new Vertex("t6");
        Vertex t7 = new Vertex("t7");
        Vertex t8 = new Vertex("t8");
        Vertex t9 = new Vertex("t9");

        g.addEdge(t1, t6);
        g.addEdge(t6, t2);
        g.addEdge(t6, t7);
        g.addEdge(t6, t9);
        g.addEdge(t2, t3);
        g.addEdge(t3, t9);
        g.addEdge(t7, t8);
        g.addEdge(t7, t4);
        g.addEdge(t4, t8);
        g.addEdge(t8, t9);
        g.addEdge(t9, t5);
        DirectedEdge backEdge = g.addEdge(t1, t5);

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(g, backEdge);

        PertinentGraphHelper helper = new PertinentGraphHelper(tctree);
        List<TCTreeNode<DirectedEdge, Vertex>> postOrderList = helper.getPostOrderList();
        Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs = helper.getPertinentGraphs();

        for(TCTreeNode<DirectedEdge, Vertex> node : postOrderList){

            System.out.println(node.getType());
            System.out.println("    " + node.getSkeleton());
            System.out.println("    " + pertinentGraphs.get(node));
        }
    }
}
