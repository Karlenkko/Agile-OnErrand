package View;

import Model.Mission;
import Model.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        if(mission.getDepartureTime() == null) {
            return;
        }
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
        ArrayList<Color> colors = getColors(mission.getAllRequests().size()+1);
        setColor(requestTable, colors);
    }

    public ArrayList<Color> getColors(int number) {
        ArrayList<Color> colors = new ArrayList<>();
        int dx = 255 / number;
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < number; ++i) {
            if (i%3 == 0) {
                r += 2*dx;
                colors.add(new Color(r, g, b));
            }
            if (i%3 == 1) {
                g += 2*dx;
                colors.add(new Color(r, g, b));
            }
            if (i%3 == 2) {
                b += 2*dx;
                colors.add(new Color(r, g, b));
            }
        }
        return colors;
    }

    public static void setColor(JTable table,ArrayList<Color> color) {
        try {
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent
                        (JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
                    //setBackground(color.get(row));
                    if (row == 0) {
                        setForeground(color.get(row));
                    } else {
                        setForeground(color.get((row+1)/2));
                    }
                    return super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
                }
            };
            int columnCount = table.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(renderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JTable getRequestTable() {
        return requestTable;
    }

    public JScrollPane getjScrollPane() {
        return jScrollPane;
    }
}
