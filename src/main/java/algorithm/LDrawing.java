package main.java.algorithm;

import com.yworks.yfiles.algorithms.GraphChecker;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.layout.YGraphAdapter;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.test.Printer;
import main.java.algorithm.holder.*;
import main.java.algorithm.typeDeterminationUtils.PTypeDetermination;
import main.java.algorithm.typeDeterminationUtils.QTypeDetermination;
import main.java.algorithm.typeDeterminationUtils.RTypeDetermination;
import main.java.algorithm.typeDeterminationUtils.STypeDetermination;

public class LDrawing {

    private IGraph initialGraph;
    private MultiDirectedGraph convertedGraph;
    private DirectedEdge backEdge;


    public void lDrawing(IGraph graph){

        this.initialGraph = graph;
        this.checkIfLDrawingPossible(initialGraph);
        this.convertedGraph = GraphConverter.convert(initialGraph);
        HolderProvider.setAugmentationHolder(new AugmentationHolder());
        this.determineBackEdge();

        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(convertedGraph, backEdge);

        HolderProvider.setSourceSinkGraphHolder(new SourceSinkGraphHolder(convertedGraph));
        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tcTree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tcTree));
        HolderProvider.setSourceSinkPertinentGraphsHolder(new SourceSinkPertinentGraphsHolder());
        HolderProvider.setSuccessorPathTypeHolder(new SuccessorPathTypeHolder());

        for(TCTreeNode<DirectedEdge, Vertex> node : HolderProvider.getPostOrderNodesHolder().getPostOrderNodes()){
            switch (node.getType()){
                case TYPE_Q:
                    QTypeDetermination.determineType(node);
                    break;
                case TYPE_S:
                    STypeDetermination.determineType(tcTree, node);
                    break;
                case TYPE_P:
                    PTypeDetermination.determineType(tcTree, node);
                    break;
                case TYPE_R:
                    RTypeDetermination.determineType(tcTree, node);
                    break;
            }
        }

        Printer.printTreePreOrder(tcTree);
        System.out.println();
        System.out.println();
    }


    public void checkIfLDrawingPossible(IGraph graph){

        YGraphAdapter graphAdapter = new YGraphAdapter(graph);
        if(!GraphChecker.isConnected(graphAdapter.getYGraph()))
            throw new RuntimeException("Graph is not connected!");
        if(GraphChecker.isCyclic(graphAdapter.getYGraph()))
            throw new RuntimeException("Graph is cyclic!");
        if(!GraphChecker.isBiconnected(graphAdapter.getYGraph()))
            throw new RuntimeException("Graph is not bi-connected!");
        if(!GraphChecker.isPlanar(graphAdapter.getYGraph()))
            throw new RuntimeException("Graph is not planar!");
    }


    private void determineBackEdge(){

        HolderProvider.setSourceSinkGraphHolder(new SourceSinkGraphHolder(convertedGraph));
        Vertex source = HolderProvider.getSourceSinkGraphHolder().getSourceNodes().iterator().next();
        Vertex target = HolderProvider.getSourceSinkGraphHolder().getSinkNodes().iterator().next();

        if(!convertedGraph.getEdges(source, target).isEmpty()){
            backEdge = convertedGraph.getEdge(source, target);
        }else{
            Vertex newSource = convertedGraph.addVertex(new Vertex("s'"));
            DirectedEdge augmentedE1 = convertedGraph.addEdge(newSource, source);
            DirectedEdge augmentedE2 = backEdge = convertedGraph.addEdge(newSource, target);

            HolderProvider.getAugmentationHolder().setAugmentedSource(newSource);
            HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedE1);
            HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedE2);
        }
    }
}
