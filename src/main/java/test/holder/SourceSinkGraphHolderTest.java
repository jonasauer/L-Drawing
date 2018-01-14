package main.java.test.holder;

import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.test.graphProvider.SimpleGraphProvider;
import main.java.algorithm.holder.SourceSinkGraphHolder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SourceSinkGraphHolderTest {

    @Test
    public void testSimpleGraph(){

        SourceSinkGraphHolder sourceSinkGraphHolder = new SourceSinkGraphHolder(SimpleGraphProvider.getSimpleGraph());

        assertTrue(sourceSinkGraphHolder.getSourceNodes().size() == 1);
        assertTrue(sourceSinkGraphHolder.getSinkNodes().size() == 1);

        assertTrue(sourceSinkGraphHolder.getSourceNodes().iterator().next().equals(SimpleGraphProvider.s1));
        assertTrue(sourceSinkGraphHolder.getSinkNodes().iterator().next().equals(SimpleGraphProvider.s9));
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
