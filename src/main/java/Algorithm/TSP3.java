package Algorithm;

import java.util.Collection;
import java.util.Iterator;

// Use this class to Test the new Iterator
public class TSP3 extends TSP1{
    @Override
    protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, Graph g) {
		return new SeqIter(unvisited, currentVertex, g);
//        return new MinFirstIter(unvisited, currentVertex, g);
    }
}
