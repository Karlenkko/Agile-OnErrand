package test.Util;

import Model.*;
import Util.XMLparser;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.xml.sax.SAXException;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static Util.XMLparser.*;
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
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder;

    private static String filePath_largeMap = "./fichiersXML2020/largeMap.xml";
    private static String filePath_mediumMap = "./fichiersXML2020/mediumMap.xml";
    private static String filePath_smallMap = "./fichiersXML2020/smallMap.xml";
    private static String filePath_requestsLarge7 = "./fichiersXML2020/requestsLarge7.xml";
    private static String filePath_requestsLarge9 = "./fichiersXML2020/requestsLarge9.xml";
    private static String filePath_requestsMedium3 = "./fichiersXML2020/requestsMedium3.xml";
    private static String filePath_requestsMedium5 = "./fichiersXML2020/requestsMedium5.xml";
    private static String filePath_requestsSmall1 = "./fichiersXML2020/requestsSmall1.xml";
    private static String filePath_requestsSmall2 = "./fichiersXML2020/requestsSmall2.xml";

//    private static Document largeMap = null;
//    private static Document mediumMap = null;
//    private static Document smallMap = null;
//    private static Document requestsLarge7 = null;
//    private static Document requestsLarge9 = null;
//    private static Document requestsMedium3 = null;
//    private static Document requestsMedium5 = null;
//    private static Document requestsSmall1 = null;
//    private static Document requestsSmall2 = null;

    private static XMLparser parser = null;

@Before
public void init()  throws ParseException {
    map = new Map();
    mission = new Mission();

//    try{
//        builder = factory.newDocumentBuilder();
//        largeMap = (Document) builder.parse(new File(filePath_largeMap));
//        mediumMap = (Document) builder.parse(new File(filePath_mediumMap));
//        smallMap = (Document) builder.parse(new File(filePath_smallMap));
//        requestsLarge7 = (Document) builder.parse(new File(filePath_requestsLarge7));
//        requestsLarge9 = (Document) builder.parse(new File(filePath_requestsLarge9));
//        requestsMedium3 = (Document) builder.parse(new File(filePath_requestsMedium3));
//        requestsMedium5 = (Document) builder.parse(new File(filePath_requestsMedium5));
//        requestsSmall1 = (Document) builder.parse(new File(filePath_requestsSmall1));
//        requestsSmall2= (Document) builder.parse(new File(filePath_requestsSmall2));
//    }
//    catch (final SAXException e) {
//        e.printStackTrace();
//    }
//    catch (final IOException e) {
//        e.printStackTrace();
//    } catch (ParserConfigurationException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//    }
}

@After
public void clean(){

//    builder = null;
//    largeMap = null;
//    mediumMap = null;
//    smallMap = null;
//    requestsLarge7 = null;
//    requestsLarge9 = null;
//    requestsMedium3 = null;
//    requestsMedium5 = null;
//    requestsSmall1 = null;
//    requestsSmall2 = null;

}
/** 
* 
* Method: parserMap(Map map) 
* 
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
* 
* Method: parserRequest(Mission mission) 
* 
*/ 
@Test
public void testParserRequest() throws Exception {
    parserMap(map);
    parserRequest(mission, map);

    // Load the large map then the requestLarge7

    ArrayList<Request> missionRequests = mission.getAllRequests();
    System.out.println(missionRequests.get(0).getPickup());
    assertEquals(1362781062, missionRequests.get(0).getPickup().getId(), 0);
    assertEquals(27359745, missionRequests.get(0).getDelivery().getId(), 0);
    assertEquals(540, missionRequests.get(0).getPickupDuration());
    assertEquals(540, missionRequests.get(0).getDeliveryDuration());

    Intersection missionDepot = mission.getDepot();
    assertEquals(25610888, missionDepot.getId(), 0);
    LocalTime eightOClock = LocalTime.of(8,0,0);
    assertEquals(eightOClock.toString(),mission.getDepartureTime().toString());

}


/** 
* 
* Method: buildMapFromDOMXML(Document document, Map map) 
* 
*/
@Test
public void testBuildMapFromDOMXML() throws Exception {

    parserMap(map);
    HashMap<Long, Intersection> mapIntersections = map.getAllIntersections();
    assertEquals(45.775486, mapIntersections.get(Long.parseLong("2684668925")).getLatitude(), 0.000001);
    assertEquals(4.888253, mapIntersections.get(Long.parseLong("2684668925")).getLongitude(), 0.000001);

}

/** 
* 
* Method: buildRequestFromDOMXML(Document document, Mission mission) 
* 
//*/
//@Test
//public void testBuildRequestFromDOMXML() throws Exception {
//    buildRequestFromDOMXML(document, request);
//    parserRequest(mission, map);
//
//    ArrayList<Request> missionRequests = mission.getAllRequests();
//    System.out.println(missionRequests.get(0).getPickup());
//    assertEquals(1362781062, missionRequests.get(0).getPickup().getId(), 0);
//    assertEquals(27359745, missionRequests.get(0).getDelivery().getId(), 0);
//}
//
///**
//*
//* Method: createIntersection(Element element)
//*
//*/
//@Test
//public void testCreateIntersection() throws Exception {
//    try {
//        Method method = XMLparser.createIntersection().getMethod("createSegment", Intersection.class, Map.class);
//        method.setAccessible(true);
//        method.invoke(<Intersection>, <5.06>);
//    } catch(NoSuchMethodException e) {
//    } catch(IllegalAccessException e) {
//    }

//}

/** 
* 
* Method: createSegment(Element element, Map map) 
* 
*/ 
//@Test
//public void testCreateSegment() throws Exception {
////TODO: Test goes here...
//try {
//   Method method = XMLparser.createSegment().getMethod("createSegment", Segment.class, Map.class);
//   method.setAccessible(true);
//   method.invoke(<Segment>, <Parameters>);
//} catch(NoSuchMethodException e) {
//} catch(IllegalAccessException e) {
//} catch(InvocationTargetException e) {
//}
//}

/** 
* 
* Method: createDepot(Element element, Mission mission) 
* 
*/ 
@Test
public void testCreateDepot() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XMLparser.getClass().getMethod("createDepot", Element.class, Mission.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: createLocalTime(Element element) 
* 
*/ 
@Test
public void testCreateLocalTime() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XMLparser.getClass().getMethod("createLocalTime", Element.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: createRequest(Element element, Mission mission) 
* 
*/ 
@Test
public void testCreateRequest() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XMLparser.getClass().getMethod("createRequest", Element.class, Mission.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
}

}
