package Algorithm;


import Model.*;
import Model.Map;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm.ManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;


public class JgraphtMapGraph implements Algorithm.Graph {
    private static Graph<Long, DefaultWeightedEdge> g;
//    private static HashMap<Long, Long> addressPriorities;
    private static HashSet<Long> allAddresses;
    private static DijkstraManyToManyShortestPaths<Long, DefaultWeightedEdge> shortestPathAlgo;
    private static ManyToManyShortestPaths<Long, DefaultWeightedEdge> shortestPaths;
    public static double minCost;
//    private static long depotAddress;

    /**
     * Constructor of JgraphtMapGraph, creates an empty directed weighted map using the jgrapht library
     */
    public JgraphtMapGraph() {
//        super();
        g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
//        addressPriorities = new HashMap<>();
        allAddresses = new HashSet<>();
//        depotAddress = -1L;
    }

    @Override
    public void reset(){
//        super.reset();
        g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        allAddresses.clear();
    }

    /**
     * @param recalculate
     * @return the number of vertices in <code>this</code>
     */
    @Override
    public int getNbVertices(boolean recalculate) {
        return 0;
    }

    @Override
    public void fillGraph(Map map) {
        for (Segment segment : map.getAllSegments()) {
            g.addVertex(segment.getOrigin().getId());
            g.addVertex(segment.getDestination().getId());
            addEdge(segment.getOrigin().getId(), segment.getDestination().getId(), segment.getLength());
        }
    }

    /**
     * Add a Segment to the JgraphtMapGraph without its name
     * @param sourceIntersectionId the origin/source Intersection id
     * @param targetIntersectionId the destination/target Intersection id
     * @param length the toIntersectionCosts of the Segment, expressed in meters
     */
    private void addEdge(long sourceIntersectionId, long targetIntersectionId, double length) {
        g.addEdge(sourceIntersectionId,targetIntersectionId);
        g.setEdgeWeight(sourceIntersectionId, targetIntersectionId, length);
    }

    @Override
    public void setRequests(List<Request> allRequests, Intersection depot) {
//        super.setRequests(allRequests, depot);
        allAddresses.add(depot.getId());
        for (Request request : allRequests) {
            allAddresses.add(request.getDelivery().getId());
            allAddresses.add(request.getPickup().getId());
        }
    }

    /**
     * Calculate the shortest paths among all pickup and delivery
     */
    public void dijkstra(boolean recalculate) {
        shortestPathAlgo = new DijkstraManyToManyShortestPaths<>(g);
        shortestPaths = shortestPathAlgo.getManyToManyPaths(allAddresses, allAddresses);
        for (Long address : allAddresses) {
            for (Long anotherAddress : allAddresses) {
//                if (address.equals(anotherAddress)) {
//                    continue;
//                }
                ArrayList<Long> shortestRoute = new ArrayList<>();
                GraphPath<Long, DefaultWeightedEdge> graphPath = shortestPaths.getPath(address, anotherAddress);
//                shortestRoute.addAll(graphPath.getVertexList());
//                solutions.put(address + " " + anotherAddress, shortestRoute);
//                graph[requests.indexOf(address)][requests.indexOf(anotherAddress)] = shortestPaths.getWeight(address, anotherAddress);
                if (minCost > shortestPaths.getWeight(address, anotherAddress)) {
                    minCost = shortestPaths.getWeight(address, anotherAddress);
                }
            }
        }
    }



    /**
     * @param origin
     * @param destination
     * @return the cost of arc (i,j) if (i,j) is an arc
     */
    @Override
    public double getCost(long origin, long destination) {
        return shortestPaths.getWeight(origin, destination);
    }

    /**
     * @param i
     * @param j
     * @return true if <code>(i,j)</code> is an arc of <code>this</code>
     */
    @Override
    public boolean isArc(long i, long j) {
        return false;
    }


}

