package View;

import Model.Map;
import Model.Mission;

import Observer.*;

import javax.swing.*;
import java.awt.*;

public class GraphicalView extends JPanel implements Observer {

    private static final long serialVersionID = 1L;
    private static final int DEFAULT_SIZE = 600;
    private static final int MIN_SIZE = 400;
    private int viewSize = DEFAULT_SIZE;    // always a square
    private Map map;
    private Mission mission;
    private Graphics g;

    /**
     * Constructor of object GraphicalView using the map, mission and the window
     * @param map the map whose informations are filled that will be painted
     * @param mission the mission whose informations are filled that will be painted
     * @param window the window where the GraphicalView will be on
     */
    public GraphicalView(Map map, Mission mission, Window window) {
        super();


        this.map = map;
        this.mission = mission;
    }

    public int getViewSize() {
        return viewSize;
    }


    @Override
    public void update(Observable observed, Object arg) {
        if (arg != null) {

        }
    }
}
