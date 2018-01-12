package main.java.test;

import java.util.*;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.Edge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.graph.MultiGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.decomposition.spqrTree.TCTree;
import main.java.test.graphProvider.SimpleGraphProvider;
import main.java.test.graphProvider.WSDM10GraphProvider;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test of a graph from the WS-FM'10 paper:
 * Artem Polyvyanyy, Jussi Vanhatalo, and Hagen Völzer:
 * Simplified Computation and Generalization of the Refined Process Structure Tree. WS-FM 2010: 25-41
 */
public class TCTreeTest {

    @Test
    public void testWSFM() {

        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(WSDM10GraphProvider.getWSFM10Graph(), WSDM10GraphProvider.backEdge);
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

            assertTrue(WSDM10GraphProvider.getWSFM10Graph().getEdges().containsAll(node.getSkeleton().getOriginalEdges()));
            edges.addAll((node.getSkeleton().getOriginalEdges()));
        }

        assertTrue(edges.containsAll(WSDM10GraphProvider.getWSFM10Graph().getEdges()));
        assertTrue(WSDM10GraphProvider.getWSFM10Graph().getEdges().containsAll(edges));
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

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(SimpleGraphProvider.getSimpleGraph(), SimpleGraphProvider.backEdge);
        Set<DirectedEdge> edges = new HashSet<>();

        for (TCTreeNode<DirectedEdge, Vertex> node : tctree.getTCTreeNodes()) {

            assertTrue(SimpleGraphProvider.getSimpleGraph().getEdges().containsAll(node.getSkeleton().getOriginalEdges()));
            edges.addAll((node.getSkeleton().getOriginalEdges()));

            if (node.getType() == TCTreeNodeType.TYPE_P)
                assertTrue(2 == node.getSkeleton().getVertices().size());
        }

        assertTrue(edges.containsAll(SimpleGraphProvider.getSimpleGraph().getEdges()));
        assertTrue(SimpleGraphProvider.getSimpleGraph().getEdges().containsAll(edges));

        assertTrue(18 == tctree.getTCTreeNodes().size());
        assertTrue(12 == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_Q).size());
        assertTrue(2  == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_P).size());
        assertTrue(0  == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_R).size());
        assertTrue(4  == tctree.getTCTreeNodes(TCTreeNodeType.TYPE_S).size());
    }

    @Test
    public void testComplexGraph() {

    }
}