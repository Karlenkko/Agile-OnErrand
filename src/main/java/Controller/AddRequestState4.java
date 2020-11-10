package Controller;

import Model.Intersection;
import View.PopupWindow;
import View.Window;

import javax.swing.*;
import java.util.ArrayList;

public class AddRequestState4 implements State{
    // Now you've chosen 3 points and waiting for entering the last point.

    /**
     * the detailed implementation of the method leftClick for the AddRequestState4
     * that selects the ending address for the new request
     * @param controller the Controller
     * @param window the main window of the application
     * @param positionX the x position of the mouse on the window
     * @param positionY the y position of the mouse on the window
     */
    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        // It will also show a window to enter the new Window which can take the duration for the pickup and delivery
        Intersection intersection = controller.getMission().NearestRequest(positionX,positionY,window.getRate());

        ArrayList<Long> newAddList = controller.getMission().getNewAddList();

        if(!controller.getMission().requestValid(newAddList.get(0), intersection.getId())){
            JOptionPane.showMessageDialog(null,
                    "The first point chosen can't be the delivery point of the last point chosen.",
                    "alert",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if(!newAddList.contains(intersection.getId())){
            controller.getMission().addNewAdd(intersection.getId());

            window.getGraphicalView().setPaintAdd(true);
            window.getGraphicalView().repaint();

            PopupWindow popupWindow = new PopupWindow(controller);


            // need for further discussion
            window.allow("addRequestState5");
            window.getTextualView().setLockInstruction(false);
            window.getTextualView().setTextAreaText("You are now adding a new request, \n" +
                    "please enter the durations in the 2 addresses of the new request, \n" +
                    "or click cancel to quit the addition");
            window.getTextualView().setLockInstruction(true);

            controller.setCurrentState(controller.addRequestState5);

        } else {
            System.out.println("You have selected the same point. Please choose another point.");
        }
    }

    /**
     * the detailed implementation of the method rightClick for the AddRequestState4
     * that cancels the selection of the delivery address
     * @param controller the Controller
     * @param window the main window of the application
     */
    @Override
    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request
        controller.getMission().removeAdd();
        window.getGraphicalView().repaint();
        window.allow("addRequestState3");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You have canceled the selection of the delivery address, \n" +
                "please select an existing intersection as the delivery address, \n" +
                "or right click to cancel the selection of the pickup address");
        window.getTextualView().setLockInstruction(true);
        controller.setCurrentState(controller.addRequestState3);
    }

}
