package Model;

import IHM.Meituan;
import Observer.Observable;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Mission extends Observable {
    private static Intersection depot;
    private static LocalTime departureTime;
    private static ArrayList<Request> allRequests;
    private static LinkedList<Intersection> tour;
    private static HashMap<Intersection, LocalDateTime> timeSchedule;
    private static Map map;

    public Mission(Map map) {
        depot = null;
        departureTime = null;
        allRequests = new ArrayList<>();
        Mission.map = map;
    }

    public static void reset() {
        depot = null;
        departureTime = null;
        allRequests.clear();
        Meituan.setPaintRequestPaint(false);
    }

    public void addDepot(Intersection depot, LocalTime departureTime) {
        Mission.depot = depot;
        Mission.departureTime = departureTime;
    }

    public void addRequest(Request request) {
        allRequests.add(request);
    }

    public Map getMap() {
        return map;
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

    public HashMap<Intersection, LocalDateTime> getTimeSchedule() {
        return timeSchedule;
    }

    public void setTour(LinkedList<Intersection> tour) {
        Mission.tour = tour;
    }

    public void setTimeSchedule(HashMap<Intersection, LocalDateTime> timeSchedule) {
        Mission.timeSchedule = timeSchedule;
    }
}
