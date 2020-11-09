package Controller;

import Model.Intersection;
import View.Window;

public class AddRequestState2 implements State{
    // Now you've chosen 1 intersection.

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        Intersection intersection = controller.getMap().NearestIntersection(positionX,positionY,window.getRate());
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
