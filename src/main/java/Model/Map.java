package Model;

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
     * Constructor of object Map that initializes the intersection list and the segment list
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

    /**
     * Obtain a hashmap which contains all the intersections in the map.
     * The key is the id of the intersection and the value is the intersection.
     * @return allIntersections a hashmap which contains all the intersections in the map.
     */
    public HashMap<Long, Intersection> getAllIntersections() {
        return allIntersections;
    }

    /**
     * Obtain a linkedList which contains all the segments in the map.
     * @return allSegments a linkedList which contains all the segments in the map.
     */
    public LinkedList<Segment> getAllSegments() {
        return allSegments;
    }

    /**
     * Obtain the minimal value for x, the horizontal position that an intersection is actually drawn on the application,
     * for all the intersections in the map
     * @return the minimal value for x
     */
    public double getMinX() {
        return minX;
    }

    /**
     * Obtain the minimal value for y, the vertical position that an intersection is actually drawn on the application,
     * for all the intersections in the map
     * @return the minimal value for y
     */
    public double getMinY() {
        return minY;
    }

    /**
     * Obtain the maximum value for x, the horizontal position that an intersection is actually drawn on the application,
     * for all the intersections in the map
     * @return the maximum value for x
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * Obtain the maximum value for y, the vertical position that an intersection is actually drawn on the application,
     * for all the intersections in the map
     * @return the maximum value for y
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * Prepare and resize all the intersections in the map for drawing them
     * @return delta the difference between the intersections on the extremity of the map
     */
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

    /**
     * Obtain the nearest Intersection to a point given by the parameter
     * @param x the horizontal position in the drawn area
     * @param y the vertical position in the drawn area
     * @param rate the rate which we zoom in or zoom out the map to show on the application
     * @return nearest the nearest Intersection to a point given by the parameter
     */
    public Intersection NearestIntersection(int x, int y, double rate) {
        Intersection nearest = null;
        double gapX = 0;
        double gapY = 0;
        for (Intersection p : allIntersections.values()) {
            if (nearest == null) {
                nearest = p;
                gapX = Math.abs(p.getX()/rate - x);
                gapY = Math.abs(p.getY()/rate - y);
                continue;
            }
            double gapX2 = Math.abs(p.getX()/rate - x);
            double gapY2 = Math.abs(p.getY()/rate - y);
            if ((gapX2 + gapY2) < (gapX + gapY)) {
                nearest = p;
                gapX = gapX2;
                gapY = gapY2;
            }

        }

        return nearest;
    }

}
