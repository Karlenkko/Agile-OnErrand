package Algorithm;

import java.util.Collection;
import java.util.Iterator;

public class TSP2 extends TSP1 {

    /**
     * implements a new version of the bound function which gives the heuristic by
     * adding the smallest inbound cost of each unvisited address vertex
     * @param currentVertex the id of the current address vertex
     * @param unvisited the collection of unvisited address vertexes
     * @param g the Graph
     * @return a lower bound casted to int value
     */
    @Override
    protected int bound(long currentVertex, Collection<Long> unvisited, Graph g) {
        double somme = 0;
        for (Long l : unvisited) {
            somme += g.getMinHash().get(l);
        }
        return (int)Math.round(somme);
    }

    /**
     * returns an implementation of the iterator for the TSP
     * @param currentVertex the current vertex
     * @param unvisited the collection of unvisited address vertexes
     * @param g the Graph
     * @return a greedy implementation of the iterator
     */
    @Override
    protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unvisited, Graph g) {
//        return new SeqIter(unvisited, currentVertex, g);
        return new MinFirstIter(unvisited, currentVertex, g);
    }

}