package Controller;

import Model.Intersection;
import View.PopupWindow;
import View.Window;

import java.util.ArrayList;

public class AddRequestState4 implements State{
    // Now you've chosen 3 points and waiting for entering the last point.

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        // It will also show a window to enter the new Window which can take the duration for the pickup and delivery
        Intersection intersection = controller.getMission().NearestRequest(positionX,positionY,window.getRate());

        ArrayList<Long> newAddList = controller.getMission().getNewAddList();

        if(!newAddList.contains(intersection.getId())){
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

        }else if(controller.getMission().requestValid(newAddList.get(0), intersection.getId())){
            System.out.println("The first point chosen can't be the delivery point of the last point chosen.");
        }else{
            System.out.println("You have selected the same point. Please choose another point.");
        }
    }

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
