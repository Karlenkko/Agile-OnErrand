package Controller;

import Algorithm.TSP;
import View.Window;

public class RequestLoadedState implements State{

    /**
     * the detailed implementation of the calculate tour method that
     * calculates the requests loaded from a file on the pre-selected map
     * @param controller the Controller
     * @param window the main Window of the application
     */
    @Override
    public void calculateTour(Controller controller, Window window) {
        System.out.println("calculating tour");
        controller.getGraph().reset();
        controller.getGraph().fillGraph(controller.getMap());
        controller.getGraph().fillMission(controller.getMission().getAllRequests(), controller.getMission().getDepot());
        controller.getGraph().dijkstra(false);

        // start TSP calculation
        TSP tsp = controller.getTsp();
        tsp.setRecalculate(false);
        Long[] solutions = tsp.searchSolution(30000, controller.getGraph());
        System.out.println("Solution of cost "+tsp.getSolutionCost());
        controller.getMission().updateTour(solutions, tsp.getBestSolIntersection(), tsp.getBestSolAddressCost());

        // update view and controller
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        window.allow("calculatedState");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("The tour is generated. Then you can add or delete requests, " +
                "or export the RoadMap");
        controller.setCurrentState(controller.calculatedState);
    }
}
