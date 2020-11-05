package Algorithm;

import java.util.LinkedList;
import java.util.List;

public interface TSP {
	/**
	 * Search for a shortest cost hamiltonian circuit in <code>g</code> within <code>timeLimit</code> milliseconds
	 * (returns the best found tour whenever the time limit is reached)
	 * Warning: The computed tour always start from vertex 0
	 * @param timeLimit
	 * @param g
	 */
	/*
	public Long[] searchSolution(int timeLimit, MapGraph g);
	 */
	public Long[] searchSolution(int timeLimit, CompleteGraph g);

	public void setRecalcul(boolean recalcul);
	/**
	 * @param i
	 * @return the ith visited vertex in the solution computed by <code>searchSolution</code> 
	 * (-1 if <code>searcheSolution</code> has not been called yet, or if i < 0 or i >= g.getNbSommets())
	 */
	public Long getSolution(int i);
	
	/** 
	 * @return the total cost of the solution computed by <code>searchSolution</code> 
	 * (-1 if <code>searcheSolution</code> has not been called yet).
	 */
	public double getSolutionCost();

	public List<Long> getBestSolIntersection();

	public double[] getBestSolAddressCost();

}
