package main.java.algorithm.utils;

import java.util.HashMap;
import java.util.Map;

public class TimeMeasurement {


    private static Map<Object, Long> startNanos = new HashMap<>();

    public static void reset(){
        startNanos = new HashMap<>();
    }

    public static void startMeasure(Object object){
        startNanos.put(object, System.nanoTime());
    }

    public static double endMeasure(Object object){
        long endTime = System.nanoTime();
        long startTime = startNanos.get(object);
        startNanos.remove(object);
        return (double)(endTime - startTime)/1_000_000.0;
    }
}
