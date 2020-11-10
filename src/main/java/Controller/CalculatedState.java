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

    /**
     * detailed implementation of the generateRoadMap method
     * @param controller the Controller
     */
    @Override
    public void generateRoadMap(Controller controller)  {
        try {
            TourSerializer tourSerializer = new TourSerializer(controller.getMission(), controller.getMap());
            tourSerializer.generateRoadMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * the implementation of the addRequest method that passes into
     * the next step, AddRequestState1 for further operation
     * @param controller the Controller
     * @param window the main Window of the application
     */
    @Override
    public void addRequest(Controller controller, Window window){
        window.allow("addRequestState1");
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You are now adding a new request, \n" +
                "please select an existing address on the map as the start point, \n" +
                "or right click to cancel the addition");
        window.getTextualView().setLockInstruction(true);

        controller.setCurrentState(controller.addRequestState1);
        System.out.println("addRequest");
    }

    /**
     * the implementation of the deleteRequest method that passes into
     * the next step, DeleteRequestState for further operation
     * @param controller the Controller
     * @param window the main Window of the application
     * @param listOfCommands the list of commands
     */
    @Override
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

    /**
     * the detailed implementation of the method undo that invokes the actual executive method in the
     * AddCommand class
     * @param listOfCommands
     * @param window the main window of the application
     */
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

    /**
     * the detailed implementation of the method redo that invokes the actual executive method in the
     * AddCommand class
     * @param listOfCommands the list of commands
     * @param window the main window of the application
     */
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
