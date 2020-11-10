package Algorithm;

import java.util.*;

public class MinFirstIter implements Iterator<Long> {
    private LinkedHashMap<Long, Double> unvisitedCosts;

    /**
     * constructor of the Min First Iterator which parses the unvisited vertexes in the ascending order
     * of their costs, it gives a better result when the calculation of the TSP is not finished,
     * since it is greedy
     * @param unvisited a collection of the ids of the unvisited vertexes
     * @param currentVertex the id of the current vertex
     * @param g the Graph
     */
    public MinFirstIter(Collection<Long> unvisited, Long currentVertex, Graph g) {
        unvisitedCosts = new LinkedHashMap<>();
        double cost;
        for (Long s : unvisited) {
            if (g.isArc(currentVertex, s)){
                cost = g.getCost(currentVertex, s);
                unvisitedCosts.put(s, cost);
            }
        }
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return !unvisitedCosts.isEmpty();
    }

    /**
     * Returns the next element in the iteration which has a next smallest cost
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Long next() {
        Double min = Double.MAX_VALUE;
        Long minKey = 0L;
        for (Long key : unvisitedCosts.keySet()){
            if (min > unvisitedCosts.get(key)){
                min = unvisitedCosts.get(key);
                minKey = key;
            }
        }
        unvisitedCosts.remove(minKey);
        return minKey;
    }
}
