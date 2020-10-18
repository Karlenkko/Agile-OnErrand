package View;

import Controller.Controller;
import Model.Map;
import Model.Mission;

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
//    private JLabel tipFrame;
    private GraphicalView graphicalView;
    private TextualView textualView;
    private JPanel buttonArea;

    private ButtonListener buttonListener;
    private MouseListener mouseListener;


    public Window(Map map, Mission mission, Controller controller){
        buttonArea = new JPanel();
        createButtons(controller);
    }

    /**
     * Create buttons on the window and bind them to button listener
     * @param controller the controller that deals with button events invoked from ButtonListener
     */
    private void createButtons(Controller controller) {
        buttonListener = new ButtonListener(controller);
        buttons = new ArrayList<>();
        for (String text : buttonTexts){
            JButton button = new JButton(text);
            buttons.add(button);
            button.setSize(buttonWidth,buttonHeight);
            button.setFocusable(false);
            button.setFocusPainted(false);
            button.addActionListener(buttonListener);

            buttonArea.add(button);
        }
    }

//    private void setWindowSize() {
//        int windowHeight = Math.max(graphicalView.getViewHeight(),allButtonHeight)+messageFrameHeight;
//        int windowWidth = graphicalView.getViewWidth()+buttonWidth+textualViewWidth+10;
//    }
}
