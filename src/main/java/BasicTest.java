import Algorithm.MapGraph;
import Controller.Controller;
import IHM.Meituan;
import Model.Map;
import Model.Mission;

import javax.xml.parsers.ParserConfigurationException;

public class BasicTest {
    public static void main(String[] args) throws ParserConfigurationException {
        //new Meituan().init();
        Map map = new Map();
        Mission mission = new Mission();
        MapGraph mapGraph = new MapGraph();
        new Controller(map, mission, mapGraph);
    }
}


