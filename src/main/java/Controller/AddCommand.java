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

    private ArrayList<Long> lastAddressList;
    private ArrayList<Long> newAddressList;
    int num = -1;

    public AddCommand(Graph g, Mission mission, TSP tsp, Request request, ArrayList<Long> replacedRequestList) {
        this.g = g;
        this.mission = mission;
        this.tsp = tsp;
        this.request = new Request(request);
        this.lastAddressList = new ArrayList<>();
        this.newAddressList = new ArrayList<>();
        this.replacedRequestList = new ArrayList<>(replacedRequestList);
    }


    @Override
    public void doCommand() {
        lastAddressList = mission.getPartialTour(replacedRequestList.get(0), replacedRequestList.get(3));

        g.setRecalculatedRequests(replacedRequestList, mission.getTour(), request);
        mission.setNewRequest(request);
        g.dijkstra(true);

        // start TSP calculation

        tsp.setRecalculate(true);
        Long[] solutions = tsp.searchSolution(30000, g);
        g.updateGraph();
        mission.updateAllRequests(num);
        mission.updatePartialTour(solutions, tsp.getBestSolIntersection(), tsp.getBestSolAddressCost());

        newAddressList = mission.getPartialTour(replacedRequestList.get(0), replacedRequestList.get(3));

        System.out.println("doCommand");
        System.out.println("Tour");
        for(int i = 0; i < mission.getTour().size(); ++i) {
            System.out.print(mission.getTour().get(i) +" ");
        }
        System.out.println("Tour");
        System.out.println("TourIntersection");
        for(int i = 0; i < mission.getTourIntersections().size(); ++i) {
            System.out.print(mission.getTourIntersections().get(i) +" ");
        }
        System.out.println("TourIntersection");
        System.out.println("all Requests");
        for(int i = 0; i < mission.getAllRequests().size(); ++i) {
            System.out.println(mission.getAllRequests().get(i).getPickup().getId());
        }
        System.out.println("all Requests");
        System.out.println("doCommand");
    }

    @Override
    public void undoCommand() {


        num = mission.deleteRequest(request);

        // delete the pickup of the request
        ArrayList<Long> addressToUpdate = mission.getBeforeAfterAddress(request.getPickup().getId());

        Long delivery = g.getDelivery(request.getPickup().getId());

        Long afterDelivery = mission.getBeforeAfterAddress(delivery).get(1);

        lastAddressList = mission.getPartialTour(addressToUpdate.get(0), afterDelivery);


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

//        newAddressList = mission.getPartialTour()

        System.out.println("undoCommand");
        System.out.println("Tour");
        for(int i = 0; i < mission.getTour().size(); ++i) {
            System.out.print(mission.getTour().get(i) +" ");
        }
        System.out.println("Tour");
        System.out.println("TourIntersection");
        for(int i = 0; i < mission.getTourIntersections().size(); ++i) {
            System.out.print(mission.getTourIntersections().get(i) +" ");
        }
        System.out.println("TourIntersection");
        System.out.println("undoCommand");
    }
}
