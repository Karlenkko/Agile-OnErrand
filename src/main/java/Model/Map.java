package Model;

import Data.XmlLoader;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class Map {

    private static HashMap<Long, Intersection> allIntersections;
    private static LinkedList<Segment> allSegments;
    private static LinkedList<Request> allRequests;
    private static String startTime;
    private static XmlLoader xmlLoader;
    private static double minX;
    private static double minY;
    private static double maxX;
    private static double maxY;

    /**
     * Constructor of object Map, creates an empty map
     * @throws ParserConfigurationException
     */
    public Map() throws ParserConfigurationException {
        xmlLoader = new XmlLoader();
        allIntersections = new HashMap<Long, Intersection>();
        allSegments = new LinkedList<Segment>();
        allRequests = new LinkedList<Request>();
    }

    /**
     * load a map using the xmlLoader class
     * @throws Exception
     */
    public void loadMap() throws Exception {
        double[] param = xmlLoader.parseMap(allIntersections, allSegments);

        if(param[0] == param[2]) {  // the case when user cancel the selection of file
            return;
        }

        minX = param[0];
        minY = param[1];
        maxX = param[2];
        maxY = param[3];

    }

    public void loadRequests() throws Exception {
        xmlLoader.parseRequest(allIntersections, allRequests, startTime);

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

    public String getStartTime() {
        return startTime;
    }
}
