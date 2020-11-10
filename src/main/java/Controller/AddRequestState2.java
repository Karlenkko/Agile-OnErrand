package Controller;

import Model.Intersection;
import View.Window;

import javax.swing.*;

public class AddRequestState2 implements State{
    // Now you've chosen 1 intersection.

    /**
     * the detailed implementation of the method leftClick for the AddRequestState2
     * that selects the pickup address for the new request
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
        if(!controller.getMission().getNewAddList().contains(intersection.getId())){
            controller.getMission().addNewAdd(intersection.getId());

            window.getGraphicalView().setPaintAdd(true);
            window.getGraphicalView().repaint();
            window.allow("addRequestState3");
            window.getTextualView().setLockInstruction(false);
            window.getTextualView().setTextAreaText("You are now adding a new request, \n" +
                    "please select another intersection as the new delivery address, \n" +
                    "or right click to cancel the selection of the pickup address");
            window.getTextualView().setLockInstruction(true);
            controller.setCurrentState(controller.addRequestState3);

        }else{
            System.out.println("You have selected the same point. Please choose another point.");
        }


    }

    /**
     * the detailed implementation of the method rightClick for the AddRequestState2
     * that cancels the selection of the start address
     * @param controller the Controller
     * @param window the main window of the application
     */
    @Override
    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request
        controller.getMission().removeAdd();
        window.getGraphicalView().repaint();
        window.allow("addRequestState1");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You have canceled the selection of the start address, \n" +
                "please select an existing address as the start address, \n" +
                "or right click to cancel the addition");
        window.getTextualView().setLockInstruction(true);
        controller.setCurrentState(controller.addRequestState1);
    }

}
