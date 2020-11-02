package View;

import Algorithm.TSP;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphicalView extends JPanel implements Observer {

    private static final long serialVersionID = 1L;
    private static final int DEFAULT_SIZE = 600;
    private static final int MIN_SIZE = 400;
    private static final int ARR_SIZE = 4;
    private static final int INTERSECTION_SIZE = 8;
    private int viewSize = DEFAULT_SIZE;    // always a square
    private Map map;
    private Mission mission;
    private TSP tsp;
    private Graphics g;
    private static boolean paintMap;
    private static boolean paintRequest;
    private static boolean paintTour;
    private static boolean paintAdd;
    private static Intersection intersection;


    private double minX;
    private double minY;
    private double rate;
    private static final int SPACE = 10;

    private static double zoomFactor;
    private boolean zoomer;
    private boolean dragger;
    private AffineTransform at;

    private double mouseX;
    private double mouseY;

    private double transX = 0;
    private double transY = 0;
    /**
     * Constructor of object GraphicalView using the map, mission and the window
     * @param map the map whose informations are filled that will be painted
     * @param mission the mission whose informations are filled that will be painted
     * @param window the window where the GraphicalView will be on
     */
    public GraphicalView(Map map, Mission mission, Window window, TSP tsp) {
        super();
        mission.addObserver(this);
        setBackground(Color.white);
        window.getContentPane().add(this);
        this.map = map;
        this.mission = mission;
        this.tsp = tsp;
        paintMap = false;
        paintRequest = false;
        paintTour = false;
        paintAdd = false;
        zoomFactor = 1;
        zoomer = false;
    }

    public void setMapSize() {
        /*
        minX = map.getMinX();
        minY = map.getMinY();
        double maxX = map.getMaxX();
        double maxY = map.getMaxY();
        double rateX = (maxX - minX)/(this.getSize().width - SPACE);
        double rateY = (maxY - minY)/(this.getSize().height - SPACE);
        rate = Math.max(rateX, rateY);
         */
        //TODO refactoring later
        map.resizeIntersection(this.getSize().width, this.getSize().height);
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

    public void setZoomFactor(double zoomFactor) {
        if(zoomFactor < this.zoomFactor){
            this.zoomFactor=this.zoomFactor/1.1 > 1 ? this.zoomFactor/1.1 : 1;
        }
        else{
            this.zoomFactor=zoomFactor;
        }
        this.zoomer=true;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!paintMap) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        // zoom or rotate
        if (zoomer) {
            g2d.translate(transX, transY);
            at = new AffineTransform();
            at.scale(zoomFactor, zoomFactor);
            double actualXSize = this.getSize().getWidth() - SPACE;
            double actualYSize = this.getSize().getHeight() - SPACE;

            double zoomableX = Math.max(actualXSize * (zoomFactor - 1), 0);
            double zoomableY = Math.max(actualYSize * (zoomFactor - 1), 0);

            transX = mouseX * (zoomFactor - 1) ;
            transY = mouseY * (zoomFactor - 1) ;
            transX = zoomableX > transX ? transX : zoomableX;
            transY = zoomableY > transY ? transY : zoomableY;
//            System.out.println(mouseX);
//            System.out.println(mouseY);
//            System.out.println(transX);
//            System.out.println(transY);
//            System.out.println("");
            zoomer = false;
            g2d.transform(at);
            g2d.translate(-transX, -transY);
        }
//        if (dragger) {
//            g2d.translate(-transX, -transY);
//            dragger = false;
//        }
        HashMap<Long, Intersection> intersections = map.getAllIntersections();
        for (Intersection intersection : intersections.values()) {

            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(new Line2D.Double(intersection.getX(),intersection.getY(),intersection.getX(),intersection.getY()));


        }

        for(Segment segment : map.getAllSegments()){
            double x = segment.getOrigin().getX();
            double y = segment.getOrigin().getY();
            double x2 = segment.getDestination().getX();
            double y2 = segment.getDestination().getY();
            g2d.setColor(Color.lightGray);
            g2d.setStroke(new BasicStroke(1));
            g2d.draw(new Line2D.Double(x,y,x2,y2));

        }

        if(paintRequest) {
            ArrayList<Request> allRequests = mission.getAllRequests();
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(INTERSECTION_SIZE));
            double x = mission.getDepot().getX();
            double y = mission.getDepot().getY();
            g2d.draw(new Line2D.Double(x,y,x,y));

            for (int i = 0; i < allRequests.size(); ++i) {
                g2d.setColor(Color.BLUE);
                double pickupX = allRequests.get(i).getPickup().getX();
                double pickupY = allRequests.get(i).getPickup().getY();
                g2d.draw(new Line2D.Double(pickupX,pickupY,pickupX,pickupY));

                g2d.setColor(Color.ORANGE);
                double deliveryX = allRequests.get(i).getDelivery().getX();
                double deliveryY = allRequests.get(i).getDelivery().getY();
                g2d.draw(new Line2D.Double(deliveryX,deliveryY,deliveryX,deliveryY));
            }
        }

        if(paintTour){
            HashMap<Long, Intersection> allIntersections = map.getAllIntersections();
            List<Long> solution = tsp.getBestSolIntersection();
            for(int i = 1; i < solution.size(); ++i) {
                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(2));
                Intersection intersection1 = allIntersections.get(solution.get(i));
                Intersection intersection2 = allIntersections.get(solution.get(i-1));
                double x1 = intersection1.getX();
                double y1 = intersection1.getY();
                double x2 = intersection2.getX();
                double y2 = intersection2.getY();
                drawArrow(g,x2,y2,x1,y1);
            }
        }

        if (paintAdd) {
            double x = intersection.getX();
            double y = intersection.getY();
            System.out.println(x+" "+y);
            g2d.setColor(Color.MAGENTA);
            g2d.setStroke(new BasicStroke(6));
            g2d.draw(new Line2D.Double(x,y,x,y));
        }



    }

    public void drawArrow(Graphics g1, double x1, double y1, double x2, double y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.setStroke(new BasicStroke(4));
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE},
                new int[] {0, -ARR_SIZE, ARR_SIZE}, 3);
    }

    public void setPaintTour(boolean paintTour) {
        GraphicalView.paintTour = paintTour;
    }

    public void setPaintAdd(boolean paintAdd, Intersection intersection) {
        GraphicalView.paintAdd = paintAdd;
        this.intersection = intersection;
    }



    @Override
    public void update(Observable observed, Object arg) {
        if (arg != null) {

        }
    }

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
        System.out.println(mouseX);
        this.mouseX -= this.getBounds().getX();
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
        System.out.println(mouseY);
        this.mouseY -= this.getBounds().getY();
    }

    public double getTransX() {
        return transX;
    }

    public void setTransX(double transX) {
        this.transX = transX;
    }

    public double getTransY() {
        return transY;
    }

    public void setTransY(double transY) {
        this.transY = transY;
    }

    public void setDragger(boolean dragger) {
        this.dragger = dragger;
    }
}
