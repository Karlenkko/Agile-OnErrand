package Model;

import IHM.Meituan;

import java.util.HashMap;
import java.util.LinkedList;

public class Map {

    private static HashMap<Long, Intersection> allIntersections;
    private static LinkedList<Segment> allSegments;
    private static LinkedList<Request> allRequests;
    private static double minX;
    private static double minY;
    private static double maxX;
    private static double maxY;


    public Map() {
        allIntersections = new HashMap<>();
        allSegments = new LinkedList<>();
        allRequests = new LinkedList<>();
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        maxX = -Double.MAX_VALUE;
        maxY = -Double.MAX_VALUE;
    }

    public void addIntersection(Intersection intersection) {
        allIntersections.put(intersection.getId(), intersection);
        minX = Math.min(minX, intersection.getX());
        minY = Math.min(minY, intersection.getY());
        maxX = Math.max(maxX, intersection.getX());
        maxY = Math.max(maxY, intersection.getY());
    }

    public void addSegment(Segment segment) {
        allSegments.add(segment);
    }

    public static void reset() {
        allIntersections.clear();
        allSegments.clear();
        Mission.reset();
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        maxX = -Double.MAX_VALUE;
        maxY = -Double.MAX_VALUE;
        Meituan.setPaint(false);
    }

    public HashMap<Long, Intersection> getAllIntersections() {
        return allIntersections;
    }

    public LinkedList<Segment> getAllSegments() {
        return allSegments;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public LinkedList<Request> getAllRequests() {
        return allRequests;
    }

}
