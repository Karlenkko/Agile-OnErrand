package Controller;

import Model.Intersection;
import View.Window;

public class AddRequestState1 implements State {
    // The state which the user can choose the intersection where starting the new State.

    @Override
    public void leftClick(Controller controller, Window window, Intersection intersection){
        // TODO: Click on the intersection already exist and mark it as the position where we cut the old roadMap

        controller.setCurrentState(controller.addRequestState2);
    }

    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request

        controller.setCurrentState(controller.calculateState);
    }


}