package main.java.test;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.typeDetermination.utils.PertinentGraphHelper;
import main.java.typeDetermination.utils.SourceSinkHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SourceSinkHelperTest {

    private MultiDirectedGraph simpleGraph;
    private DirectedEdge simpleGraphBackEdge;

    private Vertex t1;
    private Vertex s2;
    private Vertex t3;
    private Vertex t4;
    private Vertex j5;
    private Vertex s6;
    private Vertex j7;
    private Vertex t8;
    private Vertex t9;

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

        t1 = new Vertex("1");
        t3 = new Vertex("3");
        t4 = new Vertex("4");
        t8 = new Vertex("8");
        t9 = new Vertex("9");

        s2 = new Vertex("2");
        s6 = new Vertex("6");
        j7 = new Vertex("7");
        j5 = new Vertex("5");

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
        SourceSinkHelper sourceSinkHelper = new SourceSinkHelper(tctree, pertinentGraphHelper);

        assertTrue(sourceSinkHelper.getSourceNodes().get(tctree.getRoot()).equals(t1));
        assertTrue(sourceSinkHelper.getSinkNodes().get(tctree.getRoot()).equals(t9));

        assertFalse(sourceSinkHelper.getSourceNodes().containsValue(t9));
        assertFalse(sourceSinkHelper.getSinkNodes().containsValue(t1));
    }
}
