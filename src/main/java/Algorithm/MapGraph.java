package Algorithm;


import Model.Mission;
import Model.Request;
import Model.Segment;
import Model.Map;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm.ManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;


public class MapGraph {
    private static Graph<Long, DefaultWeightedEdge> g;
    private static HashMap<Long, Long> addressPriorities;
    private static HashSet<Long> allAddresses;
    private static DijkstraManyToManyShortestPaths<Long, DefaultWeightedEdge> shortestPathAlgo;
    private static ManyToManyShortestPaths<Long, DefaultWeightedEdge> shortestPaths;
    private static long depotAddress;
    /**
     * Constructor of MapGraph, creates an empty directed weighted map using the jgrapht library
     */
    public MapGraph() {
        g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        addressPriorities = new HashMap<>();
        allAddresses = new HashSet<>();
        depotAddress = -1L;
    }

    public DijkstraManyToManyShortestPaths<Long, DefaultWeightedEdge> getShortestPathAlgo() {
        return shortestPathAlgo;
    }

    public ManyToManyShortestPaths<Long, DefaultWeightedEdge> getShortestPaths() {
        return shortestPaths;
    }

    public HashMap<Long, Long> getAddressPriorities() {
        return addressPriorities;
    }

    public HashSet<Long> getAllAddresses() {
        return allAddresses;
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

    public long getDepotAddress() {
        return depotAddress;
    }

    public void setDepotAddress(long depotAddress) {
        MapGraph.depotAddress = depotAddress;
    }

    /**
     * Add a Segment to the MapGraph without its name
     * @param sourceIntersectionId the origin/source Intersection id
     * @param targetIntersectionId the destination/target Intersection id
     * @param length the length of the Segment, expressed in meters
     */
    public void addEdge(long sourceIntersectionId, long targetIntersectionId, double length) {
        g.addEdge(sourceIntersectionId,targetIntersectionId);
        g.setEdgeWeight(sourceIntersectionId, targetIntersectionId, length);
    }

    /**
     * Calculate the shortest paths among all pickup and delivery
     */
    public static void calculateShortestPaths() {
        shortestPathAlgo = new DijkstraManyToManyShortestPaths<>(g);
        shortestPaths = shortestPathAlgo.getManyToManyPaths(allAddresses, allAddresses);
    }

    public static void reset() {
        g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        depotAddress = -1L;
        if (!addressPriorities.isEmpty()) {
            addressPriorities.clear();
        }
        if (!allAddresses.isEmpty()) {
            allAddresses.clear();
        }

    }

    /**
     * @return the number of vertices in <code>this</code>
     */
//    @Override
    public int getNbVertices() {
        return allAddresses.size();
    }

    /**
     * @param i
     * @param j
     * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
     */
//    @Override
    public double getCost(long i, long j) {
        return shortestPathAlgo.getPathWeight(i, j);
    }

    /**
     * @param i
     * @param j
     * @return true if <code>(i,j)</code> is an arc of <code>this</code>
     */
//    @Override
    public static boolean isArc(long i, long j) {
        return (i != j ) && (shortestPathAlgo.getPathWeight(i, j) < 10000) && (addressPriorities.getOrDefault(j, -1L) != i);
    }

    public void fillGraph(Map map, Mission mission) {
        Set<Long> allIntersections = map.getAllIntersections().keySet();
        LinkedList<Segment> allSegments = map.getAllSegments();
        ArrayList<Request> allRequests = mission.getAllRequests();

        for (Long intersection : allIntersections) {
            addVertex(intersection);
        }
        for (Segment segment : allSegments) {
            addEdge(segment.getOrigin().getId(), segment.getDestination().getId(), segment.getLength());
        }
        for (Request request : allRequests) {
            setAddressPriorities(request.getPickup().getId(), request.getDelivery().getId());
            addAddress(request.getPickup().getId());
            addAddress(request.getDelivery().getId());
        }
        addAddress(mission.getDepot().getId());
        setDepotAddress(mission.getDepot().getId());

    }
}

