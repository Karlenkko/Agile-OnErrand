package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseListener extends MouseAdapter {
    private Controller controller;
    private GraphicalView graphicalView;
    private Window window;

    public MouseListener(Controller controller, GraphicalView graphicalView, Window window) {
        this.controller = controller;
        this.graphicalView = graphicalView;
        this.window = window;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
//        System.out.println(e.getWheelRotation());
        //Zoom in
        if(e.getWheelRotation()<0){
            window.getGraphicalView().setZoomFactor(1.1*window.getGraphicalView().getZoomFactor());
            window.getGraphicalView().repaint();
        }
        //Zoom out
        if(e.getWheelRotation()>0){
            window.getGraphicalView().setZoomFactor(window.getGraphicalView().getZoomFactor()/1.1);
            window.getGraphicalView().repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

    }
}
