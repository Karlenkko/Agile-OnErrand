package View;

import Controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class Window {
    private static final long serialVersionUID = 1L;

    // window button names
    protected static final String LOAD_MAP = "Load a new map";
    protected static final String LOAD_REQUESTS = "Load requests";
    protected static final String CALCULATE = "Calculate tour";
    protected static final String GENERATE_ROADMAP = "Generate roadmap";
    protected static final String ADD_REQUEST = "Add request";
    protected static final String DELETE_REQUEST = "Delete request";

    private final String[] buttonTexts = new String[] {
            LOAD_MAP, LOAD_REQUESTS, CALCULATE, GENERATE_ROADMAP, ADD_REQUEST, DELETE_REQUEST
    };

    private final int buttonHeight = 30;
    private final int buttonWidth = 150;
    // window components
    private ArrayList<JButton> buttons;
    private JLabel tipFrame;
    private GraphicalView graphicalView;
    private TextualView textualView;
    private ButtonListener buttonListener;
    private MouseListener mouseListener;


    private void createButtons(Controller controller) {
        buttonListener = new ButtonListener(controller);
    }
}
