package main.java.algorithm.successorPathTypeDetermination;

import main.java.printer.PrintColors;
import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.holder.HolderProvider;
import main.java.algorithm.types.FaceType;
import main.java.algorithm.types.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;

import java.util.*;

public class RTypeDetermination implements ITypeDetermination {

    private TCTree<DirectedEdge, Vertex> tcTree;
    private TCTreeNode<DirectedEdge, Vertex> tcTreeNode;
    private MultiDirectedGraph skeletonGraph;
    private MultiDirectedGraph augmentedGraph;

    private List<List<DirectedEdge>> skeletonFaces;

    private Map<Vertex, List<List<DirectedEdge>>> outgoingFacesOfVertices;

    private Map<List<DirectedEdge>, DirectedEdge> lEdgeOfFace;
    private Map<List<DirectedEdge>, DirectedEdge> rEdgeOfFace;

    private Map<DirectedEdge, List<DirectedEdge>> lFaceOfEdge;
    private Map<DirectedEdge, List<DirectedEdge>> rFaceOfEdge;

    private Map<DirectedEdge, TCTreeNode<DirectedEdge, Vertex>> virtualEdgeToTCTreeNode;

    private Map<List<DirectedEdge>, FaceType> faceTypes;
    private SuccessorPathType successorPathType = SuccessorPathType.TYPE_M;


    public void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {
        System.out.println(PrintColors.ANSI_RED + "---------------------------");
        System.out.println(PrintColors.ANSI_RED + "RType Determination! Source Vertex is " + HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(tcTreeNode));



        this.tcTreeNode = tcTreeNode;
        this.tcTree = tcTree;
        this.skeletonGraph = convertSkeletonToGraph();
        this.augmentedGraph = HolderProvider.getAugmentationHolder().getAugmentedGraph();
        this.skeletonFaces = HolderProvider.getEmbeddingHolder().getFacesOfRSkeleton(skeletonGraph, tcTreeNode);

        this.outgoingFacesOfVertices = new HashMap<>();

        this.lEdgeOfFace = new HashMap<>();
        this.rEdgeOfFace = new HashMap<>();

        this.lFaceOfEdge = new HashMap<>();
        this.rFaceOfEdge = new HashMap<>();

        this.virtualEdgeToTCTreeNode = calcTCNodeOfVirtualEdge();

        this.faceTypes = new HashMap<>();

        calculateSourceAndLREdgesOfFaces();

        orderOutgoingFaces();

        System.out.println(PrintColors.ANSI_YELLOW + "    Augment Graph");

        augmentGraph();


        HolderProvider.getSuccessorPathTypeHolder().setNodeType(tcTreeNode, successorPathType);
        System.out.println(PrintColors.ANSI_GREEN + "    SucessorPathType: " + successorPathType);
    }





    private MultiDirectedGraph convertSkeletonToGraph(){

        MultiDirectedGraph skeletonGraph = new MultiDirectedGraph();
        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
            Vertex source = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(child);
            Vertex target = HolderProvider.getSourceTargetPertinentGraphsHolder().getTargetNode(child);
            skeletonGraph.addEdge(source, target);
        }
        System.out.println(PrintColors.ANSI_RED + "    Skeleton: " + skeletonGraph);
        return skeletonGraph;
    }






    private Map<DirectedEdge, TCTreeNode<DirectedEdge, Vertex>> calcTCNodeOfVirtualEdge(){

        Map<DirectedEdge, TCTreeNode<DirectedEdge, Vertex>> virtualEdgeToTCTreeNode = new HashMap<>();

        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
            Vertex pertSource = HolderProvider.getSourceTargetPertinentGraphsHolder().getSourceNode(child);
            Vertex pertTarget = HolderProvider.getSourceTargetPertinentGraphsHolder().getTargetNode(child);
            DirectedEdge virtualEdge = skeletonGraph.getEdge(pertSource, pertTarget);
            virtualEdgeToTCTreeNode.put(virtualEdge, child);
        }

        return virtualEdgeToTCTreeNode;
    }




    private void calculateSourceAndLREdgesOfFaces(){

        for(Vertex vertex : skeletonGraph.getVertices()){
            outgoingFacesOfVertices.put(vertex, new ArrayList<>());
        }

        for(List<DirectedEdge> face : skeletonFaces){
            Vertex source = null;

            for(int i = 0; i < face.size(); i++){
                DirectedEdge edge1 = face.get((i  )%face.size());
                DirectedEdge edge2 = face.get((i+1)%face.size());
                if(edge1.getSource().equals(edge2.getSource())) {
                    source = edge1.getSource();
                    lEdgeOfFace.put(face, edge1);
                    rEdgeOfFace.put(face, edge2);
                    lFaceOfEdge.put(edge2, face);
                    rFaceOfEdge.put(edge1, face);
                    assignLabelsToFace(face);
                    break;
                }
            }
            outgoingFacesOfVertices.get(source).add(face);
        }
    }




    private void assignLabelsToFace(List<DirectedEdge> face){

        Vertex lVertex = lEdgeOfFace.get(face).getTarget();
        Vertex rVertex = rEdgeOfFace.get(face).getTarget();
        DirectedEdge lrEdge = skeletonGraph.getEdge(lVertex, rVertex);

        if(lrEdge == null) {
            faceTypes.put(face, FaceType.UNDEFINED);
            return;
        }

        if (lrEdge.getTarget().equals(lVertex)) {
            faceTypes.put(face, FaceType.TYPE_L);
        } else if (lrEdge.getTarget().equals(rVertex)) {
            faceTypes.put(face, FaceType.TYPE_R);
        } else {
            faceTypes.put(face, FaceType.UNDEFINED);
        }
    }






    private List<DirectedEdge> getOutgoingEdgesOfSkeleton(Vertex vertex){

        List<DirectedEdge> outgoingEdges = new LinkedList<>();
        List<List<DirectedEdge>> facesOfVertex = outgoingFacesOfVertices.get(vertex);

        //add first edge
        List<DirectedEdge> firstFace = facesOfVertex.get(0);
        DirectedEdge firstEdge = lEdgeOfFace.get(firstFace);
        outgoingEdges.add(firstEdge);

        //add right edges of all faces
        for(List<DirectedEdge> face : facesOfVertex)
            outgoingEdges.add(rEdgeOfFace.get(face));

        return outgoingEdges;
    }




    private void orderOutgoingFaces(){

        for(Vertex vertex : skeletonGraph.getVertices()) {

            List<List<DirectedEdge>> outgoingFacesList = outgoingFacesOfVertices.get(vertex);
            Set<List<DirectedEdge>> outgoingFacesSet = new HashSet<>(outgoingFacesList);
            DirectedEdge firstEdge = null;
            DirectedEdge lastEdge = null;

            if (outgoingFacesList.size() < 2)
                continue;

            outgoingFacesOfVertices.replace(vertex, outgoingFacesList, new LinkedList<>());
            outgoingFacesList = outgoingFacesOfVertices.get(vertex);

            while (!outgoingFacesSet.isEmpty()) {
                if(outgoingFacesList.isEmpty()){
                    List<DirectedEdge> face = outgoingFacesSet.iterator().next();
                    outgoingFacesList.add(face);
                    outgoingFacesSet.remove(face);
                    firstEdge = lEdgeOfFace.get(face);
                    lastEdge = rEdgeOfFace.get(face);
                    continue;
                }
                List<DirectedEdge> firstFace = lFaceOfEdge.get(firstEdge);
                if (firstFace != null) {
                    outgoingFacesList.add(0, firstFace);
                    firstEdge = lEdgeOfFace.get(firstFace);
                    outgoingFacesSet.remove(firstFace);
                }
                List<DirectedEdge> lastFace = rFaceOfEdge.get(lastEdge);
                if (lastFace != null) {
                    outgoingFacesList.add(lastFace);
                    lastEdge = rEdgeOfFace.get(lastFace);
                    outgoingFacesSet.remove(lastFace);
                }
            }
        }
    }







    private void augmentGraph() throws LDrawingNotPossibleException {

        for(Vertex vertex : skeletonGraph.getVertices()) {

            List<List<DirectedEdge>> outgoingFacesOfVertex = outgoingFacesOfVertices.get(vertex);
            if (outgoingFacesOfVertex.isEmpty())
                continue;

            TCTreeNode<DirectedEdge, Vertex> typeBNode = null;
            DirectedEdge typeBEdge = null;
            boolean bothTypeOfFacesContained = false;

            System.out.println(PrintColors.ANSI_YELLOW + "      Vertex " + vertex + ": ");
            for (List<DirectedEdge> face : outgoingFacesOfVertex) {
                System.out.print(PrintColors.ANSI_YELLOW + "        Face: ");
                for (DirectedEdge edge : face)
                    System.out.print(PrintColors.ANSI_YELLOW + edge + "  ");
                System.out.println();
            }

            //check if there is more than one type B child.
            for (DirectedEdge edge : getOutgoingEdgesOfSkeleton(vertex)) {
                TCTreeNode<DirectedEdge, Vertex> child = virtualEdgeToTCTreeNode.get(edge);
                if (HolderProvider.getSuccessorPathTypeHolder().getNodeType(child).equals(SuccessorPathType.TYPE_B)) {
                    if (typeBNode != null)
                        throw new LDrawingNotPossibleException("R-Node contains two children assigned with Type-B that have the same source.");
                    typeBNode = child;
                    typeBEdge = edge;
                    successorPathType = SuccessorPathType.TYPE_B;
                }
            }

            //check if all R faces are before typeB node and all L faces are after typeB node.
            if (typeBEdge != null && faceTypes.get(lFaceOfEdge.get(typeBEdge)).equals(FaceType.TYPE_L)) {
                throw new LDrawingNotPossibleException("R-Node contains a child assigned with Type-B that is the right edge of a face assigned with Type-L. Vertex: " + vertex.getName());
            }
            if (typeBEdge != null && faceTypes.get(rFaceOfEdge.get(typeBEdge)).equals(FaceType.TYPE_R)) {
                throw new LDrawingNotPossibleException("R-Node contains a child assigned with Type-B that is the left edge of a face assigned with Type-R. Vertex: " + vertex.getName());
            }

            //check if all faces with type L are after all faces with type R.
            TCTreeNode<DirectedEdge, Vertex> nodeWithApex = null;
            List<DirectedEdge> face1 = null;
            List<DirectedEdge> face2 = null;

            for (List<DirectedEdge> face : outgoingFacesOfVertex) {
                face1 = face2;
                face2 = face;
                if (face1 == null || face2 == null)
                    continue;
                if (faceTypes.get(face1).equals(FaceType.TYPE_L) && faceTypes.get(face2).equals(FaceType.TYPE_R))
                    throw new LDrawingNotPossibleException("R-Node contains a vertex with a face assigned with Type-L placed before a face assigned with Type-R.");
                if (faceTypes.get(face1).equals(FaceType.TYPE_R) && faceTypes.get(face2).equals(FaceType.TYPE_L)) {
                    bothTypeOfFacesContained = true;
                    successorPathType = SuccessorPathType.TYPE_B;
                    nodeWithApex = virtualEdgeToTCTreeNode.get(rEdgeOfFace.get(face1));
                }
            }

            if (typeBNode != null) {
                SuccessorConnector.connectWithTypeB(augmentedGraph, tcTree, tcTreeNode, vertex);
                System.out.println(PrintColors.ANSI_YELLOW + "        Finished with TypeB in successors!");
            } else if (bothTypeOfFacesContained) {
                SuccessorConnector.connectWithBothTypes(augmentedGraph, tcTree, tcTreeNode, nodeWithApex, vertex);
                System.out.println(PrintColors.ANSI_YELLOW + "        Finished with both types of faces!");
            } else {
                SuccessorConnector.connectWithOnlyOneType(augmentedGraph, tcTree, tcTreeNode, outgoingFacesOfVertices, faceTypes, vertex);
                System.out.println(PrintColors.ANSI_YELLOW + "        Finished with only one type of faces!");
            }
        }
    }
}
