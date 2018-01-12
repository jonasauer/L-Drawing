package main.java.test.graphProvider;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

public class WSDM10GraphProvider {


    private static MultiDirectedGraph WSFM10Graph;
    public static Vertex w1;
    public static Vertex w2;
    public static Vertex w3;
    public static Vertex w4;
    public static Vertex w5;
    public static Vertex w6;
    public static Vertex w7;
    public static Vertex w8;

    public static DirectedEdge backEdge;


    public static MultiDirectedGraph getWSFM10Graph(){

        if(WSFM10Graph != null)
            return WSFM10Graph;

        WSFM10Graph = new MultiDirectedGraph();

        w1 = new Vertex("1");
        w2 = new Vertex("2");
        w3 = new Vertex("3");
        w4 = new Vertex("4");
        w5 = new Vertex("5");
        w6 = new Vertex("6");
        w7 = new Vertex("7");
        w8 = new Vertex("8");

        WSFM10Graph.addEdge(w1, w3);
        WSFM10Graph.addEdge(w3, w4);
        WSFM10Graph.addEdge(w3, w5);
        WSFM10Graph.addEdge(w4, w5);
        WSFM10Graph.addEdge(w4, w6);
        WSFM10Graph.addEdge(w5, w6);
        WSFM10Graph.addEdge(w6, w7);
        WSFM10Graph.addEdge(w7, w8);
        WSFM10Graph.addEdge(w7, w8);
        WSFM10Graph.addEdge(w8, w7);
        WSFM10Graph.addEdge(w8, w2);
        backEdge = WSFM10Graph.addEdge(w2, w1);

        return WSFM10Graph;
    }
}
