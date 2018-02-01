package main.java.test.holder;

import main.java.algorithm.exception.GraphConditionsException;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.test.graphProvider.SimpleGraphProvider;
import main.java.algorithm.holder.HolderProvider;
import main.java.algorithm.holder.PertinentGraphHolder;
import main.java.algorithm.holder.PostOrderNodesHolder;
import main.java.algorithm.holder.SourceTargetPertinentGraphsHolder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SourceTargetPertinentGraphsHolderTest {

    @Test
    public void testSimpleGraph() throws GraphConditionsException {

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(SimpleGraphProvider.getSimpleGraph(), SimpleGraphProvider.backEdge);
        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tctree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tctree));
        SourceTargetPertinentGraphsHolder sourceTargetPertinentGraphsHolder = new SourceTargetPertinentGraphsHolder();

        assertTrue(sourceTargetPertinentGraphsHolder.getSourceNodes().get(tctree.getRoot()).equals(SimpleGraphProvider.s1));
        assertTrue(sourceTargetPertinentGraphsHolder.getTargetNodes().get(tctree.getRoot()).equals(SimpleGraphProvider.s9));

        assertFalse(sourceTargetPertinentGraphsHolder.getSourceNodes().containsValue(SimpleGraphProvider.s9));
        assertFalse(sourceTargetPertinentGraphsHolder.getTargetNodes().containsValue(SimpleGraphProvider.s1));
    }
}
