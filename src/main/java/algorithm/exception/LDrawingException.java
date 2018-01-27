package main.java.algorithm.exception;

public class LDrawingException extends Exception{

    public LDrawingException(String message) {
        super(message);
    }

    public LDrawingException(Throwable cause) {
        super(cause);
    }

    public LDrawingException(String message, Throwable cause) {
        super(message, cause);
    }
}
