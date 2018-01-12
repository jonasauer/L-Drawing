package main.java.application;

import com.yworks.yfiles.graph.IGraph;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.graphConverter.GraphConverter;
import main.java.graphConverter.GraphValidator;

public class LDrawing {

    public static void lDrawing(IGraph graph){

        GraphValidator.checkIfLDrawingPossible(graph);
        MultiDirectedGraph multiDirectedGraph = GraphConverter.convert(graph);

        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(multiDirectedGraph);
    }
}
