package Algorithm;

import java.util.Collection;
import java.util.Iterator;

public class TSP2 extends TSP1 {



    @Override
    protected int bound(long currentVertex, Collection<Long> unvisited) {
        double somme = 0;
        for (Long l : unvisited) {
            somme += MapGraph.minHash.get(l);
        }
        return (int)Math.round(somme);
    }

    /*
    @Override
    protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, JgraphtMapGraph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }
     */
    @Override
    protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, MapGraph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }

}