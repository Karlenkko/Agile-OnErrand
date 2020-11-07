package View;

import Model.Intersection;
import Model.Mission;
import Model.Observable;
import Model.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class TextualView extends JPanel implements Observer {

    private Mission mission;
    private JTextArea textArea;
    private JTable requestTable;
    private JScrollPane jScrollPane;
    private JScrollPane jScrollPane2;
    private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
    private ArrayList<String> requestTour = new ArrayList<>();

    public TextualView(Mission mission, Window window) {
        mission.addObserver(this);
        setLayout(boxLayout);
        this.mission = mission;
        setBackground(Color.white);
        initialise();
        window.getContentPane().add(this);
    }

    public void initialise() {
        textArea = new JTextArea(4,30);
        textArea.setText("Helpful Informations");
        textArea.setEditable(false);
        jScrollPane2 = new JScrollPane(textArea);
        String[] columNames = {"ID","Type","Duration","Arrival","Depart"};
        Object defaultData[][] = {
                {"Nothing","Nothing","Nothing","Nothing","Nothing"}
        };
        requestTable = new JTable(new DefaultTableModel(defaultData, columNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        jScrollPane = new JScrollPane(requestTable);
        this.add(Box.createVerticalStrut(20));
        this.add(jScrollPane2);
        this.add(Box.createVerticalStrut(20));
        this.add(jScrollPane);
    }

    public void setTextAreaText(String s) {
        this.textArea.setText(s);
    }

    public String getTextAreaText() {
        return this.textArea.getText();
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


    public void initiateRequestTable() {
        if(mission.getDepartureTime() == null) {
            return;
        }
        DefaultTableModel tableModel = (DefaultTableModel) requestTable.getModel();
        tableModel.getDataVector().removeAllElements();
        LinkedList<Long> tour = mission.getTour();

        requestTour.clear();
        for (int i=0; i < tour.size(); ++i) {
            if (mission.getDepot().getId() == (long)tour.get(i)) {
                requestTour.add(tour.get(i).toString());
                String[] row = {Integer.toString(i+1), "depot", "--", "--", mission.getDepartureTime().toString()};
                tableModel.addRow(row);
            } else {
                for(Request request : mission.getAllRequests()) {
                    String idRequest = request.getPickup().getId()+" "+request.getDelivery().getId();
                    if ((long)tour.get(i) == request.getPickup().getId()) {
                        if (!requestTour.contains(idRequest)) {
                            requestTour.add(idRequest);
                        }
                        String[] row1 = {Integer.toString(i+1), "pickup"+requestTour.indexOf(idRequest), Integer.toString(request.getPickupDuration()), "--", "--"};
                        tableModel.addRow(row1);
                        break;
                    }
                    if ((long)tour.get(i) == request.getDelivery().getId()) {
                        if (!requestTour.contains(idRequest)) {
                            requestTour.add(idRequest);
                        }
                        String[] row1 = {Integer.toString(i+1), "delivery"+requestTour.indexOf(idRequest), Integer.toString(request.getDeliveryDuration()), "--", "--"};
                        tableModel.addRow(row1);
                        break;
                    }
                }
            }
        }
        tableModel.fireTableDataChanged();

/*
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

 */
    }

    public void updateRequestTable() {
        if(mission.getDepartureTime() == null) {
            return;
        }
        DefaultTableModel tableModel = (DefaultTableModel) requestTable.getModel();
        tableModel.getDataVector().removeAllElements();
        LinkedList<Long> tour = mission.getTour();

        for (int i=0; i < tour.size(); ++i) {
            if (mission.getDepot().getId() == (long)tour.get(i)) {
                requestTour.add(tour.get(i).toString());
                String[] row = {
                        Integer.toString(i+1),
                        "depot",
                        "--",
                        mission.getArrivalTimeSchedule().get(tour.get(i)).toString(),
                        mission.getDepartureTime().toString()
                };
                tableModel.addRow(row);
            } else {
                for(Request request : mission.getAllRequests()) {
                    String idRequest = request.getPickup().getId()+" "+request.getDelivery().getId();
                    if ((long)tour.get(i) == request.getPickup().getId()) {
                        if (!requestTour.contains(idRequest)) {
                            requestTour.add(idRequest);
                        }
                        String[] row1 = {
                                Integer.toString(i+1),
                                "pickup"+requestTour.indexOf(idRequest),
                                Integer.toString(request.getPickupDuration()),
                                mission.getArrivalTimeSchedule().get(tour.get(i)).toString(),
                                mission.getDepartureTimeSchedule().get(tour.get(i)).toString()
                        };
                        tableModel.addRow(row1);
                        break;
                    }
                    if ((long)tour.get(i) == request.getDelivery().getId()) {
                        if (!requestTour.contains(idRequest)) {
                            requestTour.add(idRequest);
                        }
                        String[] row1 = {
                                Integer.toString(i+1),
                                "delivery"+requestTour.indexOf(idRequest),
                                Integer.toString(request.getDeliveryDuration()),
                                mission.getArrivalTimeSchedule().get(tour.get(i)).toString(),
                                mission.getDepartureTimeSchedule().get(tour.get(i)).toString()
                        };
                        tableModel.addRow(row1);
                        break;
                    }
                }
            }
        }
        tableModel.fireTableDataChanged();
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

    @Override
    public void update(Observable observed, Object arg) {

    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
