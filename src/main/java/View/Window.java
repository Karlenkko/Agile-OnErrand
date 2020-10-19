package View;

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

    private final String[] buttonTexts = new String[] {
            LOAD_MAP, CALCULATE, GENERATE_ROADMAP, LOAD_REQUESTS, ADD_REQUEST, DELETE_REQUEST
    };

    private final int buttonHeight = 30;
    private final int buttonWidth = 150;
    // window components
    private ArrayList<JButton> buttons;
    private GraphicalView graphicalView;
    private TextualView textualView;
    private JPanel buttonArea;

    private ButtonListener buttonListener;
    private MouseListener mouseListener;


    public Window(Map map, Mission mission, Controller controller){
        setSize(WINDOW_DEFAULT_WIDTH, WINDOW_DEFAULT_HEIGHT);
        createButtons(controller);
        graphicalView = new GraphicalView(map, mission, this);
        textualView = new TextualView(mission, this);
        setWindowSize();
        System.out.println(textualView.getWidth());

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
        for (String text : buttonTexts){
            JButton button = new JButton(text);
            buttons.add(button);
            button.setSize(buttonWidth,buttonHeight);
            button.setFocusable(false);
            button.setFocusPainted(false);
            button.addActionListener(buttonListener);
            buttonArea.add(button);
            getContentPane().add(buttonArea,BorderLayout.SOUTH);
        }
    }

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

    public GraphicalView getGraphicalView() {
        return graphicalView;
    }

    public TextualView getTextualView() {
        return textualView;
    }
}
