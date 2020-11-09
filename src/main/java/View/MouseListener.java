package View;

import Controller.Controller;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseListener extends MouseAdapter {
    private Controller controller;
    private GraphicalView graphicalView;
    private Window window;

    private Point mousePt;

    /**
     * Constructor for mouseListener
     * Associate with controller which operates on these buttons
     * Associate with graphicalView which the user clicks on.
     * Associate with window which contains the graphicalView.
     * @param controller the instance which we can control the users' mouse actions.
     * @param graphicalView the instance which the user clicks on.
     * @param window the instance which which contains the graphicalView.
     */
    public MouseListener(Controller controller, GraphicalView graphicalView, Window window) {
        this.controller = controller;
        this.graphicalView = graphicalView;
        this.window = window;
    }

    /**
     * The event which the user click on one place on map.
     * @param e the instance mouseevent.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                controller.leftClick(e.getX() - window.getInsets().left, e.getY() - window.getInsets().top);
                break;
            case MouseEvent.BUTTON3:
                controller.rightClick();
                break;
        }


    }

    /**
     * The event which the user move the wheel of the mouse on one place on map.
     * @param e the instance mouseevent.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        if(e.getWheelRotation()<0){
            window.getGraphicalView().setMouseX(e.getX()-window.getInsets().left);
            window.getGraphicalView().setMouseY(e.getY()-window.getInsets().top);
            window.getGraphicalView().setZoomFactor(1.1*window.getGraphicalView().getZoomFactor());
            window.getGraphicalView().repaint();
        }

        if(e.getWheelRotation()>0){
            window.getGraphicalView().setMouseX(e.getX()-window.getInsets().left);
            window.getGraphicalView().setMouseY(e.getY()-window.getInsets().top);
            window.getGraphicalView().setZoomFactor(window.getGraphicalView().getZoomFactor()/1.1);
            window.getGraphicalView().repaint();
        }
    }

    /**
     * The event which the user press on the mouse on one place on map.
     * @param e the instance mouseevent.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        mousePt = e.getPoint();
    }

    /**
     * The event which the user drag the mouse on one place on map.
     * @param e the instance mouseevent.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        window.getGraphicalView().setDragger(true);
        window.getGraphicalView().addTransX(e.getX() - mousePt.getX());
        window.getGraphicalView().addTransY(e.getY() - mousePt.getY());
        window.getGraphicalView().repaint();
    }

}
