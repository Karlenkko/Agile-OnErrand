package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class PopupWindow extends JFrame {

    JFrame jf = new JFrame("Enter the duration for the new requests' duration.");
    JButton valid = new JButton("Validate the duration");
    JButton cancel = new JButton("Cancel");
    JLabel pickup = new JLabel("Pickup duration en seconds:");
    JLabel delivery = new JLabel("Delivery duration en seconds:");
    JTextField pickupField = new JTextField(20);
    JTextField deliveryField = new JTextField(20);
    JPanel buttons = new JPanel();
    Controller controller;


    /**
     * Constructor for the window popup for adding a new request.
     * It can read the value that the user entered for the deliveryTime and pickupTime
     * Associate with an instance controller to control the actions of the user.
     * @param controller
     */
    public PopupWindow(Controller controller) {
        this.controller = controller;
        JPanel pick = new JPanel();
        // Make sure that there is only numbers in the field.
        pickupField.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){
                }else{
                    e.consume();
                }
            }
        });
        // Make sure that there is only numbers in the field.
        deliveryField.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){
                }else{
                    e.consume();
                }
            }
        });

        pick.add(pickup);
        pick.add(pickupField);
        jf.add(pick,BorderLayout.NORTH);

        JPanel deli = new JPanel();
        pick.add(delivery);
        pick.add(deliveryField);
        jf.add(deli);

        buttons.add(valid);
        buttons.add(cancel);
        jf.add(buttons, BorderLayout.SOUTH);

        valid.addActionListener(new validButtonListener());
        cancel.addActionListener(new returnActionListener());

        jf.pack();
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Obtain the pickup time added for the new request.
     * @return the pickup time added for the new request.
     */
    public int getPickUpTime(){
        return Integer.parseInt((pickupField.getText()));
    }

    /**
     * Obtain the delivery time added for the new request.
     * @return the delivery time added for the new request.
     */
    public int getDeliveryTime(){
        return Integer.parseInt((deliveryField.getText()));
    }

    /**
     * A class to listen to the button valid.
     */
    class validButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: Transmise the data of the pickup and delivery time to create a new Request.
            int pickupTime = getPickUpTime();
            int deliveryTime = getDeliveryTime();
            controller.getMission().getNewRequest().setPickupDuration(pickupTime);
            controller.getMission().getNewRequest().setDeliveryDuration(deliveryTime);

            System.out.println("The pickup time is "+ getPickUpTime() +" seconds and the delivery time is " + getDeliveryTime() + " seconds");
            jf.dispose();
            controller.validateNewRequest();
        }
    }

    /**
     * A class to listen to the button return.
     */
    class returnActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: Go back to AddRequestState4

            jf.dispose();
            controller.cancelNewRequest();
        }
    }
}