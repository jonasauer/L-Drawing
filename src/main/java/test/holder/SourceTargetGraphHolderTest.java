package main.java.test.holder;

import main.java.algorithm.exception.GraphConditionsException;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.test.graphProvider.SimpleGraphProvider;
import main.java.algorithm.holder.SourceTargetGraphHolder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SourceTargetGraphHolderTest {

    @Test
    public void testSimpleGraph() throws GraphConditionsException {

        SourceTargetGraphHolder sourceTargetGraphHolder = new SourceTargetGraphHolder(SimpleGraphProvider.getSimpleGraph());

        assertTrue(sourceTargetGraphHolder.getSourceNode().equals(SimpleGraphProvider.s1));
        assertTrue(sourceTargetGraphHolder.getTargetNode().equals(SimpleGraphProvider.s9));
    }

    @Test
    public void testCycle() throws GraphConditionsException {

        MultiDirectedGraph cycle = new MultiDirectedGraph();
        Vertex c1 = new Vertex("1");
        Vertex c2 = new Vertex("2");
        Vertex c3 = new Vertex("3");
        cycle.addEdge(c1, c2);
        cycle.addEdge(c2, c3);
        cycle.addEdge(c3, c1);
        SourceTargetGraphHolder sourceTargetGraphHolder = new SourceTargetGraphHolder(cycle);

        assertTrue(sourceTargetGraphHolder.getSourceNode() == null);
        assertTrue(sourceTargetGraphHolder.getTargetNode() == null);
    }

    @Test
    public void testEmptyGraph() throws GraphConditionsException {

        MultiDirectedGraph empty = new MultiDirectedGraph();
        SourceTargetGraphHolder sourceTargetGraphHolder = new SourceTargetGraphHolder(empty);

        assertTrue(sourceTargetGraphHolder.getSourceNode() == null);
        assertTrue(sourceTargetGraphHolder.getTargetNode() == null);
    }
}
