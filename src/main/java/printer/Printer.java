package main.java.printer;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;
import main.java.decomposition.spqrTree.TCTreeNode;
import main.java.algorithm.holder.HolderProvider;
import main.java.algorithm.holder.PertinentGraphHolder;
import main.java.algorithm.holder.PostOrderNodesHolder;

import java.util.List;

public class Printer {

    private static int dfsDepth;





    public static void printPertinentGraphsAndSkeletons(MultiDirectedGraph graph, DirectedEdge backEdge){

        TCTree<DirectedEdge, Vertex> tctree = new TCTree<>(graph, backEdge);

        HolderProvider.setPostOrderNodesHolder(new PostOrderNodesHolder(tctree));
        HolderProvider.setPertinentGraphHolder(new PertinentGraphHolder(tctree));
        List<TCTreeNode<DirectedEdge, Vertex>> postOrderList = HolderProvider.getPostOrderNodesHolder().getPostOrderNodes();

        for(TCTreeNode<DirectedEdge, Vertex> node : postOrderList){

            System.out.println(node.getType());
            System.out.println("    Skeleton: " + node.getSkeleton().toString().replace("-", "->"));
            System.out.println("    Pert    : " + HolderProvider.getPertinentGraphHolder().getPertinentGraph(node));
        }
    }





    public static void printTreePreOrder(MultiDirectedGraph graph, DirectedEdge backEdge){

        System.out.println(PrintColors.ANSI_RESET + "---------------------------");
        System.out.println(PrintColors.ANSI_RESET + "TCTree:");
        dfsDepth = -1;
        TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(graph, backEdge);
        TCTreeNode root = tcTree.getRoot();
        treePreOrderDFS(tcTree, root);
    }

    public static void printTreePreOrder(TCTree<DirectedEdge, Vertex> tcTree){

        System.out.println(PrintColors.ANSI_RESET + "---------------------------");
        System.out.println(PrintColors.ANSI_RESET + "TCTree:");
        dfsDepth = -1;
        TCTreeNode root = tcTree.getRoot();
        treePreOrderDFS(tcTree, root);
    }

    private static void treePreOrderDFS(TCTree tcTree, TCTreeNode node){
        dfsDepth++;

        System.out.print(PrintColors.ANSI_RESET + "    ");
        for(int i = 0; i < dfsDepth; i++) {
            if(i < dfsDepth-1)
                System.out.print(PrintColors.ANSI_RESET + "|   ");
            else
                System.out.print(PrintColors.ANSI_RESET + "|-> ");
        }
        System.out.println(PrintColors.ANSI_RESET + node.getType() +  "    Skeleton: " + node.getSkeleton().toString().replace(",", "").replace("-", "->"));

        for(Object o : tcTree.getChildren(node)){
            TCTreeNode n = (TCTreeNode)o;
            treePreOrderDFS(tcTree, n);
        }
        dfsDepth--;
    }




    public static void printSuccessorTypes(TCTree<DirectedEdge, Vertex> tcTree){

        dfsDepth = -1;

        if(HolderProvider.getSuccessorPathTypeHolder() == null)
            return;

        TCTreeNode root = tcTree.getRoot();
        successorTypesDFS(tcTree, root);
    }

    private static void successorTypesDFS(TCTree<DirectedEdge, Vertex> tcTree, TCTreeNode<DirectedEdge, Vertex> node){

        dfsDepth++;

        for(int i = 0; i < dfsDepth; i++) {
            if(i < dfsDepth-1)
                System.out.print("|   ");
            else
                System.out.print("|-> ");
        }
        System.out.println(node.getType() +  "    " + HolderProvider.getSuccessorPathTypeHolder().getNodeTypes().get(node));

        for(Object o : tcTree.getChildren(node)){
            TCTreeNode n = (TCTreeNode)o;
            successorTypesDFS(tcTree, n);
        }
        dfsDepth--;
    }




    public static void printSTOrdering(){
        int counter = 0;
        System.out.println(PrintColors.ANSI_PURPLE + "---------------------------");
        System.out.println(PrintColors.ANSI_PURPLE + "ST-Ordering (y-Coordinates Ordering)");
        for(Vertex vertex : HolderProvider.getStOrderingHolder().getSTOrderingList())
            System.out.println(PrintColors.ANSI_PURPLE + "    Vertex " + vertex.getName() + ":\t" + counter++);
    }


}
