package main.java.algorithm.typeDetermination;

import main.java.algorithm.types.FaceType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.ArrayList;

public class Face extends ArrayList<DirectedEdge>{

    private Vertex source;
    private Vertex target;
    private DirectedEdge lEdge;
    private DirectedEdge rEdge;
    private FaceType faceType;

    public Face(int size){
        super(size);
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

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public Vertex getLVertex(){
        return lEdge.getTarget();
    }

    public Vertex getRVertex(){
        return rEdge.getTarget();
    }

    public FaceType getFaceType() {
        return faceType;
    }

    public void setFaceType(FaceType faceType) {
        this.faceType = faceType;
    }

    public void print(){
        for(DirectedEdge edge : this){
            System.out.print(edge + " ");
        }
        System.out.println();
    }
}