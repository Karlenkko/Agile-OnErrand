package Controller;

import Model.Intersection;
import Model.Request;
import View.PopupWindow;
import View.Window;

import java.util.ArrayList;

public class AddRequestState4 implements State{
    // Now you've chosen 3 points and waiting for entering the last point.

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        // TODO: Click on the intersection already exist and mark it as the intersection which conbine the old roadMap
        // It will also show a window to enter the new Window which can take the duration for the pickup and delivery
        Intersection intersection = controller.getMission().NearestRequest(positionX,positionY);

        ArrayList<Long> newAddList = controller.getMission().getNewAddList();

        if(!newAddList.contains(intersection.getId()) && controller.getMission().requestValid(newAddList.get(0), intersection.getId())){
            controller.getMission().addNewAdd(intersection.getId());

            window.getGraphicalView().setPaintAdd(true,intersection);
            window.getGraphicalView().repaint();

            PopupWindow popupWindow = new PopupWindow();

            int pickupTime = popupWindow.getPickUpTime();
            int deliveryTime = popupWindow.getDeliveryTime();

            controller.getMission().addRequest(new Request(controller.getMap().getAllIntersections().get(newAddList.get(1)),controller.getMap().getAllIntersections().get(newAddList.get(2)),pickupTime,deliveryTime));

            // need for further discussion
            controller.setCurrentState(controller.calculatedState);

        }else{
            System.out.println("You have selected the same point. Please choose another point.");
        }
    }

    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request

        controller.setCurrentState(controller.addRequestState3);
    }

}
