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

        listOfCommands.add(new AddCommand(g, mission, tsp));



        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        controller.setCurrentState(controller.calculatedState);

        /*
        controller.getGraph().setRecalculatedRequests(controller.getMission().getNewAddList(),
                controller.getMission().getTour(), controller.getMission().getNewRequest());
        controller.getGraph().dijkstra(true);

        // start TSP calculation
        TSP tsp = controller.getTsp();
        tsp.setRecalculate(true);
        Long[] solutions = tsp.searchSolution(30000, controller.getGraph());
        controller.getGraph().updateGraph();
        controller.getMission().updateAllRequests();
        controller.getMission().updatePartialTour(solutions, tsp.getBestSolIntersection(), tsp.getBestSolAddressCost());
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        controller.setCurrentState(controller.calculatedState);
         */
    }

    public void cancelNewRequest(Controller controller, Window window){
        // TODO: cancel the add of a new Request

        controller.setCurrentState(controller.addRequestState4);
    }
}
