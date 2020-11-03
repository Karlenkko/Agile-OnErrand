package Controller;

import Model.Intersection;
import View.Window;

public class AddRequestState3 implements State{
    // Now you've chosen 2 intersection.

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        // TODO: Click on the intersection and make it as the delivery point of the new request
        Intersection intersection = controller.getMap().NearestIntersection(positionX,positionY);
        window.getGraphicalView().setPaintAdd(true,intersection);
        window.getGraphicalView().repaint();
        controller.setCurrentState(controller.addRequestState4);
    }

    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request

        controller.setCurrentState(controller.addRequestState2);
    }
}
