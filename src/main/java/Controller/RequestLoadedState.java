package Controller;

import Algorithm.TSP;
import View.Window;

public class RequestLoadedState implements State{

    @Override
    public void calculateTour(Controller controller, Window window) {
        System.out.println("calculating tour");
        //MapGraph.reset();
        //controller.getMapGraph().fillGraph(controller.getMap(), controller.getMission());
        //MapGraph.calculateShortestPaths();
        System.out.println("test...............");
        controller.getCompleteGraph().reset();
        controller.getCompleteGraph().fillGraph(controller.getMap());
        controller.getCompleteGraph().setRequests(controller.getMission().getAllRequests(), controller.getMission().getDepot());
        controller.getCompleteGraph().dijkstra();
//        controller.getCompleteGraph().show();
        System.out.println("test...............");

        // start TSP calculation
        TSP tsp = controller.getTsp();
        //Long[] solutions = tsp.searchSolution(100000, controller.getMapGraph());
        Long[] solutions = tsp.searchSolution(10000, controller.getCompleteGraph());
        System.out.println("Solution of cost "+tsp.getSolutionCost());
        controller.getMission().updateTour(solutions, tsp.getBestSolIntersection(), tsp.getBestSolAddressCost());
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        controller.setCurrentState(controller.calculatedState);
    }
}
