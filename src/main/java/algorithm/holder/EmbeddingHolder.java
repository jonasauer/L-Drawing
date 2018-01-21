package main.java.algorithm.holder;

import com.yworks.yfiles.algorithms.*;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class EmbeddingHolder {

    private Map<Vertex, Node>       origV2ConvV = new HashMap<>();
    private Map<Edge, DirectedEdge> convE2OrigE = new HashMap<>();

    private Map<Vertex, List<DirectedEdge>> incomingEdgesCircularOrdering = new HashMap<>();
    private Map<Vertex, List<DirectedEdge>> outgoingEdgesCircularOrdering = new HashMap<>();
    private List<List<DirectedEdge>> convertedFaces = null;
    private List<DirectedEdge> outerFace = null;
    private PlanarEmbedding planarEmbedding;

    public EmbeddingHolder(MultiDirectedGraph graph){

        Graph convGraph = new Graph();

        for(DirectedEdge origEdge : graph.getEdges()){

            Vertex origSource = origEdge.getSource();
            Vertex origTarget = origEdge.getTarget();

            if(!origV2ConvV.containsKey(origSource)){
                Node convSource = convGraph.createNode();
                origV2ConvV.put(origSource, convSource);
            }

            if(!origV2ConvV.containsKey(origTarget)){
                Node convTarget = convGraph.createNode();
                origV2ConvV.put(origTarget, convTarget);
            }

            Node convSource = origV2ConvV.get(origSource);
            Node convTarget = origV2ConvV.get(origTarget);

            Edge convEdge = convGraph.createEdge(convSource, convTarget);
            convE2OrigE.put(convEdge, origEdge);
        }

        planarEmbedding = new PlanarEmbedding(convGraph);

        for(Vertex vertex : graph.getVertices())
            calcOrderedEdgesCircular(vertex);
    }





    private void calcOrderedEdgesCircular(Vertex vertex){

        List<DirectedEdge> edgesCircular = new ArrayList<>();

        for(Dart dart : planarEmbedding.getOutgoingDarts(origV2ConvV.get(vertex)))
            edgesCircular.add(convE2OrigE.get(dart.getAssociatedEdge()));

        int index = 0;
        while(index < edgesCircular.size()){

            DirectedEdge edge1 = edgesCircular.get((index+0)%edgesCircular.size());
            DirectedEdge edge2 = edgesCircular.get((index+1)%edgesCircular.size());
            index++;
            if(edge1.getTarget().equals(vertex) && edge2.getSource().equals(vertex)) {

                List<DirectedEdge> orderedEdgesCircularOutgoing = new LinkedList<>();
                for (int i = index; i < index + edgesCircular.size(); i++) {
                    DirectedEdge edge = edgesCircular.get(i % edgesCircular.size());
                    if(edge.getSource().equals(vertex))
                        orderedEdgesCircularOutgoing.add(edge);
                }
                outgoingEdgesCircularOrdering.put(vertex, orderedEdgesCircularOutgoing);
                break;
            }
        }

        index = 0;
        while(index < edgesCircular.size()){

            DirectedEdge edge1 = edgesCircular.get((index+0)%edgesCircular.size());
            DirectedEdge edge2 = edgesCircular.get((index+1)%edgesCircular.size());
            index++;
            if(edge1.getSource().equals(vertex) && edge2.getTarget().equals(vertex)) {

                List<DirectedEdge> orderedEdgesCircularIncoming = new LinkedList<>();
                for (int i = index; i < index + edgesCircular.size(); i++) {
                    DirectedEdge edge = edgesCircular.get(i % edgesCircular.size());
                    if(edge.getTarget().equals(vertex))
                        orderedEdgesCircularIncoming.add(edge);
                }
                incomingEdgesCircularOrdering.put(vertex, orderedEdgesCircularIncoming);
                break;
            }
        }
    }





    public List<DirectedEdge> getIncomingEdgesCircularOrdering(Vertex vertex){
        return incomingEdgesCircularOrdering.get(vertex);
    }





    public List<DirectedEdge> getOutgoingEdgesCircularOrdering(Vertex vertex){
        return outgoingEdgesCircularOrdering.get(vertex);
    }





    public List<List<DirectedEdge>> getFaces(){

        if(convertedFaces != null)
            return convertedFaces;

        convertedFaces = new LinkedList<>();
        for(List<Dart> face : planarEmbedding.getFaces()){

            if(face.equals(planarEmbedding.getOuterFace()))
                continue;

            List<DirectedEdge> convertedFace = new LinkedList<>();
            for(Dart dart : face)
                convertedFace.add(convE2OrigE.get(dart.getAssociatedEdge()));

            convertedFaces.add(convertedFace);
        }
        return convertedFaces;
    }





    public List<DirectedEdge> getOuterFace(){

        if(outerFace != null)
            return outerFace;

        List<Dart> of = planarEmbedding.getOuterFace();
        outerFace = new LinkedList<>();
        for(Dart dart : of){
            outerFace.add(convE2OrigE.get(dart.getAssociatedEdge()));
        }
        return outerFace;
    }
}
























