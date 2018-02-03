package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class CoordinatesHolder {

    private static final int xDifference = 50;
    private static final int yDifference = 50;

    private MultiDirectedGraph graph;
    private Map<Vertex, Integer> xCoordinates;
    private Map<Vertex, Integer> yCoordinates;

    public CoordinatesHolder(MultiDirectedGraph graph){
        this.graph = graph;
        this.xCoordinates = new HashMap<>();
        this.yCoordinates = new HashMap<>();
        calculateXCoordinates();
        calculateYCoordinates();
    }

    private void calculateXCoordinates(){

        List<Vertex> stOrdering = HolderProvider.getStOrderingHolder().getSTOrdering();
        List<Vertex> xOrdering = new ArrayList<>(stOrdering.size());
        xOrdering.add(stOrdering.get(0));

        for(int i = 1; i < stOrdering.size(); i++){
            Vertex vertex = stOrdering.get(i);
            int vertexIndex = getXIndex(vertex, stOrdering, xOrdering);
            xOrdering.add(vertexIndex, vertex);
        }

        for(Vertex vertex : xOrdering){
            System.out.println(vertex.getName());
        }

        int counter = 0;
        for(Vertex vertex : xOrdering){
            xCoordinates.put(vertex, xDifference * counter++);
        }
    }


    private int getXIndex(Vertex vertex, List<Vertex> stOrdering, List<Vertex> currentOrdering){

        Collection<DirectedEdge> incomingEdges = graph.getEdgesWithTarget(vertex);

        if(incomingEdges.size() == 1){
            Vertex source = incomingEdges.iterator().next().getSource();
            List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(source);
            int sourceIndex = currentOrdering.indexOf(source);

            for(DirectedEdge outgoingEdgeSource : outgoingEdgesSource){
                Vertex sourceSuccessor = outgoingEdgeSource.getTarget();
                if(vertex.equals(sourceSuccessor))
                    break;
                if(stOrdering.indexOf(vertex) < stOrdering.indexOf(sourceSuccessor))
                    return sourceIndex+1;
            }
            return sourceIndex;
        }else{

            Vertex highestSTOrderIndexSource1 = null;
            Vertex highestSTOrderIndexSource2 = null;
            for(DirectedEdge edge : incomingEdges){

                Vertex source = edge.getSource();
                if(highestSTOrderIndexSource1 == null){
                    highestSTOrderIndexSource1 = source;
                    continue;
                }
                if(highestSTOrderIndexSource2 == null){
                    highestSTOrderIndexSource2 = source;
                    continue;
                }

                int source1Index = stOrdering.indexOf(highestSTOrderIndexSource1);
                int source2Index = stOrdering.indexOf(highestSTOrderIndexSource2);
                if(source1Index < source2Index && source1Index < stOrdering.indexOf(source)) {
                    highestSTOrderIndexSource1 = source;
                }else if(source2Index < source1Index && source2Index < stOrdering.indexOf(source)) {
                    highestSTOrderIndexSource2 = source;
                }
            }
            return currentOrdering.indexOf(highestSTOrderIndexSource1) < currentOrdering.indexOf(highestSTOrderIndexSource2) ? currentOrdering.indexOf(highestSTOrderIndexSource1) + 1 : currentOrdering.indexOf(highestSTOrderIndexSource2) + 1;
        }
    }

    private void calculateYCoordinates(){
        List<Vertex> stOrdering = HolderProvider.getStOrderingHolder().getSTOrdering();
        int counter = 0;

        for(Vertex vertex : stOrdering)
            yCoordinates.put(vertex, yDifference * counter++);
    }




    public Map<Vertex, Integer> getXCoordinates() {
        return xCoordinates;
    }

    public Map<Vertex, Integer> getYCoordinates() {
        return yCoordinates;
    }
}
