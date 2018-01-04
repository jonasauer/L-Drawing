package main.java.decomposition.test;

import java.util.HashSet;
import java.util.Set;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.Edge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.graph.MultiGraph;
import main.java.decomposition.graph.abs.AbstractEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.decomposition.spqrTree.TCTree;
import main.java.typeDetermination.PertinentGraphHelper;

public class TCTreeTest{

    /**
     * Test of a graph from the WS-FM'10 paper:
     * Artem Polyvyanyy, Jussi Vanhatalo, and Hagen Völzer:
     * Simplified Computation and Generalization of the Refined Process Structure Tree. WS-FM 2010: 25-41
     */
    public static void testWSFM() {
        MultiDirectedGraph g = new MultiDirectedGraph();

        Vertex s = new Vertex("s");
        Vertex t = new Vertex("t");
        Vertex u = new Vertex("u");
        Vertex v = new Vertex("v");
        Vertex w = new Vertex("w");
        Vertex x = new Vertex("x");
        Vertex y = new Vertex("y");
        Vertex z = new Vertex("z");

        g.addEdge(s, u);
        g.addEdge(u, v);
        g.addEdge(u, w);
        g.addEdge(v, w);
        g.addEdge(v, x);
        g.addEdge(w, x);
        g.addEdge(x, y);
        g.addEdge(y, z);
        g.addEdge(y, z);
        g.addEdge(z, y);
        g.addEdge(z, t);
        DirectedEdge backEdge = g.addEdge(t, s);

        long start = System.nanoTime();
        TCTree<DirectedEdge, Vertex> tctree = new TCTree<DirectedEdge, Vertex>(g, backEdge);
        long end = System.nanoTime();
        System.out.println("WSFM\t" + ((double) end - start) / 1000000000);

        Set<DirectedEdge> edges = new HashSet<DirectedEdge>();
        for (TCTreeNode<DirectedEdge, Vertex> node : tctree.getVertices()) {
            if (node.getType() == TCTreeNodeType.TYPE_S) {
                System.out.println(6 == node.getSkeleton().getVertices().size());
                System.out.println(4 == node.getSkeleton().getOriginalEdges().size());
                System.out.println(2 == node.getSkeleton().getVirtualEdges().size());
            }

            if (node.getType() == TCTreeNodeType.TYPE_P) {
                System.out.println(2 == node.getSkeleton().getVertices().size());
                System.out.println(3 == node.getSkeleton().getOriginalEdges().size());
                System.out.println(1 == node.getSkeleton().getVirtualEdges().size());
            }

            if (node.getType() == TCTreeNodeType.TYPE_R) {
                System.out.println(4 == node.getSkeleton().getVertices().size());
                System.out.println(5 == node.getSkeleton().getOriginalEdges().size());
                System.out.println(1 == node.getSkeleton().getVirtualEdges().size());
            }

            System.out.println(g.getEdges().containsAll(node.getSkeleton().getOriginalEdges()));
            edges.addAll((node.getSkeleton().getOriginalEdges()));
        }

        System.out.println(edges.containsAll(g.getEdges()));
        System.out.println(g.getEdges().containsAll(edges));
        System.out.println(15 == tctree.getTCTreeNodes().size());
        System.out.println(12 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
        System.out.println(1 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        System.out.println(1 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_R).size());
        System.out.println(1 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_S).size());
    }

    public static void testNULL() {
        MultiDirectedGraph g = null;
        long start = System.nanoTime();
        TCTree<DirectedEdge, Vertex> tctree = new TCTree<DirectedEdge, Vertex>(g);
        long end = System.nanoTime();
        System.out.println("NULL\t" + ((double) end - start) / 1000000000);

        System.out.println(0 == tctree.getTCTreeNodes().size());
    }

    public static void testSingleVertex() {
        MultiDirectedGraph g = new MultiDirectedGraph();
        g.addVertex(new Vertex("A"));
        long start = System.nanoTime();
        TCTree<DirectedEdge, Vertex> tctree = new TCTree<DirectedEdge, Vertex>(g);
        long end = System.nanoTime();
        System.out.println("1V\t" + ((double) end - start) / 1000000000);

        System.out.println(0 == tctree.getTCTreeNodes().size());
    }

    public static void testSingleEdge() {
        MultiDirectedGraph g = new MultiDirectedGraph();
        g.addEdge(new Vertex("A"), new Vertex("B"));
        long start = System.nanoTime();
        TCTree<DirectedEdge, Vertex> tctree = new TCTree<DirectedEdge, Vertex>(g);
        long end = System.nanoTime();
        System.out.println("1E\t" + ((double) end - start) / 1000000000);

        System.out.println(0 == tctree.getTCTreeNodes().size());
    }

    public static void testSingleBond() {
        MultiGraph g = new MultiGraph();
        Vertex a = new Vertex("A");
        Vertex b = new Vertex("B");
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        long start = System.nanoTime();
        TCTree<Edge, Vertex> tctree = new TCTree<Edge, Vertex>(g);
        long end = System.nanoTime();
        System.out.println("1BOND\t" + ((double) end - start) / 1000000000);

        System.out.println(6 == tctree.getTCTreeNodes().size());
        System.out.println(1 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        System.out.println(5 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
    }

    public static void testSingleBondAndSingleVertex() {
        MultiGraph g = new MultiGraph();
        Vertex a = new Vertex("A");
        Vertex b = new Vertex("B");
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addVertex(new Vertex("C"));
        long start = System.nanoTime();
        TCTree<Edge, Vertex> tctree = new TCTree<Edge, Vertex>(g);
        long end = System.nanoTime();
        System.out.println("1B1V\t" + ((double) end - start) / 1000000000);

        System.out.println(6 == tctree.getTCTreeNodes().size());
        System.out.println(1 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        System.out.println(5 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
    }

    public static void testSimpleGraph() {
        //		  --- t3 --- t4 ---
        //		  |				  |
        // t1 -- s2 ------------ j5 -- t9
        //	.	  |				  |		.
        //	.	  |_ s6 ---- j7 __|		.
        // 	.		  |_ t8 _|			.
        //	.............................

        MultiDirectedGraph g = new MultiDirectedGraph();

        Vertex t1 = new Vertex("1");
        Vertex t3 = new Vertex("3");
        Vertex t4 = new Vertex("4");
        Vertex t8 = new Vertex("8");
        Vertex t9 = new Vertex("9");

        Vertex s2 = new Vertex("2");
        Vertex s6 = new Vertex("6");
        Vertex j7 = new Vertex("7");
        Vertex j5 = new Vertex("5");

        g.addEdge(t1, s2);
        g.addEdge(s2, t3);
        g.addEdge(s2, s6);
        g.addEdge(s2, j5);
        g.addEdge(t3, t4);
        g.addEdge(t4, j5);
        g.addEdge(s6, j7);
        g.addEdge(s6, t8);
        g.addEdge(t8, j7);
        g.addEdge(j7, j5);
        g.addEdge(j5, t9);
        DirectedEdge backEdge = g.addEdge(t9, t1);

        long start = System.nanoTime();
        TCTree<DirectedEdge, Vertex> tctree = new TCTree<DirectedEdge, Vertex>(g, backEdge);
        long end = System.nanoTime();
        System.out.println("2B4P\t" + ((double) end - start) / 1000000000);

        Set<DirectedEdge> edges = new HashSet<DirectedEdge>();
        for (TCTreeNode<DirectedEdge, Vertex> node : tctree.getTCTreeNodes()) {
            System.out.println(g.getEdges().containsAll(node.getSkeleton().getOriginalEdges()));
            edges.addAll((node.getSkeleton().getOriginalEdges()));

            if (node.getType() == TCTreeNodeType.TYPE_P) {
                System.out.println(2 == node.getSkeleton().getVertices().size());
            }
        }

        System.out.println(edges.containsAll(g.getEdges()));
        System.out.println(g.getEdges().containsAll(edges));

        System.out.println(18 == tctree.getTCTreeNodes().size());
        System.out.println(12 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
        System.out.println(2 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        System.out.println(0 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_R).size());
        System.out.println(4 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_S).size());
    }



    private static void test(){

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

        TCTreeNode root = tctree.getRoot();
        dfs(tctree, root);
    }

    private static int dfsDepth = -1;

    private static void dfs(TCTree tcTree, TCTreeNode node){
        dfsDepth++;

        for(int i = 0; i < dfsDepth; i++)
            System.out.print("    ");
        System.out.println(node.getType() +  "    Skeleton: " + node.getSkeleton());


        if(node.getSkeleton().getVirtualEdges().iterator().hasNext()) {
            for(int i = 0; i < dfsDepth; i++)
                System.out.print("    ");
            System.out.print(" AbstractEdges:     ");
            for(Object o : node.getSkeleton().getVirtualEdges()){
                AbstractEdge e = (AbstractEdge)o;
                System.out.print("" + e + "  ");
            }
            System.out.println();
        }

        if(node.getSkeleton().getOriginalEdges().iterator().hasNext()) {
            for(int i = 0; i < dfsDepth; i++)
                System.out.print("    ");
            System.out.print(" OriginalEdges:     ");
            for(Object o : node.getSkeleton().getOriginalEdges()){
                DirectedEdge e = (DirectedEdge) o;
                System.out.print("" + e + "  ");
            }
            System.out.println();
        }

        for(Object o : tcTree.getChildren(node)){
            TCTreeNode n = (TCTreeNode)o;
            dfs(tcTree, n);
        }
        dfsDepth--;
    }




    public static void main(String [] args){
        /*
        System.out.println("Test WSFM");
        testWSFM();
        System.out.println("Test Null");
        testNULL();
        System.out.println("Test single vertex");
        testSingleVertex();
        System.out.println("Test single abs");
        testSingleEdge();
        System.out.println("Test single bond");
        testSingleBond();
        System.out.println("Test single bond and single vertex");
        testSingleBondAndSingleVertex();
        System.out.println("Test simple graph");
        testSimpleGraph();
        */
        System.out.println();
        System.out.println();
        test();
    }
}