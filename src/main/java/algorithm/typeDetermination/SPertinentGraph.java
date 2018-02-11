package main.java.algorithm.typeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.utils.PrintColors;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class SPertinentGraph extends AbstractPertinentGraph{

    private static final Logger LOGGER = LoggerFactory.getLogger(SPertinentGraph.class);

    public SPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {
        super(tcTreeNode);
    }


    @Override
    void construct(){

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

        LOGGER.debug(PrintColors.ANSI_PURPLE + "-----------------------");
        LOGGER.debug(PrintColors.ANSI_PURPLE + "    S-Node with source: " + getSource());
        LOGGER.debug(PrintColors.ANSI_PURPLE + "      Skeleton: " + getTcTreeNode().getSkeleton());
        LOGGER.debug(PrintColors.ANSI_PURPLE + "      " + getSuccessorPathType());
    }


    @Override
    public void reconstructEmbedding(){

        for(TCTreeNode<DirectedEdge, Vertex> childNode : tcTree.getChildren(getTcTreeNode()))
            pertinentGraphsOfTCTreeNodes.get(childNode).reconstructEmbedding();
    }
}
