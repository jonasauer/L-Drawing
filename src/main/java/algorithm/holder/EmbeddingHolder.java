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
    private PlanarEmbedding planarEmbedding;

    public EmbeddingHolder(MultiDirectedGraph graph){

        Graph convertedGraph = GraphConverterHolder.getMultiDirectedGraphToGraphConverter().getConvertedGraph();
        vertex2NodeMap = GraphConverterHolder.getMultiDirectedGraphToGraphConverter().getVertex2NodeMap();
        edge2DirectedEdge = GraphConverterHolder.getMultiDirectedGraphToGraphConverter().getEdge2DirectedEdgeMap();
        planarEmbedding = new PlanarEmbedding(convertedGraph);

        for(Vertex vertex : graph.getVertices()) {
            outgoingEdgesCircularOrdering.put(vertex, new LinkedList<>()); //has to be a linked list because inserting at different positions is necessary.
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
        int start = 0;
        for(; start < edgesCircular.size(); start++){

            DirectedEdge edge1 = edgesCircular.get((start  )%edgesCircular.size());
            DirectedEdge edge2 = edgesCircular.get((start+1)%edgesCircular.size());

            if(edge1.getTarget().equals(vertex) && edge2.getSource().equals(vertex)) {
                break;
            }
        }

        List<DirectedEdge> orderedEdgesCircularOutgoing = outgoingEdgesCircularOrdering.get(vertex);
        for (int i = start; i < start + edgesCircular.size(); i++) {
            DirectedEdge edge = edgesCircular.get(i % edgesCircular.size());
            if(edge.getSource().equals(vertex))
                orderedEdgesCircularOutgoing.add(edge);
        }
    }





    public List<DirectedEdge> getOutgoingEdgesCircularOrdering(Vertex vertex){
        return outgoingEdgesCircularOrdering.get(vertex);
    }





    public List<List<DirectedEdge>> getFacesOfRSkeleton(MultiDirectedGraph skeleton, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

        MultiDirectedGraph rPert = HolderProvider.getPertinentGraphHolder().getPertinentGraph(tcTreeNode);

        //determine all faces of the pertinent graph
        Set<List<Dart>> facesOfPert = new HashSet<>();
        for(List<Dart> face : planarEmbedding.getFaces()){
            facesOfPert.add(face);
            for(Dart dart : face){
                if(!rPert.contains(edge2DirectedEdge.get(dart.getAssociatedEdge()))) {//TODO: make hashcode and do that with hashset
                    facesOfPert.remove(face);
                    break;
                }
            }
        }

        //determine if a face is also a face of the skeleton and if so, determine the relevant vertices.
        Set<Vertex> skeletonVertexSet = skeleton.vertexSet();
        List<List<Vertex>> relevantVerticesOfFaces = new ArrayList<>();
        for(List<Dart> face : facesOfPert){
            List<Vertex> relevantVerticesOfFaceList = new ArrayList<>();
            Set<Vertex> relevantVerticesOfFaceSet = new HashSet<>();
            for(Dart dart : face){
                Vertex source = edge2DirectedEdge.get(dart.getAssociatedEdge()).getSource();
                Vertex target = edge2DirectedEdge.get(dart.getAssociatedEdge()).getTarget();
                if(!dart.isReversed()){
                    if(skeletonVertexSet.contains(source) && !relevantVerticesOfFaceSet.contains(source)) {
                        relevantVerticesOfFaceList.add(source);
                        relevantVerticesOfFaceSet.add(source);
                    }
                    if(skeletonVertexSet.contains(target) && !relevantVerticesOfFaceSet.contains(target)) {
                        relevantVerticesOfFaceList.add(target);
                        relevantVerticesOfFaceSet.add(target);
                    }
                }else{
                    if(skeletonVertexSet.contains(target) && !relevantVerticesOfFaceSet.contains(target)) {
                        relevantVerticesOfFaceList.add(target);
                        relevantVerticesOfFaceSet.add(target);
                    }
                    if(skeletonVertexSet.contains(source) && !relevantVerticesOfFaceSet.contains(source)) {
                        relevantVerticesOfFaceList.add(source);
                        relevantVerticesOfFaceSet.add(source);
                    }
                }
            }
            if(relevantVerticesOfFaceList.size() >= 3)
                relevantVerticesOfFaces.add(relevantVerticesOfFaceList);
        }

        List<List<DirectedEdge>> skeletonFaces = new ArrayList<>();
        for(List<Vertex> faceVertices : relevantVerticesOfFaces){
            List<DirectedEdge> face = new ArrayList<>();
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
























