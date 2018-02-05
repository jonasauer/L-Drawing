package main.java.algorithm.holder;

import com.yworks.yfiles.algorithms.*;
import main.java.printer.PrintColors;
import main.java.algorithm.graphConverter.GraphConverterHolder;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;

import java.util.*;

public class EmbeddingHolder {

    private Map<Vertex, Node> vertex2NodeMap;
    private Map<Edge, DirectedEdge> edge2DirectedEdge;

    private Map<Vertex, List<DirectedEdge>> outgoingEdgesCircularOrdering = new HashMap<>();
    private List<List<DirectedEdge>> convertedFaces = null;
    private List<DirectedEdge> outerFace = null;
    private PlanarEmbedding planarEmbedding;

    public EmbeddingHolder(MultiDirectedGraph graph){

        Graph convertedGraph = GraphConverterHolder.getMultiDirectedGraphToGraphConverter().getConvertedGraph();
        vertex2NodeMap = GraphConverterHolder.getMultiDirectedGraphToGraphConverter().getVertex2NodeMap();
        edge2DirectedEdge = GraphConverterHolder.getMultiDirectedGraphToGraphConverter().getEdge2DirectedEdgeMap();
        planarEmbedding = new PlanarEmbedding(convertedGraph);

        for(Vertex vertex : graph.getVertices()) {
            outgoingEdgesCircularOrdering.put(vertex, new LinkedList<>());
            calculateOrderedEdgesCircular(vertex);
        }
    }


    public void print(MultiDirectedGraph graph){
        System.out.println(PrintColors.ANSI_CYAN + "---------------------------");
        System.out.println(PrintColors.ANSI_CYAN + "Embedding");
        System.out.println(PrintColors.ANSI_CYAN + "    Outgoing:");
        for(Vertex vertex : graph.getVertices()) {
            System.out.print(PrintColors.ANSI_CYAN + "      " + vertex + ": ");
            for (DirectedEdge edge : outgoingEdgesCircularOrdering.get(vertex))
                System.out.print(PrintColors.ANSI_CYAN + edge + " ");
            System.out.println();
        }
    }



    private void calculateOrderedEdgesCircular(Vertex vertex){

        List<DirectedEdge> edgesCircular = new ArrayList<>();

        for(Dart dart : planarEmbedding.getOutgoingDarts(vertex2NodeMap.get(vertex)))
            edgesCircular.add(edge2DirectedEdge.get(dart.getAssociatedEdge()));

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

        //in case nothing has been added (there are not incoming AND outgoing edges)
        for(DirectedEdge edge : edgesCircular){
            if(!outgoingEdgesCircularOrdering.get(vertex).contains(edge) && edge.getSource().equals(vertex))
                outgoingEdgesCircularOrdering.get(vertex).add(edge);
        }
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
                convertedFace.add(edge2DirectedEdge.get(dart.getAssociatedEdge()));

            convertedFaces.add(convertedFace);
        }
        return convertedFaces;
    }





    public List<DirectedEdge> getOuterFace(){

        if(outerFace != null)
            return outerFace;

        List<Dart> outerFace = planarEmbedding.getOuterFace();
        this.outerFace = new LinkedList<>();
        for(Dart dart : outerFace){
            this.outerFace.add(edge2DirectedEdge.get(dart.getAssociatedEdge()));
        }
        return this.outerFace;
    }



    public List<List<DirectedEdge>> getFacesOfRNode(TCTreeNode<DirectedEdge, Vertex> tcTreeNode, MultiDirectedGraph skeleton){


        MultiDirectedGraph rPert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(tcTreeNode);

        //determine all faces of the pertinent graph
        List<List<Dart>> allFacesOfPert = new LinkedList<>();
        for(List<Dart> face : planarEmbedding.getFaces()){
            boolean isFaceOfPert = true;
            for(Dart dart : face){
                if(!rPert.contains(edge2DirectedEdge.get(dart.getAssociatedEdge())))
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
                DirectedEdge edge = edge2DirectedEdge.get(dart.getAssociatedEdge());
                if(!dart.isReversed()){
                    if(skeleton.getVertices().contains(edge.getSource()) && !realFaceVertices.contains(edge.getSource()))
                        realFaceVertices.add(edge.getSource());
                    if(skeleton.getVertices().contains(edge.getTarget()) && !realFaceVertices.contains(edge.getTarget()))
                        realFaceVertices.add(edge.getTarget());
                }else{
                    if(skeleton.getVertices().contains(edge.getTarget()) && !realFaceVertices.contains(edge.getTarget()))
                        realFaceVertices.add(edge.getTarget());
                    if(skeleton.getVertices().contains(edge.getSource()) && !realFaceVertices.contains(edge.getSource()))
                        realFaceVertices.add(edge.getSource());
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
























