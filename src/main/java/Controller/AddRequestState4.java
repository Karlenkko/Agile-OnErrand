package Controller;

import Model.Intersection;
import View.Window;

public class AddRequestState4 implements State{
    // Now you've chosen 3 points and waiting for entering the last point.

    @Override
    public void leftClick(Controller controller, Window window, Intersection intersection){
        // TODO: Click on the intersection already exist and mark it as the intersection which conbine the old roadMap
        // It will also show a window to enter the new Window which can take the duration for the pickup and delivery

        controller.setCurrentState(controller.addRequestState5);
    }

    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request

        controller.setCurrentState(controller.addRequestState3);
    }

}
