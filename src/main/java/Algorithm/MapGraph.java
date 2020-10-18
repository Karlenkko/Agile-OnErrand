package Algorithm;


import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm.ManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;
import java.util.Set;

public class MapGraph {
    private Graph<Long, DefaultWeightedEdge> g;
    private HashMap<Long, Long> addressPriorities;
    private Set<Long> allAddresses;
    private DijkstraManyToManyShortestPaths<Long, DefaultWeightedEdge> shortestPathAlgo;
    private ManyToManyShortestPaths<Long, DefaultWeightedEdge> shortestPaths;
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
    public void addVertex(long intersectionId) {
        g.addVertex(intersectionId);
    }

    /**
     * Set tour visiting priority of an address(Intersection)
     * 0 means delivery address, 1 means pickup address, 2 means depot address
     * @param pickupAddressId the id of the pickup address Intersection
     * @param deliveryAddressId the id of the delivery address Intersection
     */
    public void setAddressPriorities(long pickupAddressId, long deliveryAddressId) {
        addressPriorities.putIfAbsent(pickupAddressId, deliveryAddressId);
    }

    /**
     * add depot address id, pickup address id and delivery address id to the set
     * @param addressId the Intersection id of an address
     */
    public void addAddress(long addressId) {
        allAddresses.add(addressId);
    }

    /**
     * Add a Segment to the MapGraph without its name
     * @param sourceIntersectionId the origin/source Intersection id
     * @param targetIntersectionId the destination/target Intersection id
     * @param length the length of the Segment, expressed in meters
     */
    public void addEdge(long sourceIntersectionId, long targetIntersectionId, double length) {
        g.setEdgeWeight(sourceIntersectionId, targetIntersectionId, length);
    }

    /**
     * Calculate the shortest paths among all pickup and delivery
     */
    public void calculateShortestPaths() {
        shortestPathAlgo = new DijkstraManyToManyShortestPaths<>(g);
        shortestPaths = shortestPathAlgo.getManyToManyPaths(allAddresses, allAddresses);
    }
}

