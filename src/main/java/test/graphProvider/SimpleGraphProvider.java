package main.java.test.graphProvider;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

public class SimpleGraphProvider{

    private static MultiDirectedGraph simpleGraph = null;
    public static Vertex s1;
    public static Vertex s2;
    public static Vertex s3;
    public static Vertex s4;
    public static Vertex s5;
    public static Vertex s6;
    public static Vertex s7;
    public static Vertex s8;
    public static Vertex s9;
    public static DirectedEdge backEdge;


    public static MultiDirectedGraph getSimpleGraph(){

        //		  --- t3 --- t4 ---
        //		  |				  |
        // t1 -- t2 ------------ t5 -- t9
        //	.	  |				  |		.
        //	.	  |_ t6 ---- t7 __|		.
        // 	.		  |_ t8 _|			.
        //	.............................

        if(simpleGraph != null)
            return simpleGraph;

        simpleGraph = new MultiDirectedGraph();

       s1 = new Vertex("1");
       s2 = new Vertex("2");
       s3 = new Vertex("3");
       s4 = new Vertex("4");
       s5 = new Vertex("5");
       s6 = new Vertex("6");
       s7 = new Vertex("7");
       s8 = new Vertex("8");
       s9 = new Vertex("9");

        simpleGraph.addEdge(s1, s2);
        simpleGraph.addEdge(s2, s3);
        simpleGraph.addEdge(s2, s6);
        simpleGraph.addEdge(s2, s5);
        simpleGraph.addEdge(s3, s4);
        simpleGraph.addEdge(s4, s5);
        simpleGraph.addEdge(s6, s7);
        simpleGraph.addEdge(s6, s8);
        simpleGraph.addEdge(s8, s7);
        simpleGraph.addEdge(s7, s5);
        simpleGraph.addEdge(s5, s9);
        simpleGraph.addEdge(s1, s9);
        backEdge = simpleGraph.addEdge(s1, s9);

        return simpleGraph;
    }
}
