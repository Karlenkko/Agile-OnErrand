package Controller;

import Algorithm.Graph;
import Algorithm.TSP;
import Model.Map;
import Model.Mission;
import View.Window;

public class Controller {
    // model representations of the real life
    private Map map;
    private Mission mission;

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
    protected final DeleteRequestState deleteRequestState = new DeleteRequestState();

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

    /**
     * get the object Map from the Controller
     * @return the object Map
     */
    public Map getMap() {
        return map;
    }

    /**
     * get the object Mission from the Controller
     * @return the object Mission
     */
    public Mission getMission() {
        return mission;
    }

    /**
     * get the object TSP from the Controller
     * @return the object TSP
     */
    public TSP getTsp() {
        return tsp;
    }

    /**
     * get the implementation class of the interface Graph
     * @return the implementation class of the interface Graph
     */
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
     * that invokes the calculateTour method in a State
     * the concrete implementation is in the RequestLoadedState
     */
    public void calculateTour() {
        currentState.calculateTour(this, window, listOfCommands);
    }

    /**
     * Method called by window after a click on the button "Add request"
     * that invokes the concrete addRequest method in a State
     * the concrete implementation is in the AddRequestStates after the CalculatedState
     */
    public void addRequest(){ currentState.addRequest(this, window); }

    /**
     * Method called by window after a click on the button "Delete request"
     * that invokes the deleteRequest method in a State
     * the concrete implementation is in the DeleteRequestState after the CalculatedState
     */
    public void deleteRequest(){ currentState.deleteRequest(this,window, listOfCommands); }

    /**
     * Method called by window after a leftclick of the mouse
     * that invokes the concrete leftClick method in a State
     * the detailed implementation may largely differ in distinct states
     * depending on the functionalities
     * @param positionX the x position of the mouse on the window
     * @param positionY the y position of the mouse on the window
     */
    public void leftClick(int positionX, int positionY){ currentState.leftClick(this , window , positionX, positionY); }

    /**
     * Method called by window after a rightclick of the mouse
     * that invokes the concrete rightClick method in a State
     * normally a right click means cancel, if there is an operation
     * that is in progress
     */
    public void rightClick(){ currentState.rightClick(this, window);}

    /**
     * Method called by window after a click on the button "validate request"
     * on the pop up window
     * that invokes the concrete validateNewRequest method in the AddRequestState5
     */
    public void validateNewRequest(){ currentState.validateNewRequest(this, window, listOfCommands); }

    /**
     * Method called by window after a click on the button "cancel"
     * on the pop up window
     * that invokes the concrete cancelNewRequest method in the AddRequestState5
     */
    public void cancelNewRequest(){currentState.cancelNewRequest(this, window);}

    /**
     * Method called by window after a click on the button "Delete request"
     * that invokes the concrete deleteRequest method in a State
     * the concrete implementation is in the CalculatedState
     */
    public void generateRoadMap(){ currentState.generateRoadMap(this);}

    /**
     * Method called by window after a click on the button "undo"
     * that invokes the concrete undo method in a State
     * the concrete implementation is in the CalculatedState
     */
    public void undo(){ currentState.undo(listOfCommands, window);}

    /**
     * Method called by window after a click on the button "redo"
     * that invokes the concrete redo method in a State
     * the concrete implementation is in the CalculatedState
     */
    public void redo(){ currentState.redo(listOfCommands, window);}
}
