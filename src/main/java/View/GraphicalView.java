package View;

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
    private Graphics g;
    private static boolean paintMap;
    private static boolean paintRequest;
    private static boolean paintTour;
    private static boolean paintAdd;
    private static Intersection intersection;


    private double minX;
    private double minY;
    private double rate;
    private double[] rates;
    private double[] delta;
    private static final int SPACE = 10;

    private static double zoomFactor;
    private boolean zoomer;
    private boolean dragger;
    private AffineTransform at;

    private double mouseX;
    private double mouseY;

    private double transX = 0;
    private double transY = 0;

    private double maxX = 0;
    private double maxY = 0;

    private boolean first = false;

    private double locationX = 0;
    private double locationY = 0;

    /**
     * Constructor of object GraphicalView using the map, mission and the window
     * @param map the map whose informations are filled that will be painted
     * @param mission the mission whose informations are filled that will be painted
     * @param window the window where the GraphicalView will be on
     */
    public GraphicalView(Map map, Mission mission, Window window) {
        super();
        mission.addObserver(this);
        setBackground(Color.white);
        window.getContentPane().add(this);
        this.map = map;
        this.mission = mission;
        paintMap = false;
        paintRequest = false;
        paintTour = false;
        paintAdd = false;
        zoomFactor = 1;
        zoomer = false;

        rate = 0;
        rates = new double[2];
        delta = new double[2];
    }

    /**
     * Set the attribute first false when it is not the first time we draw the map on the table.
     * @param first the attribute which marks whether it is the first time we draw the map on the table.
     */
    public void setFirst(boolean first) {
        this.first = first;
    }

    /**
     * Resize the map when modifying the map.
     */
    public void setMapSize() {
        //TODO refactoring later
        if (map.getAllIntersections().size() != 0 && first == false) {
            delta = map.resizeIntersection();
            first = true;
        }

        rates[0] = delta[0]/this.getSize().getWidth();
        rates[1] = delta[1]/this.getSize().getHeight();
        rate = Math.max(rates[0], rates[1]);
        System.out.println(rate);

        setPaintMap(true);
    }

    /**
     * Mark the map as need to be painted(true) or not(false) for the not doing the paint each time we want to change a little things in the mission.
     * @param paintMap mark the map as need to be painted(true) or not(false)
     */
    public void setPaintMap(boolean paintMap) {
        if (map.getAllSegments().size() == 0) {
            GraphicalView.paintMap = false;
            return;
        }
        GraphicalView.paintMap = paintMap;
    }

    /**
     * Mark the request as need to be painted(true) or not(false) for not painting the request even it is not loaded.
     * @param paintRequest mark the request as need to be painted(true) or not(false)
     */
    public void setPaintRequest(boolean paintRequest) {
        if (mission.getAllRequests().size() == 0) {
            GraphicalView.paintRequest = false;
            return;
        }
        GraphicalView.paintRequest = paintRequest;
    }

    /**
     * Set the zoomFactor for zooming in or zooming out the map.
     * @param zoomFactor the factor for zoom in or zoom out.
     */
    public void setZoomFactor(double zoomFactor) {
        if(zoomFactor < this.zoomFactor){
            this.zoomFactor=this.zoomFactor/1.1 > 1 ? this.zoomFactor/1.1 : 1;
        }
        else{
            this.zoomFactor=zoomFactor;
        }
        //this.zoomFactor = Math.minCost(this.zoomFactor, 1.8);
        this.zoomer=true;
    }

    /**
     * Obtain the zoomFactor for zooming.
     * @return the zoomFactor for zooming.
     */
    public double getZoomFactor() {
        return zoomFactor;
    }

    /**
     * Paint the map or the request or the tour if needed.
     * @param g the instance of Graphics which paints.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!paintMap) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTrans = g2d.getTransform();
        // zoom or rotate
        if (zoomer) {

            at = new AffineTransform();
            at.scale(zoomFactor, zoomFactor);

            if (zoomFactor > 1) {
                //transX = (delta[0]/rate)/2 - (mouseX * zoomFactor);
                //transY = (delta[1]/rate)/3 - (mouseY * zoomFactor);
                transX = -mouseX * (zoomFactor - 1);
                transY = -mouseY * (zoomFactor - 1);
                //transX = - (delta[0]/rate) * (zoomFactor - 1) / 2;
                //transY = - (delta[1]/rate) * (zoomFactor - 1) / 2;

            }else {
                transX = 0;
                transY = 0;
            }

            //transX = Math.minCost(zoomableX, transX);
            //transY = Math.minCost(zoomableY, transY);
            zoomer = false;
            g2d.transform(at);
            g2d.translate(transX/zoomFactor, transY/zoomFactor);
        }
        if (dragger) {
            at = new AffineTransform();
            at.scale(zoomFactor, zoomFactor);
            g2d.transform(at);
            g2d.translate(transX/zoomFactor, transY/zoomFactor);
            dragger = false;
        }
        HashMap<Long, Intersection> intersections = map.getAllIntersections();
        for (Intersection intersection : intersections.values()) {

            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(new Line2D.Double(intersection.getX()/rate,intersection.getY()/rate,
                    intersection.getX()/rate,intersection.getY()/rate));


        }

        for(Segment segment : map.getAllSegments()){
            double x = segment.getOrigin().getX();
            double y = segment.getOrigin().getY();
            double x2 = segment.getDestination().getX();
            double y2 = segment.getDestination().getY();
            g2d.setColor(Color.lightGray);
            g2d.setStroke(new BasicStroke(1));
            g2d.draw(new Line2D.Double(x/rate,y/rate,x2/rate,y2/rate));

        }

        if(paintRequest) {
            ArrayList<Request> allRequests = mission.getAllRequests();
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(INTERSECTION_SIZE));
            double x = mission.getDepot().getX();
            double y = mission.getDepot().getY();
            g2d.draw(new Line2D.Double(x/rate,y/rate,x/rate,y/rate));

            for (int i = 0; i < allRequests.size(); ++i) {
                g2d.setColor(Color.BLUE);
                double pickupX = allRequests.get(i).getPickup().getX();
                double pickupY = allRequests.get(i).getPickup().getY();
                g2d.draw(new Line2D.Double(pickupX/rate,pickupY/rate,pickupX/rate,pickupY/rate));

                g2d.setColor(Color.ORANGE);
                double deliveryX = allRequests.get(i).getDelivery().getX();
                double deliveryY = allRequests.get(i).getDelivery().getY();
                g2d.draw(new Line2D.Double(deliveryX/rate,deliveryY/rate,deliveryX/rate,deliveryY/rate));
            }
        }

        if(paintTour){
            HashMap<Long, Intersection> allIntersections = map.getAllIntersections();
            List<Long> solution = mission.getTourIntersections();
            for(int i = 1; i < solution.size(); ++i) {
                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(2));
                Intersection intersection1 = allIntersections.get(solution.get(i));
                Intersection intersection2 = allIntersections.get(solution.get(i-1));
                double x1 = intersection1.getX();
                double y1 = intersection1.getY();
                double x2 = intersection2.getX();
                double y2 = intersection2.getY();
                drawArrow(g,x2/rate,y2/rate,x1/rate,y1/rate);
            }

        }

        if (paintAdd) {
            if(mission.getRequestsToDelete().size() != 0) {
                for(Request request : mission.getRequestsToDelete()) {
                    Intersection pickup = request.getPickup();
                    double x = pickup.getX();
                    double y = pickup.getY();
                    g2d.setColor(Color.cyan);
                    g2d.setStroke(new BasicStroke(12));
                    g2d.draw(new Line2D.Double(x/rate,y/rate,x/rate,y/rate));

                    Intersection delivery = request.getDelivery();
                    double x2 = delivery.getX();
                    double y2 = delivery.getY();
                    g2d.setColor(Color.cyan);
                    g2d.setStroke(new BasicStroke(12));
                    g2d.draw(new Line2D.Double(x2/rate,y2/rate,x2/rate,y2/rate));
                }
            }
            if(mission.getNewAddList().size() != 0) {
                for(Long id : mission.getNewAddList()) {
                    Intersection intersection = map.getAllIntersections().get(id);
                    double x = intersection.getX();
                    double y = intersection.getY();
                    g2d.setColor(Color.MAGENTA);
                    g2d.setStroke(new BasicStroke(6));
                    g2d.draw(new Line2D.Double(x/rate,y/rate,x/rate,y/rate));
                }
            }
        }

        g2d.translate(-transX, -transY);
        g2d.setTransform(oldTrans);

    }

    /**
     * Draw the arrows of the route calculated.
     * @param g1 the instance of Graphics which paints
     * @param x1 the start horizontal position in the drawn area.
     * @param y1 the start vertical position in the drawn area.
     * @param x2 the end horizontal position in the drawn area.
     * @param y2 the end vertical position in the drawn area.
     */
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

    /**
     * Mark the tour as need to be painted(true) or not(false) for not painting the tour even it is not calculated.
     * @param paintTour mark the tour as need to be painted(true) or not(false)
     */
    public void setPaintTour(boolean paintTour) {
        GraphicalView.paintTour = paintTour;
    }

    /**
     * Mark the newRequest as need to be painted(true) or not(false) for not painting the newRequest even it is not added.
     * @param paintAdd mark the newRequest as need to be painted(true) or not(false)
     */
    public void setPaintAdd(boolean paintAdd) {
        GraphicalView.paintAdd = paintAdd;
    }


    /**
     * When the information in the model changed. The graphicalView changed also.
     * @param observed an instance of Observable which is used to observe the information change in the model.
     * @param arg the object which has the changed information
     */
    @Override
    public void update(Observable observed, Object arg) {
        if (arg != null) {

        }
    }

    /**
     * Obtain the horizontal position of the mouse.
     * @return the value of the attribute mouseX.
     */
    public double getMouseX() {
        return mouseX;
    }

    /**
     * Set the horizontal position of the mouse.
     * @param mouseX the new value for the attribute mouseX
     */
    public void setMouseX(double mouseX) {
        locationX = mouseX;
        this.mouseX = (mouseX - transX) / zoomFactor;
        this.mouseX = Math.max(this.mouseX, 0);
        this.mouseX = Math.min(this.mouseX, delta[0]/rate);

    }

    /**
     * Obtain the vertical position of the mouse.
     * @return the value of the attribute mouseY.
     */
    public double getMouseY() {
        return mouseY;
    }

    /**
     * Set the vertical position of the mouse.
     * @param mouseY the new value for the attribute mouseX
     */
    public void setMouseY(double mouseY) {
        locationY = mouseY;
        this.mouseY = (mouseY - transY) / zoomFactor;
        this.mouseY = Math.max(this.mouseY, 0);
        this.mouseY = Math.min(this.mouseY, delta[1]/rate);


    }

    /**
     * Obtain the translation on the direction X horizontal.
     * @return the translation on the direction X horizontal
     */
    public double getTransX() {
        return transX;
    }

    /**
     * Add a translation on the direction X horizontal.
     * @param transX the translation we want to added.
     */
    public void addTransX(double transX) {
        this.transX += (transX/10);
    }

    /**
     * Obtain the translation on the direction Y vertical.
     * @return the translation on the direction Y vertical.
     */
    public double getTransY() {
        return transY;
    }

    /**
     * Add a translation on the direction Y vertical.
     * @param transY the translation we want to added.
     */
    public void addTransY(double transY) {
        this.transY += (transY/10);
    }

    /**
     * Mark the map can be dragged(true) or not(false)
     * @param dragger a boolean which marks the map can be dragged(true) or not(false)
     */
    public void setDragger(boolean dragger) {
        this.dragger = dragger;
    }

    /**
     * Obtain the rate for the map which we want to zoom in or out.
     * @return the rate we want to zoom in or out
     */
    public double getRate() {
        return rate;
    }
}
