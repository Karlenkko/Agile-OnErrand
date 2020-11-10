package Algorithm;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {


	/**
	 * implements a new version of the bound function which gives the heuristic by multiplying
	 * the number of unvisited vertexes and the smallest cost on the Graph
	 * @param currentVertex the id of the current address vertex
	 * @param unvisited the collection of unvisited address vertexes
	 * @param g the Graph
	 * @return the lower bound casted to int value
	 */
	@Override
	protected int bound(long currentVertex, Collection<Long> unvisited, Graph g) {
		return (int) (unvisited.size() * g.getMinCost());
	}

	/**
	 * returns an implementation of the iterator for the TSP
	 * @param currentVertex the current vertex
	 * @param unvisited the collection of unvisited address vertexes
	 * @param g the Graph
	 * @return an implementation of the iterator
	 */
	@Override
	protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, Graph g) {
		return new SeqIter(unvisited, currentVertex, g);
//        return new MinFirstIter(unvisited, currentVertex, g);
	}


}
