package main.java.test.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.test.graphProvider.SimpleGraphProvider;
import main.java.typeDetermination.holder.HolderProvider;
import main.java.typeDetermination.holder.PertinentGraphHolder;
import main.java.typeDetermination.holder.PostOrderNodesHolder;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Map;

public class PertinentGraphHolderTest {

    @Test
    public void testSimpleGraph(){

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(SimpleGraphProvider.getSimpleGraph(), SimpleGraphProvider.backEdge);
        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tctree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tctree));

        Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs = HolderProvider.getPertinentGraphHolder().getPertinentGraphs();

        assertTrue(pertinentGraphs.get(tctree.getRoot()).getVertices().containsAll(SimpleGraphProvider.getSimpleGraph().getVertices()));
        assertTrue(SimpleGraphProvider.getSimpleGraph().getVertices().containsAll(pertinentGraphs.get(tctree.getRoot()).getVertices()));
    }
}
