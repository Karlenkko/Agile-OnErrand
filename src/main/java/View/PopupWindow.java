package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class PopupWindow extends JFrame {

    JFrame jf = new JFrame("Enter the duration for the new requests‘ duration.");
    JButton valid = new JButton("Validate the duration");
    JButton cancel = new JButton("Cancel");
    JLabel pickup = new JLabel("Pickup duration en seconds:");
    JLabel delivery = new JLabel("Delivery duration en seconds:");
    JTextField pickupField = new JTextField(20);
    JTextField deliveryField = new JTextField(20);
    JPanel buttons = new JPanel();
    public PopupWindow(){
        JPanel pick = new JPanel();
        pickupField.addKeyListener(new KeyAdapter(){
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

        jf.pack();
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
