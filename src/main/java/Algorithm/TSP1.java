package Algorithm;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {



	@Override
	protected int bound(long currentVertex, Collection<Long> unvisited) {
		return (int) (unvisited.size() * MapGraph.minCost);
	}

	/*
	@Override
	protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, JgraphtMapGraph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}
	 */
	@Override
	protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, MapGraph g) {
//		return new SeqIter(unvisited, currentVertex, g);
        return new MinFirstIter(unvisited, currentVertex, g);
	}

}
