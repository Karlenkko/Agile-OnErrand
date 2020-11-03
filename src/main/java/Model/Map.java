package Model;

import IHM.Meituan;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.LinkedList;

public class Map {

    private static HashMap<Long, Intersection> allIntersections;
    private static LinkedList<Segment> allSegments;
    private static double minX;
    private static double minY;
    private static double maxX;
    private static double maxY;

    /**
     * Constructor of object Map that initialize the intersection list and the segment list
     */
    public Map() {
        allIntersections = new HashMap<>();
        allSegments = new LinkedList<>();
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        maxX = -Double.MAX_VALUE;
        maxY = -Double.MAX_VALUE;
    }

    /**
     * Add an Intersection to the map and update the X, Y intervals
     * @param intersection the Intersection that will be added to the map
     */
    public void addIntersection(Intersection intersection) {
        allIntersections.put(intersection.getId(), intersection);
        minX = Math.min(minX, intersection.getX());
        minY = Math.min(minY, intersection.getY());
        maxX = Math.max(maxX, intersection.getX());
        maxY = Math.max(maxY, intersection.getY());

    }

    /**
     * Add a Segment to the map
     * @param segment the Segment that will be added to the map
     */
    public void addSegment(Segment segment) {
        allSegments.add(segment);
    }

    /**
     * reset the map which clears all the Intersections and Segments,
     * then clear the Mission
     */
    public static void reset() {
        allIntersections.clear();
        allSegments.clear();
        Mission.reset();
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        maxX = -Double.MAX_VALUE;
        maxY = -Double.MAX_VALUE;
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

    public double[] resizeIntersection() {
        double deltaX = (maxX - minX);
        double deltaY = (maxY - minY);
        for (Intersection intersection : allIntersections.values()) {
            double x = intersection.getX() - minX;
            double y = intersection.getY() - minY;
            intersection.setX(x);
            intersection.setY(y);
        }
        double[] delta = new double[2];
        delta[0] = deltaX;
        delta[1] = deltaY;
        return delta;
    }

    public Intersection NearestIntersection(int x, int y) {
        Intersection nearest = null;
        double gapX = 0;
        double gapY = 0;
        for (Intersection p : allIntersections.values()) {
            if (nearest == null) {
                nearest = p;
                gapX = Math.abs(p.getX() - x);
                gapY = Math.abs(p.getY() - y);
                continue;
            }
            double gapX2 = Math.abs(p.getX() - x);
            double gapY2 = Math.abs(p.getY() - y);
            if ((gapX2 + gapY2) < (gapX + gapY)) {
                nearest = p;
                gapX = gapX2;
                gapY = gapY2;
            }

        }

        return nearest;
    }

}
