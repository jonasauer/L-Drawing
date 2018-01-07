package main.java.test;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.typeDetermination.utils.SourceSinkGraphHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SourceSinkGraphHelperTest {

    private MultiDirectedGraph simpleGraph;
    private DirectedEdge simpleGraphBackEdge;

    private Vertex t1;
    private Vertex t2;
    private Vertex t3;
    private Vertex t4;
    private Vertex t5;
    private Vertex t6;
    private Vertex t7;
    private Vertex t8;
    private Vertex t9;

    @Before
    public void simpleGraphSetup(){

        //		  --- t3 --- t4 ---
        //		  |				  |
        // t1 -- t2 ------------ t5 -- t9
        //	.	  |				  |		.
        //	.	  |_ t6 ---- t7 __|		.
        // 	.		  |_ t8 _|			.
        //	.............................

        simpleGraph = new MultiDirectedGraph();

        t1 = new Vertex("1");
        t2 = new Vertex("2");
        t3 = new Vertex("3");
        t4 = new Vertex("4");
        t5 = new Vertex("5");
        t6 = new Vertex("6");
        t7 = new Vertex("7");
        t8 = new Vertex("8");
        t9 = new Vertex("9");

        simpleGraph.addEdge(t1, t2);
        simpleGraph.addEdge(t2, t3);
        simpleGraph.addEdge(t2, t6);
        simpleGraph.addEdge(t2, t5);
        simpleGraph.addEdge(t3, t4);
        simpleGraph.addEdge(t4, t5);
        simpleGraph.addEdge(t6, t7);
        simpleGraph.addEdge(t6, t8);
        simpleGraph.addEdge(t8, t7);
        simpleGraph.addEdge(t7, t5);
        simpleGraph.addEdge(t5, t9);
        simpleGraph.addEdge(t1, t9);
        simpleGraphBackEdge = simpleGraph.addEdge(t1, t9);
    }

    @Test
    public void testSimpleGraph(){

        SourceSinkGraphHelper sourceSinkGraphHelper = new SourceSinkGraphHelper(simpleGraph);

        assertTrue(sourceSinkGraphHelper.getSourceNodes().size() == 1);
        assertTrue(sourceSinkGraphHelper.getSinkNodes().size() == 1);

        assertTrue(sourceSinkGraphHelper.getSourceNodes().get(0).equals(t1));
        assertTrue(sourceSinkGraphHelper.getSinkNodes().get(0).equals(t9));
    }

    @Test
    public void testCycle(){

        MultiDirectedGraph cycle = new MultiDirectedGraph();
        Vertex c1 = new Vertex("1");
        Vertex c2 = new Vertex("2");
        Vertex c3 = new Vertex("3");
        cycle.addEdge(c1, c2);
        cycle.addEdge(c2, c3);
        cycle.addEdge(c3, c1);
        SourceSinkGraphHelper sourceSinkGraphHelper = new SourceSinkGraphHelper(cycle);

        assertTrue(sourceSinkGraphHelper.getSourceNodes().isEmpty());
        assertTrue(sourceSinkGraphHelper.getSinkNodes().isEmpty());
    }
}
