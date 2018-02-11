package main.java.algorithm.typeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.types.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.Map;

public abstract class AbstractPertinentGraph {


    public static Map<TCTreeNode<DirectedEdge, Vertex>, AbstractPertinentGraph> pertinentGraphsOfTCTreeNodes;
    public static TCTree<DirectedEdge, Vertex> tcTree;

    private TCTreeNode<DirectedEdge, Vertex> tcTreeNode = null;
    private SuccessorPathType successorPathType = null;
    private TCTreeNodeType tcTreeNodeType;
    private Vertex leftmostVertex = null;
    private Vertex rightmostVertex = null;
    private Vertex source = null;
    private Vertex target = null;


    public AbstractPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {
        this.tcTreeNode = tcTreeNode;
        pertinentGraphsOfTCTreeNodes.put(this.tcTreeNode, this);
        construct();
    }




    abstract void construct() throws LDrawingNotPossibleException;





    public TCTreeNode<DirectedEdge, Vertex> getTcTreeNode() {
        return tcTreeNode;
    }

    public SuccessorPathType getSuccessorPathType() {
        return successorPathType;
    }

    public TCTreeNodeType getTcTreeNodeType() {
        return tcTreeNodeType;
    }

    public Vertex getLeftmostVertex() {
        return leftmostVertex;
    }

    public Vertex getRightmostVertex() {
        return this.rightmostVertex;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }



    public void setSuccessorPathType(SuccessorPathType successorPathType) {
        this.successorPathType = successorPathType;
    }

    public void setTcTreeNodeType(TCTreeNodeType tcTreeNodeType) {
        this.tcTreeNodeType = tcTreeNodeType;
    }

    public void setLeftmostVertex(Vertex leftmostVertex) {
        this.leftmostVertex = leftmostVertex;
    }

    public void setRightmostVertex(Vertex rightmostVertex) {
        this.rightmostVertex = rightmostVertex;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public abstract void reconstructEmbedding();
}
