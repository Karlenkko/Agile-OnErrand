package Data;

import Model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class XMLparser {

    public static void parserMap(Map map) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
        File xml = XMLfileOpener.getInstance().open(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        document.getDocumentElement().normalize();
        NodeList root = document.getElementsByTagName("map");
        if (root.getLength() != 0) {
            map.reset();
            buildMapFromDOMXML(document, map);
        } else
            throw new ExceptionXML("wrong format");

    }

    public static void parserRequest(Mission mission) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
        File xml = XMLfileOpener.getInstance().open(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        document.getDocumentElement().normalize();
        NodeList root = document.getElementsByTagName("planningRequest");
        if (root.getLength() != 0) {
            mission.reset();
            buildRequestFromDOMXML(document, mission);
        } else
            throw new ExceptionXML("wrong format");

    }

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

    private static void buildRequestFromDOMXML(Document document, Mission mission) {
        NodeList depotList = document.getElementsByTagName("depot");
        for (int i = 0; i < depotList.getLength(); ++i) {
            mission.addDepot
                    (createDepot((Element) depotList.item(i), mission), createLocalTime((Element) depotList.item(i)));
        }
        NodeList requestList = document.getElementsByTagName("request");
        for (int i = 0; i < requestList.getLength(); ++i) {
            mission.addRequest(createRequest((Element) requestList.item(i), mission));
        }
    }

    private static Intersection createIntersection(Element element) {
        long id = Long.parseLong(element.getAttribute("id"));
        float latitude = Float.parseFloat(element.getAttribute("latitude"));
        float longitude = Float.parseFloat(element.getAttribute("longitude"));
        Intersection intersection = new Intersection(id,latitude, longitude);
        return intersection;
    }

    private static Segment createSegment(Element element, Map map) {
        long destination = Long.parseLong(element.getAttribute("destination"));
        long origin = Long.parseLong(element.getAttribute("origin"));
        double length = Double.parseDouble(element.getAttribute("length"));
        String name = element.getAttribute("name");
        Segment segment = new Segment
                (map.getAllIntersections().get(origin), map.getAllIntersections().get(destination), name, length );
        return segment;
    }

    private static Intersection createDepot(Element element, Mission mission) {
        long id = Long.parseLong(element.getAttribute("address"));
        Intersection intersection = mission.getMap().getAllIntersections().get(id);
        return intersection;
    }

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
        LocalTime dateTime = LocalTime.parse(localTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        return  dateTime;
    }

    private static Request createRequest(Element element, Mission mission) {
        long pickupId = Long.parseLong(element.getAttribute("pickupAddress"));
        long deliveryId;
        if (element.getAttribute("deliveryAddress") == "") {
            deliveryId = Long.parseLong(element.getAttribute("adresseLivraison"));
        } else {
            deliveryId = Long.parseLong(element.getAttribute("deliveryAddress"));
        }
        int pickupDuration = Integer.parseInt(element.getAttribute("pickupDuration"));
        int deliveryDuration = Integer.parseInt(element.getAttribute("deliveryDuration"));
        return new
                Request(mission.getMap().getAllIntersections().get(pickupId),
                mission.getMap().getAllIntersections().get(deliveryId), pickupDuration, deliveryDuration);
    }

}
