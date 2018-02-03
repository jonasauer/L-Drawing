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

        List<Vertex> stOrdering = HolderProvider.getStOrderingHolder().getSTOrderingList();
        List<Vertex> xOrdering = new ArrayList<>(stOrdering.size());
        xOrdering.add(stOrdering.get(0)); //add s' because we know it has to be the first vertex.
        xOrdering.add(stOrdering.get(1)); //add the real source because it has to be the second vertex.

        for(int i = 2; i < stOrdering.size(); i++){
            Vertex vertex = stOrdering.get(i);
            xOrdering.add(getXIndex(vertex, xOrdering), vertex);
        }

        for(int i = 0; i < xOrdering.size(); i++){
            xCoordinates.put(xOrdering.get(i), xDifference * i);
        }
    }


    private int getXIndex(Vertex vertex, List<Vertex> currentOrdering){

        Collection<DirectedEdge> incomingEdges = graph.getEdgesWithTarget(vertex);
        Map<Vertex, Integer> stOrderingMap = HolderProvider.getStOrderingHolder().getSTOrderingMap();

        if(incomingEdges.size() < 2){
            Vertex source = incomingEdges.iterator().next().getSource();
            List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(source);
            int sourceIndex = currentOrdering.indexOf(source);

            for(DirectedEdge outgoingEdgeSource : outgoingEdgesSource){
                Vertex sourceSuccessor = outgoingEdgeSource.getTarget();
                if(stOrderingMap.get(vertex) < stOrderingMap.get(sourceSuccessor))
                    return sourceIndex+1;
            }
            return sourceIndex;
        }else{
            Iterator<DirectedEdge> edgeIterator = incomingEdges.iterator();
            Vertex highestSTOrderIndexSource1 = edgeIterator.next().getSource();
            Vertex highestSTOrderIndexSource2 = edgeIterator.next().getSource();

            return currentOrdering.indexOf(highestSTOrderIndexSource1) < currentOrdering.indexOf(highestSTOrderIndexSource2) ? currentOrdering.indexOf(highestSTOrderIndexSource1) + 1 : currentOrdering.indexOf(highestSTOrderIndexSource2) + 1;
        }
    }

    private void calculateYCoordinates(){

        List<Vertex> stOrdering = HolderProvider.getStOrderingHolder().getSTOrderingList();
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
