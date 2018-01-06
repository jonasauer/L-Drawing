package main.java.typeDetermination.test;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.typeDetermination.utils.PertinentGraphHelper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Map;

public class PertinentGraphHelperTest {

    private MultiDirectedGraph simpleGraph;
    private DirectedEdge simpleGraphBackEdge;

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
    public void testSimpleGraph(){

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(simpleGraph, simpleGraphBackEdge);
        PertinentGraphHelper pertinentGraphHelper = new PertinentGraphHelper(tctree);

        Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs = pertinentGraphHelper.getPertinentGraphs();

        assertTrue(pertinentGraphs.get(tctree.getRoot()).getVertices().containsAll(simpleGraph.getVertices()));
        assertTrue(simpleGraph.getVertices().containsAll(pertinentGraphs.get(tctree.getRoot()).getVertices()));
    }
}
