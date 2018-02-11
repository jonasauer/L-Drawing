package main.java.algorithm.embedding;

import com.yworks.yfiles.algorithms.*;
import main.java.algorithm.typeDetermination.Face;
import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

import java.util.*;

public class RNodeEmbedding extends AbstractEmbedding{

    private List<Face> convertedFaces;
    private Vertex source;
    private Vertex target;
    private Vertex augmentedSource;

    public RNodeEmbedding(MultiDirectedGraph graph, Vertex augmentedSource, Vertex source, Vertex target){

        super(graph);
        this.source = source;
        this.target = target;
        this.augmentedSource = augmentedSource;
    }




    public List<Face> getFaces(){

        if(convertedFaces != null)
            return convertedFaces;

        convertedFaces = new ArrayList<>();

        for(List<Dart> originalFace : planarEmbedding.getFaces()){
            boolean containsSource = false;
            boolean containsTarget = false;
            boolean containsAugmentedSource = false;
            Face convertedFace = new Face();

            for(Dart dart : originalFace) {
                DirectedEdge edge = convE2OrigE.get(dart.getAssociatedEdge());
                if(edge.getSource().equals(source) || edge.getTarget().equals(source))
                    containsSource = true;
                if(edge.getSource().equals(target) || edge.getTarget().equals(target))
                    containsTarget = true;
                if(edge.getSource().equals(augmentedSource))
                    containsAugmentedSource = true;
                convertedFace.addEdge(edge);
            }
            if((!containsSource || !containsTarget) && !containsAugmentedSource)
                convertedFaces.add(convertedFace);
        }
        return convertedFaces;
    }
}
