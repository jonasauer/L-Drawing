package main.java.algorithm.holder;

import com.yworks.yfiles.algorithms.*;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCSkeleton;
import main.java.decomposition.spqrTree.TCTreeNode;

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

        for(Vertex vertex : graph.getVertices()) {
            outgoingEdgesCircularOrdering.put(vertex, new LinkedList<>());
            incomingEdgesCircularOrdering.put(vertex, new LinkedList<>());
            calcOrderedEdgesCircular(vertex);
        }

        System.out.println( "    Incoming:");
        for(Vertex vertex : graph.getVertices()) {
            System.out.print("      " + vertex + ": ");
            for (DirectedEdge edge : incomingEdgesCircularOrdering.get(vertex))
                System.out.print(edge + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("    Outgoing:");
        for(Vertex vertex : graph.getVertices()) {
            System.out.print("      " + vertex + ": ");
            for (DirectedEdge edge : outgoingEdgesCircularOrdering.get(vertex))
                System.out.print(edge + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }




    //TODO: ok
    private void calcOrderedEdgesCircular(Vertex vertex){

        List<DirectedEdge> edgesCircular = new ArrayList<>();

        for(Dart dart : planarEmbedding.getOutgoingDarts(origV2ConvV.get(vertex)))
            edgesCircular.add(convE2OrigE.get(dart.getAssociatedEdge()));

        //get all the edges with vertex as source.
        int index = 0;
        while(index < edgesCircular.size()){

            DirectedEdge edge1 = edgesCircular.get((index+0)%edgesCircular.size());
            DirectedEdge edge2 = edgesCircular.get((index+1)%edgesCircular.size());
            index++;
            if(edge1.getTarget().equals(vertex) && edge2.getSource().equals(vertex)) {

                List<DirectedEdge> orderedEdgesCircularOutgoing = outgoingEdgesCircularOrdering.get(vertex);
                for (int i = index; i < index + edgesCircular.size(); i++) {
                    DirectedEdge edge = edgesCircular.get(i % edgesCircular.size());
                    if(edge.getSource().equals(vertex))
                        orderedEdgesCircularOutgoing.add(edge);
                }
                break;
            }
        }

        //get all edges with vertex as target.
        index = 0;
        while(index < edgesCircular.size()){

            DirectedEdge edge1 = edgesCircular.get((index+0)%edgesCircular.size());
            DirectedEdge edge2 = edgesCircular.get((index+1)%edgesCircular.size());
            index++;
            if(edge1.getSource().equals(vertex) && edge2.getTarget().equals(vertex)) {

                List<DirectedEdge> orderedEdgesCircularIncoming = incomingEdgesCircularOrdering.get(vertex);
                for (int i = index; i < index + edgesCircular.size(); i++) {
                    DirectedEdge edge = edgesCircular.get(i % edgesCircular.size());
                    if(edge.getTarget().equals(vertex))
                        orderedEdgesCircularIncoming.add(edge);
                }
                break;
            }
        }

        //in case nothing has been added (there are not incoming AND outgoing edges)
        for(DirectedEdge edge : edgesCircular){
            if(!outgoingEdgesCircularOrdering.get(vertex).contains(edge) && edge.getSource().equals(vertex))
                outgoingEdgesCircularOrdering.get(vertex).add(edge);
            if(!incomingEdgesCircularOrdering.get(vertex).contains(edge) && edge.getTarget().equals(vertex))
                incomingEdgesCircularOrdering.get(vertex).add(edge);
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



    public List<List<DirectedEdge>> getFacesOfRNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode, MultiDirectedGraph skeleton){


        MultiDirectedGraph rPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(tcTreeNode);

        //determine all faces of the pertinent graph
        List<List<Dart>> allFacesOfPert = new LinkedList<>();
        for(List<Dart> face : planarEmbedding.getFaces()){
            boolean isFaceOfPert = true;
            for(Dart dart : face){
                if(!rPert.contains(convE2OrigE.get(dart.getAssociatedEdge())))
                    isFaceOfPert = false;
            }
            if(isFaceOfPert)
                allFacesOfPert.add(new LinkedList<>(face));
        }

        //determine if a face is also a face of the skeleton and if so, determine the relevant vertices.
        List<List<Vertex>> allFacesOfSkeleton = new LinkedList<>();
        for(List<Dart> face : allFacesOfPert){
            List<Vertex> realFaceVertices = new LinkedList<>();
            for(Dart dart : face){
                DirectedEdge edge = convE2OrigE.get(dart.getAssociatedEdge());
                if(!dart.isReversed()){
                    if(skeleton.getVertices().contains(edge.getTarget()) && !realFaceVertices.contains(edge.getTarget()))
                        realFaceVertices.add(edge.getTarget());
                    if(skeleton.getVertices().contains(edge.getSource()) && !realFaceVertices.contains(edge.getSource()))
                        realFaceVertices.add(edge.getSource());
                }else{
                    if(skeleton.getVertices().contains(edge.getSource()) && !realFaceVertices.contains(edge.getSource()))
                        realFaceVertices.add(edge.getSource());
                    if(skeleton.getVertices().contains(edge.getTarget()) && !realFaceVertices.contains(edge.getTarget()))
                        realFaceVertices.add(edge.getTarget());
                }
            }
            if(realFaceVertices.size() >= 3)
                allFacesOfSkeleton.add(realFaceVertices);
        }

        List<List<DirectedEdge>> skeletonFaces = new LinkedList<>();
        for(List<Vertex> faceVertices : allFacesOfSkeleton){
            List<DirectedEdge> face = new LinkedList<>();
            for(int i = 0; i < faceVertices.size(); i++){
                Vertex v1 = faceVertices.get((i+0)%faceVertices.size());
                Vertex v2 = faceVertices.get((i+1)%faceVertices.size());
                face.add(skeleton.getEdge(v1, v2));
            }
            skeletonFaces.add(face);
        }

        return skeletonFaces;
    }
}
























