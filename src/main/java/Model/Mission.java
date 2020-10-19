package Model;

import IHM.Meituan;
import Observer.Observable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Mission extends Observable {
    private static Intersection depot;
    private static LocalTime departureTime;
    private static ArrayList<Request> allRequests;
    private static LinkedList<Intersection> tour;
    private static HashMap<Intersection, LocalTime> timeSchedule;


    /**
     * Constructor of the object Mission
     */
    public Mission() {
        depot = null;
        departureTime = null;
        allRequests = new ArrayList<>();
    }

    /**
     * reset the mission. Attention, this is a static method
     */
    public static void reset() {
        if(depot == null) return;

        depot = null;
        departureTime = null;
        allRequests.clear();
    }

    /**
     * set the depot address(Intersection) and the departure time
     * @param depot the depot address(Intersection)
     * @param departureTime the departure time of the delivery man
     */
    public void setDepot(Intersection depot, LocalTime departureTime) {
        Mission.depot = depot;
        Mission.departureTime = departureTime;
    }

    /**
     * add a request to the mission
     * @param request the request to be added to the mission
     */
    public void addRequest(Request request) {
        allRequests.add(request);
    }

    public Intersection getDepot() {
        return depot;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public ArrayList<Request> getAllRequests() {
        return allRequests;
    }

    public LinkedList<Intersection> getTour() {
        return tour;
    }

    public HashMap<Intersection, LocalTime> getTimeSchedule() {
        return timeSchedule;
    }

    /**
     * set the tour for the mission when it's firstly calculated or its request list is updated
     * @param tour the new tour for the mission
     */
    public void setTour(LinkedList<Intersection> tour) {
        Mission.tour = tour;
    }

    /**
     * set the time schedule for the mission when it's firstly calculated or its request list is updated
     * @param timeSchedule the new time schedule for the mission
     */
    public void setTimeSchedule(HashMap<Intersection, LocalTime> timeSchedule) {
        Mission.timeSchedule = timeSchedule;
    }
}
