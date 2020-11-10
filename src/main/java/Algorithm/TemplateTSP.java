package Algorithm;

import javax.swing.*;
import java.util.*;

public abstract class TemplateTSP implements TSP {
	private Long[] bestSolAddress;
	private double[] bestSolAddressCost;
	private LinkedList<Long> bestSolIntersection;
	protected Graph g;
	private double bestSolCost;
	private int timeLimit;
	private long startTime;
	private boolean until = true;
	private boolean recalculate = false;

	public void setRecalculate(boolean recalculate) {
		this.recalculate = recalculate;
	}
	
	public Long[] searchSolution(int timeLimit, Graph g){
		if (timeLimit <= 0) return null;
		startTime = System.currentTimeMillis();	
		this.timeLimit = timeLimit;
		this.g = g;

		bestSolIntersection = new LinkedList<>();
		bestSolAddress = new Long[g.getNbVertices(recalculate)];
		bestSolAddressCost = new double[g.getNbVertices(recalculate)];
		LinkedList<Long> unvisited = new LinkedList<>();

		for(Long l : g.getAllAddresses(recalculate)) {
			unvisited.add(l);
		}

		unvisited.remove(g.getStartAddress(recalculate));
		LinkedList<Long> visited = new LinkedList<>();
		visited.add(g.getStartAddress(recalculate)); // The first visited vertex is 0
		bestSolCost = Double.MAX_VALUE;
		branchAndBound(g.getStartAddress(recalculate), unvisited, visited, 0);
		//fillTour();
		completeTour();

		for (int i = 0; i < bestSolAddress.length; i ++) {
			bestSolAddressCost[i] = g.getCost(bestSolAddress[i], bestSolAddress[(i + 1 >= bestSolAddress.length? 0 : i + 1)]);
		}
		return bestSolAddress;
	}
	
	public Long getSolution(int i){
		if (g != null && i>=0 && i<g.getNbVertices(recalculate))
			return bestSolAddress[i];
		return -1L;
	}
	
	public double getSolutionCost(){
		if (g != null)
			return bestSolCost;
		return -1;
	}

	public LinkedList<Long> getBestSolIntersection() {
		return bestSolIntersection;
	}

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex the current vertex
	 * @param unvisited the collection of unvisited address vertexes
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract int bound(long currentVertex, Collection<Long> unvisited, Graph g);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex the current vertex
	 * @param unvisited the collection of unvisited address vertexes
	 * @param g the Graph
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */

	protected abstract Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, Graph g);

	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */	
	private void branchAndBound(long currentVertex, Collection<Long> unvisited,
			Collection<Long> visited, double currentCost){
		if (System.currentTimeMillis() - startTime > timeLimit) {
			if (until){
				String time = (String) JOptionPane.showInputDialog(null,"continue to calculate for (seconds)：","Input",JOptionPane.YES_NO_OPTION,null,null,30);
				if (time == null || time.equals("null")) {
					until = false;
					return;
				} else {
					int continueTime = Integer.parseInt(time) * 1000;
					timeLimit += continueTime;
				}
			} else {
				return;
			}
		}
	    if (unvisited.size() == 0){
	    	if (g.isArc(currentVertex,g.getStartAddress(recalculate))){
	    		if (currentCost+g.getCost(currentVertex,g.getStartAddress(recalculate)) < bestSolCost){
	    			visited.toArray(bestSolAddress);
	    			bestSolCost = currentCost+g.getCost(currentVertex,g.getStartAddress(recalculate));
	    		}
	    	}
	    } else if (currentCost+bound(currentVertex,unvisited, g) < bestSolCost){
			Iterator<Long> it = iterator(currentVertex, unvisited, g);
	        while (it.hasNext()){
	        	long nextVertex = it.next();
	        	if(!g.filter(nextVertex, unvisited, recalculate)) {
	        		continue;
				}
	        	visited.add(nextVertex);
	            unvisited.remove(nextVertex);

				branchAndBound(nextVertex, unvisited, visited,
	            		currentCost+g.getCost(currentVertex, nextVertex));
	            visited.remove(nextVertex);
	            unvisited.add(nextVertex);
	        }	    
	    }
	}

	private void completeTour() {
		/*
		int size = bestSolAddress.toIntersectionCosts;
		if (recalculate) {
			--size;
		}

		 */
		for (int i = 1; i < bestSolAddress.length; i++) {
			System.out.println(bestSolAddress[i-1]+" "+bestSolAddress[i]);
			if(!g.getShortestPaths(recalculate).containsKey(bestSolAddress[i-1]+" "+bestSolAddress[i])) {
				Long origin = bestSolAddress[i-1];
				Long destination = bestSolAddress[i];
				g.dijkstra(origin, destination);
			}
			bestSolIntersection.addAll(g.getShortestPaths(recalculate).get(bestSolAddress[i-1]+" "+bestSolAddress[i]));
			bestSolIntersection.remove(bestSolIntersection.size() - 1);
		}
		bestSolIntersection.add(bestSolAddress[bestSolAddress.length-1]);
		if (!recalculate) {
			bestSolIntersection.addAll(g.getShortestPaths(recalculate).get(bestSolAddress[bestSolAddress.length-1]+" "+bestSolAddress[0]));
		}
	}


	/**
	 * get the sequence of costs from one address to another in the solution
	 * @return the sequence of costs
	 */
	public double[] getBestSolAddressCost() {
		return bestSolAddressCost;
	}


}
