package Controller;

import Algorithm.Graph;
import Algorithm.TSP;
import Model.Mission;
import View.Window;

public class AddRequestState5 implements State{
    // Now you have already 4 intersections and another frame to enter the duration


    @Override
    public void validNewRequest(Controller controller, Window window, ListOfCommands listOfCommands) {

        Graph g = controller.getGraph();
        Mission mission = controller.getMission();
        TSP tsp = controller.getTsp();

        listOfCommands.add(new AddCommand(g, mission, tsp, mission.getNewRequest(), mission.getNewAddList()));



        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        controller.setCurrentState(controller.calculatedState);

    }

    public void cancelNewRequest(Controller controller, Window window){
        // TODO: cancel the add of a new Request

        controller.setCurrentState(controller.addRequestState4);
    }
}
