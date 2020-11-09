package test.Algorithm;

import Algorithm.*;
import Model.Map;
import Model.Mission;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static Util.XMLparser.parserMap;
import static Util.XMLparser.parserRequest;
import static org.junit.Assert.assertTrue;

public class TSPTest {
    Map map;
    Mission mission;
    Graph graph;

    TSP1 tsp1;
    TSP2 tsp2;
    TSP3 tsp3;

    /**
     * Initialise the test by loading the map and the mission and then calculate the dijkstra by using the MapGraph.
     * @throws Exception
     */
    @Before
    public void init() throws Exception{
        map = new Map();
        mission = new Mission();

        // Load the small Map and then the requestsSmall2

        parserMap(map);
        parserRequest(mission,map);

        // Calculate the route for Dijkstra
        graph = new MapGraph();
        graph.reset();
        graph.fillGraph(map);
        graph.setRequests(mission.getAllRequests(), mission.getDepot());
        graph.dijkstra(false);

    }

    /**
     * Test the accuracy of TSP1 by using the iterator MinFirst.
     */
    @Test
    public void testTSP1(){

        // Test the correction of the order of all the intersections calculated forTSP1
        tsp1 = new TSP1();
        Long[] solutions = tsp1.searchSolution(3000, graph);

        assertTrue("The solution for TSP1, iterator MinFirstIter, the 1st point is incorrecte",solutions[0]== Long.parseLong("2835339774"));
        assertTrue("The solution for TSP1, iterator MinFirstIter, the 2nd point is incorrecte",solutions[1]== Long.parseLong("208769120"));
        assertTrue("The solution for TSP1, iterator MinFirstIter, the 3rd point is incorrecte",solutions[2]== Long.parseLong("1679901320"));
        assertTrue("The solution for TSP1, iterator MinFirstIter, the 4th point is incorrecte",solutions[3]== Long.parseLong("208769457"));
        assertTrue("The solution for TSP1, iterator MinFirstIter, the 5th point is incorrecte",solutions[4]== Long.parseLong("25336179"));

    }

    /**
     * Test the accuracy of TSP2 by using the iterator MinFirst.
     */
    @Test
    public void testTSP2(){

        // Test the correction of the order of all the intersections calculated for TSP2
        tsp2 = new TSP2();
        Long[] solutions = tsp2.searchSolution(3000, graph);

        assertTrue("The solution for TSP2, iterator MinFirstIter, the 1st point is incorrecte",solutions[0]== Long.parseLong("2835339774"));
        assertTrue("The solution for TSP2, iterator MinFirstIter, the 2nd point is incorrecte",solutions[1]== Long.parseLong("208769120"));
        assertTrue("The solution for TSP2, iterator MinFirstIter, the 3rd point is incorrecte",solutions[2]== Long.parseLong("1679901320"));
        assertTrue("The solution for TSP2, iterator MinFirstIter, the 4th point is incorrecte",solutions[3]== Long.parseLong("208769457"));
        assertTrue("The solution for TSP2, iterator MinFirstIter, the 5th point is incorrecte",solutions[4]== Long.parseLong("25336179"));

    }

    /**
     * Test the accuracy of TSP1 by using the iterator Sequence.
     */
    @Test
    public void testTSP1IterSel(){

        // Test the correction of the order of all the intersections calculated for TSP1 with the Interator Selq
        tsp3 = new TSP3();
        Long[] solutions = tsp3.searchSolution(3000, graph);

        assertTrue("The solution for TSP1, iterator SelqIter, the 1st point is incorrecte",solutions[0]== Long.parseLong("2835339774"));
        assertTrue("The solution for TSP1, iterator SelqIter, the 2nd point is incorrecte",solutions[1]== Long.parseLong("208769120"));
        assertTrue("The solution for TSP1, iterator SelqIter, the 3rd point is incorrecte",solutions[2]== Long.parseLong("1679901320"));
        assertTrue("The solution for TSP1, iterator SelqIter, the 4th point is incorrecte",solutions[3]== Long.parseLong("208769457"));
        assertTrue("The solution for TSP1, iterator SelqIter, the 5th point is incorrecte",solutions[4]== Long.parseLong("25336179"));

    }


    /**
     * Comparing the difference between time for calculating the tour by using TSP1 and TSP2 on using the same iterator(MinFirst).
     */
    @Test
    public void testCompareTSP1TSP2(){

        // Count the time for TSP1 calculating for 1000 times
        tsp1 = new TSP1();

        Date beforeTSP1 = new Date();
        for (int i = 0; i < 1000; i++) {
            tsp1.searchSolution(3000, graph);
        }
        Date afterTSP1 = new Date();

        System.out.println("The TSP1 runs for "+ (afterTSP1.getTime() - beforeTSP1.getTime()) + " ms");

        // Count the time for TSP2 calculating for 1000 times
        tsp2 = new TSP2();

        Date beforeTSP2 = new Date();
        for (int i = 0; i < 1000; i++) {
            tsp2.searchSolution(3000, graph);
        }
        Date afterTSP2 = new Date();

        System.out.println("The TSP2 runs for "+ (afterTSP2.getTime() - beforeTSP2.getTime()) + " ms");


    }

    /**
     * Comparing the difference between time for calculating the tour by using different iterator on using the same bound(TSP1).
     */
    @Test
    public void testCompareIterator(){

        // Count the time for TSP1 calculating for 1000 times By using iterator MinFirstIter
        tsp1 = new TSP1();

        Date beforeMinFirstIter = new Date();
        for (int i = 0; i < 1000; i++) {
            tsp1.searchSolution(3000, graph);
        }
        Date afterMinFirstIter = new Date();

        System.out.println("The TSP1 with iterator MinFirstIter runs for "+ (afterMinFirstIter.getTime() - beforeMinFirstIter.getTime()) + " ms");

        // Count the time for TSP1 calculating for 1000 times By using iterator SelqIter
        tsp3 = new TSP3();

        Date beforeSelqIter = new Date();
        for (int i = 0; i < 1000; i++) {
            tsp3.searchSolution(3000, graph);
        }
        Date afterSelqIter = new Date();

        System.out.println("The TSP1 with iterator SelqIter runs for "+ (afterSelqIter.getTime() - beforeSelqIter.getTime()) + " ms");


    }
}
