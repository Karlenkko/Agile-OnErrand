package View;

import Controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {
    private Controller controller;

    public ButtonListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Window.LOAD_MAP: controller.loadMap(); break;
            case Window.LOAD_REQUESTS: controller.loadRequests(); break;
            case Window.CALCULATE: controller.calculateTour(); break;
            //TODO: three other button need to be implement later
        }

    }
}
