import Algorithm.*;
import Controller.Controller;
import Model.Map;
import Model.Mission;

import javax.xml.parsers.ParserConfigurationException;

public class BasicTest {
    public static void main(String[] args) throws ParserConfigurationException {
        Map map = new Map();
        Mission mission = new Mission();
        JgraphtMapGraph jgraphtMapGraph = new JgraphtMapGraph();
        MapGraph mapGraph = new MapGraph();
        TSP tsp = new TSP1();

        // TODO: initialisation tsp move to map
        // MAP: new tsp & mapgraph
        new Controller(map, mission, jgraphtMapGraph, tsp, mapGraph);
    }
}


