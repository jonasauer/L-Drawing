package main.java.algorithm;

import com.yworks.yfiles.algorithms.GraphChecker;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.layout.YGraphAdapter;
import main.java.PrintColors;
import main.java.algorithm.exception.LDrawingNotPossibeExceptionException;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.algorithm.holder.*;
import main.java.algorithm.typeDeterminationUtils.typeDetermination.PTypeDetermination;
import main.java.algorithm.typeDeterminationUtils.typeDetermination.QTypeDetermination;
import main.java.algorithm.typeDeterminationUtils.typeDetermination.RTypeDetermination;
import main.java.algorithm.typeDeterminationUtils.typeDetermination.STypeDetermination;

public class LDrawing {

    private IGraph initialGraph;
    private MultiDirectedGraph convertedGraph;
    private DirectedEdge backEdge;


    public void lDrawing(IGraph graph) throws LDrawingNotPossibeExceptionException {

        this.initialGraph = graph;
        this.checkIfLDrawingPossible(initialGraph);
        this.convertedGraph = GraphConverter.convert(initialGraph);
        HolderProvider.setAugmentationHolder(new AugmentationHolder(convertedGraph));
        this.determineBackEdge();

        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(convertedGraph, backEdge);
        //TODO: check if tc tree is correct, ...
        System.out.println();
        System.out.println(PrintColors.ANSI_GREEN + "-----------------------------------------------------------------------------------------------------------------------------");
        System.out.println(PrintColors.ANSI_GREEN + "-----------------------------------------------------------------------------------------------------------------------------");
        System.out.println(PrintColors.ANSI_GREEN + "LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING");
        System.out.println(PrintColors.ANSI_GREEN + "-----------------------------------------------------------------------------------------------------------------------------");
        System.out.println(PrintColors.ANSI_GREEN + "-----------------------------------------------------------------------------------------------------------------------------");


        HolderProvider.setSourceTargetGraphHolder(new SourceTargetGraphHolder(convertedGraph));
        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tcTree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tcTree));
        HolderProvider.setSourceTargetPertinentGraphsHolder(new SourceTargetPertinentGraphsHolder());
        HolderProvider.setSuccessorPathTypeHolder(new SuccessorPathTypeHolder());
        HolderProvider.setEmbeddingHolder(new EmbeddingHolder(convertedGraph));
        HolderProvider.getEmbeddingHolder().print(convertedGraph);

        for(TCTreeNode<DirectedEdge, Vertex> node : HolderProvider.getPostOrderNodesHolder().getPostOrderNodes()){
            switch (node.getType()){
                case TYPE_Q:
                    new QTypeDetermination().determineType(tcTree, node);
                    break;
                case TYPE_S:
                    new STypeDetermination().determineType(tcTree, node);
                    break;
                case TYPE_P:
                    new PTypeDetermination().determineType(tcTree, node);
                    break;
                case TYPE_R:
                    new RTypeDetermination().determineType(tcTree, node);
                    break;
            }
        }
        System.out.println(PrintColors.ANSI_WHITE + "---------------------------");
        System.out.println(PrintColors.ANSI_WHITE + "Finish");
        System.out.println(PrintColors.ANSI_WHITE + "    AugmentedGraph: " + HolderProvider.getAugmentationHolder().getAugmentedGraph());
        HolderProvider.getEmbeddingHolder().print(convertedGraph);
    }


    public void checkIfLDrawingPossible(IGraph graph) throws LDrawingNotPossibeExceptionException {

        YGraphAdapter graphAdapter = new YGraphAdapter(graph);
        if(initialGraph.getNodes().size() < 2)
            throw new LDrawingNotPossibeExceptionException("The input graph has no edges in it.");
        if(!GraphChecker.isConnected(graphAdapter.getYGraph()))
            throw new LDrawingNotPossibeExceptionException("The input graph is not connected.");
        if(GraphChecker.isCyclic(graphAdapter.getYGraph()))
            throw new LDrawingNotPossibeExceptionException("The input graph is cyclic.");
        if(!GraphChecker.isBiconnected(graphAdapter.getYGraph()))
            throw new LDrawingNotPossibeExceptionException("The input graph is not biconnected.");
        if(!GraphChecker.isPlanar(graphAdapter.getYGraph()))
            throw new LDrawingNotPossibeExceptionException("The input graph is not planar.");
    }


    private void determineBackEdge(){

        HolderProvider.setSourceTargetGraphHolder(new SourceTargetGraphHolder(convertedGraph));
        Vertex source = HolderProvider.getSourceTargetGraphHolder().getSourceNodes().iterator().next();
        Vertex target = HolderProvider.getSourceTargetGraphHolder().getTargetNodes().iterator().next();

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
