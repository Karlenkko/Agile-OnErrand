package Controller;

import Model.Intersection;
import View.PopupWindow;
import View.Window;

import java.util.ArrayList;

public class AddRequestState4 implements State{
    // Now you've chosen 3 points and waiting for entering the last point.

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        // TODO: Click on the intersection already exist and mark it as the intersection which conbine the old roadMap
        // It will also show a window to enter the new Window which can take the duration for the pickup and delivery
        Intersection intersection = controller.getMission().NearestRequest(positionX,positionY,window.getRate());

        ArrayList<Long> newAddList = controller.getMission().getNewAddList();

        if(!newAddList.contains(intersection.getId())){
            controller.getMission().addNewAdd(intersection.getId());

            window.getGraphicalView().setPaintAdd(true);
            window.getGraphicalView().repaint();

            PopupWindow popupWindow = new PopupWindow(controller);


            // need for further discussion
            controller.setCurrentState(controller.addRequestState5);

        }else if(controller.getMission().requestValid(newAddList.get(0), intersection.getId())){
            System.out.println("The first point chosen can't be the delivery point of the last point chosen.");
        }else{
            System.out.println("You have selected the same point. Please choose another point.");
        }
    }

    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request
        controller.getMission().removeAdd();
        window.getGraphicalView().repaint();
        controller.setCurrentState(controller.addRequestState3);
    }

}
