package main.java.decomposition.spqrTree.container;

import com.yworks.yfiles.graph.IEdge;
import com.yworks.yfiles.graph.INode;

import java.util.HashMap;

public class EdgeMap<E extends IEdge, N extends INode> extends HashMap<E, Object> {

    private static final long serialVersionUID = -3122883772335954023L;


    public int getInt(E edge) {
        return (Integer) this.get(edge);
    }


    public void setInt(E edge, int i) {
        this.put(edge, i);
    }


    public boolean getBool(E edge) {
        if (this.get(edge) == null)
            return false;
        return (Boolean) this.get(edge);
    }


    public void setBool(E edge, boolean flag) {
        this.put(edge, flag);
    }


    public void initialiseWithFalse() {
        for (E edge : this.keySet()) {
            this.put(edge, false);
        }
    }
}
