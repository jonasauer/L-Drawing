package main.java.algorithm.typeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.HashSet;
import java.util.Set;

public class SPertinentGraph extends AbstractPertinentGraph{


    public SPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {
        super(tcTreeNode);
    }


    @Override
    void construct(){
        System.out.println("P Node");

        Set<Vertex> sources = new HashSet<>(getTcTreeNode().getSkeleton().getVertices());
        Set<Vertex> targets = new HashSet<>(getTcTreeNode().getSkeleton().getVertices());
        for(TCTreeNode<DirectedEdge, Vertex> childNode : tcTree.getChildren(getTcTreeNode())){
            AbstractPertinentGraph childPert = pertinentGraphsOfTCTreeNodes.get(childNode);
            sources.remove(childPert.getTarget());
            targets.remove(childPert.getSource());
        }
        setSource(sources.iterator().next());
        setTarget(targets.iterator().next());
        for(TCTreeNode<DirectedEdge, Vertex> childNode : tcTree.getChildren(getTcTreeNode())){
            AbstractPertinentGraph childPert = pertinentGraphsOfTCTreeNodes.get(childNode);
            if(getSource() == childPert.getSource()){
                setLeftmostVertex(childPert.getLeftmostVertex());
                setRightmostVertex(childPert.getRightmostVertex());
                setSuccessorPathType(childPert.getSuccessorPathType());
                break;
            }
        }
        setTcTreeNodeType(TCTreeNodeType.TYPE_S);
    }


    @Override
    public void reconstructEmbedding(){

        for(TCTreeNode<DirectedEdge, Vertex> childNode : tcTree.getChildren(getTcTreeNode()))
            pertinentGraphsOfTCTreeNodes.get(childNode).reconstructEmbedding();
    }
}
