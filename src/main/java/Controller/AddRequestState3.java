package Controller;

import Model.Intersection;
import Model.Request;
import View.Window;

import javax.swing.*;
import java.util.ArrayList;

public class AddRequestState3 implements State{
    // Now you've chosen 2 intersection.

    /**
     * the detailed implementation of the method leftClick for the AddRequestState3
     * that selects the delivery address for the new request
     * @param controller the Controller
     * @param window the main window of the application
     * @param positionX the x position of the mouse on the window
     * @param positionY the y position of the mouse on the window
     */
    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        Intersection intersection = controller.getMap().NearestIntersection(positionX,positionY,window.getRate());

        while(!controller.getGraph().isReachable(intersection.getId())) {
            JOptionPane.showMessageDialog(null, "the intersection is unreachable, please select the correct intersection", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Long> newAddList = controller.getMission().getNewAddList();
        if(!newAddList.contains(intersection.getId())){
            controller.getMission().addNewAdd(intersection.getId());

            window.getGraphicalView().setPaintAdd(true);
            window.getGraphicalView().repaint();

            controller.getMission().setNewRequest(new Request(controller.getMap().getAllIntersections().get(newAddList.get(1))
                    ,controller.getMap().getAllIntersections().get(newAddList.get(2)),0,0));

            window.allow("addRequestState4");
            window.getTextualView().setLockInstruction(false);
            window.getTextualView().setTextAreaText("You are now adding a new request, \n" +
                    "please select an existing address as the ending address, \n" +
                    "or right click to cancel the selection of the delivery address");
            window.getTextualView().setLockInstruction(true);
            controller.setCurrentState(controller.addRequestState4);

        }else{
            System.out.println("You have selected the same point. Please choose another point.");
        }
    }

    /**
     * the detailed implementation of the method rightClick for the AddRequestState3
     * that cancels the selection of the pickup address
     * @param controller the Controller
     * @param window the main window of the application
     */
    @Override
    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request
        controller.getMission().removeAdd();
        window.getGraphicalView().repaint();
        window.allow("addRequestState2");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You have canceled the selection of the pickup address, \n" +
                "please select an intersection as the pickup address, \n" +
                "or right click to cancel the selection of the start address");
        window.getTextualView().setLockInstruction(true);
        controller.setCurrentState(controller.addRequestState2);
    }
}
