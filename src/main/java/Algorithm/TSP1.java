package Algorithm;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {



	@Override
	protected double bound(long currentVertex, Collection<Long> unvisited) {
		return 0;
	}

	@Override
	protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, MapGraph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

}
