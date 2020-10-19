package Controller;

import Algorithm.MapGraph;
import Model.Map;
import Model.Mission;
import View.Window;

public class Controller {
    private Map map;
    private Mission mission;
    private MapGraph mapGraph;
    private Window window;
    private State currentState;

    //state instances
    protected final InitialState initialState = new InitialState();
    protected final MapLoadedState mapLoadedState = new MapLoadedState();
    protected final RequestLoadedState requestLoadedState = new RequestLoadedState();
    protected final CalculateState calculateState = new CalculateState();

    /**
     * Constructor of object Controller, creates an instance of Controller
     * with the empty but instanced cityMap, mission and mapGraph
     * @param map the object Map, currently empty
     * @param mission the object Mission, currently empty
     * @param mapGraph the object MapGraph, currently empty
     */
    public Controller(Map map, Mission mission, MapGraph mapGraph) {
        this.map = map;
        this.mission = mission;
        this.mapGraph = mapGraph;
        window = new Window(this.map, this.mission, this);
        currentState = initialState;
    }

    /**
     * set the currentState of the Controller
     * @param state the "next" state
     */
    protected void setCurrentState(State state) {
        currentState = state;
    }

    public Map getMap() {
        return map;
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
