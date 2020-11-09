package test.Util;

import Model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static Util.XMLparser.parserMap;
import static Util.XMLparser.parserRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
* XMLparser Tester. 
* 
* @author <Authors name> 
* @since <pre>Oct 18, 2020</pre> 
* @version 1.0 
*/ 
public class XMLparserTest {
    /*Global variables*/
    Map map;
    Mission mission;


    /**
     * Initialise the map and mission for testing
      */
@Before
public void init()  {
    map = new Map();
    mission = new Mission();

}

@After
public void clean(){
}

    /**
     * read the map and test the correction of the map.
     * @throws Exception
     */
    @Test
public void testParserMap() throws Exception { 
    parserMap(map);

    // Load the largeMap.

    HashMap<Long, Intersection> mapIntersections = map.getAllIntersections();

    // Verify the data on the intersections

    // Verify that one of the information of the intersection which we chose randomly is correct

    assertEquals(45.775486, mapIntersections.get(Long.parseLong("2684668925")).getLatitude(), 0.000001);
    assertEquals(4.888253, mapIntersections.get(Long.parseLong("2684668925")).getLongitude(), 0.000001);

    // Verify the number total of the intersection is correct

    assertEquals(3736, mapIntersections.size());

    // Verify that all the three properties(latitude, longitude, id) of a intersection at Lyon is correct

    for(Long idIntersection : mapIntersections.keySet()){
        if(idIntersection <= 0){
            assertTrue("The idIntersection of latitude "+ mapIntersections.get(idIntersection).getLatitude()
                    +" and of longitude "+ mapIntersections.get(idIntersection).getLatitude() +" has a negative value.",false);
        }else if(mapIntersections.get(idIntersection).getLatitude() <= 45.5 || mapIntersections.get(idIntersection).getLatitude() >= 46.0){
            assertTrue("The idIntersection of latitude "+ mapIntersections.get(idIntersection).getLatitude()
                    +" and of longitude "+ mapIntersections.get(idIntersection).getLatitude() +" is not in the range of Lyon.",false);
        }else if(mapIntersections.get(idIntersection).getLongitude() <= 4.5 || mapIntersections.get(idIntersection).getLongitude() >= 5.0){
            assertTrue("The idIntersection of latitude "+ mapIntersections.get(idIntersection).getLatitude()
                    +" and of longitude "+ mapIntersections.get(idIntersection).getLatitude() +" is not in the range of Lyon.",false);
        }
    }

    // Verify the data of the segments

    LinkedList<Segment> mapSegments = map.getAllSegments();

    // Verify that one of the information of the first segment is correct

    assertEquals(Long.parseLong("2509481775"), mapSegments.get(0).getDestination().getId(), 0);
    assertEquals(Long.parseLong("2684668925"), mapSegments.get(0).getOrigin().getId(), 0);
    assertEquals("Rue Château-Gaillard", mapSegments.get(0).getName());
    assertEquals(97.249695, mapSegments.get(0).getLength(), 0.000001);

    // Verify the number total of the intersection is correct

    assertEquals(7811, mapSegments.size());

    // Verify that the each segment has a length positive

    for (int i = 0; i < mapSegments.size(); i++) {
        if(mapSegments.get(i).getLength() < 0){
            assertTrue("The length of the Segment "+ mapSegments.get(i).getName() +" can't be negative.",false);
        }
    }

    //Verify that the origin and destination of a segment should be a part of the map
    for (int i = 0; i < mapSegments.size(); i++) {
        Intersection intersectionDestination = mapSegments.get(i).getDestination();
        Intersection intersectionOrigin = mapSegments.get(i).getOrigin();
        if(!mapIntersections.containsValue(intersectionDestination) || !mapIntersections.containsValue(intersectionOrigin)){
            assertTrue("The origin or the destination of the segment "+ mapSegments.get(i).getName() +" is not on the map given.",false);
        }
    }

}

    /**
     * Read the map and the mission and then test the correction of the request.
     * @throws Exception
     */
    @Test
public void testParserRequest() throws Exception {
    parserMap(map);
    parserRequest(mission, map);

    // Load the large map then the requestLarge7

    ArrayList<Request> missionRequests = mission.getAllRequests();
    Intersection missionDepot = mission.getDepot();
    HashMap<Long, Intersection> mapIntersections = map.getAllIntersections();

    // Verify that the first request of the mission is correct.

    assertEquals(1362781062, missionRequests.get(0).getPickup().getId(),0);
    assertEquals(27359745, missionRequests.get(0).getDelivery().getId(),0);
    assertEquals(540, missionRequests.get(0).getPickupDuration());
    assertEquals(540, missionRequests.get(0).getDeliveryDuration());

    // Verify for each request, they have a valid duration( > 0) and the pickup address and the delivery address is in the given map.

    for (int i = 0; i < missionRequests.size(); i++) {
        Request request = missionRequests.get(i);

        assertTrue("The pickup duration cannot be negative",request.getPickupDuration() >= 0);
        assertTrue("The delivery duration cannot be negative",request.getDeliveryDuration() >= 0);

        Intersection intersectionPickup = request.getPickup();
        Intersection intersectionDelivery = request.getDelivery();
        if(!mapIntersections.containsValue(intersectionPickup) || !mapIntersections.containsValue(intersectionDelivery)){
            assertTrue("The intersection for pickup or delivery of request is not on the map given.",false);
        }

    }

    // Verify that the depot of the mission is correct.

    assertEquals(25610888, missionDepot.getId(), 0);
    LocalTime eightOClock = LocalTime.of(8,0,0);
    assertEquals(eightOClock.toString(),mission.getDepartureTime().toString());

    // Verify that the depot point is on the map given.

    if(!mapIntersections.containsValue(missionDepot)){
        assertTrue("The intersection for mission depot is not on the map given.",false);
    }

    // The intersection of the mission depot cannot be the same as the intersections for pickup and delivery.

    for (int i = 0; i < missionRequests.size(); i++) {
        Request request = missionRequests.get(i);
        assertTrue("The pickup or delivery intersection cannot be the depot intersection",
                request.getPickup().getId() != missionDepot.getId()
                        && request.getDelivery().getId() != missionDepot.getId());
    }
}
}
