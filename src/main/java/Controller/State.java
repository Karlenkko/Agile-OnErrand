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
            XMLparser.parserMap(controller.getCityMap());
            //TODO: window shows the map (change the boolean of paint)
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: show alert
        }
    }

    default void loadRequests(Controller controller, Window window) {
        try {
           XMLparser.parserRequest(controller.getMission());
           //TODO: windows show the requests on the map and update the request list (change boolean of requestpaint, changetable)
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: show alert
        }
    }

    default void calculateTour(Controller controller, Window window) {
    }


}
