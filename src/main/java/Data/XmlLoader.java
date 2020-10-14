package Data;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.HashMap;

public class XmlLoader {

    public static HashMap<Long, Intersection> map = new HashMap<Long, Intersection>();
    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private DocumentBuilder db;

    public XmlLoader() throws ParserConfigurationException {
        db = dbf.newDocumentBuilder();
    }

    public double[] chargeMap() throws Exception {

        double[] parameters = new double[4]; // minX, minY, maxX, maxY

        JFileChooser fileChooser = new JFileChooser(new File("."));
        int val = fileChooser.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("intersection");

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);

                long id = Long.parseLong(node.getAttributes().getNamedItem("id").getNodeValue());
                float latitude = Float.parseFloat(node.getAttributes().getNamedItem("latitude").getNodeValue());
                float longitude = Float.parseFloat(node.getAttributes().getNamedItem("longitude").getNodeValue());

                Intersection intersection = new Intersection(id,latitude, longitude);
                map.put(id, intersection);

                if (i == 0) {
                    parameters[0] = intersection.x;
                    parameters[1] = intersection.y;
                    parameters[2] = intersection.x;
                    parameters[3] = intersection.y;
                } else {
                    parameters[0] = intersection.x < parameters[0] ? intersection.x : parameters[0];
                    parameters[1] = intersection.y < parameters[1] ? intersection.y : parameters[1];
                    parameters[2] = intersection.x > parameters[2] ? intersection.x : parameters[2];
                    parameters[3] = intersection.y > parameters[3] ? intersection.y : parameters[3];
                }

            }

            NodeList segmentList = doc.getElementsByTagName("segment");

            for (int i = 0; i < segmentList.getLength(); ++i) {
                Node node = segmentList.item(i);

                long destination = Long.parseLong(node.getAttributes().getNamedItem("destination").getNodeValue());
                long origin = Long.parseLong(node.getAttributes().getNamedItem("origin").getNodeValue());
                double length = Double.parseDouble(node.getAttributes().getNamedItem("length").getNodeValue());

                int found = 0;
                Intersection intersectionAdd = map.get(destination);

                map.get(origin).addIntersections(intersectionAdd, length);

            }

        } else {
        }


        return parameters;

    }

    /*
    public void chargeRequest(LinkedList<Point> points, LinkedList<Point> sommets) throws Exception {

        JFileChooser fileChooser = new JFileChooser(new File("."));
        int val = fileChooser.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList depotNode = doc.getElementsByTagName("depot");
            Node depot = depotNode.item(0);
            long idDepot = Long.parseLong(depot.getAttributes().getNamedItem("address").getNodeValue());
            String departureTime = depot.getAttributes().getNamedItem("departureTime").getNodeValue();

            System.out.println(idDepot + " , " +departureTime);

            for (Point p : points) {
                if (p.id == idDepot) {
                    p.setType(Point.DEPOT_POINT,0,departureTime);
                    sommets.add(p);
                    break;
                }
            }

            NodeList requestList = doc.getElementsByTagName("request");

            for (int i = 0; i < requestList.getLength(); ++i) {
                Node node = requestList.item(i);

                long pickupAddress = Long.parseLong(node.getAttributes().getNamedItem("pickupAddress").getNodeValue());
                long deliveryAddress = Long.parseLong(node.getAttributes().getNamedItem("deliveryAddress").getNodeValue());
                int pickupDuration = Integer.parseInt(node.getAttributes().getNamedItem("pickupDuration").getNodeValue());
                int deliveryDuration = Integer.parseInt(node.getAttributes().getNamedItem("deliveryDuration").getNodeValue());

                System.out.println(pickupAddress+" , " + deliveryAddress + " , " + pickupDuration + " , " + deliveryDuration);

                int finish = 0;
                for(Point p : points) {

                    if(p.id == pickupAddress) {
                        p.setType(Point.PICKUP_POINT,pickupDuration,"");
                        sommets.add(p);
                        ++finish;
                    }
                    if(p.id == deliveryAddress) {
                        p.setType(Point.DELIVERY_POINT,deliveryDuration,"");
                        sommets.add(p);
                        ++finish;
                    }

                    if(finish == 2) {
                        break;
                    }
                }


            }


        } else {
        }


    }
     */
}
