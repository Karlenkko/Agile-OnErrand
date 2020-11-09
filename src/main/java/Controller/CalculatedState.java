package Controller;

import Algorithm.Graph;
import Model.Mission;
import Model.Request;
import Util.TourSerializer;
import View.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class CalculatedState implements State{


    public void generateRoadMap(Controller controller)  {
        try {
            TourSerializer tourSerializer = new TourSerializer(controller.getMission(), controller.getMap());
            tourSerializer.generateRoadMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addRequest(Controller controller, Window window){

        //TODO:Click on the button "add request to add two points of pickup and delivery
        window.allow("addRequestState1");
        controller.setCurrentState(controller.addRequestState1);
        System.out.println("addRequest");
    }

    public void deleteRequest(Controller controller, Window window, ListOfCommands listOfCommands){
        controller.setCurrentState(controller.deleteRequestState);
        System.out.println("addRequest");
    }



    @Override
    public void undo(ListOfCommands listOfCommands, Window window){
        listOfCommands.undo();
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
    }

    @Override
    public void redo(ListOfCommands listOfCommands, Window window){
        listOfCommands.redo();
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
    }
}
