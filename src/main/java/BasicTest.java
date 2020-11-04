import Algorithm.CompleteGraph;
import Algorithm.MapGraph;
import Algorithm.TSP;
import Algorithm.TSP1;
import Controller.Controller;
import Model.Map;
import Model.Mission;

import javax.xml.parsers.ParserConfigurationException;

public class BasicTest {
    public static void main(String[] args) throws ParserConfigurationException {
        Map map = new Map();
        Mission mission = new Mission();
        MapGraph mapGraph = new MapGraph();
        CompleteGraph completeGraph = new CompleteGraph();
        TSP tsp = new TSP1();

        // TODO: initialisation tsp move to map
        // MAP: new tsp & mapgraph
        new Controller(map, mission, mapGraph, tsp, completeGraph);
    }
}


