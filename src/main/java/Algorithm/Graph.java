package Algorithm;

import Model.Intersection;
import Model.Map;
import Model.Request;

import java.util.*;

public interface Graph {
	/**
	 * @return the number of vertices in <code>this</code>
	 */
	int getNbVertices(boolean recalculate);

	/**
	 * convert a real map of a city into an abstract representation of Graph,
	 * the very first step after creating a Graph instance
	 * @param map the map of a city
	 */
	void fillGraph(Map map);

	/**
	 * fill the requests as well as the depot point into the Graph,
	 * the very second step after converting the map into the Graph
	 * @param allRequests a list of all requests
	 * @param depot the Intersection of the depot address
	 */
	void fillMission(List<Request> allRequests, Intersection depot);

	/**
	 * set the newly added request to the Graph in the AddRequestState
	 * @param addRequestAddressList a list of the manually selected 4 addresses on the map
	 * @param tour the current tour before the addition
	 * @param newRequest the newly added Request
	 */
	void setRecalculatedAddresses(ArrayList<Long> addRequestAddressList, LinkedList<Long> tour, Request newRequest);

	/**
	 * reset the whole Graph
	 */
	void reset();

	/**
	 * calculate the shortest paths among all addresses (pickup, delivery, depot),
	 * the parameter recalculate will further calculate for the newly added addresses
	 * @param recalculate indicator whether the operation is for the addition of a new request
	 */
	void dijkstra(boolean recalculate);

	/**
	 * calculate a single shortest path from the origin to the destination and store it
	 * @param origin the Intersection id of the origin address
	 * @param destination the Intersection id of the destination address
	 * @return the cost of this shortest path from the origin to the destination
	 */
	double dijkstra(Long origin, Long destination);

	/**
	 * return all the addresses for the current calculation
	 * @param recalculate indicator of the calculation mode
	 * @return a list of addresses, can either be the original one or the latest added one
	 */
	ArrayList<Long> getAllAddresses(boolean recalculate);

	/**
	 * verifies that the next vertex is not a delivery address for a pickup address that is not yet visited
	 * @param nextVertex the id of the next address vertex
	 * @param unvisited the collection of ids of the unvisited address vertexes
	 * @param recalculate the calculation mode indicator
	 * @return indicator whether the next vertex should be added or not
	 */
	boolean filter(Long nextVertex, Collection<Long> unvisited, boolean recalculate);

	/**
	 * get the cost from address i to address j
	 * @param i the id of the starting address
	 * @param j the id of the ending address
	 * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
	 */
	double getCost(long i, long j);

	/**
	 * indicate whether i, j is an arc
	 * @param i the id of the starting address
	 * @param j the id of the ending address
	 * @return true if <code>(i,j)</code> is an arc of <code>this</code>
	 */
	boolean isArc(long i, long j);

	/**
	 * get all the paths of passing intersections for every address to another,
	 * the String combines the starting and the ending address as an identifier
	 * @param recalculate the indicator of the mode
	 * @return the HashMap of all Shortest Paths
	 */
	HashMap<String, ArrayList<Long>> getShortestPaths(boolean recalculate);

	/**
	 * get the starting address for the current calculation,
	 * can be either the depot address or the starting address of an addition of request
	 * @param recalculate the indicator of the calculation mode
	 * @return the id of the starting address
	 */
	Long getStartAddress(boolean recalculate);

	/**
	 * update the current Graph after an addition of a new request
	 */
	void updateGraph();

	/**
	 * get the smallest cost among all pairs of addresses
	 * @return the smallest cost value
	 */
	double getMinCost();

	/**
	 * get the smallest inbound costs of all addresses, the Long is the id of a destination address,
	 * used by TSP2
	 * @return the HashMap of smallest inbound costs
	 */
	HashMap<Long, Double> getMinArrivalCosts();

	/**
	 * get the id of the delivery address of a pickup address
	 * @param pickup the id of the pickup address
	 * @return the delivery address, or null if it cannot be found
	 */
	Long getDelivery(Long pickup);

	List<Long> getRoute(Long[] bestSolAddress);

	double[] getSolutionCost(Long[] bestSolAddress);

	boolean isReachable(Long id);

}
