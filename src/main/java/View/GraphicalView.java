package View;

import Algorithm.TSP;
import Model.*;

import Observer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class GraphicalView extends JPanel implements Observer {

    private static final long serialVersionID = 1L;
    private static final int DEFAULT_SIZE = 600;
    private static final int MIN_SIZE = 400;
    private int viewSize = DEFAULT_SIZE;    // always a square
    private Map map;
    private Mission mission;
    private TSP tsp;
    private Graphics g;
    private static boolean paintMap;
    private static boolean paintRequest;
    private static boolean paintTour;

    private double minX;
    private double minY;
    private double rate;
    private static final int SPACE = 10;

    /**
     * Constructor of object GraphicalView using the map, mission and the window
     * @param map the map whose informations are filled that will be painted
     * @param mission the mission whose informations are filled that will be painted
     * @param window the window where the GraphicalView will be on
     */
    public GraphicalView(Map map, Mission mission, Window window, TSP tsp) {
        super();
        //TODO: added to Observer
        setBackground(Color.white);
        window.getContentPane().add(this);
        this.map = map;
        this.mission = mission;
        this.tsp = tsp;
        paintMap = false;
        paintRequest = false;
        paintTour = false;
    }

    public void setMapSize() {
        minX = map.getMinX();
        minY = map.getMinY();
        double maxX = map.getMaxX();
        double maxY = map.getMaxY();
        double rateX = (maxX - minX)/(this.getSize().width - SPACE);
        double rateY = (maxY - minY)/(this.getSize().height - SPACE);
        rate = Math.max(rateX, rateY);
        setPaintMap(true);
    }

    public void setPaintMap(boolean paintMap) {
        if (map.getAllSegments().size() == 0) {
            GraphicalView.paintMap = false;
            return;
        }
        GraphicalView.paintMap = paintMap;
    }

    public void setPaintRequest(boolean paintRequest) {
        if (mission.getAllRequests().size() == 0) {
            GraphicalView.paintRequest = false;
            return;
        }
        GraphicalView.paintRequest = paintRequest;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!paintMap) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        HashMap<Long, Intersection> intersections = map.getAllIntersections();
        for (Intersection intersection : intersections.values()) {
            double x = intersection.getX() - minX;
            double y = intersection.getY() - minY;
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(new Line2D.Double(x/rate,y/rate,x/rate,y/rate));

        }

        for(Segment segment : map.getAllSegments()){
            double x = segment.getOrigin().getX() - minX;
            double y = segment.getOrigin().getY() - minY;
            double x2 = segment.getDestination().getX() - minX;
            double y2 = segment.getDestination().getY() - minY;
            g2d.setColor(Color.lightGray);
            g2d.setStroke(new BasicStroke(1));
            g2d.draw(new Line2D.Double(x/rate,y/rate,x2/rate,y2/rate));
        }

        if(paintRequest) {
            ArrayList<Request> allRequests = mission.getAllRequests();
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(5));
            double x = mission.getDepot().getX() - minX;
            double y = mission.getDepot().getY() - minY;
            g2d.draw(new Line2D.Double(x/rate,y/rate,x/rate,y/rate));

            for (int i = 0; i < allRequests.size(); ++i) {
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(5));
                double pickupX = allRequests.get(i).getPickup().getX() - minX;
                double pickupY = allRequests.get(i).getPickup().getY() - minY;
                g2d.draw(new Line2D.Double(pickupX/rate,pickupY/rate,pickupX/rate,pickupY/rate));

                g2d.setColor(Color.ORANGE);
                g2d.setStroke(new BasicStroke(5));
                double deliveryX = allRequests.get(i).getDelivery().getX() - minX;
                double deliveryY = allRequests.get(i).getDelivery().getY() - minY ;
                g2d.draw(new Line2D.Double(deliveryX/rate,deliveryY/rate,deliveryX/rate,deliveryY/rate));
            }
        }

        if(paintTour){
            HashMap<Long, Intersection> allIntersections = map.getAllIntersections();
            List<Long> solution = tsp.getBestSolIntersection();
            for(int i = 1; i < solution.size(); ++i) {
                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(5));
                Intersection intersection1 = allIntersections.get(solution.get(i));
                Intersection intersection2 = allIntersections.get(solution.get(i-1));
                double x1 = intersection1.getX() - minX;
                double y1 = intersection1.getY() - minY;
                double x2 = intersection2.getX() - minX;
                double y2 = intersection2.getY() - minY;
                g2d.draw(new Line2D.Double(x1/rate,y1/rate,x2/rate,y2/rate));
            }
        }

    }

    public void setPaintTour(boolean paintTour) {
        GraphicalView.paintTour = paintTour;
    }

    @Override
    public void update(Observable observed, Object arg) {
        if (arg != null) {

        }
    }
}
