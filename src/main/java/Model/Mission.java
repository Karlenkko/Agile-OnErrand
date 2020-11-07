package Model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class Mission extends Observable {
    private static Intersection depot;
    private static LocalTime departureTime;
    private static ArrayList<Request> allRequests;
    private static LinkedList<Long> tour;
    private static LinkedList<Long> tourIntersections;
    private static HashMap<Long, LocalTime> arrivalTimeSchedule;
    private static HashMap<Long, LocalTime> departureTimeSchedule;
    private final static double SPEED = 25.0/6.0; // m/s

    private static ArrayList<Long> newAddList;
    private static Request newRequest;

    /**
     * Constructor of the object Mission
     */
    public Mission() {
        depot = null;
        departureTime = null;
        allRequests = new ArrayList<>();
        tour = new LinkedList<>();
        tourIntersections = new LinkedList<>();
        arrivalTimeSchedule = new HashMap<>();
        departureTimeSchedule = new HashMap<>();
        newAddList = new ArrayList<>();
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

    public LinkedList<Long> getTour() {
        return tour;
    }

    public HashMap<Long, LocalTime> getArrivalTimeSchedule() {
        return arrivalTimeSchedule;
    }

    public HashMap<Long, LocalTime> getDepartureTimeSchedule() {
        return departureTimeSchedule;
    }

    public void updateTour(Long[] sequence, List<Long> bestSolIntersection, double[] interAddressLength) {
        tour = new LinkedList<>(Arrays.asList(sequence));
        tourIntersections = new LinkedList<>(bestSolIntersection);
        LocalTime tempTime;
        tempTime = departureTime;
        // first arrival
        departureTimeSchedule.put(depot.getId(), departureTime);
        for (Request request : allRequests) {
            if (request.getPickup().getId().equals(sequence[1])) {
                tempTime = tempTime.plusSeconds((long) (interAddressLength[0] / SPEED));
                arrivalTimeSchedule.put(sequence[1], tempTime);
                tempTime = tempTime.plusSeconds(request.getPickupDuration());
                departureTimeSchedule.put(sequence[1], tempTime);
                break;
            }
        }

        for (int i = 2; i < interAddressLength.length; i++) {
            tempTime = tempTime.plusSeconds((long) (interAddressLength[i - 1] / SPEED));
            arrivalTimeSchedule.put(sequence[i], tempTime);
            for (Request request : allRequests) {
                if (request.getPickup().getId().equals(sequence[i])) {
                    tempTime = tempTime.plusSeconds(request.getPickupDuration());
                    departureTimeSchedule.put(sequence[i], tempTime);
                    break;
                } else if (request.getDelivery().getId().equals(sequence[i])) {
                    tempTime = tempTime.plusSeconds(request.getDeliveryDuration());
                    departureTimeSchedule.put(sequence[i], tempTime);
                    break;
                }
            }
        }
        // arrival back to the depot
        tempTime = tempTime.plusSeconds((long) (interAddressLength[interAddressLength.length - 1] / SPEED));
        arrivalTimeSchedule.put(depot.getId(), tempTime);
    }

    public void addTour(Long[] sequence, List<Long> bestSolIntersection, double[] interAddressLength) {

        updateAllRequests();

        int start = tour.indexOf(sequence[0]);
        int end = tour.indexOf(sequence[sequence.length-1]);
        for (int i = start+1; i<end; ++i) {
            System.out.println(tour.remove(start+1));

        }
        for (int i = 1; i < sequence.length-1; ++i) {
            tour.add(start+i, sequence[i]);

        }

        System.out.println("bestSolution");
        for(Long l : tourIntersections) {
            System.out.print(l+" ");
        }
        System.out.println("bestSolution");

        start = tourIntersections.indexOf(sequence[0]);
        end = tourIntersections.indexOf(sequence[sequence.length-1]);
        for (int i = start+1; i<end; ++i) {
            tourIntersections.remove(start+1);
        }
        for (int i = 1; i < bestSolIntersection.size()-1; ++i) {
            tourIntersections.add(start+i, bestSolIntersection.get(i));
        }

        LocalTime tempTime = departureTimeSchedule.get(sequence[0]);

        for (int i = 1; i < interAddressLength.length-1; i++) {
            tempTime = tempTime.plusSeconds((long) (interAddressLength[i - 1] / SPEED));
            arrivalTimeSchedule.put(sequence[i], tempTime);
            for (Request request : allRequests) {
                if (request.getPickup().getId().equals(sequence[i])) {
                    tempTime = tempTime.plusSeconds(request.getPickupDuration());
                    departureTimeSchedule.put(sequence[i], tempTime);
                    break;
                } else if (request.getDelivery().getId().equals(sequence[i])) {
                    tempTime = tempTime.plusSeconds(request.getDeliveryDuration());
                    departureTimeSchedule.put(sequence[i], tempTime);
                    break;
                }
            }
        }

        LocalTime previousTime = arrivalTimeSchedule.get(sequence[sequence.length-1]);
        tempTime = tempTime.plusSeconds((long) (interAddressLength[interAddressLength.length-1] / SPEED));
        long elapsedSeconds = Duration.between(previousTime, tempTime).toSeconds();
        for (int i = tour.indexOf(sequence[sequence.length-1]); i < tour.size(); ++i) {
            Long addressId = tour.get(i);
            arrivalTimeSchedule.put(addressId, arrivalTimeSchedule.get(addressId).plusSeconds(elapsedSeconds));
            departureTimeSchedule.put(addressId, departureTimeSchedule.get(addressId).plusSeconds(elapsedSeconds));
        }
        arrivalTimeSchedule.put(depot.getId(), arrivalTimeSchedule.get(depot.getId()).plusSeconds(elapsedSeconds));
    }

    public void initialTour() {
        tour.clear();
        if (depot != null) {
            tour.add(depot.getId());
            for (Request request : allRequests) {
                tour.add(request.getPickup().getId());
                tour.add(request.getDelivery().getId());
            }
        }
    }

    /**
     * set the tour for the mission when it's firstly calculated or its request list is updated
     * @param tour the new tour for the mission
     */
    public void setTour(LinkedList<Long> tour) {
        this.tour.set(0, tour.get(0));
        for(int i = 1; i < tour.size(); ++i) {
            this.tour.set(i, tour.get(tour.size()-i));
        }
    }

    /**
     * set the time schedule for the mission when it's firstly calculated or its request list is updated
     * @param timeSchedule the new time schedule for the mission
     */
    public void setArrivalTimeSchedule(HashMap<Long, LocalTime> timeSchedule) {
        Mission.arrivalTimeSchedule = timeSchedule;
    }

    public Intersection NearestRequest(int x, int y, double rate) {
        Intersection nearest = depot;
        double gapX = Math.abs(nearest.getX()/rate - x);
        double gapY = Math.abs(nearest.getY()/rate - y);
        for (Request request : allRequests) {
            Intersection p1 = request.getPickup();
            Intersection p2 = request.getDelivery();
            double gapX2 = Math.abs(p1.getX()/rate - x);
            double gapY2 = Math.abs(p1.getY()/rate - y);
            if ((gapX2 + gapY2) < (gapX + gapY)) {
                nearest = p1;
                gapX = gapX2;
                gapY = gapY2;
            }
            gapX2 = Math.abs(p2.getX()/rate - x);
            gapY2 = Math.abs(p2.getY()/rate - y);
            if ((gapX2 + gapY2) < (gapX + gapY)) {
                nearest = p2;
                gapX = gapX2;
                gapY = gapY2;
            }
        }
        return nearest;
    }

    public LocalTime getArrivalTime(Long addressId) {
        return arrivalTimeSchedule.getOrDefault(addressId, LocalTime.MIDNIGHT);
    }

    // there is a overloading
    public LocalTime getDepartureTime(Long addressId) {
        return departureTimeSchedule.getOrDefault(addressId, LocalTime.MIDNIGHT);
    }

    public LinkedList<Long> getTourIntersections() {
        return tourIntersections;
    }

    public ArrayList<Long> getNewAddList(){
        return newAddList;
    }

    public void addNewAdd(Long id) {
        newAddList.add(id);
    }

    public void resetNewAdd() {
        newAddList.clear();
    }

    public void updateAllRequests() {
        allRequests.add(newRequest);
        resetNewAdd();
        setNewRequest(null);
    }

    public void removeAdd() {
        if (newAddList.size() != 0) {
            newAddList.remove(newAddList.size()-1);
        }
    }

    public Request getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(Request newRequest) {
        Mission.newRequest = newRequest;
    }

    public boolean requestValid(Long idBefore, Long idAfter) {
        for (Request request : allRequests) {
            if (idAfter == request.getPickup().getId() && idBefore == request.getDelivery().getId()) {
                return false;
            }
        }
        return true;
    }


}
