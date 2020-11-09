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
        controller.setCurrentState(controller.addRequestState1);
    }

}
