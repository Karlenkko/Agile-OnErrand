package Algorithm;

import java.util.*;

public class MinFirstIter implements Iterator<Long> {
    private LinkedHashMap<Long, Double> unvisitedCosts;

    public MinFirstIter(Collection<Long> unvisited, Long currentVertex, MapGraph g) {
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
     * Returns the next element in the iteration.
     *
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
