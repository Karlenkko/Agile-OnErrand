package Algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {
	private Long[] bestSolAddress;
	private ArrayList<Long> bestSolIntersection;
	protected MapGraph g;
	private double bestSolCost;
	private int timeLimit;
	private long startTime;
	
	public void searchSolution(int timeLimit, MapGraph g){
		if (timeLimit <= 0) return;
		startTime = System.currentTimeMillis();	
		this.timeLimit = timeLimit;
		this.g = g;

		bestSolIntersection = new ArrayList<>();
		bestSolAddress = new Long[g.getNbVertices()];
//		Collection<Long> unvisited = new ArrayList<Long>(g.getNbVertices()-1);
//		for (int i=1; i<g.getNbVertices(); i++) unvisited.add((long)i);
		Collection<Long> unvisited = g.getAllAddresses();
		unvisited.remove(g.getDepotAddress());
		Collection<Long> visited = new ArrayList<Long>(g.getNbVertices());
		visited.add(g.getDepotAddress()); // The first visited vertex is 0
		bestSolCost = Double.MAX_VALUE;
		branchAndBound(g.getDepotAddress(), unvisited, visited, 0);
		for (int i = 0; i < bestSolAddress.length; i++) {
			System.out.println(bestSolAddress[i]);

		}
		fillTour();

		for (int i = 0; i < bestSolAddress.length; i ++) {
			System.out.print((long)bestSolAddress[i]);
			System.out.print("  ,");
		}
		System.out.println();
		System.out.println(bestSolIntersection);
	}
	
	public Long getSolution(int i){
		if (g != null && i>=0 && i<g.getNbVertices())
			return bestSolAddress[i];
		return -1L;
	}
	
	public double getSolutionCost(){
		if (g != null)
			return bestSolCost;
		return -1;
	}

	public ArrayList<Long> getBestSolIntersection() {
		return bestSolIntersection;
	}

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract double bound(long currentVertex, Collection<Long> unvisited);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, MapGraph g);
	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */	
	private void branchAndBound(long currentVertex, Collection<Long> unvisited,
			Collection<Long> visited, double currentCost){
		if (System.currentTimeMillis() - startTime > timeLimit) return;
	    if (unvisited.size() == 0){ 
	    	if (MapGraph.isArc(currentVertex,g.getDepotAddress())){
	    		if (currentCost+g.getCost(currentVertex,g.getDepotAddress()) < bestSolCost){
	    			visited.toArray(bestSolAddress);
	    			bestSolCost = currentCost+g.getCost(currentVertex,g.getDepotAddress());
	    		}
	    	}
	    } else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){
	        Iterator<Long> it = iterator(currentVertex, unvisited, g);
	        while (it.hasNext()){
	        	long nextVertex = it.next();
	        	visited.add(nextVertex);
	            unvisited.remove(nextVertex);
	            branchAndBound(nextVertex, unvisited, visited, 
	            		currentCost+(int)g.getCost(currentVertex, nextVertex));
	            visited.remove(nextVertex);
	            unvisited.add(nextVertex);
	        }	    
	    }
	}

	private void fillTour() {
		for (int i = 1; i < bestSolAddress.length; i++) {
			bestSolIntersection.addAll(g.getShortestPathAlgo().getPath(bestSolAddress[i - 1], bestSolAddress[i]).getVertexList());
			bestSolIntersection.remove(bestSolIntersection.size() - 1);
		}
		bestSolIntersection.addAll(g.getShortestPathAlgo().getPath(bestSolAddress[bestSolAddress.length - 1], bestSolAddress[0]).getVertexList());
	}

}
