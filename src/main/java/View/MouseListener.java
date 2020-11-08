package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseListener extends MouseAdapter {
    private Controller controller;
    private GraphicalView graphicalView;
    private Window window;

    private double dragStartX;
    private double dragStartY;
    private Point mousePt;

    public MouseListener(Controller controller, GraphicalView graphicalView, Window window) {
        this.controller = controller;
        this.graphicalView = graphicalView;
        this.window = window;
    }

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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
//        System.out.println(e.getWheelRotation());
        //Zoom in
        if(e.getWheelRotation()<0){
            window.getGraphicalView().setMouseX(e.getX()-window.getInsets().left);
            window.getGraphicalView().setMouseY(e.getY()-window.getInsets().top);
            window.getGraphicalView().setZoomFactor(1.1*window.getGraphicalView().getZoomFactor());
            window.getGraphicalView().repaint();
        }
        //Zoom out
        if(e.getWheelRotation()>0){
            window.getGraphicalView().setMouseX(e.getX()-window.getInsets().left);
            window.getGraphicalView().setMouseY(e.getY()-window.getInsets().top);
            window.getGraphicalView().setZoomFactor(window.getGraphicalView().getZoomFactor()/1.1);
            window.getGraphicalView().repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePt = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        window.getGraphicalView().setDragger(true);
        window.getGraphicalView().addTransX(e.getX() - mousePt.getX());
        window.getGraphicalView().addTransY(e.getY() - mousePt.getY());
        window.getGraphicalView().repaint();
    }

//    @Override
//    public void mouseEntered(MouseEvent e) {
//        super.mouseEntered(e);
//        System.out.println(e.getButton());
//    }
}
