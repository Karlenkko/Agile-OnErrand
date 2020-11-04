package Controller;

import View.Window;

public class AddRequestState5 implements State{
    // Now you have already 4 intersections and another frame to enter the duration


    @Override
    public void validNewRequest(Controller controller, Window newWindow) {
        // TODO: validate the two duration entered and then create a new Object Request.

        controller.setCurrentState(controller.calculatedState);
    }

    public void cancelNewRequest(Controller controller, Window newWindow){
        // TODO: cancel the add of a new Request

        controller.setCurrentState(controller.addRequestState4);
    }
}
