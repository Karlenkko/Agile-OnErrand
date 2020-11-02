package Controller;

import Util.XMLparser;
import View.Window;

public class InitialState implements State{
    @Override
    public void loadRequests(Controller controller, Window window) {
        System.out.println("initial state");
    }
}
