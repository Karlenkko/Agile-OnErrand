package Controller;

import Algorithm.TSP;
import View.Window;

public class RequestLoadedState implements State{

    @Override
    public void calculateTour(Controller controller, Window window) {
        System.out.println("calculating tour");
        //JgraphtMapGraph.reset();
        //controller.getMapGraph().fillGraph(controller.getMap(), controller.getMission());
        //JgraphtMapGraph.calculateShortestPaths();
        System.out.println("test...............");
        controller.getGraph().reset();
        controller.getGraph().fillGraph(controller.getMap());
        controller.getGraph().setRequests(controller.getMission().getAllRequests(), controller.getMission().getDepot());
        controller.getGraph().dijkstra(false);
//        controller.getCompleteGraph().show();
        System.out.println("test...............");

        // start TSP calculation
        TSP tsp = controller.getTsp();
        tsp.setRecalculate(false);
        //Long[] solutions = tsp.searchSolution(100000, controller.getMapGraph());
        Long[] solutions = tsp.searchSolution(30000, controller.getGraph());
        System.out.println("Solution of cost "+tsp.getSolutionCost());
        controller.getMission().updateTour(solutions, tsp.getBestSolIntersection(), tsp.getBestSolAddressCost());
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        controller.setCurrentState(controller.calculatedState);
    }
}
