package IHM;

import Data.Intersection;
import Data.XmlLoader;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The IHM of the main Fonction
 *
 * @author yzong
 * @create 12-10-2020 at 16:21
 */
public class Meituan {

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
    JPanel mapShow = new JPanel();
    JTextArea tips = new JTextArea("Operation tips: \n Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

    JTable requestList = new JTable(new tableModel());
    JScrollPane jsp = new JScrollPane(requestList);

    private class tableModel extends AbstractTableModel{
        String[] columnNames = {"ID","Type","Duration","Arrival","Depart"};

        // data is a object with the info incorrect. Adapting the programme later.
        Object data[][] = {
                {"01","depot","--","--","8:00"},
                {"02","pickup","20","9:30","9:50"},
                {"03","delivery","10","10:25","10:35"}
        };


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
            return false;
        }

        // change the value of a cell if it is editable
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    private class MyCanvas extends JPanel{

        public void paint(Graphics g){
            // draw the border of the map
            g.setColor(new Color(0,120,120));
            g.drawRect(0,0,600,600);
            g.drawLine(0,0,600,600);
            g.drawLine(0,600,600,0);
            // Add the roads after changing the map

            // Add the points after loading the requests

            // Add the route in a different color after calculate the route

            // Add the others points after add request

            // Delete a pair of the points after delete request
        }
    }

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
        bTopRight.add(jsp);

        // Add the components to the central area
        bTop.add(Box.createHorizontalStrut(50));
        bTop.add(mapShow);
        bTop.add(Box.createHorizontalStrut(50));
        bTop.add(bTopRight);
        bTop.add(Box.createHorizontalStrut(50));
        frame.add(bTop);

            b1.addActionListener(new ChangeMapActionListener());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    class MapPanel extends JPanel {

            private LinkedList<Intersection> intersectionParticular = new LinkedList<Intersection>();


        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (!paint) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g;

            double rateX = (maxX-minX)/(this.getSize().width-20);
            double rateY = (maxY-minY)/(this.getSize().height-20);

            rate = rateX > rateY ? rateX : rateY;
            HashMap<Long, Intersection> map = XmlLoader.map;

            for (Intersection intersection : map.values()) {
                double x = intersection.getX() - minX;
                double y = intersection.getY() - minY;

                Color color = Color.red;
                int size = 4;

                switch (intersection.getType()) {
                    case Intersection.DEPOT_POINT:
                        color = Color.black;
                        size = 10;
                        intersectionParticular.add(intersection);
                        break;
                    case Intersection.PICKUP_POINT:
                        color = Color.BLUE;
                        size = 10;
                        intersectionParticular.add(intersection);
                        break;
                    case  Intersection.DELIVERY_POINT:
                        color = Color.MAGENTA;
                        size = 10;
                        intersectionParticular.add(intersection);
                        break;
                    default:
                        break;
                }

                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(size));

                g2d.draw(new Line2D.Double(x/rate,y/rate,x/rate,y/rate));
                for(Intersection toIntersection : intersection.getToIntersections()){
                    double x2 = toIntersection.getX() - minX;
                    double y2 = toIntersection.getY() - minY;

                    g2d.setColor(Color.green);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new Line2D.Double(x/rate,y/rate,x2/rate,y2/rate));
                }
            }


        }
    }

    class ChangeMapActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            XmlLoader xmlLoader = null;
            try {
                xmlLoader = new XmlLoader();
            } catch (Exception exception) {
            }
            double[] param = new double[4];
            try {
                param = xmlLoader.chargeMap();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            minX = param[0];
            minY = param[1];
            maxX = param[2];
            maxY = param[3];

            paint = true;

            mapShow.repaint();


        }
    }

}
