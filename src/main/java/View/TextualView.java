package View;

import Model.Mission;
import Model.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;

public class TextualView extends JPanel {

    private Mission mission;
    private JTextArea textArea;
    private JTable requestTable;
    private JScrollPane jScrollPane;
    private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);

    public TextualView(Mission mission, Window window) {
        setLayout(boxLayout);
        this.mission = mission;
        setBackground(Color.white);
        initialise();
        window.getContentPane().add(this);
    }

    public void initialise() {
        textArea = new JTextArea(4,30);
        textArea.setText("Helpful Informations");
        String[] columNames = {"ID","Type","Duration","Arrival","Depart"};
        Object defaultData[][] = {
                {"Nothing","Nothing","Nothing","Nothing","Nothing"}
        };
        requestTable = new JTable(new DefaultTableModel(defaultData, columNames));
        jScrollPane = new JScrollPane(requestTable);
        this.add(Box.createVerticalStrut(20));
        this.add(textArea);
        this.add(Box.createVerticalStrut(20));
        this.add(jScrollPane);
    }

    public void setTableSize() {
        int number = requestTable.getColumnModel().getColumnCount();
        int size = this.getWidth()/number;
        for (int i = 0; i < number; ++i) {
            TableColumn column = requestTable.getColumnModel().getColumn(i);
            column.setMinWidth(size);
            column.setMaxWidth(size);
            column.setPreferredWidth(size);
        }
    }


    public void updateRequestTable() {
        DefaultTableModel tableModel = (DefaultTableModel) requestTable.getModel();
        tableModel.getDataVector().removeAllElements();
        String[] row = {"1", "depot", "--", "--", mission.getDepartureTime().toString()};
        tableModel.addRow(row);
        int i = 2;
        for(Request request : mission.getAllRequests()) {
            String[] row1 = {Integer.toString(i++), "pickup", Integer.toString(request.getPickupDuration()), "--", "--"};
            String[] row2 = {Integer.toString(i++), "delivery", Integer.toString(request.getDeliveryDuration()), "--", "--"};
            tableModel.addRow(row1);
            tableModel.addRow(row2);
            tableModel.fireTableDataChanged();
        }
    }

    public JTable getRequestTable() {
        return requestTable;
    }

    public JScrollPane getjScrollPane() {
        return jScrollPane;
    }
}
