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
    private MultiDirectedGraph augmentedGraph;
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
        this.augmentedGraph = HolderProvider.getAugmentationHolder().getAugmentedGraph();
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
            if(faceTarget.equals(left)) {
                faceTypes.put(face, FaceType.TYPE_L);
            }else{
                faceTypes.put(face, FaceType.TYPE_R);
            }
        }

        for(Vertex vertex : skeleton.getVertices()){
            connectVertices(vertex);
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




    private void flipNodeInEmbedding(TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(tcTreeNode);
        Vertex pertSource = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(tcTreeNode);
        Vertex pertTarget = HolderProvider.getSourceSinkPertinentGraphsHolder().getSinkNodes().get(tcTreeNode);

        List<Vertex> successorVertices = new LinkedList<>();
        for(DirectedEdge edge : pert.getEdgesWithSource(pertSource))
            successorVertices.add(edge.getTarget());
        List<Vertex> predecessorVertices = new LinkedList<>();
        for(DirectedEdge edge : pert.getEdgesWithTarget(pertTarget)){
            predecessorVertices.add(edge.getSource());
        }

        //get the corresponding edges
        List<DirectedEdge> successorEdges = new LinkedList<>();
        for(DirectedEdge edge : augmentedGraph.getEdgesWithTargets(successorVertices)){
            if(edge.getSource().equals(pertSource)) successorEdges.add(edge);
        }
        List<DirectedEdge> predecessorEdges = new LinkedList<>();
        for(DirectedEdge edge : augmentedGraph.getEdgesWithSources(predecessorVertices)){
            if(edge.getTarget().equals(pertTarget)) predecessorEdges.add(edge);
        }

        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(pertSource);
        List<DirectedEdge> incomingEdgesTarget = HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(pertTarget);

        //switch edges in embedding of sourceNode of the pertinent graph
        if(successorEdges.size() > 1){
            int insertIndex = 0;
            for(DirectedEdge outgoingEdge : outgoingEdgesSource){
                if(successorEdges.contains(outgoingEdge)){
                    outgoingEdgesSource.removeAll(successorEdges);
                    break;
                }
                insertIndex++;
            }

            for(int i = successorEdges.size()-1; i >=0; i--){
                outgoingEdgesSource.add(insertIndex++, successorEdges.get(i));
            }
        }

        //switch edges in embedding of targetNode of the pertinent graph
        if(predecessorEdges.size() > 1){
            int insertIndex = 0;
            for(DirectedEdge incomingEdge : incomingEdgesTarget){
                if(predecessorEdges.contains(incomingEdge)){
                    incomingEdgesTarget.removeAll(predecessorEdges);
                    break;
                }
                insertIndex++;
            }

            for(int i = predecessorEdges.size()-1; i >=0; i--){
                incomingEdgesTarget.add(insertIndex++, predecessorEdges.get(i));
            }
        }

        //switch all other edges in the embedding.
        Collection<Vertex> vertices = pert.getVertices();
        vertices.remove(pertSource);
        vertices.remove(pertTarget);

        for(Vertex vertex : vertices){

            List<DirectedEdge> outgoingEdges = new LinkedList<>(HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex));
            HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex).clear();
            for(int i = outgoingEdges.size()-1; i >= 0; i--){
                HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex).add(outgoingEdges.get(i));
            }

            List<DirectedEdge> incomingEdges = new LinkedList<>(HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(vertex));
            HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(vertex).clear();
            for(int i = incomingEdges.size()-1; i >= 0; i--){
                HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(vertex).add(incomingEdges.get(i));
            }
        }
    }




    private void connectVertices(Vertex vertex){
    }





    private enum FaceType{
        TYPE_L,
        TYPE_R
    }
}
