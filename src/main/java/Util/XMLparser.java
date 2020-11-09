package Util;

import Model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class XMLparser {

    /**
     * Read a map from a XML file and then stock the useful information in the parameter map.
     * @param map the instance of map where we stock the map which we read.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws ExceptionXML
     */
    public static void parserMap(Map map) throws ParserConfigurationException, IOException, SAXException, ExceptionXML {
        File xml = FileOpener.getInstance().open(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        document.getDocumentElement().normalize();
        NodeList root = document.getElementsByTagName("map");
        if (root.getLength() != 0) {
            Map.reset();
            buildMapFromDOMXML(document, map);
        } else
            JOptionPane.showMessageDialog(null, "Please select a correct map", "alert", JOptionPane.ERROR_MESSAGE);

    }

    /**
     * Read a mission from a XML file and then stock the useful information in the parameter mission.
     * @param mission the instance of mission where we stock the mission which we read.
     * @param map the instance of map where correspond the mission read from XML file.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws ExceptionXML
     */
    public static void parserRequest(Mission mission, Map map) throws ParserConfigurationException, IOException, SAXException, ExceptionXML {
        File xml = FileOpener.getInstance().open(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        document.getDocumentElement().normalize();
        NodeList root = document.getElementsByTagName("planningRequest");
        if (root.getLength() != 0) {
            Mission.reset();
            try {
                buildRequestFromDOMXML(document, mission, map);
            } catch (ExceptionXML e) {
                throw e;
            }
        } else
            JOptionPane.showMessageDialog(null, "Please select a correct request", "alert", JOptionPane.ERROR_MESSAGE);

    }

    /**
     * Read le XML file and added the intersections and segments into the map.
     * @param document the instance of the document which is going to be read.
     * @param map the instance of map where we stock the map which we read.
     */
    private static void buildMapFromDOMXML(Document document, Map map) {
        NodeList intersectionList = document.getElementsByTagName("intersection");
        for (int i = 0; i < intersectionList.getLength(); ++i) {
            map.addIntersection(createIntersection((Element) intersectionList.item(i)));
        }
        NodeList segmentList = document.getElementsByTagName("segment");
        for (int i = 0; i < segmentList.getLength(); ++i) {
            map.addSegment(createSegment((Element) segmentList.item(i), map));
        }

    }

    /**
     * Read le XML file and added the depot and requests into the mission.
     * @param document the instance of the document which is going to be read.
     * @param mission the instance of mission where we stock the mission which we read.
     * @param map the instance of map where correspond the mission read from XML file.
     * @throws ExceptionXML
     */
    private static void buildRequestFromDOMXML(Document document, Mission mission, Map map) throws ExceptionXML {
        NodeList depotList = document.getElementsByTagName("depot");
        for (int i = 0; i < depotList.getLength(); ++i) {
            mission.setDepot
                    (createDepot((Element) depotList.item(i), mission, map), createLocalTime((Element) depotList.item(i)));
        }
        NodeList requestList = document.getElementsByTagName("request");
        for (int i = 0; i < requestList.getLength(); ++i) {
            mission.addRequest(createRequest((Element) requestList.item(i), mission, map));
        }
    }

    /**
     * Create a new intersection through the information given in the parameter element.
     * @param element the instance of the element where stocks the information for one intersection.
     * @return
     */
    private static Intersection createIntersection(Element element) {
        long id = Long.parseLong(element.getAttribute("id"));
        float latitude = Float.parseFloat(element.getAttribute("latitude"));
        float longitude = Float.parseFloat(element.getAttribute("longitude"));
        return new Intersection(id,latitude, longitude);
    }

    /**
     * Create a new request through the information given in the parameter element.
     * @param element the instance of the element where stocks the information for one request.
     * @param map the instance of map where correspond the request and intersection read from XML file.
     * @return
     */
    private static Segment createSegment(Element element, Map map) {
        long destination = Long.parseLong(element.getAttribute("destination"));
        long origin = Long.parseLong(element.getAttribute("origin"));
        double length = Double.parseDouble(element.getAttribute("length"));
        String name = element.getAttribute("name");
        return new Segment
                (map.getAllIntersections().get(origin), map.getAllIntersections().get(destination), name, length );
    }

    /**
     * Create a new depot intersection through the information given in the parameter element.
     * @param element the instance of the element where stocks the information for one depot intersection.
     * @param mission the instance of the mission where the depot intersection belongs to.
     * @param map the instance of map where correspond the mission read from XML file.
     * @return the instance of the intersection correspond the depot intersection.
     * @throws ExceptionXML
     */
    private static Intersection createDepot(Element element, Mission mission, Map map) throws ExceptionXML {
        long id = Long.parseLong(element.getAttribute("address"));
        Intersection intersection = map.getAllIntersections().get(id);
        if (intersection == null) {
            throw new ExceptionXML("request doesn't match the map");
        }
        return intersection;
    }

    /**
     * Create a new instance of localTime through the information given in the parameter element.
     * @param element the instance of the element where stocks the information for one localTime.
     * @return the new instance of localTime
     */
    private static LocalTime createLocalTime(Element element) {
        String localTime = element.getAttribute("departureTime");
        StringBuffer stringBuffer = new StringBuffer(localTime);
        if (':' == stringBuffer.charAt(1)) {
            stringBuffer.insert(0,"0");
        }
        if (':' == stringBuffer.charAt(4)) {
            stringBuffer.insert(3,"0");
        }
        if (stringBuffer.length() == 7) {
            stringBuffer.insert(6,"0");
        }
        localTime = stringBuffer.toString();
        return LocalTime.parse(localTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * Create a new instance of request through the information given in the parameter element.
     * @param element the instance of the element where stocks the information for one request.
     * @param mission the instance of the mission where the request belongs to.
     * @param map the instance of map where correspond the request read from XML file.
     * @return the instance of the new request created.
     * @throws ExceptionXML
     */
    private static Request createRequest(Element element, Mission mission, Map map) throws ExceptionXML {
        long pickupId = Long.parseLong(element.getAttribute("pickupAddress"));
        long deliveryId;
        if (element.getAttribute("deliveryAddress").equals("")) {
            deliveryId = Long.parseLong(element.getAttribute("adresseLivraison"));
        } else {
            deliveryId = Long.parseLong(element.getAttribute("deliveryAddress"));
        }
        int pickupDuration = Integer.parseInt(element.getAttribute("pickupDuration"));
        int deliveryDuration = Integer.parseInt(element.getAttribute("deliveryDuration"));
        Intersection pickup = map.getAllIntersections().get(pickupId);
        Intersection delivery = map.getAllIntersections().get(deliveryId);
        if (pickup == null || delivery == null) {
            throw new ExceptionXML("request doesn't match the map");
        }
        return new Request(pickup, delivery, pickupDuration, deliveryDuration);
    }

}
