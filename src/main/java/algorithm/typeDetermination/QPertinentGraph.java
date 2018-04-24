package main.java.algorithm.typeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.embedding.GraphEmbedding;
import main.java.algorithm.types.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.List;

public class QPertinentGraph extends AbstractPertinentGraph {


    public QPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {
        super(tcTreeNode);
    }


    @Override
    void construct(){
        DirectedEdge edge = this.getTcTreeNode().getSkeleton().getOriginalEdges().iterator().next();
        setSource(edge.getSource());
        setTarget(edge.getTarget());
        setLeftmostVertex(edge.getTarget());
        setRightmostVertex(edge.getTarget());
        setSuccessorPathType(SuccessorPathType.TYPE_M);
        setTcTreeNodeType(TCTreeNodeType.TYPE_Q);
    }


    @Override
    public void reconstructEmbedding(){
        List<DirectedEdge> outgoingEdges = GraphEmbedding.getEmbedding().getOutgoingEdges(getSource());
        outgoingEdges.add(this.getTcTreeNode().getSkeleton().getOriginalEdges().iterator().next());

        List<DirectedEdge> incomingEdges = GraphEmbedding.getEmbedding().getIncomingEdges(getTarget());
        incomingEdges.add(this.getTcTreeNode().getSkeleton().getOriginalEdges().iterator().next());
    }
}
