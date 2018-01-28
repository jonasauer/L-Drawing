package main.java.algorithm.exception;

public class GraphConditionsException extends Exception{

    public GraphConditionsException(String message) {
        super(message);
    }

    public GraphConditionsException(Throwable cause) {
        super(cause);
    }

    public GraphConditionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
