package main.java.algorithm.holder;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.decomposition.spqrTree.TCTreeNodeType;

import java.util.*;

public class SourceSinkPertinentGraphsHolder {

    private Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> sourceNodes;
    private Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> sinkNodes;

    public SourceSinkPertinentGraphsHolder(){

        this.sourceNodes = new HashMap<>();
        this.sinkNodes = new HashMap<>();

        determineSourcesAndSinks();
    }

    private void determineSourcesAndSinks(){

        List<TCTreeNode<DirectedEdge, Vertex>> postOrderNodes = HolderProvider.getPostOrderNodesHolder().getPostOrderNodes();

        for(TCTreeNode<DirectedEdge, Vertex> node : postOrderNodes){

            if(node.getType().equals(TCTreeNodeType.TYPE_Q)){

                DirectedEdge e = node.getSkeleton().getOriginalEdges().iterator().next();
                sourceNodes.put(node, e.getSource());
                sinkNodes.put(node, e.getTarget());

            }else {

                MultiDirectedGraph pert = HolderProvider.getPertinentGraphHolder().getPertinentGraphs().get(node);
                Collection<Vertex> pertVertices = pert.getVertices();
                Collection<DirectedEdge> pertEdges = pert.getEdges();
                Collection<Vertex> potentialSources = new HashSet<>(pertVertices);
                Collection<Vertex> potentialSinks = new HashSet<>(pertVertices);

                for(Vertex vertex : pertVertices){
                    for(DirectedEdge edge : pertEdges){


                        if(edge.getTarget().equals(vertex))
                            potentialSources.remove(vertex);
                        if(edge.getSource().equals(vertex))
                            potentialSinks.remove(vertex);
                    }
                }

                sourceNodes.put(node, potentialSources.iterator().next());
                sinkNodes.put(node, potentialSinks.iterator().next());
            }
        }
    }

    public Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> getSourceNodes() {
        return sourceNodes;
    }

    public Map<TCTreeNode<DirectedEdge, Vertex>, Vertex> getSinkNodes() {
        return sinkNodes;
    }
}
