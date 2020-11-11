package Controller;

import Algorithm.Graph;
import Algorithm.TSP;
import Model.Mission;
import Model.Request;

import java.util.ArrayList;

public class AddCommand implements Command{
    private Graph g;
    private Mission mission;
    private TSP tsp;
    private Request request;
    private ArrayList<Long> replacedAddresses;

    private ArrayList<Long> lastAddressList;
    private ArrayList<Long> newAddressList;
    int num = -1;

    private boolean newOperation;

    /**
     * Constructor of the AddCommand class, that is the basic concrete element in the list of commands,
     * while the ReverseCommand simply interchange the doCommand and the undoCommand of the AddCommand
     * @param g the Graph
     * @param mission the Mission
     * @param tsp the TSP algorithm
     * @param request the concerned Request in one command (to be deleted or to be added)
     * @param replacedAddresses
     */
    public AddCommand(Graph g, Mission mission, TSP tsp, Request request, ArrayList<Long> replacedAddresses) {
        this.g = g;
        this.mission = mission;
        this.tsp = tsp;
        this.request = new Request(request);
        this.lastAddressList = new ArrayList<>();
        this.newAddressList = new ArrayList<>();
        this.replacedAddresses = new ArrayList<>(replacedAddresses);
        newOperation = true;

    }

    /**
     * the executive method that adds a new Request
     */
    @Override
    public void doCommand() {
        if(newOperation) {
            lastAddressList = new ArrayList<>(mission.getPartialTour(replacedAddresses.get(0), replacedAddresses.get(3)));
            System.out.println("lastAdressList");
            System.out.println(lastAddressList.toString());
            System.out.println("lastAdressList");

        }

        if (newOperation) {
            g.setRecalculatedAddresses(replacedAddresses, mission.getTour(), request);
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
            newAddressList = new ArrayList<>(mission.getPartialTour(replacedAddresses.get(0), replacedAddresses.get(3)));
            System.out.println("newAddressList");
            System.out.println(newAddressList.toString());
            System.out.println("newAddressList");
            newOperation = false;
        }

    }

    /**
     * the executive method that deletes a selected Request
     */
    @Override
    public void undoCommand() {

        if(newOperation) {
            newAddressList = new ArrayList<>(mission.getPartialTour(replacedAddresses.get(0), replacedAddresses.get(3)));
        }

        num = mission.deleteRequest(request);
        /*
        if (num == -1){
            return;
        }

         */

        if (newOperation) {
            // delete the pickup of the request
            ArrayList<Long> addressToUpdate = mission.getBeforeAfterAddress(request.getPickup().getId());
            Long[] sequence = new Long[2];
            sequence[0] = addressToUpdate.get(0);
            sequence[1] = addressToUpdate.get(1);
            ArrayList<Long> bestSolIntersection = new ArrayList<>();


            if(!g.getShortestPaths(false).containsKey(sequence[0]+" "+sequence[1])) {
                Long origin = sequence[0];
                Long destination = sequence[1];
                g.dijkstra(origin, destination);
            }
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


            if(!g.getShortestPaths(false).containsKey(sequence[0]+" "+sequence[1])) {
                Long origin = sequence[0];
                Long destination = sequence[1];
                g.dijkstra(origin, destination);
            }
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
            lastAddressList = new ArrayList<>(mission.getPartialTour(replacedAddresses.get(0), replacedAddresses.get(3)));
            newOperation = false;
        }

    }
}
