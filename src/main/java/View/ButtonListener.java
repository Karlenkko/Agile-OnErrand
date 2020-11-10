package View;

import Controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {
    private Controller controller;

    /**
     * Constructor for the ButtonListener.
     * Associate with controller which operates on these buttons
     * @param controller the instance which we can control these buttons.
     */
    public ButtonListener(Controller controller) {
        this.controller = controller;
    }


    /**
     * Identify different behaviors for different buttons.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Window.LOAD_MAP: controller.loadMap(); break;
            case Window.LOAD_REQUESTS: controller.loadRequests(); break;
            case Window.CALCULATE: controller.calculateTour(); break;
            case Window.ADD_REQUEST: controller.addRequest(); break;
            case Window.GENERATE_ROADMAP: controller.generateRoadMap(); break;
            case Window.DELETE_REQUEST: controller.deleteRequest(); break;
            case Window.UNDO: controller.undo(); break;
            case Window.REDO: controller.redo(); break;
            //TODO: three other button need to be implement later

        }

    }
}
