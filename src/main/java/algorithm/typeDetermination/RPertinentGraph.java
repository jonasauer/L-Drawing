package main.java.algorithm.typeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.embedding.RNodeEmbedding;
import main.java.algorithm.types.FaceType;
import main.java.algorithm.types.SuccessorPathType;
import main.java.algorithm.utils.Augmentation;
import main.java.algorithm.utils.PrintColors;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RPertinentGraph extends AbstractPertinentGraph{

    private static final Logger LOGGER = LoggerFactory.getLogger(RPertinentGraph.class);

    private MultiDirectedGraph convertedSkeleton;
    private RNodeEmbedding embedding;
    private Map<DirectedEdge, AbstractPertinentGraph> virtualEdges2PertinentGraphs;
    private Map<Vertex, List<Face>> outgoingFacesOfVertices;
    private Map<DirectedEdge, Face> lFaceOfEdge;
    private Map<DirectedEdge, Face> rFaceOfEdge;
    private Map<Vertex, DirectedEdge> apexOfVertices;


    public RPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {
        super(tcTreeNode);
    }




    @Override
    void construct() throws LDrawingNotPossibleException {

        virtualEdges2PertinentGraphs = new HashMap<>();
        outgoingFacesOfVertices = new HashMap<>();
        lFaceOfEdge = new HashMap<>();
        rFaceOfEdge = new HashMap<>();
        apexOfVertices = new HashMap<>();

        setTcTreeNodeType(TCTreeNodeType.TYPE_R);
        setSuccessorPathType(SuccessorPathType.TYPE_M);
        convertSkeleton();
        Set<Vertex> sourceNodes = convertedSkeleton.vertexSet();
        Set<Vertex> targetNodes = convertedSkeleton.vertexSet();
        for(DirectedEdge edge : convertedSkeleton.getEdges()){
            sourceNodes.remove(edge.getTarget());
            targetNodes.remove(edge.getSource());
        }
        Vertex source = sourceNodes.iterator().next();
        Vertex target = targetNodes.iterator().next();
        setSource(source);
        setTarget(target);


        Vertex augmentedSource = new Vertex("s'");
        convertedSkeleton.addVertex(augmentedSource);
        DirectedEdge augmentedEdge1 = convertedSkeleton.addEdge(augmentedSource, source);
        DirectedEdge augmentedEdge2 = convertedSkeleton.addEdge(augmentedSource, target);
        embedding = new RNodeEmbedding(convertedSkeleton, augmentedSource, source, target);
        convertedSkeleton.removeEdge(augmentedEdge2);
        convertedSkeleton.removeEdge(augmentedEdge1);
        convertedSkeleton.removeVertex(augmentedSource);

        calculateFaceInformation();
        orderOutgoingFaces();
        calculateApexOfVertices();
        checkIfApexAfterRFaces();
        checkIfApexBeforeLFaces();
        checkIfRFacesBeforeLFaces();

        List<DirectedEdge> outgoingEdgesOfSource = embedding.getOutgoingEdges(source);

        Face firstFace = outgoingFacesOfVertices.get(source).get(0);
        AbstractPertinentGraph lPert = virtualEdges2PertinentGraphs.get(outgoingEdgesOfSource.get(0));
        AbstractPertinentGraph rPert = virtualEdges2PertinentGraphs.get(outgoingEdgesOfSource.get(outgoingEdgesOfSource.size()-1));

        if(firstFace.getFaceType() == FaceType.TYPE_L){
            setLeftmostVertex(rPert.getRightmostVertex());
            setRightmostVertex(lPert.getLeftmostVertex());
        }else{
            setLeftmostVertex(lPert.getLeftmostVertex());
            setRightmostVertex(rPert.getRightmostVertex());
        }


        LOGGER.debug(PrintColors.ANSI_GREEN + "-----------------------");
        LOGGER.debug(PrintColors.ANSI_GREEN + "    R-Node with source: " + getSource());
        LOGGER.debug(PrintColors.ANSI_GREEN + "      Skeleton: " + getTcTreeNode().getSkeleton());
        augmentGraph();
        LOGGER.debug(PrintColors.ANSI_GREEN + "      " + getSuccessorPathType());
    }




    private void convertSkeleton(){

        convertedSkeleton = new MultiDirectedGraph();
        for(TCTreeNode<DirectedEdge, Vertex> childNode : tcTree.getChildren(getTcTreeNode())){
            AbstractPertinentGraph childPert = pertinentGraphsOfTCTreeNodes.get(childNode);
            Vertex source = childPert.getSource();
            Vertex target = childPert.getTarget();
            DirectedEdge virtualEdge = convertedSkeleton.addEdge(source, target);
            virtualEdges2PertinentGraphs.put(virtualEdge, childPert);
        }
    }


    private void calculateFaceInformation(){

        for(Vertex vertex : convertedSkeleton.getVertices()){
            outgoingFacesOfVertices.put(vertex, new ArrayList<>());
        }

        for(Face face : embedding.getFaces()){
            Vertex source = null;
            Vertex target = null;

            for(int i = 0; i < face.size(); i++){
                DirectedEdge edge1 = face.get((i  )%face.size());
                DirectedEdge edge2 = face.get((i+1)%face.size());
                if(edge1.getSource() == edge2.getSource()) {
                    source = edge1.getSource();
                    face.setSource(source);
                    face.setLEdge(edge1);
                    face.setREdge(edge2);
                    lFaceOfEdge.put(edge2, face);
                    rFaceOfEdge.put(edge1, face);
                }
                if(edge1.getTarget() == edge2.getTarget()){
                    target = edge1.getTarget();
                    face.setTarget(target);
                }
            }
            assignLabelsToFace(face);
            outgoingFacesOfVertices.get(source).add(face);
        }
    }

    private void assignLabelsToFace(Face face){

        Vertex lVertex = face.getLVertex();
        Vertex rVertex = face.getRVertex();
        Vertex target = face.getTarget();

        if (target == lVertex) {
            face.setFaceType(FaceType.TYPE_L);
        } else if (target == rVertex) {
            face.setFaceType(FaceType.TYPE_R);
        } else {
            face.setFaceType(FaceType.UNDEFINED);
        }
    }



    private void orderOutgoingFaces(){

        for(Vertex vertex : convertedSkeleton.getVertices()) {

            List<Face> outgoingFacesList = outgoingFacesOfVertices.get(vertex);
            Set<Face> outgoingFacesSet = new HashSet<>(outgoingFacesList);
            DirectedEdge lEdge = null;
            DirectedEdge rEdge = null;

            if (outgoingFacesList.size() < 2)
                continue;

            outgoingFacesOfVertices.replace(vertex, outgoingFacesList, new LinkedList<>());
            outgoingFacesList = outgoingFacesOfVertices.get(vertex);

            while (!outgoingFacesSet.isEmpty()) {

                if(outgoingFacesList.isEmpty()){
                    Face face = outgoingFacesSet.iterator().next();
                    outgoingFacesList.add(face);
                    outgoingFacesSet.remove(face);
                    lEdge = face.getLEdge();
                    rEdge = face.getREdge();
                    continue;
                }

                Face firstFace = lFaceOfEdge.get(lEdge);
                if (firstFace != null) {
                    outgoingFacesList.add(0, firstFace);
                    lEdge = firstFace.getLEdge();
                    outgoingFacesSet.remove(firstFace);
                }

                Face lastFace = rFaceOfEdge.get(rEdge);
                if (lastFace != null) {
                    outgoingFacesList.add(lastFace);
                    rEdge = lastFace.getREdge();
                    outgoingFacesSet.remove(lastFace);
                }
            }
        }
    }



    private void calculateApexOfVertices() throws LDrawingNotPossibleException {

        for(Vertex vertex : convertedSkeleton.getVertices()) {
            DirectedEdge apexEdge = null;
            for (DirectedEdge virtualEdge : embedding.getOutgoingEdges(vertex)) {
                AbstractPertinentGraph childPert = virtualEdges2PertinentGraphs.get(virtualEdge);
                if(childPert.getSuccessorPathType() == SuccessorPathType.TYPE_B){
                    if(apexEdge != null)
                        throw new LDrawingNotPossibleException("R-Node contains a vertex which contains two apizes. Vertex is " + vertex);
                    apexEdge = virtualEdge;
                    setSuccessorPathType(SuccessorPathType.TYPE_B);
                }
            }
            apexOfVertices.put(vertex, apexEdge);
        }
    }



    private void checkIfApexAfterRFaces() throws LDrawingNotPossibleException {

        for(Vertex vertex : convertedSkeleton.getVertices()) {
            DirectedEdge apexEdge = apexOfVertices.get(vertex);
            List<DirectedEdge> edges = new ArrayList<>();
            if (apexEdge != null) {
                for (DirectedEdge virtualEdge : embedding.getOutgoingEdges(vertex)) {
                    AbstractPertinentGraph pert = virtualEdges2PertinentGraphs.get(virtualEdge);
                    FaceType lFaceType = lFaceOfEdge.get(virtualEdge) != null ? lFaceOfEdge.get(virtualEdge).getFaceType() : null;
                    FaceType rFaceType = rFaceOfEdge.get(virtualEdge) != null ? rFaceOfEdge.get(virtualEdge).getFaceType() : null;
                    if (pert.getSuccessorPathType() == SuccessorPathType.TYPE_B || lFaceType == FaceType.TYPE_R || rFaceType == FaceType.TYPE_R) {
                        edges.add(virtualEdge);
                    }
                }
                if (!edges.isEmpty() && edges.get(edges.size() - 1) != apexEdge)
                    throw new LDrawingNotPossibleException("R-Node contains a vertex with a typeB child which is places before a face assigned with type R. Vertex is " + vertex);
            }
        }
    }


    private void checkIfApexBeforeLFaces() throws LDrawingNotPossibleException {

        for(Vertex vertex : convertedSkeleton.getVertices()) {
            DirectedEdge apexEdge = apexOfVertices.get(vertex);
            List<DirectedEdge> edges = new ArrayList<>();
            if (apexEdge != null) {
                for (DirectedEdge virtualEdge : embedding.getOutgoingEdges(vertex)) {
                    AbstractPertinentGraph pert = virtualEdges2PertinentGraphs.get(virtualEdge);
                    FaceType lFaceType = lFaceOfEdge.get(virtualEdge) != null ? lFaceOfEdge.get(virtualEdge).getFaceType() : null;
                    FaceType rFaceType = rFaceOfEdge.get(virtualEdge) != null ? rFaceOfEdge.get(virtualEdge).getFaceType() : null;
                    if (pert.getSuccessorPathType() == SuccessorPathType.TYPE_B || lFaceType == FaceType.TYPE_L || rFaceType == FaceType.TYPE_L)
                        edges.add(virtualEdge);
                }
                if (edges.get(0) != apexEdge)
                    throw new LDrawingNotPossibleException("R-Node contains a vertex with a typeB child which is places before a face assigned with type R. Vertex is " + vertex);
            }
        }
    }



    private void checkIfRFacesBeforeLFaces() throws LDrawingNotPossibleException {

        for(Vertex vertex : convertedSkeleton.getVertices()){
            List<Face> assignedFaces = new ArrayList<>();
            for(Face face : outgoingFacesOfVertices.get(vertex)){
                if(face.getFaceType() != FaceType.UNDEFINED)
                    assignedFaces.add(face);
            }

            for(int i = 0; i < assignedFaces.size()-1; i++){
                Face face1 = assignedFaces.get(i);
                Face face2 = assignedFaces.get(i+1);
                if(face1.getFaceType() == FaceType.TYPE_L && face2.getFaceType() == FaceType.TYPE_R)
                    throw new LDrawingNotPossibleException("R-Node contains a face assigned with type L before a face assigned with type R. Vertex is " + vertex);
                if(face1.getFaceType() == FaceType.TYPE_R && face2.getFaceType() == FaceType.TYPE_L){
                    apexOfVertices.computeIfAbsent(vertex, arg -> face2.getLEdge());
                    setSuccessorPathType(SuccessorPathType.TYPE_B);
                }
            }
        }
    }





    private void augmentGraph() throws LDrawingNotPossibleException {

        for(Vertex vertex : convertedSkeleton.getVertices()) {
            List<Face> outgoingFaces = outgoingFacesOfVertices.get(vertex);
            MultiDirectedGraph augmentedGraph = Augmentation.getAugmentation().getAugmentedGraph();
            boolean changedDirection = false;
            boolean containsL = false;
            boolean containsR = false;

            //all right faces
            for(Face face : outgoingFaces){
                if(face.getFaceType() == FaceType.TYPE_R){
                    containsR = true;
                    AbstractPertinentGraph lPert = virtualEdges2PertinentGraphs.get(face.getLEdge());
                    AbstractPertinentGraph rPert = virtualEdges2PertinentGraphs.get(face.getREdge());
                    DirectedEdge augmentedEdge = augmentedGraph.addEdge(lPert.getRightmostVertex(), rPert.getLeftmostVertex());
                    Augmentation.getAugmentation().getAugmentedEdges().add(augmentedEdge);
                    LOGGER.debug(PrintColors.ANSI_GREEN + "        Insert Edge: " + augmentedEdge);
                }
            }
            //all left faces and undefined if they are after the first L-Face
            for(Face face : outgoingFaces){
                if(face.getFaceType() == FaceType.TYPE_L){
                    containsL = true;
                    AbstractPertinentGraph lPert = virtualEdges2PertinentGraphs.get(face.getLEdge());
                    AbstractPertinentGraph rPert = virtualEdges2PertinentGraphs.get(face.getREdge());
                    DirectedEdge augmentedEdge = augmentedGraph.addEdge(rPert.getLeftmostVertex(), lPert.getRightmostVertex());
                    Augmentation.getAugmentation().getAugmentedEdges().add(augmentedEdge);
                    LOGGER.debug(PrintColors.ANSI_GREEN + "        Insert Edge: " + augmentedEdge);
                }
            }

            for(Face face : outgoingFaces){

                if(face.getFaceType() == FaceType.UNDEFINED){
                    AbstractPertinentGraph lPert = virtualEdges2PertinentGraphs.get(face.getLEdge());
                    AbstractPertinentGraph rPert = virtualEdges2PertinentGraphs.get(face.getREdge());
                    DirectedEdge augmentedEdge;

                    if(containsL && containsR){
                        if(!changedDirection) {
                            augmentedEdge = augmentedGraph.addEdge(lPert.getRightmostVertex(), rPert.getLeftmostVertex());
                            face.setFaceType(FaceType.TYPE_R);
                        }else {
                            augmentedEdge = augmentedGraph.addEdge(rPert.getLeftmostVertex(), lPert.getRightmostVertex());
                            face.setFaceType(FaceType.TYPE_L);
                        }
                    }else if(containsR){
                        augmentedEdge = augmentedGraph.addEdge(lPert.getRightmostVertex(), rPert.getLeftmostVertex());
                        face.setFaceType(FaceType.TYPE_R);
                    }else if(containsL){
                        augmentedEdge = augmentedGraph.addEdge(rPert.getLeftmostVertex(), lPert.getRightmostVertex());
                        face.setFaceType(FaceType.TYPE_L);
                    }else{
                        augmentedEdge = augmentedGraph.addEdge(lPert.getRightmostVertex(), rPert.getLeftmostVertex());
                        face.setFaceType(FaceType.TYPE_R);
                    }
                    Augmentation.getAugmentation().getAugmentedEdges().add(augmentedEdge);
                    LOGGER.debug(PrintColors.ANSI_GREEN + "        Insert Edge: " + augmentedEdge);

                }
                if(face.getFaceType() == FaceType.TYPE_L){
                    changedDirection = true;
                }
            }
        }
    }


    @Override
    public void reconstructEmbedding() {

        Face firstFace = outgoingFacesOfVertices.get(getSource()).get(0);

        if(firstFace.getFaceType() == FaceType.TYPE_R){
            for(Vertex vertex : convertedSkeleton.getVertices()){
                List<DirectedEdge> outgoingEdges = embedding.getOutgoingEdges(vertex);
                for(int i = 0; i < outgoingEdges.size(); i++){
                    DirectedEdge virtualEdge = outgoingEdges.get(i);
                    AbstractPertinentGraph pert = virtualEdges2PertinentGraphs.get(virtualEdge);
                    pert.reconstructEmbedding();
                }
            }
        }else{
            for(Vertex vertex : convertedSkeleton.getVertices()){
                List<DirectedEdge> outgoingEdges = embedding.getOutgoingEdges(vertex);
                for(int i = outgoingEdges.size()-1; i >= 0; i--){
                    DirectedEdge virtualEdge = outgoingEdges.get(i);
                    AbstractPertinentGraph pert = virtualEdges2PertinentGraphs.get(virtualEdge);
                    pert.reconstructEmbedding();
                }
            }
        }
    }
}
