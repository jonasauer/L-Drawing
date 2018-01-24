package main.java.algorithm.typeDeterminationUtils;

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

    private List<List<DirectedEdge>> skeletonFaces;

    private Map<List<DirectedEdge>, Vertex> sourceOfFace;
    private Map<Vertex, List<List<DirectedEdge>>> facesOfSource;
    private Map<List<DirectedEdge>, Vertex> targetOfFace;

    private Map<List<DirectedEdge>, DirectedEdge> leftEdge;
    private Map<List<DirectedEdge>, DirectedEdge> rightEdge;

    private Map<DirectedEdge, TCTreeNode<DirectedEdge, Vertex>> virtualEdgeToTCTreeNode;

    private Map<List<DirectedEdge>, FaceType> faceTypes;
    private SuccessorPathType successorPathType = SuccessorPathType.TYPE_M;


    //TODO: OK
    public void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {
        System.out.println("---------------------------");
        System.out.println("    RType Determination");


        if(!tcTreeNode.getType().equals(TCTreeNodeType.TYPE_R)) return;

        this.tcTreeNode = tcTreeNode;
        this.tcTree = tcTree;
        this.skeleton = convertSkeletonToGraph();
        this.augmentedGraph = HolderProvider.getAugmentationHolder().getAugmentedGraph();
        this.skeletonFaces = HolderProvider.getEmbeddingHolder().getFacesOfRNode(tcTreeNode, skeleton);

        this.sourceOfFace = new HashMap<>();
        this.facesOfSource = new HashMap<>();
        this.targetOfFace = new HashMap<>();

        this.leftEdge = new HashMap<>();
        this.rightEdge = new HashMap<>();

        this.virtualEdgeToTCTreeNode = calcVirtualEdgeToTCTreeNode();

        this.faceTypes = new HashMap<>();

        calcSourceAndTargetOfFaces();

        assignLabelsToFaces();

        System.out.println("    ConnectVertices");
        for(Vertex vertex : skeleton.getVertices()){
            connectVertices(vertex);
        }

        HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().put(tcTreeNode, successorPathType);
        System.out.println("    SucessorPathType: " + successorPathType);
    }




    //TODO: OK
    private MultiDirectedGraph convertSkeletonToGraph(){

        MultiDirectedGraph skeletonGraph = new MultiDirectedGraph();
        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
            Vertex source = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(child);
            Vertex target = HolderProvider.getSourceSinkPertinentGraphsHolder().getSinkNodes().get(child);
            skeletonGraph.addEdge(source, target);
        }
        System.out.println("      Skeleton: " + skeletonGraph);
        return skeletonGraph;
    }





    //TODO: OK
    private Map<DirectedEdge, TCTreeNode<DirectedEdge, Vertex>> calcVirtualEdgeToTCTreeNode(){

        Map<DirectedEdge, TCTreeNode<DirectedEdge, Vertex>> virtualEdgeToTCTreeNode = new HashMap<>();

        for(DirectedEdge edge : skeleton.getEdges()){
            for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                Vertex edgeSource = edge.getSource();
                Vertex edgeTarget = edge.getTarget();
                Vertex pertSource = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(child);
                Vertex pertTarget = HolderProvider.getSourceSinkPertinentGraphsHolder().getSinkNodes().get(child);
                if(edgeSource.equals(pertSource) && edgeTarget.equals(pertTarget) || edgeSource.equals(pertTarget) && edgeTarget.equals(pertSource))
                    virtualEdgeToTCTreeNode.put(edge, child);
            }
        }
        return virtualEdgeToTCTreeNode;
    }



    //TODO: OK
    private void calcSourceAndTargetOfFaces(){

        for(Vertex vertex : skeleton.getVertices()){
            facesOfSource.put(vertex, new LinkedList<>());
        }

        for(List<DirectedEdge> face : skeletonFaces){

            Vertex source = null;
            Vertex target = null;

            for(int i = 0; i < face.size(); i++){

                DirectedEdge edge1 = face.get((i+0)%face.size());
                DirectedEdge edge2 = face.get((i+1)%face.size());
                if(edge1.getSource().equals(edge2.getSource())) {
                    source = edge1.getSource();
                    leftEdge.put(face, edge1);
                    rightEdge.put(face, edge2);
                }
                if(edge1.getTarget().equals(edge2.getTarget())) {
                    target = edge1.getTarget();
                }
            }
            sourceOfFace.put(face, source);
            targetOfFace.put(face, target);
            facesOfSource.get(source).add(face);
        }
    }



    //TODO: OK
    private void assignLabelsToFaces(){

        System.out.println("    Faces:");
        for(List<DirectedEdge> face : skeletonFaces){
            FaceType faceType = FaceType.UNDEFINED;
            Vertex target = targetOfFace.get(face);
            Vertex vL = leftEdge.get(face).getTarget();
            Vertex vR = rightEdge.get(face).getTarget();
            if(target.equals(vL)) {
                faceType = FaceType.TYPE_L;
            }
            if(target.equals(vR)){
                faceType = FaceType.TYPE_R;
            }
            faceTypes.put(face, faceType);
            System.out.print("      Face: ");
            for(DirectedEdge edge : face)
                System.out.print(edge + "  ");
            System.out.print(faceType + "  ");
            System.out.print("Source: " + sourceOfFace.get(face) + "  ");
            System.out.print("Target: " + targetOfFace.get(face) + "  ");
            System.out.print("LeftEdge: " + leftEdge.get(face) + "  ");
            System.out.print("RightEdge: " + rightEdge.get(face));
            System.out.println();
        }
    }






    //TODO: OK
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
            if(edge.getSource().equals(pertSource))
                successorEdges.add(edge);
        }
        List<DirectedEdge> predecessorEdges = new LinkedList<>();
        for(DirectedEdge edge : augmentedGraph.getEdgesWithSources(predecessorVertices)){
            if(edge.getTarget().equals(pertTarget))
                predecessorEdges.add(edge);
        }

        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(pertSource);
        List<DirectedEdge> incomingEdgesTarget = HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(pertTarget);

        //switch edges in skeletonEmbedding of sourceNode of the pertinent graph
        if(successorEdges.size() > 1){
            int insertIndex = 0;
            for(DirectedEdge outgoingEdge : outgoingEdgesSource){
                if(successorEdges.contains(outgoingEdge)){
                    outgoingEdgesSource.removeAll(successorEdges);
                    break;
                }
                insertIndex++;
            }

            for(int i = successorEdges.size()-1; i >= 0; i--){
                outgoingEdgesSource.add(insertIndex++, successorEdges.get(i));
            }
        }

        //switch edges in skeletonEmbedding of targetNode of the pertinent graph
        if(predecessorEdges.size() > 1){
            int insertIndex = 0;
            for(DirectedEdge incomingEdge : incomingEdgesTarget){
                if(predecessorEdges.contains(incomingEdge)){
                    incomingEdgesTarget.removeAll(predecessorEdges);
                    break;
                }
                insertIndex++;
            }

            for(int i = predecessorEdges.size()-1; i >= 0; i--){
                incomingEdgesTarget.add(insertIndex++, predecessorEdges.get(i));
            }
        }

        //switch all other edges in the skeletonEmbedding.
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


    private List<DirectedEdge> getOutgoingEdgesOfSeleton(Vertex vertex){

        List<DirectedEdge> outgoingEdges = new LinkedList<>();
        List<List<DirectedEdge>> facesOfVertex = facesOfSource.get(vertex);

        int index = 0;
        for(int i = 0; i < facesOfVertex.size(); i++){
            List<DirectedEdge> face = facesOfVertex.get(i);
            for(int j = 0; j < face.size(); j++){
                DirectedEdge e1 = face.get((j+0)%face.size());
                DirectedEdge e2 = face.get((j+1)%face.size());
                if(e1.getTarget().equals(vertex) && e2.getSource().equals(vertex))
                    index = i;
            }
        }

        for(int i = index; i < index + facesOfVertex.size(); i++){
            List<DirectedEdge> face = facesOfVertex.get(i);
            for(int j = 0; j < face.size(); j++) {
                DirectedEdge e1 = face.get((j + 0) % face.size());
                DirectedEdge e2 = face.get((j + 1) % face.size());
                if (e1.getSource().equals(vertex) && !outgoingEdges.contains(e1))
                    outgoingEdges.add(e1);
                if (e2.getSource().equals(vertex) && !outgoingEdges.contains(e2))
                    outgoingEdges.add(e2);
            }

        }
        return outgoingEdges;
    }



    //TODO: OK
    private void connectVertices(Vertex vertex){

        List<List<DirectedEdge>> outgoingFaces = facesOfSource.get(vertex);
        if(outgoingFaces.isEmpty()) return;
        List<List<DirectedEdge>> outgoingFacesOrdered = new LinkedList<>();
        TCTreeNode<DirectedEdge, Vertex> optTypeBNode = null;
        boolean bothTypeOfFacesContained = false;

        //order outgoing faces from left to right
        int index = 0;
        outgoingFacesOrdered.add(outgoingFaces.get(0));
        while(index < outgoingFaces.size()){
            List<DirectedEdge> face = outgoingFaces.get(index++);
            List<DirectedEdge> first = outgoingFacesOrdered.get(0);
            List<DirectedEdge> last = outgoingFacesOrdered.get(outgoingFacesOrdered.size()-1);
            if(rightEdge.get(face).equals(leftEdge.get(first))){
                outgoingFacesOrdered.add(0, face);
                index = 0;
            }
            if(rightEdge.get(last).equals(leftEdge.get(face))){
                outgoingFacesOrdered.add(face);
                index = 0;
            }
        }

        System.out.println("      " + vertex + ": ");
        for(List<DirectedEdge> face : outgoingFacesOrdered){
            System.out.print("        ");
            for(DirectedEdge edge : face)
                System.out.print(edge + "  ");
            System.out.println();
        }

        //check if there is more than one type B child.
        for(DirectedEdge edge : getOutgoingEdgesOfSeleton(vertex)){
            TCTreeNode<DirectedEdge, Vertex> child = virtualEdgeToTCTreeNode.get(edge);
            if(HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(child).equals(SuccessorPathType.TYPE_B)){
                if(optTypeBNode != null)
                    throw new RuntimeException("Vertex has two children with Type B!");
                optTypeBNode = child;
                successorPathType = SuccessorPathType.TYPE_B;
            }
        }

        //check if all R faces are before typeB node and all L faces are after typeB node.
        if(optTypeBNode != null){
            for(List<DirectedEdge> face : outgoingFacesOrdered){
                DirectedEdge left = leftEdge.get(face);
                DirectedEdge right = rightEdge.get(face);
                if(faceTypes.get(face).equals(FaceType.TYPE_L) && optTypeBNode.equals(virtualEdgeToTCTreeNode.get(right)))
                        throw new RuntimeException("Type B node is the right edge of a face with Type L!");
                if(faceTypes.get(face).equals(FaceType.TYPE_R) && optTypeBNode.equals(virtualEdgeToTCTreeNode.get(left)))
                        throw new RuntimeException("Type B node is the left edge of a face with Type R!");
            }
        }

        //check if all faces with type L are after all faces with type R.
        TCTreeNode<DirectedEdge, Vertex> nodeWithApex = null;
        for(int i = 0; i < outgoingFacesOrdered.size()-1; i++) {
            List<DirectedEdge> face1 = outgoingFacesOrdered.get(i);
            List<DirectedEdge> face2 = outgoingFacesOrdered.get(i+1);
            if (faceTypes.get(face1).equals(FaceType.TYPE_L) && faceTypes.get(face2).equals(FaceType.TYPE_R))
                throw new RuntimeException("Face with type L is before face with type R!");
            if (faceTypes.get(face1).equals(FaceType.TYPE_R) && faceTypes.get(face2).equals(FaceType.TYPE_L)) {
                bothTypeOfFacesContained = true;
                successorPathType = SuccessorPathType.TYPE_B;
                nodeWithApex = virtualEdgeToTCTreeNode.get(rightEdge.get(face1));
            }
        }

        if(optTypeBNode != null){
            connectWithTypeB(vertex);
            System.out.println("        Finished with TypeB in successors! Vertex: " + vertex);
            return;
        }
        if(bothTypeOfFacesContained){
            connectWithBothTypes(vertex, nodeWithApex);
            System.out.println("        Finished with both types of faces! Vertex: " + vertex);
            return;
        }

        connectWithOnlyOneType(vertex);
        System.out.println("        Finished with only one type of faces! Vertex: " + vertex);
    }






    private void connectWithTypeB(Vertex vertex){

        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
        if(outgoingEdgesSource.size() <= 0) return;
        int apexIndex = -1;

        for(int i = 1; i < outgoingEdgesSource.size()-1; i++){
            Vertex vertex1 = outgoingEdgesSource.get(i-1).getTarget();
            Vertex vertex2 = outgoingEdgesSource.get(i).getTarget();
            Vertex vertex3 = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge leftMiddle = augmentedGraph.getEdge(vertex1, vertex2);
            DirectedEdge rightMiddle = augmentedGraph.getEdge(vertex2, vertex3);

            if(leftMiddle != null && rightMiddle != null){
                if(leftMiddle.getTarget().equals(vertex2) && rightMiddle.getTarget().equals(vertex2)){
                    apexIndex = i;
                    break;
                }
            }
        }

        for(int i = 0; i < apexIndex-1; i++){
            Vertex vertex1 = outgoingEdgesSource.get(i+0).getTarget();
            Vertex vertex2 = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge edge = augmentedGraph.getEdge(vertex1, vertex2);
            if(edge != null && edge.getSource().equals(vertex2) && edge.getTarget().equals(vertex1)){
                for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                    MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(child);
                    if(childPert.getVertices().contains(vertex1) && childPert.getVertices().contains(vertex2))
                        flipNodeInEmbedding(child);
                }
            }
        }

        for(int i = apexIndex+1; i < outgoingEdgesSource.size()-1; i++){
            Vertex v1 = outgoingEdgesSource.get(i).getTarget();
            Vertex v2 = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge edge = augmentedGraph.getEdge(v1, v2);
            if(edge != null && edge.getSource().equals(v1) && edge.getTarget().equals(v2)){
                for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                    MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(child);
                    if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                        flipNodeInEmbedding(child);
                }
            }
        }

        for(int i = 0; i < apexIndex-1; i++){
            Vertex first = outgoingEdgesSource.get(i).getTarget();
            Vertex second = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge edge = augmentedGraph.getEdge(first, second);
            if(edge == null){
                DirectedEdge augmentedEdge = augmentedGraph.addEdge(first, second);
                HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(first).add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(second).add(augmentedEdge);
            }
        }

        for(int i = apexIndex+1; i < outgoingEdgesSource.size()-1; i++){
            Vertex first = outgoingEdgesSource.get(i).getTarget();
            Vertex second = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge edge = augmentedGraph.getEdge(first, second);
            if(edge == null){
                DirectedEdge augmentedEdge = augmentedGraph.addEdge(second, first);
                HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(second).add(0, augmentedEdge);
                HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(first).add(0 ,augmentedEdge);
            }
        }
    }


    private void connectWithBothTypes(Vertex vertex, TCTreeNode<DirectedEdge, Vertex> nodeWithApex){

        int apexIndex = 0;
        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
        MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(nodeWithApex);
        for(DirectedEdge edge : outgoingEdgesSource){
            if(pert.getEdge(edge.getSource(), edge.getTarget()) != null)
                break;
            apexIndex++;
        }

        for(int i = 0; i < apexIndex-1; i++){
            Vertex v1 = outgoingEdgesSource.get(i).getTarget();
            Vertex v2 = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge edge = augmentedGraph.getEdge(v1, v2);
            if(edge != null && edge.getSource().equals(v2) && edge.getTarget().equals(v1)){
                for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                    MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(child);
                    if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                        flipNodeInEmbedding(child);
                }
            }
        }

        for(int i = apexIndex+1; i < outgoingEdgesSource.size()-1; i++){
            Vertex v1 = outgoingEdgesSource.get(i).getTarget();
            Vertex v2 = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge edge = augmentedGraph.getEdge(v1, v2);
            if(edge != null && edge.getSource().equals(v1) && edge.getTarget().equals(v2)){
                for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                    MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(child);
                    if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                        flipNodeInEmbedding(child);
                }
            }
        }

        for(int i = 0; i < apexIndex-1; i++){
            Vertex first = outgoingEdgesSource.get(i).getTarget();
            Vertex second = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge edge = augmentedGraph.getEdge(first, second);
            if(edge == null){
                DirectedEdge augmentedEdge = augmentedGraph.addEdge(first, second);
                HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(first).add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(second).add(augmentedEdge);
            }
        }

        for(int i = apexIndex+1; i < outgoingEdgesSource.size()-1; i++){
            Vertex first = outgoingEdgesSource.get(i).getTarget();
            Vertex second = outgoingEdgesSource.get(i+1).getTarget();
            DirectedEdge edge = augmentedGraph.getEdge(first, second);
            if(edge == null){
                DirectedEdge augmentedEdge = augmentedGraph.addEdge(second, first);
                HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(second).add(0, augmentedEdge);
                HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(first).add(0 ,augmentedEdge);
            }
        }
    }


    private void connectWithOnlyOneType(Vertex vertex){
        List<DirectedEdge> outgoingEdgesSource = HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(vertex);
        if(outgoingEdgesSource.size() <= 0) return;
        if(facesOfSource.get(vertex).size() <= 0) return;
        FaceType faceType = faceTypes.get(facesOfSource.get(vertex).iterator().next());

        if(faceType.equals(FaceType.TYPE_R)){
            //flip nodes that are from right to left.
            for(int i = 0; i < outgoingEdgesSource.size()-1; i++){
                Vertex v1 = outgoingEdgesSource.get(i).getTarget();
                Vertex v2 = outgoingEdgesSource.get(i+1).getTarget();
                DirectedEdge edge = augmentedGraph.getEdge(v1, v2);
                if(edge != null && edge.getSource().equals(v2) && edge.getTarget().equals(v1)){
                    for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                        MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(child);
                        if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                            flipNodeInEmbedding(child);
                    }
                }
            }

            for(int i = 0; i < outgoingEdgesSource.size()-1; i++){
                Vertex first = outgoingEdgesSource.get(i).getTarget();
                Vertex second = outgoingEdgesSource.get(i+1).getTarget();
                DirectedEdge edge = augmentedGraph.getEdge(first, second);
                if(edge == null){
                    DirectedEdge augmentedEdge = augmentedGraph.addEdge(first, second);
                    HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(first).add(augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(second).add(augmentedEdge);
                }
            }
        }else{

            //flip nodes that are from right to left.
            for(int i = 0; i < outgoingEdgesSource.size()-1; i++){
                Vertex v1 = outgoingEdgesSource.get(i).getTarget();
                Vertex v2 = outgoingEdgesSource.get(i+1).getTarget();
                DirectedEdge edge = augmentedGraph.getEdge(v1, v2);
                if(edge != null && edge.getSource().equals(v1) && edge.getTarget().equals(v2)){
                    for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
                        MultiDirectedGraph childPert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(child);
                        if(childPert.getVertices().contains(v1) && childPert.getVertices().contains(v2))
                            flipNodeInEmbedding(child);
                    }
                }
            }

            for(int i = 0; i < outgoingEdgesSource.size()-1; i++){
                Vertex first = outgoingEdgesSource.get(i).getTarget();
                Vertex second = outgoingEdgesSource.get(i+1).getTarget();
                DirectedEdge edge = augmentedGraph.getEdge(first, second);
                if(edge == null){
                    DirectedEdge augmentedEdge = augmentedGraph.addEdge(second, first);
                    HolderProvider.getAugmentationHolder().getAugmentedEdges().add(augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getOutgoingEdgesCircularOrdering(second).add(0, augmentedEdge);
                    HolderProvider.getEmbeddingHolder().getIncomingEdgesCircularOrdering(first).add(0, augmentedEdge);
                }
            }

        }
    }


    private enum FaceType{
        TYPE_L,
        TYPE_R,
        UNDEFINED
    }
}
