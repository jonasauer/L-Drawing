package main.java.algorithm.graphConverter;

import com.yworks.yfiles.algorithms.Edge;
import com.yworks.yfiles.algorithms.Graph;
import com.yworks.yfiles.algorithms.Node;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class MultiDirectedGraphToGraphConverter {

    private Map<Vertex, Node> vertex2NodeMap;
    private Map<Edge, DirectedEdge> edge2DirectedEdgeMap;
    private MultiDirectedGraph graph;
    private Graph convertedGraph;

    public MultiDirectedGraphToGraphConverter(MultiDirectedGraph graph){
        this.graph = graph;
        this.vertex2NodeMap = new HashMap<>();
        this.edge2DirectedEdgeMap = new HashMap<>();
        convert();
    }

    private void convert(){

        convertedGraph = new Graph();
        for(DirectedEdge origEdge : graph.getEdges()){

            Vertex origSource = origEdge.getSource();
            Vertex origTarget = origEdge.getTarget();

            if(!vertex2NodeMap.containsKey(origSource)){
                Node convertedSource = convertedGraph.createNode();
                vertex2NodeMap.put(origSource, convertedSource);
            }

            if(!vertex2NodeMap.containsKey(origTarget)){
                Node convertedTarget = convertedGraph.createNode();
                vertex2NodeMap.put(origTarget, convertedTarget);
            }

            Node convertedSource = vertex2NodeMap.get(origSource);
            Node convertedTarget = vertex2NodeMap.get(origTarget);

            Edge convertedEdge = convertedGraph.createEdge(convertedSource, convertedTarget);
            edge2DirectedEdgeMap.put(convertedEdge, origEdge);
        }
    }




    public Map<Vertex, Node> getVertex2NodeMap() {
        return vertex2NodeMap;
    }

    public Map<Edge, DirectedEdge> getEdge2DirectedEdgeMap() {
        return edge2DirectedEdgeMap;
    }

    public Graph getConvertedGraph() {
        return convertedGraph;
    }
}
