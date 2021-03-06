package Controller;

import Model.Intersection;
import View.Window;

public class AddRequestState1 implements State {
    // The state which the user can choose the intersection where starting the new State.

    /**
     * the detailed implementation of the method leftClick for the AddRequestState1
     * that selects the start address for the new request
     * @param controller the Controller
     * @param window the main window of the application
     * @param positionX the x position of the mouse on the window
     * @param positionY the y position of the mouse on the window
     */
    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        Intersection intersection = controller.getMission().NearestRequest(positionX,positionY, window.getRate());

        controller.getMission().addNewAdd(intersection.getId());
        window.getGraphicalView().setPaintAdd(true);
        window.getGraphicalView().repaint();

        window.allow("addRequestState2");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You are now adding a new request, \n" +
                "please select an intersection as the new pickup address, \n" +
                "or right click to cancel the selection of the start address");
        window.getTextualView().setLockInstruction(true);
        controller.setCurrentState(controller.addRequestState2);
    }

    /**
     * the detailed implementation of the method rightClick for the AddRequestState1
     * that quits the addition mode
     * @param controller the Controller
     * @param window the main window of the application
     */
    @Override
    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request
        controller.getMission().removeAdd();
        window.getGraphicalView().setPaintAdd(false);
        window.getGraphicalView().repaint();
        window.allow("calculatedState");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You have cancel the addition of a new request");
        window.getTextualView().setLockInstruction(true);
        controller.setCurrentState(controller.calculatedState);
    }

}
