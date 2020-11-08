package test.Algorithm;

import Algorithm.*;
import Model.Map;
import Model.Mission;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testTSP1IterSel(){
        tsp3 = new TSP3();
        Long[] solutions = tsp3.searchSolution(3000, graph);

        assertTrue("The solution for TSP1, iterator SelqIter, the 1st point is incorrecte",solutions[0]== Long.parseLong("2835339774"));
        assertTrue("The solution for TSP1, iterator SelqIter, the 2nd point is incorrecte",solutions[1]== Long.parseLong("208769120"));
        assertTrue("The solution for TSP1, iterator SelqIter, the 3rd point is incorrecte",solutions[2]== Long.parseLong("1679901320"));
        assertTrue("The solution for TSP1, iterator SelqIter, the 4th point is incorrecte",solutions[3]== Long.parseLong("208769457"));
        assertTrue("The solution for TSP1, iterator SelqIter, the 5th point is incorrecte",solutions[4]== Long.parseLong("25336179"));

    }




}
