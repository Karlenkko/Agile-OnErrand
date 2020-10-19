package Controller;

import Data.ExceptionXML;
import Data.XMLparser;
import View.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface State {

    default void loadMap(Controller controller, Window window) {
        try {
            XMLparser.parserMap(controller.getMap());
            window.getGraphicalView().setMapSize();
            window.getGraphicalView().setPaintRequest(false);
            window.getGraphicalView().repaint();
            controller.setCurrentState(controller.mapLoadedState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void loadRequests(Controller controller, Window window) {
        try {
           XMLparser.parserRequest(controller.getMission(), controller.getMap());
           window.getGraphicalView().setPaintRequest(true);
           window.getGraphicalView().repaint();
           window.getTextualView().updateRequestTable();
           controller.setCurrentState(controller.calculateState);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: show alert
        }
    }

    default void calculateTour(Controller controller, Window window) {
    }


}
