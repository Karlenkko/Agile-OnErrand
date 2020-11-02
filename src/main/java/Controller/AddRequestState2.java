package Controller;

import Model.Intersection;
import View.Window;

public class AddRequestState2 implements State{
    // Now you've chosen 1 intersection.

    @Override
    public void leftClick(Controller controller, Window window, Intersection intersection){
        // TODO: Click on the intersection and make it as the pickup point of the new request

        controller.setCurrentState(controller.addRequestState3);
    }

    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request

        controller.setCurrentState(controller.addRequestState1);
    }

}
