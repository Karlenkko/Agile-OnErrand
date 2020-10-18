package IHM;

import Data.XMLparser;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


/**
 * The IHM of the main Fonction
 *
 * @author yzong
 * @create 12-10-2020 at 16:21
 */
public class Meituan {

        private Map map = new Map();
        private Mission mission = new Mission(map);
        private static double rate;
        boolean paint = false;
        boolean paintRequest = false;
        private String[] columnNames = {"ID","Type","Duration","Arrival","Depart"};
        private Object data[][] = {};

        private Vector titleV = new Vector();
        private Vector<Vector> dataV = new Vector<Vector>();

        JFrame frame = new JFrame("Meituan");

        JButton b1 = new JButton("Change Map");
        JButton b2 = new JButton("Calculate Route");
        JButton b3 = new JButton("Generate Roadmap");
        JButton b4 = new JButton("Load Request");
        JButton b5 = new JButton("Add Request");
        JButton b6 = new JButton("Delete Request");

        JPanel pBottom = new JPanel();
        Box bTop = Box.createHorizontalBox();
        Box bTopRight = Box.createVerticalBox();
        JPanel mapShow = new MapPanel();
        JTextArea tips = new JTextArea("Operation tips: \n Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        DefaultTableModel dtm;
        JTable requestList;

    public Meituan() throws ParserConfigurationException {

    }
    /*
    private class tableModel extends AbstractTableModel{
            String[] columnNames = {"ID","Type","Duration","Arrival","Depart"};

            // data is a object with the info incorrect. Adapting the programme later.
            Object data[][]={
                    {"01","depot","--","--","8:00"}
            };
            // {"01","depot","--","--","8:00"},
            // {"02","pickup","20","9:30","9:50"},
            // {"03","delivery","10","10:25","10:35"}


            public void chargeRequests(){
                LinkedList<Request> allRequests = map.getAllRequests();
                setValueAt(1,0,0);
                setValueAt("depot",0,1);
                setValueAt("--",0,2);
                setValueAt("--",0,3);
                setValueAt(map.getStartTime(),0,4);

                for(int i = 1; i < allRequests.size(); ++i){
                    setValueAt(2 * i,2 * i - 1,0);
                    setValueAt("pickup",2 * i - 1,1);
                    setValueAt(allRequests.get(i).getPickupDuration(),2 * i - 1,2);
                    setValueAt("--",2 * i - 1,3);
                    setValueAt("--",2 * i - 1,4);
                    setValueAt(2 * i,2 * i,0);
                    setValueAt("delivery",2 * i,1);
                    setValueAt(allRequests.get(i).getDeliveryDuration(),2 * i,2);
                    setValueAt("--",2 * i,3);
                    setValueAt("--",2 * i,4);
                }


            }

            public String getColumnName(int column){
                return columnNames[column];
            }

            // return the nbr of the rows
            public int getRowCount() {
                return data.length;
            }

            // return the nbr of the columns
            public int getColumnCount() {
                return columnNames.length;
            }

            // return the value of the table at a given position
            public Object getValueAt(int rowIndex, int columnIndex) {
                return data[rowIndex][columnIndex];
            }

            // get the type of the value in a column
            public Class<?> getColumnClass(int columnIndex) {
                return data[0][columnIndex].getClass();
            }

            // set all the information in the table not-editable
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }

            // change the value of a cell if it is editable
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                data[rowIndex][columnIndex] = aValue;
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
        */

        public void init(){

            frame.setBounds(0,0,1280,720);

            // Add all the button to the south area of the Window
            pBottom.add(b1);
            pBottom.add(b2);
            pBottom.add(b3);
            pBottom.add(b4);
            pBottom.add(b5);
            pBottom.add(b6);
            frame.add(pBottom,BorderLayout.SOUTH);


            // Set the map area
            mapShow.setPreferredSize(new Dimension(600,600));
            mapShow.setBackground(new Color(0,120,120));

            // Set the request area

            tips.setPreferredSize(new Dimension(500,200));
            tips.setBorder(BorderFactory.createLineBorder(Color.black));
            tips.setEditable(false);
            tips.setAlignmentY(JTextArea.TOP_ALIGNMENT);
            bTopRight.add(tips);
            bTopRight.add(Box.createVerticalStrut(20));
            initTableInfo();
            dtm = new DefaultTableModel(dataV,titleV);
            requestList = new JTable(dtm);
            JScrollPane jsp = new JScrollPane(requestList);
            bTopRight.add(jsp);

            // Add the components to the central area
            bTop.add(Box.createHorizontalStrut(50));
            bTop.add(mapShow);
            bTop.add(Box.createHorizontalStrut(50));
            bTop.add(bTopRight);
            bTop.add(Box.createHorizontalStrut(50));
            frame.add(bTop);

            b1.addActionListener(new ChangeMapActionListener());
            b3.addActionListener(new GenerateRoadmapActionListener());
            b4.addActionListener(new LoadRequestsActionListener());

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }

        private void initTableInfo(){
            for (int i = 0; i < columnNames.length; i++) {
                titleV.add(columnNames[i]);
            }

            for (int i = 0; i < data.length; i++) {
                Vector t = new Vector();
                for (int j = 0; j < data[i].length; j++) {
                    t.add(data[i][j]);
                }
                dataV.add(t);
            }
        }

        private void changeTable(){
            dtm.getDataVector().removeAllElements();
            ArrayList<Request> allRequests = mission.getAllRequests();
            dtm.addRow(new Object[]{0,"depot","--","--",mission.getDepartureTime()});
            for (int i = 0; i < allRequests.size(); i++) {
                dtm.addRow(new Object[]{2 * (i+1) - 1,"pickup",allRequests.get(i).getPickupDuration(),"--","--"});
                dtm.addRow(new Object[]{2 * (i+1),"delivery",allRequests.get(i).getDeliveryDuration(),"--","--"});
            }
        }

    class MapPanel extends JPanel {


        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (!paint) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g;

            double minX = map.getMinX();
            double minY = map.getMinY();
            double maxX = map.getMaxX();
            double maxY = map.getMaxY();

            double rateX = (maxX - minX)/(this.getSize().width - 10);
            double rateY = (maxY - minY)/(this.getSize().height - 10);

            rate = rateX > rateY ? rateX : rateY;
            HashMap<Long, Intersection> intersections = map.getAllIntersections();


            for (Intersection intersection : intersections.values()) {
                double x = intersection.getX() - minX + 30;
                double y = intersection.getY() - minY + 30;

                g2d.setColor(Color.black);
                g2d.setStroke(new BasicStroke(4));

                g2d.draw(new Line2D.Double(x/rate,y/rate,x/rate,y/rate));

            }

            for(Segment segment : map.getAllSegments()){
                double x = segment.getOrigin().getX() - minX + 30;
                double y = segment.getOrigin().getY() - minY + 30;
                double x2 = segment.getDestination().getX() - minX + 30;
                double y2 = segment.getDestination().getY() - minY + 30;

                g2d.setColor(Color.green);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new Line2D.Double(x/rate,y/rate,x2/rate,y2/rate));
            }

            if(paintRequest) {
                ArrayList<Request> allRequests = mission.getAllRequests();
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(5));
                double x = mission.getDepot().getX() - minX + 30;
                double y = mission.getDepot().getY() - minY + 30;
                g2d.draw(new Line2D.Double(x/rate,y/rate,x/rate,y/rate));

                for (int i = 0; i < allRequests.size(); ++i) {
                    g2d.setColor(Color.BLUE);
                    g2d.setStroke(new BasicStroke(5));
                    double pickupX = allRequests.get(i).getPickup().getX() - minX + 30;
                    double pickupY = allRequests.get(i).getPickup().getY() - minY + 30;
                    g2d.draw(new Line2D.Double(pickupX/rate,pickupY/rate,pickupX/rate,pickupY/rate));

                    g2d.setColor(Color.ORANGE);
                    g2d.setStroke(new BasicStroke(5));
                    double deliveryX = allRequests.get(i).getDelivery().getX() - minX + 30;
                    double deliveryY = allRequests.get(i).getDelivery().getY() - minY + 30;
                    g2d.draw(new Line2D.Double(deliveryX/rate,deliveryY/rate,deliveryX/rate,deliveryY/rate));
                }
            }

        }
    }

    class ChangeMapActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                XMLparser.parserMap(map);
            } catch (Exception exception) {
                // deal with the exception that user cancel the selection of file
            }
            paint = true;

            mapShow.repaint();


        }
    }

    class LoadRequestsActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                XMLparser.parserRequest(mission);
                for (Request request : mission.getAllRequests()) {
                    System.out.println(request.getPickup().getId());
                }
                changeTable();
            } catch (Exception exception) {
                System.out.println(exception);
                // deal with the exception that user cancel the selection of file
            }
            paintRequest = true;

            mapShow.repaint();

        }
    }

    class GenerateRoadmapActionListener implements ActionListener{

            public void actionPerformed(ActionEvent e){
                try {
                    ExportRoute er = new ExportRoute();
                    er.init();
                } catch (Exception exception) {
                }
            }
    }
}
