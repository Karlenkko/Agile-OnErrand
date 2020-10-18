package Algorithm;


import org.jgrapht.*;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.alg.shortestpath.*;

import java.util.HashMap;

public class MapGraph {
    private Graph<Integer, DefaultWeightedEdge> g;
    private HashMap<Integer, Integer> addressPriorities;
    private DijkstraManyToManyShortestPaths<Integer, DefaultWeightedEdge> shortestPaths;
    /**
     * Constructor of MapGraph, creates an empty directed weighted map using the jgrapht library
     */
    public MapGraph() {
        g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    /**
     * Add an Intersection to the MapGraph using its id
     * @param intersectionId the id of the Intersection
     */
    public void addVertex(int intersectionId) {
        g.addVertex(intersectionId);
    }

    /**
     * Set tour visiting priority of an address(Intersection)
     * 0 means delivery address, 1 means pickup address, 2 means depot address
     * @param addressIntersectionId the id of the address Intersection
     * @param priority the priority of the address Intersection
     */
    public void setAddressPriority(int addressIntersectionId, int priority) {
        addressPriorities.putIfAbsent(addressIntersectionId, priority);
    }
    /**
     * Add a Segment to the MapGraph without its name
     * @param sourceIntersectionId the origin/source Intersection id
     * @param targetIntersectionId the destination/target Intersection id
     * @param length the length of the Segment, expressed in meters
     */
    public void addEdge(int sourceIntersectionId, int targetIntersectionId, double length) {
        g.setEdgeWeight(sourceIntersectionId, targetIntersectionId, length);
    }

//    public void calculateShortestPaths() {
//        shortestPaths = new DijkstraManyToManyShortestPaths<>(g);
//    }
}
