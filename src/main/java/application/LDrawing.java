package main.java.application;

import com.yworks.yfiles.graph.IGraph;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import main.java.graphConverter.GraphConverter;
import main.java.graphConverter.GraphValidator;
import main.java.test.Printer;
import main.java.typeDetermination.holder.*;
import main.java.typeDetermination.typeDeterminationUtils.PTypeDetermination;
import main.java.typeDetermination.typeDeterminationUtils.QTypeDetermination;
import main.java.typeDetermination.typeDeterminationUtils.RTypeDetermination;
import main.java.typeDetermination.typeDeterminationUtils.STypeDetermination;

public class LDrawing {

    public static void lDrawing(IGraph graph){

        GraphValidator.checkIfLDrawingPossible(graph);
        MultiDirectedGraph multiDirectedGraph = GraphConverter.convert(graph);

        HolderProvider.setSourceSinkGraphHolder(new SourceSinkGraphHolder(multiDirectedGraph));

        Vertex source = HolderProvider.getSourceSinkGraphHolder().getSourceNodes().iterator().next();
        Vertex target = HolderProvider.getSourceSinkGraphHolder().getSinkNodes().iterator().next();
        DirectedEdge backEdge = null;
        if(multiDirectedGraph.getEdge(source, target) != null){
            backEdge = multiDirectedGraph.getEdge(source, target);
        }


        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(multiDirectedGraph, backEdge);

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

        /**for(TCTreeNode<DirectedEdge, Vertex> node : HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().keySet()){
            System.out.println(node.getType() + "      " + HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(node));
        }
        System.out.println();**/

        Printer.printTreePreOrder(tcTree);
        System.out.println();
        System.out.println();
    }
}
