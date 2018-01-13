package main.java.decomposition.spqrTree;

import com.yworks.yfiles.graph.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SPQRSkeleton extends DefaultGraph{

    protected Set<IEdge> virtualEdges = new HashSet<>();
    protected Map<IEdge, IEdge> e2o = new HashMap<>();
    protected Map<IEdge, IEdge> o2e = new HashMap<>();


    protected SPQRSkeleton() {
        super();
    }


    protected SPQRSkeleton(IGraph g, Map<IEdge, IEdge> e2o) {
        super();
        for (IEdge edge : g.getEdges()) {
            IEdge newEdge = this.createEdge(edge.getSourceNode(), edge.getTargetNode());
            e2o.put(newEdge, edge);
        }
    }


    protected IEdge addVirtualEdge(INode source, INode target) {
        IEdge e = super.createEdge(source, target);
        if (e != null)
            this.virtualEdges.add(e);

        return e;
    }


    public Set<IEdge> getVirtualEdges() {

        return this.virtualEdges;
    }


    public boolean isVirtual(IEdge e) {

        return this.virtualEdges.contains(e);
    }


    public IEdge addEdge(INode source, INode target, IEdge edge) {
        IEdge newEdge = super.createEdge(source, target);

        if (newEdge!=null) {
            this.e2o.put(newEdge, edge);
            this.o2e.put(edge, newEdge);
        }
        return newEdge;
    }


    public IEdge removeEdge(IEdge edge) {

        this.virtualEdges.remove(edge);
        this.o2e.remove(this.e2o.get(edge));
        this.e2o.remove(edge);
        super.remove(edge);
        return edge;
    }


    public IEdge getOriginalEdge(IEdge edge) {
        return this.e2o.get(edge);
    }


    public Set<IEdge> getOriginalEdges() {

        return this.o2e.keySet();
    }


    public void removeOriginalEdge(IEdge edge) {

        this.removeEdge(this.o2e.get(edge));
    }
}
