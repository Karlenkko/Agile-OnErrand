package Algorithm;

import Model.Intersection;
import Model.Map;
import Model.Request;

import java.util.*;

public interface Graph {
	/**
	 * @return the number of vertices in <code>this</code>
	 */
	public abstract int getNbVertices(boolean recalculate);

	public abstract void fillGraph(Map map);

	public abstract void setRequests(List<Request> allRequests, Intersection depot);

	public abstract void setRecalculatedRequests(ArrayList<Long> addRequestAddressList, LinkedList<Long> tour, Request newRequest);
	
	public abstract void reset();
	
	public abstract void dijkstra(boolean recalculate);

//	public abstract ArrayList<Long> getAllAddresses();

	public abstract ArrayList<Long> getAllAddresses(boolean recalculate);
	
	public abstract boolean filter(Long nextVertex, Collection<Long> unvisited, boolean recalculate);
	/**
	 * @param i 
	 * @param j 
	 * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
	 */
	public abstract double getCost(long i, long j);

	/**
	 * @param i 
	 * @param j 
	 * @return true if <code>(i,j)</code> is an arc of <code>this</code>
	 */
	public abstract boolean isArc(long i, long j);

	public abstract HashMap<String, ArrayList<Long>> getShortestPaths(boolean recalculate);

	public abstract Long getStartAddress(boolean recalculate);

	public abstract void updateGraph();

	public abstract double getMinCost();

	public abstract HashMap<Long, Double> getMinHash();
}
