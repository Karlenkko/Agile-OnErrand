package Algorithm;

import java.util.Collection;
import java.util.Iterator;

public class SeqIter implements Iterator<Long> {
	private Long[] candidates;
	private int nbCandidates;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 */
	/*
	public SeqIter(Collection<Long> unvisited, Long currentVertex, JgraphtMapGraph g){
		this.candidates = new Long[unvisited.size()];
		for (Long s : unvisited){
			if (g.isArc(currentVertex, s))
				candidates[nbCandidates++] = s;
		}
	}

	 */
	public SeqIter(Collection<Long> unvisited, Long currentVertex, Graph g){
		this.candidates = new Long[unvisited.size()];
		for (Long s : unvisited){
			if (g.isArc(currentVertex, s))
				candidates[nbCandidates++] = s;
		}
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidates > 0;
	}

	@Override
	public Long next() {
		nbCandidates--;
		return candidates[nbCandidates];
	}

	@Override
	public void remove() {}

}
