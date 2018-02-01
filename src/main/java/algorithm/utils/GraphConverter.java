package main.java.algorithm.utils;

import com.yworks.yfiles.graph.*;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class GraphConverter {


    public static MultiDirectedGraph convert(IGraph graph){

        MultiDirectedGraph conv = new MultiDirectedGraph();
        Map<INode, Vertex> vertexMap = new HashMap<>();

        for(INode node : graph.getNodes()){
            vertexMap.put(node, new Vertex(node.getLabels().first().getText()));
        }

        for(IEdge edge : graph.getEdges()){
            INode source = edge.getSourceNode();
            INode target = edge.getTargetNode();
            conv.addEdge(vertexMap.get(source), vertexMap.get(target));
        }
        return conv;
    }
}
