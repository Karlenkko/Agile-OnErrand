package test.Data; 

import Model.*;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static Data.XMLparser.*;
import static org.junit.Assert.assertEquals;

/** 
* XMLparser Tester. 
* 
* @author <Authors name> 
* @since <pre>Oct 18, 2020</pre> 
* @version 1.0 
*/ 
public class XMLparserTest {

    Map map;
    Mission mission;
@Before
public void init() {
    map = new Map();
    mission = new Mission();

}

@After
public void exit(){
}
/** 
* 
* Method: parserMap(Map map) 
* 
*/ 
@Test
public void testParserMap() throws Exception { 
    parserMap(map);

    HashMap<Long, Intersection> mapIntersections = map.getAllIntersections();
    assertEquals(45.775486, mapIntersections.get(Long.parseLong("2684668925")).getLatitude(), 0.000001);
    assertEquals(4.888253, mapIntersections.get(Long.parseLong("2684668925")).getLongitude(), 0.000001);

    LinkedList<Segment> mapSegments = map.getAllSegments();
    assertEquals(Long.parseLong("2509481775"), mapSegments.get(0).getDestination().getId(), 0);
    assertEquals(Long.parseLong("2684668925"), mapSegments.get(0).getOrigin().getId(), 0);
    assertEquals("Rue Château-Gaillard", mapSegments.get(0).getName());
    assertEquals(97.249695, mapSegments.get(0).getLength(), 0.000001);

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

    ArrayList<Request> missionRequests = mission.getAllRequests();
    System.out.println(missionRequests.get(0).getPickup());
    assertEquals(1362781062, missionRequests.get(0).getPickup().getId(), 0);
    assertEquals(27359745, missionRequests.get(0).getDelivery().getId(), 0);
    assertEquals(540, missionRequests.get(0).getPickupDuration());
    assertEquals(540, missionRequests.get(0).getDeliveryDuration());

    Intersection missionDepot = mission.getDepot();
    assertEquals(25610888, missionDepot.getId(), 0);

}


/** 
* 
* Method: buildMapFromDOMXML(Document document, Map map) 
* 
*/ 
@Test
public void testBuildMapFromDOMXML() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XMLparser.getClass().getMethod("buildMapFromDOMXML", Document.class, Map.class); 
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
* Method: buildRequestFromDOMXML(Document document, Mission mission) 
* 
*/ 
@Test
public void testBuildRequestFromDOMXML() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XMLparser.getClass().getMethod("buildRequestFromDOMXML", Document.class, Mission.class); 
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
* Method: createIntersection(Element element) 
* 
*/ 
@Test
public void testCreateIntersection() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XMLparser.getClass().getMethod("createIntersection", Element.class); 
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
* Method: createSegment(Element element, Map map) 
* 
*/ 
@Test
public void testCreateSegment() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XMLparser.getClass().getMethod("createSegment", Element.class, Map.class); 
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
