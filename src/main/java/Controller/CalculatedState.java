package Controller;

import Util.TourSerializer;
import View.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CalculatedState implements State{


    public void generateRoadMap(Controller controller)  {
        try {
            TourSerializer tourSerializer = new TourSerializer(controller.getMission(), controller.getMap());
            tourSerializer.generateRoadMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addRequest(Controller controller, Window window){

        //TODO:Click on the button "add request to add two points of pickup and delivery

        controller.setCurrentState(controller.addRequestState1);
        System.out.println("addRequest");
    }

    public void deleteRequest(Controller controller, Window window){
        JTable table = window.getTextualView().getRequestTable();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        int rowNumber = table.getSelectedRow();

        String type = (String)table.getValueAt(rowNumber,1);
        if (type.charAt(0) == 'p'){
            String[] res = type.split("p");
            for (int i = 0; i < table.getRowCount(); i++) {
                String requestType = (String)table.getValueAt(i,1);
                if(requestType.contains(res[2])){
                    tableModel.removeRow(i);
                    -- i;
                }
            }
        }else if(type.charAt(0) == 'd'){
            String[] res = type.split("y");
            for (int i = 0; i < table.getRowCount(); i++) {
                String requestType = (String)table.getValueAt(i,1);
                if(requestType.contains(res[1])){
                    tableModel.removeRow(i);
                    -- i;
                }
            }
        }else{
            System.out.println("You cannot delete the information for depot.");
        }

        // Num now represents which request is being deleted.

        // TODO: Update the request list

        // TODO: Repaint the Graphical View

        // TODO: Update the route calculated and the time calculated in the table
    }
}
