package main.java.algorithm.graphConverter;

public class GraphConverterHolder {

    private static IGraphToMultiDirectedGraphConverter iGraphToMultiDirectedGraphConverter;
    private static MultiDirectedGraphToGraphConverter multiDirectedGraphToGraphConverter;


    public static void setIGraphToMultiDirectedGraphConverter(IGraphToMultiDirectedGraphConverter iGraphToMultiDirectedGraphConverter) {
        GraphConverterHolder.iGraphToMultiDirectedGraphConverter = iGraphToMultiDirectedGraphConverter;
    }

    public static void setMultiDirectedGraphToGraphConverter(MultiDirectedGraphToGraphConverter multiDirectedGraphToGraphConverter) {
        GraphConverterHolder.multiDirectedGraphToGraphConverter = multiDirectedGraphToGraphConverter;
    }





    public static IGraphToMultiDirectedGraphConverter getiGraphToMultiDirectedGraphConverter() {
        return iGraphToMultiDirectedGraphConverter;
    }

    public static MultiDirectedGraphToGraphConverter getMultiDirectedGraphToGraphConverter() {
        return multiDirectedGraphToGraphConverter;
    }
}
