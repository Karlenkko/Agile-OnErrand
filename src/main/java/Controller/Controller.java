package Controller;

import Algorithm.Graph;
import Algorithm.TSP;
import Model.Map;
import Model.Mission;
import View.Window;

public class Controller {
    private Map map;
    private Mission mission;
//    private JgraphtMapGraph jgraphtMapGraph;
//    private MapGraph mapGraph;
    private Graph graph;

    private TSP tsp;

    private Window window;
    private State currentState;
    private ListOfCommands listOfCommands;

    //state instances
    protected final InitialState initialState = new InitialState();
    protected final MapLoadedState mapLoadedState = new MapLoadedState();
    protected final RequestLoadedState requestLoadedState = new RequestLoadedState();
    protected final CalculatedState calculatedState = new CalculatedState();
    protected final AddRequestState1 addRequestState1 = new AddRequestState1();
    protected final AddRequestState2 addRequestState2 = new AddRequestState2();
    protected final AddRequestState3 addRequestState3 = new AddRequestState3();
    protected final AddRequestState4 addRequestState4 = new AddRequestState4();
    protected final AddRequestState5 addRequestState5 = new AddRequestState5();

    /**
     * Constructor of object Controller, creates an instance of Controller
     * with the empty but instanced cityMap, mission and jgraphtMapGraph
     * @param map the object Map, currently empty
     * @param mission the object Mission, currently empty
     * @param graph the object JgraphtMapGraph, currently empty
     */
    public Controller(Map map, Mission mission, Graph graph, TSP tsp) {
        this.map = map;
        this.mission = mission;
//        this.jgraphtMapGraph = jgraphtMapGraph;
        this.tsp = tsp;
        this.graph = graph;
        window = new Window(this.map, this.mission, tsp, this);
        window.allow("initialState");
        currentState = initialState;
        listOfCommands = new ListOfCommands();
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


    public TSP getTsp() {
        return tsp;
    }

    public Graph getGraph() {
        return graph;
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

    public void addRequest(){ currentState.addRequest(this, window); }

    public void deleteRequest(){ currentState.deleteRequest(this,window, listOfCommands); }
    public void leftClick(int positionX, int positionY){ currentState.leftClick(this , window , positionX, positionY); }

    public void rightClick(){ currentState.rightClick(this, window);}

    public void validateNewRequest(){ currentState.validateNewRequest(this, window, listOfCommands); }

    public void cancelNewRequest(){currentState.cancelNewRequest(this, window);}

    public void generateRoadMap(){ currentState.generateRoadMap(this);}

    public void undo(){ currentState.undo(listOfCommands, window);}

    public void redo(){ currentState.redo(listOfCommands, window);}

    public State getCurrentState() {
        return currentState;
    }
}
