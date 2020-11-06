package Algorithm;

import Model.Intersection;
import Model.Map;
import Model.Request;

import java.util.List;

public interface Graph {
	/**
	 * @return the number of vertices in <code>this</code>
	 */
	public abstract int getNbVertices(boolean recalculate);

	public abstract void fillGraph(Map map);

	public abstract void setRequests(List<Request> allRequests, Intersection depot);

	public abstract void reset();

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

}
