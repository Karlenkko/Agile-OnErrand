package Controller;

import Algorithm.Graph;
import Algorithm.TSP;
import Model.Mission;

public class AddCommand implements Command{
    private Graph g;
    private Mission mission;
    private TSP tsp;

    public AddCommand(Graph g, Mission mission, TSP tsp) {
        this.g = g;
        this.mission = mission;
        this.tsp = tsp;
    }

    @Override
    public void doCommand() {
        g.setRecalculatedRequests(mission.getNewAddList(), mission.getTour(), mission.getNewRequest());
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


    }
}
