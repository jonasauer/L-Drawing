package main.java.decomposition.spqrTree;

import com.yworks.yfiles.graph.SimpleNode;

public class SPQRTreeNode extends SimpleNode{

    protected SPQRTreeNodeType type = SPQRTreeNodeType.UNDEFINED;
    protected SPQRSkeleton skeleton = new SPQRSkeleton();


    public SPQRTreeNodeType getType() {

        return this.type;
    }


    public SPQRSkeleton getSkeleton() {

        return this.skeleton;
    }


    @Override
    public String toString() {

        return this.getLabels().first().getText() + " - skeleton: " + this.skeleton + " virtual: " + this.skeleton.virtualEdges;
    }
}
