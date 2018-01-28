package main.java.algorithm.exception;

public class LDrawingNotPossibleException extends Exception{

    public LDrawingNotPossibleException(String message) {
        super(message);
    }

    public LDrawingNotPossibleException(Throwable cause) {
        super(cause);
    }

    public LDrawingNotPossibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
