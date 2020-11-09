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
    private static HashMap<Long, Long> requestPairs;
    private static ArrayList<Long> allAddresses;
    private static ArrayList<Long> recalculatedRequests;
    private static DijkstraManyToManyShortestPaths<Long, DefaultWeightedEdge> shortestPathAlgo;
    private static ManyToManyShortestPaths<Long, DefaultWeightedEdge> jgraphtShortestPaths;
    protected HashMap<String, ArrayList<Long>> shortestPaths;
    protected double minCost;
    protected HashMap<Long, Double> minHash;
//    private static long depotAddress;

    /**
     * Constructor of JgraphtMapGraph, creates an empty directed weighted map using the jgrapht library
     */
    public JgraphtMapGraph() {
//        super();
        g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
//        addressPriorities = new HashMap<>();
        shortestPaths = new HashMap<>();
        allAddresses = new ArrayList<>();
        recalculatedRequests = new ArrayList<>();
        requestPairs = new HashMap<>();
        minCost = Double.MAX_VALUE;
        minHash = new HashMap<>();
//        depotAddress = -1L;
    }

    @Override
    public void reset(){
//        super.reset();
        g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        allAddresses.clear();
        recalculatedRequests.clear();
        requestPairs.clear();
        shortestPaths.clear();
        minCost = Double.MAX_VALUE;
        minHash.clear();
    }

    @Override
    public ArrayList<Long> getAllAddresses(boolean recalculate) {
        if (recalculate) {
            return recalculatedRequests;
        }
        return allAddresses;
    }

//    @Override
//    public ArrayList<Long> getAllAddresses() {
//        return new ArrayList<>(allAddresses);
//    }


    /**
     * @param recalculate
     * @return the number of vertices in <code>this</code>
     */
    @Override
    public int getNbVertices(boolean recalculate) {
        return allAddresses.size();
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
        allAddresses.add(depot.getId());
        for (Request request : allRequests) {
            allAddresses.add(request.getDelivery().getId());
            allAddresses.add(request.getPickup().getId());
            this.requestPairs.put(request.getPickup().getId(),request.getDelivery().getId());
        }
    }

    @Override
    public void setRecalculatedRequests(ArrayList<Long> addRequestAddressList, LinkedList<Long> tour, Request newRequest) {
        Long before = addRequestAddressList.get(0);
        Long after = addRequestAddressList.get(3);
        this.requestPairs.put(newRequest.getPickup().getId(),newRequest.getDelivery().getId());
        recalculatedRequests.add(addRequestAddressList.get(0));
        recalculatedRequests.add(addRequestAddressList.get(1));
        recalculatedRequests.add(addRequestAddressList.get(2));
        boolean add = false;

        for (Long aLong : tour) {
            if (add) {
                recalculatedRequests.add(aLong);
            }
            if (aLong.equals(before)) {
                add = true;
            }
            if (aLong.equals(after)) {
                add = false;
            }
        }
    }

    /**
     * Calculate the shortest paths among all pickup and delivery
     */
    public void dijkstra(boolean recalculate) {
        shortestPathAlgo = new DijkstraManyToManyShortestPaths<>(g);
        HashSet<Long> addressSet = new HashSet<>(allAddresses);
        if (recalculate) {
            addressSet.addAll(recalculatedRequests);
        }
        jgraphtShortestPaths = shortestPathAlgo.getManyToManyPaths(addressSet, addressSet);
        for (Long address : addressSet) {
            for (Long anotherAddress : addressSet) {
//                if (address.equals(anotherAddress)) {
//                    continue;
//                }
                ArrayList<Long> shortestRoute = new ArrayList<>();
                GraphPath<Long, DefaultWeightedEdge> graphPath = jgraphtShortestPaths.getPath(address, anotherAddress);
                shortestRoute.addAll(graphPath.getVertexList());
                shortestPaths.put(address + " " + anotherAddress, shortestRoute);
//                graph[requests.indexOf(address)][requests.indexOf(anotherAddress)] = jgraphtShortestPaths.getWeight(address, anotherAddress);
                if (!address.equals(anotherAddress)) {
                    if (minCost > jgraphtShortestPaths.getWeight(address, anotherAddress)){
                        minCost = jgraphtShortestPaths.getWeight(address, anotherAddress);
                    }
                    updateMinHash(anotherAddress, jgraphtShortestPaths.getWeight(address, anotherAddress));
                }
            }
        }
    }

    private void updateMinHash(Long id, Double length) {
        if (minHash.containsKey(id)) {
            if (minHash.get(id) > length) {
                minHash.put(id, length);
            }
        } else {
            minHash.put(id, length);
        }
    }


    /**
     * @param origin
     * @param destination
     * @return the cost of arc (i,j) if (i,j) is an arc
     */
    @Override
    public double getCost(long origin, long destination) {
        return jgraphtShortestPaths.getWeight(origin, destination);
    }

    /**
     * @param origin
     * @param destination
     * @return true if <code>(i,j)</code> is an arc of <code>this</code>
     */
    @Override
    public boolean isArc(long origin, long destination) {
        int i = allAddresses.indexOf(origin);
        int j = allAddresses.indexOf(destination);
        if (i == -1 || j == -1) {
            i = recalculatedRequests.indexOf(origin);
            j = recalculatedRequests.indexOf(destination);
            if (i<0 || i>=recalculatedRequests.size() || j<0 || j>=recalculatedRequests.size())
                return false;
            return i != j;
        }
        if (i<0 || i>=getNbVertices(false) || j<0 || j>=getNbVertices(false))
            return false;
        return i != j;
    }

    @Override
    public HashMap<String, ArrayList<Long>> getShortestPaths(boolean recalculate) {
        return shortestPaths;
    }

    @Override
    public Long getStartAddress(boolean recalculate) {
        if (recalculate) {
            return recalculatedRequests.get(0);
        }
        return allAddresses.get(0);
    }

    @Override
    public void updateGraph() {
        allAddresses.add(recalculatedRequests.get(1));
        allAddresses.add(recalculatedRequests.get(2));

        recalculatedRequests.clear();
    }

    @Override
    public double getMinCost() {
        return minCost;
    }

    @Override
    public HashMap<Long, Double> getMinHash() {
        return minHash;
    }

    @Override
    public Long getDelivery(Long pickup) {
        return requestPairs.get(pickup);
    }

    @Override
    public List<Long> getRoute(Long[] bestSolAddress) {
        LinkedList<Long> bestSolIntersection = new LinkedList<>();
        for (int i = 1; i < bestSolAddress.length; i++) {
            System.out.println(bestSolAddress[i-1]+" "+bestSolAddress[i]);
            bestSolIntersection.addAll(getShortestPaths(false).get(bestSolAddress[i-1]+" "+bestSolAddress[i]));
            bestSolIntersection.remove(bestSolIntersection.size() - 1);
        }
        bestSolIntersection.add(bestSolAddress[bestSolAddress.length-1]);
        return  bestSolIntersection;
    }

    @Override
    public double[] getSolutionCost(Long[] bestSolAddress) {
        double[] bestSolAddressCost = new double[bestSolAddress.length];
        for (int i = 0; i < bestSolAddress.length; i ++) {
            bestSolAddressCost[i] = getCost(bestSolAddress[i], bestSolAddress[(i + 1 >= bestSolAddress.length? 0 : i + 1)]);
        }
        return bestSolAddressCost;
    }

    @Override
    public void showPaths() {
        for (String s : shortestPaths.keySet()) {
            System.out.println(s+" : "+shortestPaths.get(s).toString());
        }
    }

    public boolean filter(Long nextVertex, Collection<Long> unvisited, boolean recalculate) {
        if (recalculate) {
            if (nextVertex.equals(recalculatedRequests.get(recalculatedRequests.size()-1)) && unvisited.size()!=1){
                return false;
            }
        }

        if (requestPairs.containsKey(nextVertex)){
            return true;
        } else {
            for(Long origin : requestPairs.keySet()) {
                if(requestPairs.get(origin).equals(nextVertex)) {
                    return !unvisited.contains(origin);
                }
            }
        }
        return true;
    }

}

