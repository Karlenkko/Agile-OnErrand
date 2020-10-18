package Controller;

import Algorithm.MapGraph;
import Model.Map;
import Model.Mission;
import View.Window;

public class Controller {
    private Map cityMap;
    private Mission mission;
    private MapGraph mapGraph;
    private Window window;
    private State currentState;

    //state instances
    private final InitialState initialState = new InitialState();
    private final MapLoadedState mapLoadedState = new MapLoadedState();
    private final RequestLoadedState requestLoadedState = new RequestLoadedState();
    private final CalculateState calculateState = new CalculateState();

    /**
     * Constructor of object Controller, creates an instance of Controller
     * with the empty but instanced cityMap, mission and mapGraph
     * @param cityMap the object Map, currently empty
     * @param mission the object Mission, currently empty
     * @param mapGraph the object MapGraph, currently empty
     */
    public Controller(Map cityMap, Mission mission, MapGraph mapGraph) {
        this.cityMap = cityMap;
        this.mission = mission;
        this.mapGraph = mapGraph;
    }

    /**
     * set the currentState of the Controller
     * @param state the "next" state
     */
    protected void setCurrentState(State state) {
        currentState = state;
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

    /**
     * Method called by window after a click on the button "Load a new map"
     * that invokes the concrete loadMap method in a State
     */
    public void loadMap() {
        currentState.loadMap(this, window);
    }

    /**
     * Method called by window after a click on the button "Load requests"
     * that invokes the concrete loadRequests method in a State
     */
    public void loadRequests() {
        currentState.loadRequests(this, window);
    }

    /**
     * Method called by window after a click on the button "Calculate tour"
     * that invokes the concrete calculateTour method in a State
     */
    public void calculateTour() {
        currentState.calculateTour(this, window);
    }

}
