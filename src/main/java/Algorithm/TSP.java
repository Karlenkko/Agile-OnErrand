package Algorithm;

import java.util.List;

public interface TSP {
	/**
	 * Search for a shortest cost hamiltonian circuit in <code>g</code> within <code>timeLimit</code> milliseconds
	 * (returns the best found tour whenever the time limit is reached)
	 * Warning: The computed tour always start from vertex 0
	 * @param timeLimit the time limit of the first calculation, in milliseconds
	 * @param g the Graph to be passed for calculation
	 */
	Long[] searchSolution(int timeLimit, Graph g);

	/**
	 * set the algorithm whether it is in the partial recalculation for adding a request
	 * or a complete calculation of the existing requests
	 * @param recalculate indicator of the mode
	 */
	void setRecalculate(boolean recalculate);

	/**
	 * get the address id of the given index on the solution
	 * @param i index
	 * @return the ith visited vertex in the solution computed by <code>searchSolution</code> 
	 * (-1 if <code>search Solution</code> has not been called yet, or if i < 0 or i >= g.getNbSommets())
	 */
	Long getSolution(int i);
	
	/**
	 * get the total cost of a calculated solution
	 * @return the total cost of the solution computed by <code>searchSolution</code> 
	 * (-1 if <code>searcheSolution</code> has not been called yet).
	 */
	double getSolutionCost();

	/**
	 * get the complete solution that includes all intersections to be passed in the solution,
	 * not only the addresses
	 * @return the sequence of all the passed intersections of the solution
	 */
	List<Long> getBestSolIntersection();

	/**
	 * get the sequence of costs from one address to another in the solution
	 * @return the sequence of costs
	 */
	double[] getBestSolAddressCost();

}
