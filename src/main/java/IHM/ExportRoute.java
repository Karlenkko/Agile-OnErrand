package IHM;

import Util.ExceptionXML;
import Util.XMLfileOpener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The IHM which allow to export the route as XML
 *
 * @author yzong
 * @create 16-10-2020 at 13:52
 */
public class ExportRoute {

    JFrame f = new JFrame();

    JTextArea textArea = new JTextArea("First take the road ... \nthen take the second turn on the left...");

    JButton b1 = new JButton("Export as file XML");
    JButton b2 = new JButton("Back");

    JPanel bottom = new JPanel();

    public void init(){
        bottom.add(b1);
        bottom.add(b2);

        f.add(bottom, BorderLayout.SOUTH);

        textArea.setPreferredSize(new Dimension(500,200));
        textArea.setBorder(BorderFactory.createLineBorder(Color.black));
        textArea.setEditable(false);
        textArea.setAlignmentY(JTextArea.TOP_ALIGNMENT);

        f.add(textArea);

        b1.addActionListener(new ExportActionListener());
        b2.addActionListener(new ReturnActionListener());

        f.pack();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    class ReturnActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            f.dispose();
        }
    }

    class ExportActionListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            File file = null;
            try {
                file = XMLfileOpener.getInstance().open(false);
            } catch (ExceptionXML exceptionXML) {
                exceptionXML.printStackTrace();
            }

            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                fw.write(textArea.getText());
                fw.close();
                // System.out.println(textArea.getText());
                // System.out.println(file);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }
}
