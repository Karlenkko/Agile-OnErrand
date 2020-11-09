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

    private boolean newOperation;

    public AddCommand(Graph g, Mission mission, TSP tsp, Request request, ArrayList<Long> replacedRequestList) {
        this.g = g;
        this.mission = mission;
        this.tsp = tsp;
        this.request = new Request(request);
        this.lastAddressList = new ArrayList<>();
        this.newAddressList = new ArrayList<>();
        this.replacedRequestList = new ArrayList<>(replacedRequestList);
        newOperation = true;

    }


    @Override
    public void doCommand() {

        if(newOperation) {
            lastAddressList = new ArrayList<>(mission.getPartialTour(replacedRequestList.get(0), replacedRequestList.get(3)));
            System.out.println("lastAdressList");
            System.out.println(lastAddressList.toString());
            System.out.println("lastAdressList");

        }

        if (newOperation) {
            g.setRecalculatedRequests(replacedRequestList, mission.getTour(), request);
            mission.setNewRequest(request);
            g.dijkstra(true);

            // start TSP calculation

            tsp.setRecalculate(true);
            Long[] solutions = tsp.searchSolution(30000, g);
            g.updateGraph();
            mission.updateAllRequests(num);
            mission.updatePartialTour(solutions, tsp.getBestSolIntersection(), tsp.getBestSolAddressCost());
        } else {
            mission.setNewRequest(request);
            mission.updateAllRequests(num);
            Long[] solutions = new Long[newAddressList.size()];
            for(int i = 0; i < solutions.length; ++i) {
                solutions[i] = newAddressList.get(i);
            }
            mission.updatePartialTour(solutions, g.getRoute(solutions), g.getSolutionCost(solutions));
        }



        if(newOperation) {
            newAddressList = new ArrayList<>(mission.getPartialTour(replacedRequestList.get(0), replacedRequestList.get(3)));
            System.out.println("newAddressList");
            System.out.println(newAddressList.toString());
            System.out.println("newAddressList");
            newOperation = false;
        }


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

        if(newOperation) {
            newAddressList = new ArrayList<>(mission.getPartialTour(replacedRequestList.get(0), replacedRequestList.get(3)));
        }

        num = mission.deleteRequest(request);

        if (newOperation) {
            // delete the pickup of the request
            ArrayList<Long> addressToUpdate = mission.getBeforeAfterAddress(request.getPickup().getId());
            Long[] sequence = new Long[2];
            sequence[0] = addressToUpdate.get(0);
            sequence[1] = addressToUpdate.get(1);
            ArrayList<Long> bestSolIntersection = new ArrayList<>();

            System.out.println(sequence[0]+" "+sequence[1]);

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

            System.out.println(sequence[0]+" "+sequence[1]);

            bestSolIntersection.addAll(g.getShortestPaths(false).get(sequence[0]+" "+sequence[1]));
            interAddressLength = new double[1];
            interAddressLength[0] = g.getCost(sequence[0], sequence[1]);
            mission.updatePartialTour(sequence, bestSolIntersection,interAddressLength);

        } else {
            System.out.println("lastAddressList undo");
            System.out.println(lastAddressList.toString());
            System.out.println("lastAddressList undo");
            Long[] solutions = new Long[lastAddressList.size()];
            for(int i = 0; i < solutions.length; ++i) {
                solutions[i] = lastAddressList.get(i);
            }
            mission.updatePartialTour(solutions, g.getRoute(solutions), g.getSolutionCost(solutions));
        }


        if(newOperation) {
            lastAddressList = new ArrayList<>(mission.getPartialTour(replacedRequestList.get(0), replacedRequestList.get(3)));
            newOperation = false;
        }

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
