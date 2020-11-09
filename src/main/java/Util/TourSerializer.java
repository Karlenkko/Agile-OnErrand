package Util;

import Model.Map;
import Model.Mission;
import Model.Request;
import Model.Segment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ListIterator;

public class TourSerializer {
    private Mission mission;
    private Map map;
    private StringBuffer navMessage;
    private Segment tempSegment;

    /**
     * Constructor of the class TourSerializer. It initialise the route map which we are
     * @param mission the instance of the mission where the we want to generate the route map from
     * @param map the instance of the map where we base the route map.
     */
    public TourSerializer(Mission mission, Map map) {
        this.mission = mission;
        this.map = map;
        navMessage = new StringBuffer();
        tempSegment = null;
    }

    /**
     * Generate the roadMap and then close it correctly.
     * @throws ExceptionXML
     * @throws IOException
     */
    public void generateRoadMap() throws ExceptionXML, IOException {
        fillRoadMap();
        File txt = FileOpener.getInstance().open(false);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(txt));
        bufferedWriter.write(navMessage.toString());
        bufferedWriter.close();
    }

    /**
     * Fill the roadMap by using the given mission.
     */
    private void fillRoadMap() {
        depotNavigation();
        long current;
        long next;
        ListIterator i = mission.getTourIntersections().listIterator();
        current = (long) i.next();
        ArrayList<Request> passedPickups = new ArrayList<>();
        while (i.hasNext()) {
            next = (long) i.next();
            if (current == next) {
                continue;
            } else {
                System.out.println(current);
                System.out.println(next);
                segmentNavigation(current, next);
                for (Request request : mission.getAllRequests()) {
                    if (request.getPickup().getId().equals(next)) {
                        passedPickups.add(request);
                        addressNavigation(request, "pickup", mission.getArrivalTime(next), mission.getDepartureTime(next));
                    } else if (request.getDelivery().getId().equals(next)){
                        for (Request passedPickup : passedPickups) {
                            if (passedPickup.getDelivery().getId().equals(next)){
                                addressNavigation(request, "delivery", mission.getArrivalTime(next), mission.getDepartureTime(next));
                                break;
                            }
                        }
                    }
                }
                current = next;
            }
        }
        returningNavigation();
//        System.out.println(navMessage.toString());
    }

    /**
     * For a request, write down how to go from the departure point to the arrival point.
     * @param request the request which the user want to add to the roadMap.
     * @param type the type of the request. Can be 'pickup' or 'delivery'
     * @param arrivalTime the arrival time of the request
     * @param departureTime the departure time of the request
     */
    private void addressNavigation(Request request, String type, LocalTime arrivalTime, LocalTime departureTime) {
        navMessage.append("\n");
        navMessage.append("You are expected to arrive at the next ");
        navMessage.append(type).append(" Address, which is the intersection ");
        navMessage.append(request.getPickup().getId());
        if ("pickup".equals(type)){
            navMessage.append(", whose longitude is ").append(request.getPickup().getLongitude());
            navMessage.append(" and whose latitude is ").append(request.getPickup().getLatitude()).append(", ");
        } else if ("delivery".equals(type)){
            navMessage.append(", whose longitude is ").append(request.getDelivery().getLongitude());
            navMessage.append(" and whose latitude is ").append(request.getDelivery().getLatitude()).append(", ");
        }
        navMessage.append("at about ").append(arrivalTime.toString());
        navMessage.append(". The ").append(type).append(" is expected to last for ");
        if ("pickup".equals(type)) {
            navMessage.append(request.getPickupDuration());
        } else if ("delivery".equals(type)) {
            navMessage.append(request.getDeliveryDuration());
        }
        navMessage.append(" seconds.");
        navMessage.append(" Then at ").append(departureTime.toString()).append(", you should move on.");
        navMessage.append("\n");
        navMessage.append("\n");
    }

    /**
     * Write in the document about the information for the depot.
     */
    private void depotNavigation() {
        navMessage.append("Good morning Mr./ Ms. Staff, today your mission should start at ");
        navMessage.append(mission.getDepartureTime().toString());
        navMessage.append(", the entrepot/depot address is at the intersection ");
        navMessage.append(mission.getDepot().getId());
        navMessage.append(", whose longitude is ").append(mission.getDepot().getLongitude());
        navMessage.append(" and whose latitude is ").append(mission.getDepot().getLatitude()).append(".");
        navMessage.append("\n");
        navMessage.append("\n");
    }

    /**
     * Write in the document the information for the segment.
     * @param origin the intersection origin for the segment.
     * @param destination the intersection destination for the segment.
     */
    private void segmentNavigation(Long origin, Long destination) {
        for (Segment segment : map.getAllSegments()) {
            if (segment.getOrigin().getId().equals(origin) && segment.getDestination().getId().equals(destination)) {
                tempSegment = segment;
                break;
            }
        }
        navMessage.append("At the ").append(tempSegment.getName()).append(", ");
        navMessage.append("go along from the intersection ").append(origin).append(" ");
        navMessage.append("for ").append(tempSegment.getLength()).append(" meters.");
        navMessage.append(" Then you will arrive at the intersection ").append(destination).append(".");
        navMessage.append("\n");
    }

    /**
     * Write in the document after the user finish the tour and try to go back to the depot point.
     */
    private void returningNavigation() {
        navMessage.append("\n");
        navMessage.append("Now you've finished your daily mission and you are back to the entrepot at ");
        navMessage.append(mission.getArrivalTimeSchedule().get(mission.getDepot().getId()));
        navMessage.append(". Thank you.");
    }
}
