package main.java.algorithm.typeDeterminationUtils;

import main.java.algorithm.holder.EmbeddingHolder;
import main.java.algorithm.holder.HolderProvider;
import main.java.algorithm.holder.SourceSinkPertinentGraphsHolder;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCSkeleton;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.*;

public class RTypeDetermination{

    public static void determineType(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode) {

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

        MultiDirectedGraph skeleton = convertSkeletonToGraph(tcTree, tcTreeNode);
        EmbeddingHolder embeddingHolder = new EmbeddingHolder(skeleton);
        List<List<DirectedEdge>> faces = embeddingHolder.getFaces();

        Map<Vertex, List<List<DirectedEdge>>> sourceToFacesMapping = new HashMap<>();
        Map<Vertex, List<List<DirectedEdge>>> targetToFacesMapping = new HashMap<>();

        calcSourceAndTargetsOfFaces(sourceToFacesMapping, targetToFacesMapping, faces, skeleton);






    }

    private static MultiDirectedGraph convertSkeletonToGraph(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> tcTreeNode){

        MultiDirectedGraph skeletonGraph = new MultiDirectedGraph();
        for(TCTreeNode<DirectedEdge, Vertex> child : tcTree.getChildren(tcTreeNode)){
            Vertex source = HolderProvider.getSourceSinkPertinentGraphsHolder().getSourceNodes().get(child);
            Vertex target = HolderProvider.getSourceSinkPertinentGraphsHolder().getSinkNodes().get(child);
            skeletonGraph.addEdge(source, target);
        }
        return skeletonGraph;
    }



    private static void calcSourceAndTargetsOfFaces(Map<Vertex, List<List<DirectedEdge>>> sourceToFacesMapping,
                                                    Map<Vertex, List<List<DirectedEdge>>> targetToFacesMapping,
                                                    List<List<DirectedEdge>> faces,
                                                    MultiDirectedGraph skeleton){

        sourceToFacesMapping = new HashMap<>();
        targetToFacesMapping = new HashMap<>();
        for(Vertex vertex : skeleton.getVertices()){
            sourceToFacesMapping.put(vertex, new LinkedList<>());
            targetToFacesMapping.put(vertex, new LinkedList<>());
        }

        for(List<DirectedEdge> face : faces){

            Set<Vertex> possibleSources = new HashSet<>(skeleton.getVertices());
            Set<Vertex> possibleTargets = new HashSet<>(skeleton.getVertices());

            for(DirectedEdge edge : face){
                for(Vertex vertex : skeleton.getVertices()){
                    if(edge.getSource().equals(vertex))
                        possibleTargets.remove(vertex);
                    if(edge.getTarget().equals(vertex))
                        possibleSources.remove(vertex);
                }
            }
            sourceToFacesMapping.get(possibleSources.iterator().next()).add(face);
            targetToFacesMapping.get(possibleTargets.iterator().next()).add(face);
        }
    }
}
