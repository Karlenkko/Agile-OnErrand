import Algorithm.MapGraph;
import Algorithm.TSP;
import Algorithm.TSP1;
import Controller.Controller;
import IHM.Meituan;
import Model.Map;
import Model.Mission;

import javax.xml.parsers.ParserConfigurationException;

public class BasicTest {
    public static void main(String[] args) throws ParserConfigurationException {
        Map map = new Map();
        Mission mission = new Mission();
        MapGraph mapGraph = new MapGraph();
        TSP tsp = new TSP1();
        new Controller(map, mission, mapGraph, tsp);
    }
}


