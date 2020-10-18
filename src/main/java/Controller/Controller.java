package Controller;

import Algorithm.MapGraph;
import Model.Map;
import Model.Mission;

public class Controller {
    private Map cityMap;
    private Mission mission;
    private MapGraph mapGraph;

    public Controller(Map cityMap, Mission mission, MapGraph mapGraph) {
        this.cityMap = cityMap;
        this.mission = mission;
        this.mapGraph = mapGraph;
    }

    public Map getCityMap() {
        return cityMap;
    }

    public Mission getMission() {
        return mission;
    }

    public MapGraph getMapGraph() {
        return mapGraph;
    }
}
