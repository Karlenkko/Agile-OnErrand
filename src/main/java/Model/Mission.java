package Model;

import Observer.Observable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Mission extends Observable {
    private Intersection depot;
    private LocalDateTime departureTime;
    private ArrayList<Request> allRequests;
    private LinkedList<Intersection> tour;
    private HashMap<Intersection, LocalDateTime> timeSchedule;

    /**
     * Constructor of object Mission, creates a mission with all requests and the depot info
     * @param depot the depot intersection for the delivery man to start his mission
     * @param departureTime the departure time of the delivery man from the depot
     * @param allRequests the list of all requests
     */
    public Mission(Intersection depot, LocalDateTime departureTime, ArrayList<Request> allRequests) {
        this.depot = depot;
        this.departureTime = departureTime;
        this.allRequests = allRequests;
    }

    public Intersection getDepot() {
        return depot;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public ArrayList<Request> getAllRequests() {
        return allRequests;
    }

    public LinkedList<Intersection> getTour() {
        return tour;
    }

    public HashMap<Intersection, LocalDateTime> getTimeSchedule() {
        return timeSchedule;
    }

    public void setTour(LinkedList<Intersection> tour) {
        this.tour = tour;
    }

    public void setTimeSchedule(HashMap<Intersection, LocalDateTime> timeSchedule) {
        this.timeSchedule = timeSchedule;
    }
}
