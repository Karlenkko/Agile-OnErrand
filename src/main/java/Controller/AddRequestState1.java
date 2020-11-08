package Controller;

import Model.Intersection;
import View.Window;

public class AddRequestState1 implements State {
    // The state which the user can choose the intersection where starting the new State.

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        Intersection intersection = controller.getMission().NearestRequest(positionX,positionY, window.getRate());

        controller.getMission().addNewAdd(intersection.getId());
        window.getGraphicalView().setPaintAdd(true);
        window.getGraphicalView().repaint();

        controller.setCurrentState(controller.addRequestState2);
    }

    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request
        controller.getMission().removeAdd();
        window.getGraphicalView().setPaintAdd(false);
        window.getGraphicalView().repaint();
        controller.setCurrentState(controller.calculatedState);
    }

}
