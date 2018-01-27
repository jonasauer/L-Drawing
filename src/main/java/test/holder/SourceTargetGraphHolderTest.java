package main.java.test.holder;

import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.test.graphProvider.SimpleGraphProvider;
import main.java.algorithm.holder.SourceTargetGraphHolder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SourceTargetGraphHolderTest {

    @Test
    public void testSimpleGraph(){

        SourceTargetGraphHolder sourceTargetGraphHolder = new SourceTargetGraphHolder(SimpleGraphProvider.getSimpleGraph());

        assertTrue(sourceTargetGraphHolder.getSourceNodes().size() == 1);
        assertTrue(sourceTargetGraphHolder.getTargetNodes().size() == 1);

        assertTrue(sourceTargetGraphHolder.getSourceNodes().iterator().next().equals(SimpleGraphProvider.s1));
        assertTrue(sourceTargetGraphHolder.getTargetNodes().iterator().next().equals(SimpleGraphProvider.s9));
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
        SourceTargetGraphHolder sourceTargetGraphHolder = new SourceTargetGraphHolder(cycle);

        assertTrue(sourceTargetGraphHolder.getSourceNodes().isEmpty());
        assertTrue(sourceTargetGraphHolder.getTargetNodes().isEmpty());
    }

    @Test
    public void testEmptyGraph(){

        MultiDirectedGraph empty = new MultiDirectedGraph();
        SourceTargetGraphHolder sourceTargetGraphHolder = new SourceTargetGraphHolder(empty);

        assertTrue(sourceTargetGraphHolder.getSourceNodes().isEmpty());
        assertTrue(sourceTargetGraphHolder.getTargetNodes().isEmpty());
    }
}
