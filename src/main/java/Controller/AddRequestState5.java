package Controller;

import Algorithm.TSP;
import Model.Request;
import View.Window;

public class AddRequestState5 implements State{
    // Now you have already 4 intersections and another frame to enter the duration


    @Override
    public void validNewRequest(Controller controller, Window window) {
        // TODO: validate the two duration entered and then create a new Object Request.
        System.out.println("calculating tour");
        //JgraphtMapGraph.reset();
        //controller.getMapGraph().fillGraph(controller.getMap(), controller.getMission());
        //JgraphtMapGraph.calculateShortestPaths();
        System.out.println("test...............");
        controller.getCompleteGraph().setRecalculatedRequests(controller.getMission().getNewAddList(),
                controller.getMission().getTour(), controller.getMission().getNewRequest());
        controller.getCompleteGraph().dijkstra(true);
//        controller.getCompleteGraph().show();
        System.out.println("test...............");

        // start TSP calculation
        TSP tsp = controller.getTsp();
        tsp.setRecalcul(true);
        //Long[] solutions = tsp.searchSolution(100000, controller.getMapGraph());
        Long[] solutions = tsp.searchSolution(30000, controller.getCompleteGraph());
        System.out.println("Solution of cost "+tsp.getSolutionCost());
        controller.getMission().addTour(solutions, tsp.getBestSolIntersection(), tsp.getBestSolAddressCost());
        controller.getMission().updateAllRequests();
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        //TODO: update TextualView
        //window.getTextualView().updateRequestTable();
        controller.setCurrentState(controller.calculatedState);
    }

    public void cancelNewRequest(Controller controller, Window window){
        // TODO: cancel the add of a new Request

        controller.setCurrentState(controller.addRequestState4);
    }
}
