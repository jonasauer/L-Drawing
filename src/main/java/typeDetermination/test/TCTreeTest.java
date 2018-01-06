package main.java.typeDetermination.test;

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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test of a graph from the WS-FM'10 paper:
 * Artem Polyvyanyy, Jussi Vanhatalo, and Hagen Völzer:
 * Simplified Computation and Generalization of the Refined Process Structure Tree. WS-FM 2010: 25-41
 */
public class TCTreeTest {

    private MultiDirectedGraph WSFM10;
    private DirectedEdge WSFM10BackEdge;

    private MultiDirectedGraph simpleGraph;
    private DirectedEdge simpleGraphBackEdge;

    @Before
    public void WSFM10Setup(){

        WSFM10 = new MultiDirectedGraph();

        Vertex s = new Vertex("s");
        Vertex t = new Vertex("t");
        Vertex u = new Vertex("u");
        Vertex v = new Vertex("v");
        Vertex w = new Vertex("w");
        Vertex x = new Vertex("x");
        Vertex y = new Vertex("y");
        Vertex z = new Vertex("z");

        WSFM10.addEdge(s, u);
        WSFM10.addEdge(u, v);
        WSFM10.addEdge(u, w);
        WSFM10.addEdge(v, w);
        WSFM10.addEdge(v, x);
        WSFM10.addEdge(w, x);
        WSFM10.addEdge(x, y);
        WSFM10.addEdge(y, z);
        WSFM10.addEdge(y, z);
        WSFM10.addEdge(z, y);
        WSFM10.addEdge(z, t);
        WSFM10BackEdge = WSFM10.addEdge(t, s);
    }

    @Before
    public void simpleGraphSetup(){

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

    @Test
    public void testWSFM() {

        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(WSFM10, WSFM10BackEdge);
        Set<DirectedEdge> edges = new HashSet<>();

        for (TCTreeNode<DirectedEdge, Vertex> node : tcTree.getVertices()) {

            if (node.getType() == TCTreeNodeType.TYPE_S) {
                assertTrue(6 == node.getSkeleton().getVertices().size());
                assertTrue(4 == node.getSkeleton().getOriginalEdges().size());
                assertTrue(2 == node.getSkeleton().getVirtualEdges().size());
            }

            if (node.getType() == TCTreeNodeType.TYPE_P) {
                assertTrue(2 == node.getSkeleton().getVertices().size());
                assertTrue(3 == node.getSkeleton().getOriginalEdges().size());
                assertTrue(1 == node.getSkeleton().getVirtualEdges().size());
            }

            if (node.getType() == TCTreeNodeType.TYPE_R) {
                assertTrue(4 == node.getSkeleton().getVertices().size());
                assertTrue(5 == node.getSkeleton().getOriginalEdges().size());
                assertTrue(1 == node.getSkeleton().getVirtualEdges().size());
            }

            assertTrue(WSFM10.getEdges().containsAll(node.getSkeleton().getOriginalEdges()));
            edges.addAll((node.getSkeleton().getOriginalEdges()));
        }

        assertTrue(edges.containsAll(WSFM10.getEdges()));
        assertTrue(WSFM10.getEdges().containsAll(edges));
        assertTrue(15 == tcTree.getTCTreeNodes().size());
        assertTrue(12 == tcTree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
        assertTrue(1  == tcTree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        assertTrue(1  == tcTree.getTCTreeNodes(TCTreeNodeType.TYPE_R).size());
        assertTrue(1  == tcTree.getTCTreeNodes(TCTreeNodeType.TYPE_S).size());
    }

    @Test
    public void testNULL() {

        MultiDirectedGraph g = null;
        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(g);
        assertTrue(0 == tctree.getTCTreeNodes().size());
    }

    @Test
    public void testSingleVertex() {

        MultiDirectedGraph g = new MultiDirectedGraph();
        g.addVertex(new Vertex("A"));
        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(g);
        assertTrue(0 == tctree.getTCTreeNodes().size());
    }

    @Test
    public void testSingleEdge() {

        MultiDirectedGraph g = new MultiDirectedGraph();
        g.addEdge(new Vertex("A"), new Vertex("B"));
        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(g);
        assertTrue(0 == tctree.getTCTreeNodes().size());
    }

    @Test
    public void testSingleBond() {

        MultiGraph g = new MultiGraph();
        Vertex a = new Vertex("A");
        Vertex b = new Vertex("B");
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        TCTree<Edge, Vertex> tctree = new TCTree<>(g);

        assertTrue(6 == tctree.getTCTreeNodes().size());
        assertTrue(1 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        assertTrue(5 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
    }

    @Test
    public void testSingleBondAndSingleVertex() {

        MultiGraph g = new MultiGraph();
        Vertex a = new Vertex("A");
        Vertex b = new Vertex("B");
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addEdge(a, b);
        g.addVertex(new Vertex("C"));
        TCTree<Edge, Vertex> tctree = new TCTree<>(g);

        assertTrue(6 == tctree.getTCTreeNodes().size());
        assertTrue(1 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        assertTrue(5 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
    }

    @Test
    public void testSimpleGraph() {
        //		  --- t3 --- t4 ---
        //		  |				  |
        // t1 -- s2 ------------ j5 -- t9
        //	.	  |				  |		.
        //	.	  |_ s6 ---- j7 __|		.
        // 	.		  |_ t8 _|			.
        //	.............................

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(simpleGraph, simpleGraphBackEdge);
        Set<DirectedEdge> edges = new HashSet<>();

        for (TCTreeNode<DirectedEdge, Vertex> node : tctree.getTCTreeNodes()) {

            assertTrue(simpleGraph.getEdges().containsAll(node.getSkeleton().getOriginalEdges()));
            edges.addAll((node.getSkeleton().getOriginalEdges()));

            if (node.getType() == TCTreeNodeType.TYPE_P)
                assertTrue(2 == node.getSkeleton().getVertices().size());
        }

        assertTrue(edges.containsAll(simpleGraph.getEdges()));
        assertTrue(simpleGraph.getEdges().containsAll(edges));

        assertTrue(18 == tctree.getTCTreeNodes().size());
        assertTrue(12 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
        assertTrue(2  == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        assertTrue(0  == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_R).size());
        assertTrue(4  == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_S).size());
    }
}