package main.java.algorithm.graphConverter;

import com.yworks.yfiles.graph.*;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class IGraphToMultiDirectedGraphConverter {

    private Map<Vertex, INode> vertex2INode;
    private Map<DirectedEdge, IEdge> directedEdge2IEdge;
    private IGraph graph;
    private MultiDirectedGraph convertedGraph;



    public IGraphToMultiDirectedGraphConverter(IGraph graph){
        this.graph = graph;
        this.vertex2INode = new HashMap<>();
        this.directedEdge2IEdge = new HashMap<>();
        convert();
    }



    public void convert(){

        convertedGraph = new MultiDirectedGraph();
        Map<INode, Vertex> vertexMap = new HashMap<>();

        for(INode node : graph.getNodes()){
            Vertex convertedVertex = new Vertex(node.getLabels().first().getText());
            vertexMap.put(node, convertedVertex);
            vertex2INode.put(convertedVertex, node);
        }

        for(IEdge edge : graph.getEdges()){
            INode source = edge.getSourceNode();
            INode target = edge.getTargetNode();
            DirectedEdge convertedEdge = convertedGraph.addEdge(vertexMap.get(source), vertexMap.get(target));
            directedEdge2IEdge.put(convertedEdge, edge);
        }
    }



    public Map<Vertex, INode> getVertex2INode() {
        return vertex2INode;
    }

    public Map<DirectedEdge, IEdge> getDirectedEdge2IEdge() {
        return directedEdge2IEdge;
    }

    public MultiDirectedGraph getConvertedGraph() {
        return convertedGraph;
    }
}
