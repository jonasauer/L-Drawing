package main.java.decomposition.spqrTree.container;


import com.yworks.yfiles.graph.IEdge;
import com.yworks.yfiles.graph.INode;

import java.util.LinkedList;

public class EdgeList<E extends IEdge, N extends INode> extends LinkedList<E> {

    private static final long serialVersionUID = 2649534465829537370L;


    public EdgeList(EdgeList<E, N> list) {
        super(list);
    }


    public EdgeList(E edge) {
        super();
        this.add(edge);
    }


    public EdgeList() {
        super();
    }


    @Override
    public EdgeList<E, N> clone() {
        return new EdgeList<>(this);
    }
}

