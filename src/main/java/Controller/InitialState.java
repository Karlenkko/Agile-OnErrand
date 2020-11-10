package Controller;

import Util.XMLparser;
import View.Window;

public class InitialState implements State{
    /**
     * since only the load map and the load request operation are the two most global operation
     * in all states, whereas the load request must be in a condition where the map is loaded,
     * here we override the load request operation to an empty one to reduce code replication
     * @param controller the Controller
     * @param window the main Window of the application
     */
    @Override
    public void loadRequests(Controller controller, Window window) {
        System.out.println("initial state");
    }
}
