package Controller;

import Util.XMLparser;
import View.Window;

import javax.swing.*;

public interface State {

    default void loadMap(Controller controller, Window window) {
        try {
            XMLparser.parserMap(controller.getMap());
            window.getGraphicalView().setFirst(false);
            window.getGraphicalView().setMapSize();
            window.getGraphicalView().setPaintRequest(false);
            window.getGraphicalView().setPaintTour(false);
            window.getGraphicalView().repaint();
            window.getTextualView().updateRequestTable();
            window.allow("mapLoadedState");
            controller.setCurrentState(controller.mapLoadedState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void loadRequests(Controller controller, Window window) {
        try {

           XMLparser.parserRequest(controller.getMission(), controller.getMap());
           controller.getMission().initialTour();
           window.getGraphicalView().setPaintTour(false);
           window.getGraphicalView().setPaintRequest(true);
           window.getGraphicalView().repaint();
           window.getTextualView().initiateRequestTable();
           window.allow("requestLoadedState");
           controller.setCurrentState(controller.requestLoadedState);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    default void calculateTour(Controller controller, Window window) {

    }

    default void addRequest(Controller controller, Window window){

    }

    default void leftClick(Controller controller, Window window, int positionX, int positionY){

    }

    default void rightClick(Controller controller, Window window){

    }

    default void deleteRequest(Controller controller, Window window, ListOfCommands listOfCommands){}

    default void validateNewRequest(Controller controller, Window newWindow, ListOfCommands listOfCommands){}

    default void cancelNewRequest(Controller controller, Window newWindow){}

    default void generateRoadMap(Controller controller){}

    default void undo(ListOfCommands l, Window window){};

    default void redo(ListOfCommands l, Window window){};
}
