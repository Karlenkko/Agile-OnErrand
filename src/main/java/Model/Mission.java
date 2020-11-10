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
    private static ArrayList<Integer> indexTable;
    private static int maxIndex;
    private static ArrayList<Request> requestsToDelete;

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
        indexTable = new ArrayList<>();
        maxIndex = 1;
        requestsToDelete = new ArrayList<>();
    }

    /**
     * reset the mission. Attention, this is a static method
     */
    public static void reset() {
        if(depot == null) return;

        depot = null;
        departureTime = null;
        allRequests.clear();
        indexTable.clear();
        tour.clear();
        tourIntersections.clear();
        arrivalTimeSchedule.clear();
        departureTimeSchedule.clear();
        newAddList.clear();
        maxIndex = 1;
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
        incrementIndexTable();
    }

    /**
     * Make the index table add one.
     */
    private void incrementIndexTable() {
        indexTable.add(maxIndex++);
    }

    /**
     * Make the index table added a certain number.
     * @param num the number which we want to add.
     */
    private void incrementIndexTable(int num) {
        indexTable.add(num);
    }

    /**
     * Obtain the intersection where the mission begins and ends.
     * @return depot the intersection where the mission begins and ends.
     */
    public Intersection getDepot() {
        return depot;
    }

    /**
     * Obtain the localTime which the mission starts.
     * @return departureTime the localTime which the mission starts.
     */
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Obtain all the requests of this mission
     * @return allRequests all the requests of this mission
     */
    public ArrayList<Request> getAllRequests() {
        return allRequests;
    }

    /**
     * Obtain the sequence of the intersections the user should pass to complete the tour.
     * @return tour a linkedList which contains all the id of the intersections the user should pass.
     */
    public LinkedList<Long> getTour() {
        return tour;
    }

    /**
     * Obtain a hashmap which contains all the arrival's localTime for each intersection the user should pass.
     * The key is the id for the intersection and the value is the localTime.
     * @return arrivalTimeSchedule a hashmap which contains all the arrival's localTime for each intersection the user should pass.
     */
    public HashMap<Long, LocalTime> getArrivalTimeSchedule() {
        return arrivalTimeSchedule;
    }

    /**
     * Obtain a hashmap which contains all the departure's localTime for each intersection the user should pass.
     * The key is the id for the intersection and the value is the localTime.
     * @return departureTimeSchedule a hashmap which contains all the departure's localTime for each intersection the user should pass.
     */
    public HashMap<Long, LocalTime> getDepartureTimeSchedule() {
        return departureTimeSchedule;
    }

    /**
     * When the user chooses to add a request or delete a request in the mission, the system will update the tour calculated.
     * @param sequence
     * @param bestSolIntersection
     * @param interAddressLength
     */
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

    public void updatePartialTour(Long[] sequence, List<Long> bestSolIntersection, double[] interAddressLength) {

        //updateAllRequests();
        System.out.println("Tour");
        for(int i = 0; i < tour.size(); ++i) {
            System.out.print(tour.get(i) +" ");
        }
        System.out.println("Tour");
        System.out.println("Sequence");
        for(int i = 0; i < sequence.length; ++i) {
            System.out.print(sequence[i] +" ");
        }
        System.out.println("Sequence");

        int start = tour.indexOf(sequence[0]);
        for (int i = tour.size()-1; i > tour.indexOf(sequence[0]); --i) {
            if (tour.get(i).equals(sequence[0]))
                start = i;
        }
        int end = tour.indexOf(sequence[sequence.length-1]);
        end = end == 0 ? tour.size(): end;

        if(sequence.length == 2) {
            Long i = tour.get(start+1);
            for(Request request : allRequests) {
                if (request.getPickup().getId().equals(i) || request.getDelivery().getId().equals(i)) {
                    return;
                }
            }
        }



        for (int i = start+1; i<end; ++i) {
            boolean exist = false;
            for(Request request : allRequests) {
                if (request.getPickup().getId().equals(i) || request.getDelivery().getId().equals(i)) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                continue;
            }
            tour.remove(start+1);
        }


        for (int i = 1; i < sequence.length-1; ++i) {
            tour.add(start+i, sequence[i]);
        }


        start = tourIntersections.indexOf(sequence[0]);
        for (int i = tourIntersections.size()-1; i > tourIntersections.indexOf(sequence[0]); --i) {
            if (tourIntersections.get(i).equals(sequence[0]))
                start = i;
        }
        /*
        for (int i= tourIntersections.size()-1; i > start; --i) {
            if (sequence[sequence.length - 1].equals(tourIntersections.get(i))) {
                end = i;
                break;
            }
        }

         */
        end = tourIntersections.indexOf(sequence[sequence.length - 1]);
        end = end == 0 ? tourIntersections.size(): end;
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

    public void updateAllRequests(int num) {
        allRequests.add(newRequest);
        if (num == -1) {
            incrementIndexTable();
        } else {
            incrementIndexTable(num);
        }
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
            System.out.println("checking" + request.getPickup().getId());
            if (idAfter.equals(request.getPickup().getId()) && idBefore.equals(request.getDelivery().getId())) {
                return false;
            }
        }
        return true;
    }

    public Request deleteRequest(int index) {
        for (int i = 0; i < indexTable.size(); ++i) {
            if (indexTable.get(i).equals(index)) {
                indexTable.remove(i);
                return allRequests.remove(i);
            }
        }
        return null;
    }

    public int deleteRequest(Request request) {
        int num = -1;
        if (allRequests.contains(request)) {
            int index = allRequests.indexOf(request);
            num = indexTable.remove(index);
        }
        allRequests.remove(request);
        return num;
    }

    public ArrayList<Long> getBeforeAfterAddress(Long id) {
        ArrayList<Long> addressToUpdate = new ArrayList<>();
        int i = tour.indexOf(id);
        addressToUpdate.add(tour.get(i-1));
        if (i+1 > tour.size()-1){
            addressToUpdate.add(tour.get(0));
        } else {
            addressToUpdate.add(tour.get(i+1));
        }
        return addressToUpdate;
    }


    public int getMaxIndex() {
        return maxIndex;
    }

    public ArrayList<Long> getReplacedRequestsList(Request request) {
        ArrayList<Long> replacedRequestsList = new ArrayList<>();
        for (int i = 0; i < tour.size(); ++i) {
            if(tour.get(i) - request.getPickup().getId() == 0) {
                replacedRequestsList.add(tour.get(i-1));
                replacedRequestsList.add(tour.get(i));
            }
            if(tour.get(i) - request.getDelivery().getId() == 0) {
                replacedRequestsList.add(tour.get(i));
                if (i == tour.size()-1) {
                    replacedRequestsList.add(tour.get(0));
                }else {
                    replacedRequestsList.add(tour.get(i+1));
                }
                break;
            }
        }
        return replacedRequestsList;
    }

    public ArrayList<Integer> getIndexTable() {
        return indexTable;
    }

    public ArrayList<Long> getPartialTour(Long startAddress, Long arrivalAddress) {

        int lastOccurrence = 0;
        for (int i = tour.size()-1; i > 0; --i) {
            if (tour.get(i).equals(arrivalAddress)) {
                lastOccurrence = i;
                break;
            }
        }

        ArrayList<Long> partialTour = new ArrayList<>();
        boolean add = false;
        int i = 0;
        for (Long address : tour) {
            if (address.equals(startAddress)) {
                add = true;
                partialTour.add(address);
                continue;
            }
            if (add) {
                partialTour.add(address);
            }
            if (i == lastOccurrence && i != 0){
                add = false;
            }
            ++i;
        }

        if (lastOccurrence == 0) {
            partialTour.add(arrivalAddress);
        }
        return partialTour;
    }

    public void setDelete(Intersection intersection) {
        requestsToDelete.clear();
        for (Request request : allRequests) {
            if (request.getPickup().getId().equals(intersection.getId()) || request.getDelivery().getId().equals(intersection.getId())) {
                requestsToDelete.add(request);
                System.out.println(request.toString());
            }
        }
    }

    public void setDelete(int num) {
        requestsToDelete.clear();
        if(num == -1)
            return;
        requestsToDelete.add(allRequests.get(indexTable.indexOf(num)));
    }

    public void resetDelete() {
        requestsToDelete.clear();
    }

    public ArrayList<Request> getRequestsToDelete() {
        return requestsToDelete;
    }

    public boolean isDeletedRequest(String s) {
        for (Request request : requestsToDelete) {
            int index = indexTable.get(allRequests.indexOf(request));
            if(s.contains(index+"")) {
                System.out.println("isDeletedRequest");
                return true;
            }
        }
        return false;
    }
}
