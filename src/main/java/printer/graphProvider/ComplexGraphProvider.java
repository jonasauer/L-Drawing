package main.java.printer.graphProvider;

import main.java.decomposition.graph.DirectedEdge;
import main.java.decomposition.graph.MultiDirectedGraph;
import main.java.decomposition.hyperGraph.Vertex;

public class ComplexGraphProvider {

    private static MultiDirectedGraph complexGraph = null;
    public static Vertex c1;
    public static Vertex c2;
    public static Vertex c3;
    public static Vertex c4;
    public static Vertex c5;
    public static Vertex c6;
    public static Vertex c7;
    public static Vertex c8;
    public static Vertex c9;
    public static Vertex c10;
    public static DirectedEdge backEdge;

    public static MultiDirectedGraph getComplexGraph(){

        //
        //
        //        |---->c3---->c7------|
        //        |     |      |       |
        //        |     ∨      |      ∨
        //        |---->c4-----|      c9----->c10
        //        |     |      |      ∧       ∧
        //        |     ∨      ∨      |       |
        // c1--->c2---->c5---->c8------|       |
        //  |     |     ∧      ∧              |
        //  |     |     |      |               |
        //  |     |---->c6-----|               |
        //  |                                  |
        //  |-----------------------------------
        //
        //
        //

        if(complexGraph != null)
            return complexGraph;

        complexGraph = new MultiDirectedGraph();

        c1 = new Vertex("1");
        c2 = new Vertex("2");
        c3 = new Vertex("3");
        c4 = new Vertex("4");
        c5 = new Vertex("5");
        c6 = new Vertex("6");
        c7 = new Vertex("7");
        c8 = new Vertex("8");
        c9 = new Vertex("9");
        c10 = new Vertex("10");

        complexGraph.addEdge(c1, c2);
        complexGraph.addEdge(c2, c3);
        complexGraph.addEdge(c2, c4);
        complexGraph.addEdge(c2, c5);
        complexGraph.addEdge(c2, c6);
        complexGraph.addEdge(c3, c4);
        complexGraph.addEdge(c4, c5);
        complexGraph.addEdge(c6, c5);
        complexGraph.addEdge(c3, c7);
        complexGraph.addEdge(c4, c8);
        complexGraph.addEdge(c5, c8);
        complexGraph.addEdge(c6, c8);
        complexGraph.addEdge(c7, c8);
        complexGraph.addEdge(c7, c9);
        complexGraph.addEdge(c8, c9);
        complexGraph.addEdge(c9, c10);

        backEdge = complexGraph.addEdge(c1, c10);

        return complexGraph;
    }
}
