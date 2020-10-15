package Data;

import Model.Intersection;
import Model.Request;
import Model.Segment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class XmlLoader {

    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private DocumentBuilder db;

    public XmlLoader() throws ParserConfigurationException {
        db = dbf.newDocumentBuilder();
    }

    public double[] chargeMap(HashMap<Long, Intersection> allIntersections, LinkedList<Segment> allSegments) throws Exception {

        double[] parameters = new double[4]; // minX, minY, maxX, maxY

        JFileChooser fileChooser = new JFileChooser(new File("."));
        int val = fileChooser.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("intersection");

            if(nodeList.getLength() != 0) {
                allIntersections.clear();
                allSegments.clear();
            }

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);

                long id = Long.parseLong(node.getAttributes().getNamedItem("id").getNodeValue());
                float latitude = Float.parseFloat(node.getAttributes().getNamedItem("latitude").getNodeValue());
                float longitude = Float.parseFloat(node.getAttributes().getNamedItem("longitude").getNodeValue());

                Intersection intersection = new Intersection(id,latitude, longitude);
                allIntersections.put(id, intersection);

                if (i == 0) {
                    parameters[0] = intersection.getX();
                    parameters[1] = intersection.getY();
                    parameters[2] = intersection.getX();
                    parameters[3] = intersection.getY();
                } else {
                    parameters[0] = intersection.getX() < parameters[0] ? intersection.getX() : parameters[0];
                    parameters[1] = intersection.getY() < parameters[1] ? intersection.getY() : parameters[1];
                    parameters[2] = intersection.getX() > parameters[2] ? intersection.getX() : parameters[2];
                    parameters[3] = intersection.getY() > parameters[3] ? intersection.getY() : parameters[3];
                }

            }

            NodeList segmentList = doc.getElementsByTagName("segment");

            for (int i = 0; i < segmentList.getLength(); ++i) {
                Node node = segmentList.item(i);

                long destination = Long.parseLong(node.getAttributes().getNamedItem("destination").getNodeValue());
                long origin = Long.parseLong(node.getAttributes().getNamedItem("origin").getNodeValue());
                double length = Double.parseDouble(node.getAttributes().getNamedItem("length").getNodeValue());
                String name = node.getAttributes().getNamedItem("name").getNodeValue();

                Intersection intersectionDestination = allIntersections.get(destination);
                Intersection intersectionOrigin = allIntersections.get(origin);

                allSegments.add(new Segment(intersectionOrigin, intersectionDestination,name, length));

            }

        } else {
        }


        return parameters;

    }


    public void chargeRequest(HashMap<Long, Intersection> allIntersections, LinkedList<Request> allRequests, String startTime) throws Exception {

        JFileChooser fileChooser = new JFileChooser(new File("."));
        int val = fileChooser.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList depotNode = doc.getElementsByTagName("depot");
            Node depot = depotNode.item(0);

            allRequests.clear();

            long idDepot = Long.parseLong(depot.getAttributes().getNamedItem("address").getNodeValue());
            startTime = depot.getAttributes().getNamedItem("departureTime").getNodeValue();

            allRequests.add(new Request(null, allIntersections.get(idDepot), 0, 0));

            NodeList requestList = doc.getElementsByTagName("request");

            for (int i = 0; i < requestList.getLength(); ++i) {
                Node node = requestList.item(i);

                long pickupAddress = Long.parseLong(node.getAttributes().getNamedItem("pickupAddress").getNodeValue());
                long deliveryAddress = Long.parseLong(node.getAttributes().getNamedItem("deliveryAddress").getNodeValue());
                int pickupDuration = Integer.parseInt(node.getAttributes().getNamedItem("pickupDuration").getNodeValue());
                int deliveryDuration = Integer.parseInt(node.getAttributes().getNamedItem("deliveryDuration").getNodeValue());

                allRequests.add(new Request(allIntersections.get(pickupAddress), allIntersections.get(deliveryAddress), pickupDuration, deliveryDuration));

            }


        } else {
        }


    }

}
