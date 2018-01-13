package main.java.decomposition.spqrTree;

import com.yworks.yfiles.graph.DefaultGraph;
import com.yworks.yfiles.graph.IEdge;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.INode;
import main.java.decomposition.spqrTree.container.EdgeList;
import main.java.decomposition.spqrTree.container.EdgeMap;
import main.java.decomposition.spqrTree.container.MetaInfo;
import main.java.decomposition.spqrTree.container.MetaInfoContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class SPQRTree extends DefaultGraph{

    protected IGraph graph = null;
    protected IEdge backEdge = null;
    private Map<IEdge, IEdge> internal2original = new HashMap<>();



    public SPQRTree(IGraph graph) {
        super();
        if (graph==null) return;
        if (graph.getEdges().size() <= 0) return;
        if (graph.getNodes().size() <= 0) return;

        this.graph = graph;
        this.backEdge = graph.getEdges().iterator().next();
        this.construct();
    }


    public SPQRTree(IGraph graph, IEdge backEdge) {
        super();
        if (graph==null) return;
        if (!graph.contains(backEdge)) return;

        this.graph = graph;
        this.backEdge = backEdge;

        this.construct();
    }


    protected void construct() {
        /**Vector<EdgeList<IEdge, INode>> components = new Vector<>();

        EdgeMap<IEdge, INode> virtualEdgeMap = this.createEdgeMap(this.graph);
        virtualEdgeMap.initialiseWithFalse();
        virtualEdgeMap.put(backEdge,true);
        EdgeMap<IEdge, INode> assignedVirtEdgeMap = this.createEdgeMap(this.graph);
        EdgeMap<IEdge, INode> isHiddenMap = this.createEdgeMap(this.graph);
        isHiddenMap.initialiseWithFalse();

        MetaInfoContainer meta = new MetaInfoContainer();
        meta.setMetaInfo(MetaInfo.VIRTUAL_EDGES, virtualEdgeMap);
        meta.setMetaInfo(MetaInfo.ASSIGNED_VIRTUAL_EDGES, assignedVirtEdgeMap);
        meta.setMetaInfo(MetaInfo.HIDDEN_EDGES, isHiddenMap);

        // discover triconnected components
        SPQRSkeleton mainSkeleton = new SPQRSkeleton(this.graph, this.internal2original);
        this.splitOffInitialMultipleEdges(mainSkeleton, components, virtualEdgeMap, assignedVirtEdgeMap, isHiddenMap);
        this.findSplitComponents(mainSkeleton, components, virtualEdgeMap, assignedVirtEdgeMap, isHiddenMap, meta, backEdge.getSourceNode());

        // construct TCTreeNodes and TCSkeletons from components
        for (EdgeList<IEdge, INode> edgeList : components) {
            if (components.size() <= 1)
                continue;
            SPQRTreeNode node = new SPQRTreeNode();
            for (IEdge edge : edgeList) {
                if (virtualEdgeMap.getBool(edge))
                    node.skeleton.addVirtualEdge(edge.getSourceNode(), edge.getTargetNode());
                else
                    node.skeleton.addEdge(edge.getSourceNode(), edge.getTargetNode(), this.internal2original.get(edge));
            }
            this.addVertex(node);
        }

        // classify triconnected components into polygons, bonds, and rigids
        this.classifyComponents();

        // construct index
        Map<Object, Set<SPQRTreeNode>> ve2nodes = new HashMap<>();
        this.indexComponents(ve2nodes);

        // merge bonds and polygons
        this.mergePolygonsAndBonds(ve2nodes);

        // assign names to components
        this.nameComponents();

        // construct the tree of components
        this.constructTree(ve2nodes);
         **/
    }

}
