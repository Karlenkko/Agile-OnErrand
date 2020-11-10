package Controller;

import Algorithm.Graph;
import Algorithm.TSP;
import Model.Mission;
import View.Window;

public class AddRequestState5 implements State{
    // Now you have already 4 intersections and another frame to enter the duration

    /**
     * the detailed implementation of the method validateNewRequest
     * that invokes the add command of the ListOfCommands that invokes the doCommand
     * @param controller the Controller
     * @param window the new pop up window
     * @param listOfCommands the list of commands
     */
    @Override
    public void validateNewRequest(Controller controller, Window window, ListOfCommands listOfCommands) {

        Graph g = controller.getGraph();
        Mission mission = controller.getMission();
        TSP tsp = controller.getTsp();

        listOfCommands.add(new AddCommand(g, mission, tsp, mission.getNewRequest(), mission.getNewAddList()));
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        window.allow("calculatedState");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You have added a new request");
        window.getTextualView().setLockInstruction(true);
        controller.setCurrentState(controller.calculatedState);

    }

    /**
     * the detailed implementation of the method cancelNewRequest
     * that returns back to the CalculatedState and invokes no calculation
     * @param controller the Controller
     * @param window the new pop up window
     */
    @Override
    public void cancelNewRequest(Controller controller, Window window){
        // TODO: cancel the add of a new Request
        controller.getMission().resetNewAdd();
        window.getGraphicalView().repaint();
        window.allow("calculatedState");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You have cancel the addition of a new request");
        window.getTextualView().setLockInstruction(true);
        controller.setCurrentState(controller.calculatedState);
    }
}
