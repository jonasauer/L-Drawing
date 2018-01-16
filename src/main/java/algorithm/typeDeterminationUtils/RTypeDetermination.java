package main.java.algorithm.typeDeterminationUtils;

import main.java.algorithm.holder.EmbeddingHolder;
import main.java.algorithm.holder.HolderProvider;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.*;

public class RTypeDetermination{

    private TCTree<DirectedEdge, Vertex> tcTree;
    private TCTreeNode<DirectedEdge, Vertex> tcTreeNode;
    private MultiDirectedGraph skeleton;
    private EmbeddingHolder embedding;

    private List<List<DirectedEdge>> faces;

    private Map<List<DirectedEdge>, Vertex> sourceToFacesMapping;
    private Map<List<DirectedEdge>, Vertex> targetToFacesMapping;

    private Map<List<DirectedEdge>, Vertex> leftVertex;
    private Map<List<DirectedEdge>, Vertex> rightVertex;

    private Map<List<DirectedEdge>, FaceType> faceTypes;

    private boolean isSkeletonEmbeddingMirrored;

    public void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

        this.tcTreeNode = tcTreeNode;
        this.tcTree = tcTree;
        this.skeleton = convertSkeletonToGraph();
        this.embedding = new EmbeddingHolder(skeleton);

        this.faces = embedding.getFaces();

        this.sourceToFacesMapping = new HashMap<>();
        this.targetToFacesMapping = new HashMap<>();

        this.leftVertex = new HashMap<>();
        this.rightVertex = new HashMap<>();

        this.faceTypes = new HashMap<>();

        this.isSkeletonEmbeddingMirrored = isSkeletonEmbeddingMirrored();


        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_R)) return;

        TCTreeNode<DirectedEdge, Vertex> optTypeBNode = null;
        SuccessorPathType successorPathType = SuccessorPathType.TYPE_M;

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){

            if(HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(child).equals(SuccessorPathType.TYPE_B)){
                if(optTypeBNode != null)
                    throw new RuntimeException("Type B is occurring twice in P-Node!");
                optTypeBNode = child;
                successorPathType = SuccessorPathType.TYPE_B;
            }
        }

        calcSourceAndTargetOfFaces();
        for(List<DirectedEdge> face : faces){
            Vertex faceTarget = targetToFacesMapping.get(face);
            Vertex left = leftVertex.get(face);
            if(faceTarget.equals(left))
                faceTypes.put(face, FaceType.TYPE_L);
            else
                faceTypes.put(face, FaceType.TYPE_R);
        }


    }

    private MultiDirectedGraph convertSkeletonToGraph(){

        MultiDirectedGraph skeletonGraph = new MultiDirectedGraph();
        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
            Vertex source = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(child);
            Vertex target = HolderProvider.getSourceSinkPertinentGraphsHolder().getSinkNodes().get(child);
            skeletonGraph.addEdge(source, target);
        }
        return skeletonGraph;
    }



    private void calcSourceAndTargetOfFaces(){

        for(List<DirectedEdge> face : faces){

            Vertex source = null;
            Vertex target = null;

            for(int i = 0; i < face.size(); i++){

                DirectedEdge first = face.get(i%face.size());
                DirectedEdge second = face.get((i+1)%face.size());
                if(first.getSource().equals(second.getSource())) {
                    source = first.getSource();
                    if(isSkeletonEmbeddingMirrored){
                        leftVertex.put(face, first.getTarget());
                        rightVertex.put(face, second.getTarget());
                    }else{
                        leftVertex.put(face, second.getTarget());
                        rightVertex.put(face, first.getTarget());
                    }
                }
                if(first.getTarget().equals(second.getTarget()))
                    target = first.getTarget();
            }
            sourceToFacesMapping.put(face, source);
            targetToFacesMapping.put(face, target);
        }
    }



    private boolean isSkeletonEmbeddingMirrored(){

        List<DirectedEdge> observedFace = null;
        for(List<DirectedEdge> face : faces){
            if(face.size() == 3){
                observedFace = face;
                break;
            }
        }

        List<Vertex> observedFaceVertices = new LinkedList<>();
        for(DirectedEdge edge : observedFace){
            observedFaceVertices.add(edge.getSource());
        }

        List<Vertex> equalVertices = null;
        for(List<DirectedEdge> face : HolderProvider.getEmbeddingHolder().getFaces()){
            equalVertices = new LinkedList<>();
            for(DirectedEdge edge : face){
                if(observedFaceVertices.contains(edge.getSource()))
                    equalVertices.add(edge.getSource());
            }
            if(equalVertices.size() == 3)
                break;

        }

        Vertex first = observedFaceVertices.get(0);
        Vertex second = observedFaceVertices.get(1);
        for(int i = 0; i < 3; i++){
            if(first.equals(equalVertices.get(i))){
                return !second.equals(equalVertices.get(i%3));
            }
        }
        throw new RuntimeException("Could not determine if the face is mirrored!");
    }










    private enum FaceType{
        TYPE_L,
        TYPE_R
    }
}
