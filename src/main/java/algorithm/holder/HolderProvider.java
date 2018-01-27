package main.java.algorithm.holder;

public class HolderProvider {

    private static PertinentGraphHolder pertinentGraphHolder;
    private static SourceTargetGraphHolder sourceTargetGraphHolder;
    private static SourceTargetPertinentGraphsHolder sourceTargetPertinentGraphsHolder;
    private static SuccessorPathTypeHolder successorPathTypeHolder;
    private static PostOrderNodesHolder postOrderNodesHolder;
    private static AugmentationHolder augmentationHolder;
    private static EmbeddingHolder embeddingHolder;




    public static PertinentGraphHolder getPertinentGraphHolder() {
        return pertinentGraphHolder;
    }

    public static SourceTargetGraphHolder getSourceTargetGraphHolder() {
        return sourceTargetGraphHolder;
    }

    public static SourceTargetPertinentGraphsHolder getSourceTargetPertinentGraphsHolder() {
        return sourceTargetPertinentGraphsHolder;
    }

    public static SuccessorPathTypeHolder getSuccessorPathTypeHolder() {
        return successorPathTypeHolder;
    }

    public static PostOrderNodesHolder getPostOrderNodesHolder(){
        return postOrderNodesHolder;
    }

    public static AugmentationHolder getAugmentationHolder(){
        return augmentationHolder;
    }

    public static EmbeddingHolder getEmbeddingHolder(){
        return embeddingHolder;
    }




    public static void setPertinentGraphHolder(PertinentGraphHolder pertinentGraphHolder) {
        HolderProvider.pertinentGraphHolder = pertinentGraphHolder;
    }

    public static void setSourceTargetGraphHolder(SourceTargetGraphHolder sourceTargetGraphHolder) {
        HolderProvider.sourceTargetGraphHolder = sourceTargetGraphHolder;
    }

    public static void setSourceTargetPertinentGraphsHolder(SourceTargetPertinentGraphsHolder sourceTargetPertinentGraphsHolder) {
        HolderProvider.sourceTargetPertinentGraphsHolder = sourceTargetPertinentGraphsHolder;
    }

    public static void setSuccessorPathTypeHolder(SuccessorPathTypeHolder successorPathTypeHolder) {
        HolderProvider.successorPathTypeHolder = successorPathTypeHolder;
    }

    public static void setPostOrderNodesHolder(PostOrderNodesHolder postOrderNodesHolder){
        HolderProvider.postOrderNodesHolder = postOrderNodesHolder;
    }

    public static void setAugmentationHolder(AugmentationHolder augmentationHolder){
        HolderProvider.augmentationHolder = augmentationHolder;
    }

    public static void setEmbeddingHolder(EmbeddingHolder embeddingHolder){
        HolderProvider.embeddingHolder = embeddingHolder;
    }
}
