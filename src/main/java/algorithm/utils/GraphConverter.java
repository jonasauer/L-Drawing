package main.java.algorithm.utils;

import com.yworks.yfiles.graph.*;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class GraphConverter {

    private Map<Vertex, INode> convV2OrigV;
    private Map<DirectedEdge, IEdge> convE2OrigE;
    private IGraph originalGraph;
    private MultiDirectedGraph convertedGraph;

    private static GraphConverter singleton;

    public static GraphConverter createGraphConverter(IGraph graph){
        singleton = new GraphConverter(graph);
        return singleton;
    }

    public static GraphConverter getGraphConverter() {
        return singleton;
    }


    private GraphConverter(IGraph graph){
        this.originalGraph = graph;
        this.convV2OrigV = new HashMap<>();
        this.convE2OrigE = new HashMap<>();
        convert();
    }



    private void convert(){

        convertedGraph = new MultiDirectedGraph();
        Map<INode, Vertex> vertexMap = new HashMap<>();

        for(INode node : originalGraph.getNodes()){
            Vertex convertedVertex = new Vertex(node.getLabels().first().getText());
            vertexMap.put(node, convertedVertex);
            convV2OrigV.put(convertedVertex, node);
        }

        for(IEdge edge : originalGraph.getEdges()){
            INode source = edge.getSourceNode();
            INode target = edge.getTargetNode();
            DirectedEdge convertedEdge = convertedGraph.addEdge(vertexMap.get(source), vertexMap.get(target));
            convE2OrigE.put(convertedEdge, edge);
        }
    }



    public Map<Vertex, INode> getConvV2OrigV() {
        return convV2OrigV;
    }

    public Map<DirectedEdge, IEdge> getConvE2OrigE() {
        return convE2OrigE;
    }

    public MultiDirectedGraph getConvertedGraph() {
        return convertedGraph;
    }
}
