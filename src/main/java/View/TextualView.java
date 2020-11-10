package View;

import Model.Mission;
import Model.Observable;
import Model.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

public class TextualView extends JPanel implements Observer {

    private Mission mission;
    private JTextArea textArea;
    private JTable requestTable;
    private JScrollPane jScrollPane;
    private JScrollPane jScrollPane2;
    private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
    private ArrayList<String> requestTour = new ArrayList<>();
    private boolean lockInstruction = false;
    private Window window;

    /**
     * Constructor of the TextualView which contains the information of the mission.
     * Associate with the window for adding itself to the window.
     * @param mission the mission we want to add to the table.
     * @param window the instance window which contains the textualView
     */
    public TextualView(Mission mission, Window window) {
        mission.addObserver(this);
        setLayout(boxLayout);
        this.mission = mission;
        setBackground(Color.white);
        initialise();
        this.window = window;
        window.getContentPane().add(this);
    }

    /**
     * initalize the table area. Filling it with 'Nothing'.
     */
    public void initialise() {
        textArea = new JTextArea(4,30);
        textArea.setText("Helpful Informations");
        textArea.setEditable(false);
        jScrollPane2 = new JScrollPane(textArea);
        String[] columNames = {"ID","Type","Duration","Arrival","Depart"};
        Object[][] defaultData = {
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

        requestTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("hhhhhh");
                int[] selectedRow = requestTable.getSelectedRows();
                String type = (String) requestTable.getValueAt(selectedRow[0], 1);
                int num = -1;
                if (type.charAt(0) == 'p') {
                    String[] res = type.split("p");
                    num = Integer.parseInt(res[2]);
                } else if (type.charAt(0) == 'd' && type.charAt(2) == 'l') {
                    String[] res = type.split("y");
                    num = Integer.parseInt(res[1]);
                }
                mission.setDelete(num);
                requestTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

                for (int i = 0; i < requestTable.getRowCount(); i++) {
                    String requestType = (String) requestTable.getValueAt(i, 1);
                    if (mission.isDeletedRequest(requestType)) {
                        requestTable.changeSelection(i, 1, true, false);
                    }
                }
                window.getGraphicalView().setPaintAdd(true);
                window.getGraphicalView().repaint();
            }
        });

    }

    /**
     * Change the value for lockInstructions.
     * For some actions, we want to make the ancient instructions conserved so need to set the lockInstruction true.
     * @param lockInstruction the boolean value which marks if we need to conserve the ancient instruction or not.
     */
    public void setLockInstruction(boolean lockInstruction) {
        this.lockInstruction = lockInstruction;
    }

    /**
     * Put a description in the textarea of the application.
     * @param s the description we want to add.
     */
    public void setTextAreaText(String s) {
        if (lockInstruction) return;
        this.textArea.setText(s);
    }

    /**
     * Obtain the description on the textarea.
     * @return the description on the textarea.
     */
    public String getTextAreaText() {
        return this.textArea.getText();
    }

    /**
     * Modify the size of the table according to the content of the table.
     */
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

    /**
     * Initialise the table with the mission loaded.
     * ID,type,duration added for each pickup and delivery point.
     * ID,type,departureTime added for depot point.
     */
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

    /**
     * Update the requestTable after calculating the tour.
     * Change the order by the order of passing each point.
     * DepartureTime and arrivalTime calculated for every point.
     */
    public void updateRequestTable() {
        if(mission.getDepartureTime() == null) {
            return;
        }
        DefaultTableModel tableModel = (DefaultTableModel) requestTable.getModel();
        tableModel.getDataVector().removeAllElements();
        LinkedList<Long> tour = mission.getTour();

        for (int i=0; i < tour.size(); ++i) {
            if (mission.getDepot().getId() == (long)tour.get(i)) {
//                requestTour.add(tour.get(i).toString());
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
                        //break;
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
                        //break;
                    }
                }
            }
        }
        tableModel.fireTableDataChanged();
    }

    /**
     * Obtain a random color
     * @param number a number which helps to generate a color
     * @return the arraylist of the color generated.
     */
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

    /**
     * Set the color the table by using a list of the colors.
     * @param table the table which we want to change the color.
     * @param color the list of the color we want to change.
     */
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

    /**
     * Obtain the requestTable in the window
     * @return the requestTable in the window
     */
    public JTable getRequestTable() {
        return requestTable;
    }

    /**
     * When the information in the model changed. The textualView changed also.
     * @param observed an instance of Observable which is used to observe the information change in the model.
     * @param arg the object which has the changed information
     */
    @Override
    public void update(Observable observed, Object arg) {

    }

    /**
     * Obtain the textArea in the window.
     * @return the textarea in the window.
     */
    public JTextArea getTextArea() {
        return textArea;
    }
}
