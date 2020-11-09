package Controller;

import Model.Intersection;
import Model.Request;
import View.Window;

import java.util.ArrayList;

public class AddRequestState3 implements State{
    // Now you've chosen 2 intersection.

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        Intersection intersection = controller.getMap().NearestIntersection(positionX,positionY,window.getRate());

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
