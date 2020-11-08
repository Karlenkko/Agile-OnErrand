package Controller;

import Model.Intersection;
import Model.Request;
import View.Window;

import java.util.ArrayList;

public class AddRequestState3 implements State{
    // Now you've chosen 2 intersection.

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        // TODO: Click on the intersection and make it as the delivery point of the new request
        Intersection intersection = controller.getMap().NearestIntersection(positionX,positionY,window.getRate());

        ArrayList<Long> newAddList = controller.getMission().getNewAddList();
        if(!newAddList.contains(intersection.getId())){
            controller.getMission().addNewAdd(intersection.getId());

            window.getGraphicalView().setPaintAdd(true);
            window.getGraphicalView().repaint();

            controller.getMission().setNewRequest(new Request(controller.getMap().getAllIntersections().get(newAddList.get(1))
                    ,controller.getMap().getAllIntersections().get(newAddList.get(2)),0,0));

            controller.setCurrentState(controller.addRequestState4);

        }else{
            System.out.println("You have selected the same point. Please choose another point.");
        }
    }

    public void rightClick(Controller controller, Window window) {
        // TODO: Cancel the addition of the new request
        controller.getMission().removeAdd();
        window.getGraphicalView().repaint();
        controller.setCurrentState(controller.addRequestState2);
    }
}
