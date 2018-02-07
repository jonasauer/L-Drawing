package main.java.algorithm.successorPathTypeDetermination;

import main.java.decomposition.graph.DirectedEdge;

import java.util.ArrayList;
import java.util.List;

public class Face {

    private DirectedEdge lEdge;
    private DirectedEdge rEdge;
    private List<DirectedEdge> edges;

    public Face(List<DirectedEdge> edges){
        this.edges = edges;
    }

    public Face(){
        this.edges = new ArrayList<>();
    }

    public void addEdge(DirectedEdge edge){
        this.edges.add(edge);
    }

    public int size(){
        return this.edges.size();
    }

    public DirectedEdge get(int index){
        return this.edges.get(index);
    }

    public void setLEdge(DirectedEdge lEdge){
        this.lEdge = lEdge;
    }

    public void setREdge(DirectedEdge rEdge) {
        this.rEdge = rEdge;
    }

    public DirectedEdge getLEdge() {
        return lEdge;
    }

    public DirectedEdge getREdge() {
        return rEdge;
    }

    public List<DirectedEdge> getEdges() {
        return edges;
    }
}
