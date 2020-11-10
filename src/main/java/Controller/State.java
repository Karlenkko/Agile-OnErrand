package Controller;

import Util.XMLparser;
import View.Window;

import javax.swing.*;

public interface State {

    /**
     * since the load map operation is the most global operation
     * in all states, here we define the detailed method in the interface to reduce code replication
     * @param controller the Controller
     * @param window the main Window of the application
     */
    default void loadMap(Controller controller, Window window) {
        try {
            XMLparser.parserMap(controller.getMap());

            // update view and controller
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

    /**
     * since only the load map and the load request operation are the two most global operation
     * in all states, whereas the load request must be in a condition where the map is loaded,
     * here we define the load request method in the interface to reduce code replication
     * @param controller the Controller
     * @param window the main Window of the application
     */
    default void loadRequests(Controller controller, Window window) {
        try {
           XMLparser.parserRequest(controller.getMission(), controller.getMap());
           controller.getMission().initialTour();

           // update view and controller
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

    /**
     * the detailed calculatedTour method should be implemented in the RequestLoadedState
     * according to the stateChart diagram
     * @param controller the Controller
     * @param window the main Window of the application
     */
    default void calculateTour(Controller controller, Window window) {

    }

    /**
     * the detailed addRequest method should be implemented in the CalculatedState
     * according to the stateChart diagram
     * @param controller the Controller
     * @param window the main Window of the application
     */
    default void addRequest(Controller controller, Window window){

    }

    /**
     * the detailed leftClick method should be differently implemented in
     * different states
     * @param controller the Controller
     * @param window the main window of the application
     * @param positionX the x position of the mouse on the window
     * @param positionY the y position of the mouse on the window
     */
    default void leftClick(Controller controller, Window window, int positionX, int positionY){

    }

    /**
     * the detailed rightClick method should be differently implemented in
     * different states
     * @param controller the Controller
     * @param window the main window of the application
     */
    default void rightClick(Controller controller, Window window){

    }

    /**
     * the detailed deleteRequest method should be implemented in the CalculatedState
     * according to the stateChart diagram
     * @param controller the Controller
     * @param window the main Window of the application
     */
    default void deleteRequest(Controller controller, Window window, ListOfCommands listOfCommands){}

    /**
     * the detailed validateNewRequest method should be implemented in the AddRequestState5
     * according to the stateChart diagram
     * @param controller the Controller
     * @param newWindow the popup window for entering address durations
     * @param listOfCommands the list of commands
     */
    default void validateNewRequest(Controller controller, Window newWindow, ListOfCommands listOfCommands){}

    /**
     * the detailed cancelNewRequest method should be implemented in the AddRequestState5
     * according to the stateChart diagram
     * @param controller the Controller
     * @param newWindow the popup window for entering address durations
     */
    default void cancelNewRequest(Controller controller, Window newWindow){}

    /**
     * the detailed generateRoadMap method should be implemented in the CalculatedState
     * according to the stateChart diagram
     * @param controller the Controller
     */
    default void generateRoadMap(Controller controller){}

    /**
     * the detailed undo method should be implemented in the CalculatedState
     * according to the stateChart diagram
     * @param l the list of commands
     * @param window the main window of the application
     */
    default void undo(ListOfCommands l, Window window){};

    /**
     * the detailed redo method should be implemented in the CalculatedState
     * according to the stateChart diagram
     * @param l the list of commands
     * @param window the main window of the application
     */
    default void redo(ListOfCommands l, Window window){};
}
