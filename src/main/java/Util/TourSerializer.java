package Util;

import Model.*;

import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.ListIterator;

public class TourSerializer {
    private Mission mission;
    private Map map;
    private StringBuffer navMessage;
    private Segment tempSegment;

    public TourSerializer(Mission mission, Map map) {
        this.mission = mission;
        this.map = map;
        navMessage = new StringBuffer();
        tempSegment = null;
    }

    public void generateRoadMap() throws ExceptionXML, IOException {
        fillRoadMap();
        File txt = FileOpener.getInstance().open(false);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(txt));
        bufferedWriter.write(navMessage.toString());
        bufferedWriter.close();
    }

    private void fillRoadMap() {
        depotNavigation();
        long current;
        long next;
        ListIterator i = mission.getTourIntersections().listIterator();
        current = (long) i.next();
        LocalTime arrivalTime = null;
        LocalTime departureTime = null;
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
                        addressNavigation(request, "pickup", mission.getArrivalTime(next), mission.getDepartureTime(next));
                    } else if (request.getDelivery().getId().equals(next)){
                        addressNavigation(request, "delivery", mission.getArrivalTime(next), mission.getDepartureTime(next));
                    }
                }
                current = next;
            }
        }
        returningNavigation();
//        System.out.println(navMessage.toString());
    }

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
    private void depotNavigation() {
        navMessage.append("Good morning Mr./ Ms. Staff, today your mission should start at ");
        navMessage.append(mission.getDepartureTime().toString());
        navMessage.append(", the entrepot/depot address is at the intersection ");
        navMessage.append(mission.getDepot().getId());
        navMessage.append(", whose longitude is ").append(mission.getDepot().getLongitude());
        navMessage.append(" and whose latitude is ").append(mission.getDepot().getLatitude()).append(".");
        navMessage.append("\n");
    }

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

    private void returningNavigation() {
        navMessage.append("\n");
        navMessage.append("Now you've finished your daily mission and you are back to the entrepot at ");
        navMessage.append(mission.getArrivalTimeSchedule().get(mission.getDepot().getId()));
        navMessage.append(". Thank you.");
    }
}
