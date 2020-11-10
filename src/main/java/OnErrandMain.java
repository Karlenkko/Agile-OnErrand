import Algorithm.*;
import Controller.Controller;
import Model.Map;
import Model.Mission;

import javax.xml.parsers.ParserConfigurationException;

public class OnErrandMain {
    public static void main(String[] args) {
        Map map = new Map();
        Mission mission = new Mission();
        // the JgraphtMapGraph is deprecated in v1.5.1
        Graph graph = new MapGraph();
        TSP tsp = new TSP2();

        // TODO: initialisation tsp move to map?
        new Controller(map, mission, graph, tsp);
    }
}


