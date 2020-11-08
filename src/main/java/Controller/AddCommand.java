package Controller;

import Algorithm.Graph;
import Algorithm.TSP;
import Model.Mission;
import Model.Request;

import javax.management.remote.rmi.RMIConnectionImpl;
import java.time.LocalTime;
import java.util.ArrayList;

public class AddCommand implements Command{
    private Graph g;
    private Mission mission;
    private TSP tsp;
    private Request request;
    private ArrayList<Long> replacedRequestList;

    public AddCommand(Graph g, Mission mission, TSP tsp, Request request, ArrayList<Long> replacedRequestList) {
        this.g = g;
        this.mission = mission;
        this.tsp = tsp;
        this.request = new Request(request);
        this.replacedRequestList = new ArrayList<>(replacedRequestList);
        System.out.println("replacedRequests");
        for(Long l : replacedRequestList) {
            System.out.println(l);
        }
        System.out.println("replacedRequests");

    }


    @Override
    public void doCommand() {
        g.setRecalculatedRequests(replacedRequestList, mission.getTour(), request);
        mission.setNewRequest(request);
        g.dijkstra(true);

        // start TSP calculation

        tsp.setRecalculate(true);
        Long[] solutions = tsp.searchSolution(30000, g);
        g.updateGraph();
        mission.updateAllRequests();
        mission.updatePartialTour(solutions, tsp.getBestSolIntersection(), tsp.getBestSolAddressCost());
    }

    @Override
    public void undoCommand() {

        System.out.println("entering");
        mission.deleteRequest(request);

        // delete the pickup of the request
        ArrayList<Long> addressToUpdate = mission.getBeforeAfterAddress(request.getPickup().getId());
        Long[] sequence = new Long[2];
        sequence[0] = addressToUpdate.get(0);
        sequence[1] = addressToUpdate.get(1);
        ArrayList<Long> bestSolIntersection = new ArrayList<>();
        bestSolIntersection.addAll(g.getShortestPaths(false).get(sequence[0]+" "+sequence[1]));
        double[] interAddressLength = new double[1];
        interAddressLength[0] = g.getCost(sequence[0], sequence[1]);
        mission.updatePartialTour(sequence, bestSolIntersection,interAddressLength);

        // delete the delivery of the request
        addressToUpdate = mission.getBeforeAfterAddress(request.getDelivery().getId());
        sequence = new Long[2];
        sequence[0] = addressToUpdate.get(0);
        sequence[1] = addressToUpdate.get(1);
        bestSolIntersection = new ArrayList<>();
        bestSolIntersection.addAll(g.getShortestPaths(false).get(sequence[0]+" "+sequence[1]));
        interAddressLength = new double[1];
        interAddressLength[0] = g.getCost(sequence[0], sequence[1]);
        mission.updatePartialTour(sequence, bestSolIntersection,interAddressLength);
    }
}
