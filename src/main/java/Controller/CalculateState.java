package Controller;

import Algorithm.MapGraph;
import Algorithm.TSP;
import Algorithm.TSP1;
import Model.Request;
import Model.Segment;
import View.Window;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

public class CalculateState implements State{

    @Override
    public void calculateTour(Controller controller, Window window) {
        //TODO: execute algorithm, then update mission, show on window
        System.out.println("calculating tour");
        MapGraph.reset();
        Set<Long> allIntersections = controller.getMap().getAllIntersections().keySet();
        LinkedList<Segment> allSegments = controller.getMap().getAllSegments();
        ArrayList<Request> allRequests = controller.getMission().getAllRequests();
        // initialize and fill MapGraph
        for (Long intersection : allIntersections) {
            controller.getMapGraph().addVertex(intersection);
        }
        for (Segment segment : allSegments) {
            controller.getMapGraph().addEdge(segment.getOrigin().getId(), segment.getDestination().getId(), segment.getLength());
        }
        for (Request request : allRequests) {
            controller.getMapGraph().setAddressPriorities(request.getPickup().getId(), request.getDelivery().getId());
            controller.getMapGraph().addAddress(request.getPickup().getId());
            controller.getMapGraph().addAddress(request.getDelivery().getId());
        }
        controller.getMapGraph().addAddress(controller.getMission().getDepot().getId());
        controller.getMapGraph().setDepotAddress(controller.getMission().getDepot().getId());

        MapGraph.calculateShortestPaths();
        // start TSP calculation
        TSP tsp = controller.getTsp();
        tsp.searchSolution(100000, controller.getMapGraph());
        System.out.print("Solution of cost "+tsp.getSolutionCost());
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
    }
}
