package main.java.algorithm.embedding;

import com.yworks.yfiles.algorithms.*;
import main.java.algorithm.typeDetermination.Face;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RNodeEmbedding {

    private MultiDirectedGraph originalGraph;
    private Graph convertedGraph;

    private Map<Vertex, Node> origV2ConvV = new HashMap<>();
    private Map<Edge, DirectedEdge> convE2OrigE = new HashMap<>();
    private Map<Vertex, List<DirectedEdge>> outgoingEdges = new HashMap<>();
    private Map<Vertex, List<DirectedEdge>> incomingEdges = new HashMap<>();
    private PlanarEmbedding planarEmbedding;

    private List<Face> convertedFaces;
    private Vertex source;
    private Vertex target;
    private Vertex augmentedSource;

    public RNodeEmbedding(MultiDirectedGraph graph, Vertex augmentedSource, Vertex source, Vertex target){

        this.originalGraph = graph;
        convertGraph();
        this.planarEmbedding = new PlanarEmbedding(convertedGraph);
        calculateOrderedEdges();
        this.source = source;
        this.target = target;
        this.augmentedSource = augmentedSource;
    }


    private void convertGraph(){
        convertedGraph = new Graph();
        for(DirectedEdge origEdge : originalGraph.getEdges()){
            Vertex origSource = origEdge.getSource();
            Vertex origTarget = origEdge.getTarget();
            if(!origV2ConvV.containsKey(origSource)){
                Node convertedSource = convertedGraph.createNode();
                origV2ConvV.put(origSource, convertedSource);
            }
            if(!origV2ConvV.containsKey(origTarget)){
                Node convertedTarget = convertedGraph.createNode();
                origV2ConvV.put(origTarget, convertedTarget);
            }
            Node convertedSource = origV2ConvV.get(origSource);
            Node convertedTarget = origV2ConvV.get(origTarget);
            Edge convertedEdge = convertedGraph.createEdge(convertedSource, convertedTarget);
            convE2OrigE.put(convertedEdge, origEdge);
        }
    }



    private void calculateOrderedEdges(){

        for(Vertex vertex : originalGraph.getVertices()) {
            outgoingEdges.put(vertex, new ArrayList<>());
            incomingEdges.put(vertex, new ArrayList<>());

            List<DirectedEdge> edges = new ArrayList<>();
            for (Dart dart : planarEmbedding.getOutgoingDarts(origV2ConvV.get(vertex)))
                edges.add(convE2OrigE.get(dart.getAssociatedEdge()));

            int start = 0;
            for (; start < edges.size(); start++) {
                DirectedEdge edge1 = edges.get((start) % edges.size());
                DirectedEdge edge2 = edges.get((start + 1) % edges.size());
                if (edge1.getTarget().equals(vertex) && edge2.getSource().equals(vertex))
                    break;
            }

            //outgoing edges
            List<DirectedEdge> outgoingEdges = this.outgoingEdges.get(vertex);
            for (int i = start; i < start + edges.size(); i++) {
                DirectedEdge edge = edges.get(i % edges.size());
                if (edge.getSource().equals(vertex))
                    outgoingEdges.add(edge);
            }

            //incoming edges
            List<DirectedEdge> incomingEdges = this.incomingEdges.get(vertex);
            for(int i = start; i < start + edges.size(); i++){
                DirectedEdge edge = edges.get(i % edges.size());
                if(edge.getTarget().equals(vertex))
                    incomingEdges.add(edge);
            }
        }
    }


    public List<DirectedEdge> getOutgoingEdges(Vertex vertex){
        return outgoingEdges.get(vertex);
    }

    public List<DirectedEdge> getIncomingEdges(Vertex vertex){
        return incomingEdges.get(vertex);
    }


    public List<Face> getFaces(){

        if(convertedFaces != null)
            return convertedFaces;

        convertedFaces = new ArrayList<>();

        for(List<Dart> originalFace : planarEmbedding.getFaces()){
            boolean containsSource = false;
            boolean containsTarget = false;
            boolean containsAugmentedSource = false;
            Face convertedFace = new Face(originalFace.size());

            for(Dart dart : originalFace) {
                DirectedEdge edge = convE2OrigE.get(dart.getAssociatedEdge());
                if(edge.getSource().equals(source) || edge.getTarget().equals(source))
                    containsSource = true;
                if(edge.getSource().equals(target) || edge.getTarget().equals(target))
                    containsTarget = true;
                if(edge.getSource().equals(augmentedSource))
                    containsAugmentedSource = true;
                convertedFace.add(edge);
            }
            if((!containsSource || !containsTarget) && !containsAugmentedSource)
                convertedFaces.add(convertedFace);
        }
        return convertedFaces;
    }
}
