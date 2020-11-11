package test.Algorithm;

import Algorithm.Graph;
import Algorithm.JgraphtMapGraph;
import Algorithm.MapGraph;
import Model.Map;
import Model.Mission;
import java.util.Date;


import org.junit.Before;
import org.junit.Test;

import static Util.XMLparser.parserMap;
import static Util.XMLparser.parserRequest;
import static org.junit.Assert.assertTrue;


public class DijkstraTest {
    Map map;
    Mission mission;
    Graph graph1;
    Graph graph2;

    /**
     * initialise the test by loading the map and mission associate.
     * Create also two instance for graph to calculate dijkstra.
     * @throws Exception
     */
    @Before
    public void init() throws Exception{
        map = new Map();
        mission = new Mission();

        // Load the small Map and then the requestsSmall2

        parserMap(map);
        parserRequest(mission,map);

        graph1 = new JgraphtMapGraph();
        graph2 = new MapGraph();
    }


    /**
     * Test the accuracy of the JGraph
     */
    @Test
    public void testDijkstraJgraph(){

        // Test the correction of the result of the JgraphtMapGraph.

        graph1.reset();
        graph1.fillGraph(map);
        graph1.fillMission(mission.getAllRequests(), mission.getDepot());
        graph1.dijkstra(false);

        assertTrue("The route which we found on Google Map is not the same as the one calculated.",
                graph1.getShortestPaths(false).get("208769457 2835339774").toString().equals(
                        "[208769457, 208769499, 55475018, 55475025, 55475038, 208769047, 208769163, 3267479470, " +
                                "3870641963, 208769091, 208769219, 208769227, 208769112, 26086128, 26086127, " +
                                "2587460578, 26079653, 2835339775, 2835339774]"));

    }

    /**
     * Test the accuracy of the MapGraph
     */
    @Test
    public void testDijkstraMapGraph(){

        // Test the correction of the result of the MapGraph.

        graph2.reset();
        graph2.fillGraph(map);
        graph2.fillMission(mission.getAllRequests(), mission.getDepot());
        graph2.dijkstra(false);

        assertTrue("The route which we found on Google Map is not the same as the one calculated.",
                graph2.getShortestPaths(false).get("208769457 2835339774").toString().equals(
                        "[208769457, 208769499, 55475018, 55475025, 55475038, 208769047, 208769163, 3267479470, " +
                                "3870641963, 208769091, 208769219, 208769227, 208769112, 26086128, 26086127, " +
                                "2587460578, 26079653, 2835339775, 2835339774]"));
    }


    /**
     * Compare the time we use to calculate the dijkstra by using the two graphs different.
     */
    @Test
    public void testTwoDijkstraTimeCompare(){

        // Runs each calculate for 1000 times then compare them.

        graph1.reset();
        graph1.fillGraph(map);
        graph1.fillMission(mission.getAllRequests(), mission.getDepot());
        Date beforeJgraph = new Date();
        for (int i = 0; i < 1000; i++) {
            graph1.dijkstra(false);
        }
        Date afterJgraph = new Date();

        System.out.println("The JgraphtMapGraph runs for "+ (afterJgraph.getTime() - beforeJgraph.getTime()) + " ms");

        graph2.reset();
        graph2.fillGraph(map);
        graph2.fillMission(mission.getAllRequests(), mission.getDepot());
        Date beforeMapgraph = new Date();
        for (int i = 0; i < 1000; i++) {
            graph2.dijkstra(false);
        }
        Date afterMapgraph = new Date();

        System.out.println("The MapGraph runs for "+ (afterMapgraph.getTime() - beforeMapgraph.getTime()) + " ms");

    }

}


