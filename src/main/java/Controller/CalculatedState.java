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
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You are now adding a new request, \n" +
                "please select an existing address on the map as the start point, \n" +
                "or right click to cancel the addition");
        window.getTextualView().setLockInstruction(true);

        controller.setCurrentState(controller.addRequestState1);
        System.out.println("addRequest");
    }

    public void deleteRequest(Controller controller, Window window, ListOfCommands listOfCommands){
        window.allow("deleteRequestState");
        controller.setCurrentState(controller.deleteRequestState);
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You are now in the delete mode, \n" +
                "please select an existing address on the map or choose one in the list, " +
                "then click the delete request button, \n" +
                "or right click to cancel the deletion");
        window.getTextualView().setLockInstruction(true);
        System.out.println("addRequest");
    }



    @Override
    public void undo(ListOfCommands listOfCommands, Window window){
        listOfCommands.undo();
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You have undone an operation");
        window.getTextualView().setLockInstruction(true);
    }

    @Override
    public void redo(ListOfCommands listOfCommands, Window window){
        listOfCommands.redo();
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You have redone an operation");
        window.getTextualView().setLockInstruction(true);
    }
}
