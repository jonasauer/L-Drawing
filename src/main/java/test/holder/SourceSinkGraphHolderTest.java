package main.java.test.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.typeDetermination.holder.SourceSinkGraphHolder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SourceSinkGraphHolderTest {

    private MultiDirectedGraph simpleGraph;

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
    }

    @Test
    public void testSimpleGraph(){

        SourceSinkGraphHolder sourceSinkGraphHolder = new SourceSinkGraphHolder(simpleGraph);

        assertTrue(sourceSinkGraphHolder.getSourceNodes().size() == 1);
        assertTrue(sourceSinkGraphHolder.getSinkNodes().size() == 1);

        assertTrue(sourceSinkGraphHolder.getSourceNodes().iterator().next().equals(t1));
        assertTrue(sourceSinkGraphHolder.getSinkNodes().iterator().next().equals(t9));
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
        SourceSinkGraphHolder sourceSinkGraphHolder = new SourceSinkGraphHolder(cycle);

        assertTrue(sourceSinkGraphHolder.getSourceNodes().isEmpty());
        assertTrue(sourceSinkGraphHolder.getSinkNodes().isEmpty());
    }

    @Test
    public void testUnconnectedGraph(){

        MultiDirectedGraph unconnected = new MultiDirectedGraph();
        Vertex u1 = new Vertex("1");
        Vertex u2 = new Vertex("2");
        Vertex u3 = new Vertex("3");
        unconnected.addVertex(u1);
        unconnected.addVertex(u2);
        unconnected.addVertex(u3);
        SourceSinkGraphHolder sourceSinkGraphHolder = new SourceSinkGraphHolder(unconnected);

        assertTrue(sourceSinkGraphHolder.getSourceNodes().isEmpty());
        assertTrue(sourceSinkGraphHolder.getSinkNodes().isEmpty());
    }

    @Test
    public void testEmptyGraph(){

        MultiDirectedGraph empty = new MultiDirectedGraph();
        SourceSinkGraphHolder sourceSinkGraphHolder = new SourceSinkGraphHolder(empty);

        assertTrue(sourceSinkGraphHolder.getSourceNodes().isEmpty());
        assertTrue(sourceSinkGraphHolder.getSinkNodes().isEmpty());
    }
}
