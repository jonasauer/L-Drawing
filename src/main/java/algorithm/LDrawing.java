package main.java.algorithm;

import com.yworks.yfiles.algorithms.GraphChecker;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.layout.YGraphAdapter;
import main.java.PrintColors;
import main.java.algorithm.exception.GraphConditionsException;
import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.graphConverter.GraphConverterHolder;
import main.java.algorithm.graphConverter.IGraphToMultiDirectedGraphConverter;
import main.java.algorithm.graphConverter.MultiDirectedGraphToGraphConverter;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.algorithm.holder.*;
import main.java.algorithm.successorPathTypeDetermination.PTypeDetermination;
import main.java.algorithm.successorPathTypeDetermination.QTypeDetermination;
import main.java.algorithm.successorPathTypeDetermination.RTypeDetermination;
import main.java.algorithm.successorPathTypeDetermination.STypeDetermination;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.test.Printer;

import java.util.Set;

public class LDrawing {

    private IGraph initialGraph;
    private MultiDirectedGraph convertedGraph;
    private DirectedEdge backEdge;
    private TCTree<DirectedEdge, Vertex> tcTree;


    public void lDrawing(IGraph graph) throws GraphConditionsException, LDrawingNotPossibleException {

        this.initialGraph = graph;
        this.checkIfLDrawingPossible(initialGraph);
        GraphConverterHolder.setIGraphToMultiDirectedGraphConverter(new IGraphToMultiDirectedGraphConverter(graph));
        this.convertedGraph = GraphConverterHolder.getiGraphToMultiDirectedGraphConverter().getConvertedGraph();
        GraphConverterHolder.setMultiDirectedGraphToGraphConverter(new MultiDirectedGraphToGraphConverter(convertedGraph));
        HolderProvider.setAugmentationHolder(new AugmentationHolder(convertedGraph));
        HolderProvider.setSourceTargetGraphHolder(new SourceTargetGraphHolder(convertedGraph));

        buildTCTree();



        System.out.println(PrintColors.ANSI_GREEN + "-----------------------------------------------------------------------------------------------------------------------------");
        System.out.println(PrintColors.ANSI_GREEN + "-----------------------------------------------------------------------------------------------------------------------------");
        System.out.println(PrintColors.ANSI_GREEN + "LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING-LDRAWING");
        System.out.println(PrintColors.ANSI_GREEN + "-----------------------------------------------------------------------------------------------------------------------------");
        System.out.println(PrintColors.ANSI_GREEN + "-----------------------------------------------------------------------------------------------------------------------------");
        Printer.printTreePreOrder(tcTree);

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

        HolderProvider.getAugmentationHolder().removeAugmentedParts();
        System.out.println(PrintColors.ANSI_WHITE + "    OriginalGraph");
        HolderProvider.setSourceTargetGraphHolder(new SourceTargetGraphHolder(convertedGraph));
        HolderProvider.getEmbeddingHolder().print(convertedGraph);
        HolderProvider.setStOrderingHolder(new STOrderingHolder(convertedGraph));
        HolderProvider.setCoordinatesHolder(new CoordinatesHolder(convertedGraph));
    }


    public void checkIfLDrawingPossible(IGraph graph) throws GraphConditionsException {

        YGraphAdapter graphAdapter = new YGraphAdapter(graph);
        if(initialGraph.getNodes().size() < 2)
            throw new GraphConditionsException("The input graph contains less than two nodes. Please add nodes to the graph until it contains at least two nodes.");
        if(initialGraph.getEdges().size() < 2)
            throw new GraphConditionsException("The input graph contains less than two edges. Please add edges to the graph until it contains at least two edges.");
        if(!GraphChecker.isConnected(graphAdapter.getYGraph()))
            throw new GraphConditionsException("The input graph is not connected. Please connect all nodes of the graph to a biconnected graph.");
        if(GraphChecker.isCyclic(graphAdapter.getYGraph()))
            throw new GraphConditionsException("The input graph is cyclic. Please remove or change edges to make the graph acyclic.");
        if(!GraphChecker.isBiconnected(graphAdapter.getYGraph()))
            throw new GraphConditionsException("The input graph is not biconnected. Please add edges to make the graph biconnected.");
        if(!GraphChecker.isPlanar(graphAdapter.getYGraph()))
            throw new GraphConditionsException("The input graph is not planar. Please make sure the graph admits a planar embedding.");
    }

    private void buildTCTree(){


        Vertex source = HolderProvider.getSourceTargetGraphHolder().getSourceNode();
        Vertex target = HolderProvider.getSourceTargetGraphHolder().getTargetNode();
        this.backEdge = convertedGraph.addEdge(source, target);
        this.tcTree = new TCTree<>(convertedGraph, backEdge);
        Set<TCTreeNode<DirectedEdge, Vertex>> children = tcTree.getChildren(tcTree.getRoot());
        for(TCTreeNode<DirectedEdge, Vertex> child : children){
            if(child.getType().equals(TCTreeNodeType.TYPE_Q)){
                tcTree.removeVertex(child);
                break;
            }
        }
        convertedGraph.removeEdge(backEdge);
    }
}
