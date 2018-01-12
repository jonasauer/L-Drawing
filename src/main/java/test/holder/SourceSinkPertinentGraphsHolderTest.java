package main.java.test.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.test.graphProvider.SimpleGraphProvider;
import main.java.typeDetermination.holder.HolderProvider;
import main.java.typeDetermination.holder.PertinentGraphHolder;
import main.java.typeDetermination.holder.PostOrderNodesHolder;
import main.java.typeDetermination.holder.SourceSinkPertinentGraphsHolder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SourceSinkPertinentGraphsHolderTest {

    @Test
    public void testSimpleGraph(){

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(SimpleGraphProvider.getSimpleGraph(), SimpleGraphProvider.backEdge);
        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tctree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tctree));
        SourceSinkPertinentGraphsHolder sourceSinkPertinentGraphsHolder = new SourceSinkPertinentGraphsHolder();

        assertTrue(sourceSinkPertinentGraphsHolder.getSourceNodes().get(tctree.getRoot()).equals(SimpleGraphProvider.s1));
        assertTrue(sourceSinkPertinentGraphsHolder.getSinkNodes().get(tctree.getRoot()).equals(SimpleGraphProvider.s9));

        assertFalse(sourceSinkPertinentGraphsHolder.getSourceNodes().containsValue(SimpleGraphProvider.s9));
        assertFalse(sourceSinkPertinentGraphsHolder.getSinkNodes().containsValue(SimpleGraphProvider.s1));
    }
}
