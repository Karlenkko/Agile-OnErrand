package View;

import Algorithm.TSP;
import Controller.Controller;
import Model.Map;
import Model.Mission;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class Window extends JFrame{
    private static final long serialVersionUID = 1L;

    // window default size
    private static final int WINDOW_DEFAULT_WIDTH = 1280;
    private static final int WINDOW_DEFAULT_HEIGHT = 720;

    // window button names
    protected static final String LOAD_MAP = "Load a new map";
    protected static final String LOAD_REQUESTS = "Load requests";
    protected static final String CALCULATE = "Calculate tour";
    protected static final String GENERATE_ROADMAP = "Generate roadmap";
    protected static final String ADD_REQUEST = "Add request";
    protected static final String DELETE_REQUEST = "Delete request";
    protected static final String UNDO = "Undo";
    protected static final String REDO = "Redo";

    private final String[] buttonTexts = new String[] {
            LOAD_MAP, LOAD_REQUESTS, CALCULATE, ADD_REQUEST, DELETE_REQUEST, GENERATE_ROADMAP, UNDO, REDO
    };
    // window button indication information
    protected static final String LOAD_MAP_INFO = "load a .xml map and display it";
    protected static final String LOAD_REQUESTS_INFO = "load a .xml request list and display it";
    protected static final String CALCULATE_INFO = "calculate the tour that completes all requests";
    protected static final String GENERATE_ROADMAP_INFO = "generate a human-understandable navigation roadmap " +
            "using the tour";
    protected static final String ADD_REQUEST_INFO = "add a request: \n1.select an existing starting address" +
            "\n2.select a pickup address of the new request" +
            "\n3.select a delivery address of the new request" +
            "\n4.select an existing ending address" +
            "\n5.fill the information";
    protected static final String DELETE_REQUEST_INFO = "delete a selected request";
    protected static final String UNDO_INFO = "undo an operation";
    protected static final String REDO_INFO = "redo an operation";

    private final String[] buttonInfos = new String[] {
            LOAD_MAP_INFO, LOAD_REQUESTS_INFO, CALCULATE_INFO,
            ADD_REQUEST_INFO, DELETE_REQUEST_INFO, GENERATE_ROADMAP_INFO,
            UNDO_INFO, REDO_INFO
    };

    private static String lastInfo;

    private final int buttonHeight = 30;
    private final int buttonWidth = 150;
    // window components
    private ArrayList<JButton> buttons;
    private GraphicalView graphicalView;
    private TextualView textualView;
    private JPanel buttonArea;

    private ButtonListener buttonListener;
    private MouseListener mouseListener;


    /**
     * Constructor for window, create the main window and add the components.
     * Associate also the map, the mission which are going to show on the application.
     * Associate an instance tsp which is used to calculate the tour.
     * Associate an instance controller which is used to controlle the system later.
     * @param map the map which is going to show on the application.
     * @param mission the mission which is going to show on the application.
     * @param tsp an instance tsp which is used to calculate the tour.
     * @param controller an instance controller which is used to controlle the system later.
     */
    public Window(Map map, Mission mission, TSP tsp, Controller controller){
        setSize(WINDOW_DEFAULT_WIDTH, WINDOW_DEFAULT_HEIGHT);
        createButtons(controller);
        graphicalView = new GraphicalView(map, mission, this);
        textualView = new TextualView(mission, this);
        setWindowSize();
        System.out.println(textualView.getWidth());

        // mouse listener
        mouseListener = new MouseListener(controller, graphicalView, this);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        addMouseWheelListener(mouseListener);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setWindowSize();
            }
        });
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Create buttons on the window and bind them to button listener
     * @param controller the controller that deals with button events invoked from ButtonListener
     */
    private void createButtons(Controller controller) {
        buttonArea = new JPanel();
        buttonListener = new ButtonListener(controller);
        buttons = new ArrayList<>();
        for (int i = 0; i < buttonTexts.length; i ++){
            JButton button = new JButton(buttonTexts[i]);
            buttons.add(button);
            button.setSize(buttonWidth,buttonHeight);
            button.setFocusable(false);
            button.setFocusPainted(false);
            button.addActionListener(buttonListener);
            int finalI = i;
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e){
                    lastInfo = textualView.getTextAreaText();
                    textualView.setTextAreaText(buttonInfos[finalI]);
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    textualView.setTextAreaText(lastInfo);
                }
            });
            buttonArea.add(button);
            getContentPane().add(buttonArea,BorderLayout.SOUTH);
        }
    }

    /**
     * Set the window to a preferred size which is coherent with the graphicalView and the textualView.
     */
    private void setWindowSize() {
        int graphicalViewSize = this.getHeight() - buttonArea.getHeight()-20;
        graphicalView.setSize(graphicalViewSize, graphicalViewSize);
        graphicalView.setLocation(0, 0);
        int textualViewWidth = this.getWidth() - graphicalViewSize;
        textualView.setSize(textualViewWidth,graphicalViewSize);
        textualView.setLocation(graphicalViewSize,0);
        textualView.setTableSize();
        graphicalView.setMapSize();
    }

    /**
     * Obtain the graphcalView which is used to draw or modify on the application
     * @return graphicalView is used to draw or modifyon the application
     */
    public GraphicalView getGraphicalView() {
        return graphicalView;
    }

    /**
     * Obtain the textualView which is used to add or modify or delete the table or the texual description
     * @return textualView which is used to add or modify or delete the table or the texual description
     */
    public TextualView getTextualView() {
        return textualView;
    }

    /**
     * Obtain the rate which we used to zoom in or zoom out the map
     * @return the rate which we used to zoom in or zoom out the map
     */
    public double getRate() {
        return this.getGraphicalView().getRate();
    }

    /**
     * Make sure that the buttons are enable or disable on different situation(state) given by state.
     * @param state the String which contains the situation(state) of the application
     */
    public void allow(String state) {
        switch (state){
            case "initialState":
                for (JButton button : buttons) {
                    button.setEnabled(false);
                }
                buttons.get(0).setEnabled(true);
                break;
            case "mapLoadedState":
                for (JButton button : buttons) {
                    button.setEnabled(false);
                }
                buttons.get(0).setEnabled(true);
                buttons.get(1).setEnabled(true);
                break;
            case "requestLoadedState":
                for (JButton button : buttons) {
                    button.setEnabled(false);
                }
                buttons.get(0).setEnabled(true);
                buttons.get(1).setEnabled(true);
                buttons.get(2).setEnabled(true);
                break;
            case "calculatedState":
                for (JButton button : buttons) {
                    button.setEnabled(false);
                }
                buttons.get(0).setEnabled(true);
                buttons.get(1).setEnabled(true);
                buttons.get(3).setEnabled(true);
                buttons.get(4).setEnabled(true);
                buttons.get(5).setEnabled(true);
                buttons.get(6).setEnabled(true);
                buttons.get(7).setEnabled(true);
                break;
            case "addRequestState1":
            case "addRequestState2":
            case "addRequestState3":
            case "addRequestState4":
            case "addRequestState5":
                for (JButton button : buttons) {
                    button.setEnabled(false);
                }
                break;
            case "deleteRequestState":
                for (JButton button : buttons) {
                    button.setEnabled(false);
                }
                buttons.get(4).setEnabled(true);
                break;
            default:
                for (JButton button : buttons) {
                    button.setEnabled(true);
                }
        }

    }
}
