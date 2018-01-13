package main.java.test.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.tcTree.TCTree;
import main.java.decomposition.tcTree.TCTreeNode;
import main.java.decomposition.tcTree.TCTreeNodeType;
import main.java.test.graphProvider.ComplexGraphProvider;
import main.java.test.graphProvider.SimpleGraphProvider;
import main.java.typeDetermination.holder.HolderProvider;
import main.java.typeDetermination.holder.PertinentGraphHolder;
import main.java.typeDetermination.holder.PostOrderNodesHolder;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
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

        List<Integer> possibleSNodeEdges = new ArrayList<>();
        possibleSNodeEdges.add(12);
        possibleSNodeEdges.add(5);
        possibleSNodeEdges.add(3);
        possibleSNodeEdges.add(2);

        List<Integer> possiblePNodeEdges = new ArrayList<>();
        possiblePNodeEdges.add(9);
        possiblePNodeEdges.add(3);

        for(TCTreeNode<DirectedEdge, Vertex> node : tctree.getTCTreeNodes()){

            if(node.getType().equals(TCTreeNodeType.TYPE_S)){
                int edges = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(node).getEdges().size();
                assertTrue(possibleSNodeEdges.contains(edges));
                possibleSNodeEdges.remove((Integer)edges);
            }
            if(node.getType().equals(TCTreeNodeType.TYPE_P)){
                int edges = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(node).getEdges().size();
                assertTrue(possiblePNodeEdges.contains(edges));
                possiblePNodeEdges.remove((Integer)edges);
            }
        }
    }

    @Test
    public void testComplexGraph(){

        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(ComplexGraphProvider.getComplexGraph(), ComplexGraphProvider.backEdge);
        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tcTree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tcTree));

        Map<TCTreeNode<DirectedEdge, Vertex>, MultiDirectedGraph> pertinentGraphs = HolderProvider.getPertinentGraphHolder().getPertinentGraphs();

        assertTrue(pertinentGraphs.get(tcTree.getRoot()).getVertices().containsAll(ComplexGraphProvider.getComplexGraph().getVertices()));
        assertTrue(ComplexGraphProvider.getComplexGraph().getVertices().containsAll(pertinentGraphs.get(tcTree.getRoot()).getVertices()));

        List<Integer> possibleSNodeEdges = new ArrayList<>();
        possibleSNodeEdges.add(17);

        List<Integer> possibleRNodeEdges = new ArrayList<>();
        possibleRNodeEdges.add(14);

        for(TCTreeNode<DirectedEdge, Vertex> node : tcTree.getTCTreeNodes()){

            if(node.getType().equals(TCTreeNodeType.TYPE_S)){
                int edges = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(node).getEdges().size();
                assertTrue(possibleSNodeEdges.contains(edges));
                possibleSNodeEdges.remove((Integer)edges);
            }
            if(node.getType().equals(TCTreeNodeType.TYPE_R)){
                int edges = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(node).getEdges().size();
                assertTrue(possibleRNodeEdges.contains(edges));
                possibleRNodeEdges.remove((Integer)edges);
            }
        }
    }
}
