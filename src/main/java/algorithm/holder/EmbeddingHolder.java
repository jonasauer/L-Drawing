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

        Set<Vertex> pertVertexSet = HolderProvider.getPertinentGraphHolder().getPertinentGraph(tcTreeNode).vertexSet();
        Set<Vertex> skeletonVertexSet = skeleton.vertexSet();
        List<List<Vertex>> verticesOfFaces = new ArrayList<>();

        for(List<Dart> face : planarEmbedding.getFaces()){
            List<Vertex> verticesOfFaceList = new ArrayList<>();
            Set<Vertex> verticesOfFaceSet = new HashSet<>();
            boolean faceIsOutsideOfPert = false;

            for(Dart dart : face){
                Vertex source = edge2DirectedEdge.get(dart.getAssociatedEdge()).getSource();
                Vertex target = edge2DirectedEdge.get(dart.getAssociatedEdge()).getTarget();
                if(!pertVertexSet.contains(source) || !pertVertexSet.contains(target)){
                    faceIsOutsideOfPert = true;
                    break;
                }
                if (!dart.isReversed() && skeletonVertexSet.contains(source) && !verticesOfFaceSet.contains(source)) {
                    verticesOfFaceList.add(source);
                    verticesOfFaceSet.add(source);
                }
                if (!dart.isReversed() && skeletonVertexSet.contains(target) && !verticesOfFaceSet.contains(target)) {
                    verticesOfFaceList.add(target);
                    verticesOfFaceSet.add(target);
                }
                if (dart.isReversed() && skeletonVertexSet.contains(target) && !verticesOfFaceSet.contains(target)) {
                    verticesOfFaceList.add(target);
                    verticesOfFaceSet.add(target);
                }
                if (dart.isReversed() && skeletonVertexSet.contains(source) && !verticesOfFaceSet.contains(source)) {
                    verticesOfFaceList.add(source);
                    verticesOfFaceSet.add(source);
                }
            }
            if(verticesOfFaceList.size() >= 3 && !faceIsOutsideOfPert)
                verticesOfFaces.add(verticesOfFaceList);
        }


        List<List<DirectedEdge>> faces = new ArrayList<>();

        for(List<Vertex> verticesOfFace : verticesOfFaces){
            List<DirectedEdge> face = new ArrayList<>();
            for(int i = 0; i < verticesOfFace.size(); i++){
                Vertex v1 = verticesOfFace.get((i  )%verticesOfFace.size());
                Vertex v2 = verticesOfFace.get((i+1)%verticesOfFace.size());
                face.add(skeleton.getEdge(v1, v2));
            }
            faces.add(face);
        }
        return faces;
    }
}
























