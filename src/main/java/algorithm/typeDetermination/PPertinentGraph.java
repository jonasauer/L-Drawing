package main.java.algorithm.typeDetermination;

import main.java.algorithm.exception.LDrawingNotPossibleException;
import main.java.algorithm.utils.Augmentation;
import main.java.algorithm.types.SuccessorPathType;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.ArrayList;
import java.util.List;

public class PPertinentGraph extends AbstractPertinentGraph{

    private List<AbstractPertinentGraph> orderedChildPerts;

    public PPertinentGraph(TCTreeNode<DirectedEdge, Vertex> tcTreeNode) throws LDrawingNotPossibleException {
        super(tcTreeNode);
        augmentGraph();
    }

    @Override
    void construct() throws LDrawingNotPossibleException {

        orderedChildPerts = new ArrayList<>();

        //add all TypeM children that are not Q-Nodes
        for(TCTreeNode<DirectedEdge, Vertex> childNode : tcTree.getChildren(getTcTreeNode())){
            AbstractPertinentGraph childPert = pertinentGraphsOfTCTreeNodes.get(childNode);
            TCTreeNodeType childTCType = childNode.getType();
            SuccessorPathType childSuccessorPathType = childPert.getSuccessorPathType();
            if(childTCType != TCTreeNodeType.TYPE_Q && childSuccessorPathType != SuccessorPathType.TYPE_B)
                orderedChildPerts.add(childPert);
        }

        //add all QNodes
        for(TCTreeNode<DirectedEdge, Vertex> childNode : tcTree.getChildren(getTcTreeNode())){
            if(childNode.getType() == TCTreeNodeType.TYPE_Q){
                AbstractPertinentGraph childPert = pertinentGraphsOfTCTreeNodes.get(childNode);
                orderedChildPerts.add(childPert);
            }
        }

        //add TypeB Node or throw exception if there are more than one TypeB Node or QNodes.
        for(TCTreeNode<DirectedEdge, Vertex> childNode : tcTree.getChildren(getTcTreeNode())){
            AbstractPertinentGraph childPert = pertinentGraphsOfTCTreeNodes.get(childNode);
            if(childPert.getSuccessorPathType() == SuccessorPathType.TYPE_B){
                AbstractPertinentGraph lastChildPert = orderedChildPerts.isEmpty() ? null : orderedChildPerts.get(orderedChildPerts.size()-1);
                if(lastChildPert != null && (lastChildPert.getSuccessorPathType() == SuccessorPathType.TYPE_B || lastChildPert.getTcTreeNodeType() == TCTreeNodeType.TYPE_Q))
                    throw new LDrawingNotPossibleException("P-Node contains either two children assigned with Type-B or one child assigned with Type-B and one Q-Node. Source of P-Node is " + lastChildPert.getSource());
                orderedChildPerts.add(childPert);
            }
        }

        AbstractPertinentGraph lChildPert = orderedChildPerts.get(0);
        AbstractPertinentGraph rChildPert = orderedChildPerts.get(orderedChildPerts.size()-1);
        setSource(lChildPert.getSource());
        setTarget(lChildPert.getTarget());
        setLeftmostVertex(lChildPert.getLeftmostVertex());
        setRightmostVertex(rChildPert.getRightmostVertex());
        setSuccessorPathType(rChildPert.getSuccessorPathType());
        setTcTreeNodeType(TCTreeNodeType.TYPE_P);
    }


    private void augmentGraph(){

        System.out.println("Vertex: " + getSource());
        for(int i = 0; i < orderedChildPerts.size()-1; i++){
            AbstractPertinentGraph childPert1 = orderedChildPerts.get(i);
            AbstractPertinentGraph childPert2 = orderedChildPerts.get(i+1);
            if(childPert1.getRightmostVertex() != childPert2.getLeftmostVertex()) {
                DirectedEdge augmentedEdge = Augmentation.getAugmentation().getAugmentedGraph().addEdge(childPert1.getRightmostVertex(), childPert2.getLeftmostVertex());
                Augmentation.getAugmentation().getAugmentedEdges().add(augmentedEdge);
                System.out.println(    "Insert Edge: " + augmentedEdge);
            }
        }
    }



    public void reconstructEmbedding(){

        for(AbstractPertinentGraph childPert : orderedChildPerts){
            childPert.reconstructEmbedding();
        }
    }
}
