package Controller;

import Algorithm.Graph;
import Model.Mission;
import Model.Request;
import Util.TourSerializer;
import View.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

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
        window.allow("addRequestState1");
        controller.setCurrentState(controller.addRequestState1);
        window.getTextualView().setLockInstruction(false);
        window.getTextualView().setTextAreaText("You are now adding a new request, \n" +
                "please select an existing address on the map as the start address, \n" +
                "or right click to cancel the addition");
        window.getTextualView().setLockInstruction(true);
        System.out.println("addRequest");
    }

    public void deleteRequest(Controller controller, Window window, ListOfCommands listOfCommands){
        JTable table = window.getTextualView().getRequestTable();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        int rowNumber = table.getSelectedRow();


        int num = -1;
        try {
            String type = (String)table.getValueAt(rowNumber,1);
            if (type.charAt(0) == 'p') {
                String[] res = type.split("p");
                num = Integer.parseInt(res[2]);

                for (int i = 0; i < table.getRowCount(); i++) {
                    String requestType = (String) table.getValueAt(i, 1);
                    if (requestType.contains(res[2])) {
                        tableModel.removeRow(i);
                        --i;
                    }
                }
            } else if (type.charAt(0) == 'd' && type.charAt(2) == 'l') {
                String[] res = type.split("y");
                num = Integer.parseInt(res[1]);

                for (int i = 0; i < table.getRowCount(); i++) {
                    String requestType = (String) table.getValueAt(i, 1);
                    if (requestType.contains(res[1])) {
                        tableModel.removeRow(i);
                        --i;
                    }
                }
            } else {
                // TODO: handle exception
                window.getTextualView().setLockInstruction(false);
                window.getTextualView().setTextAreaText("You cannot delete the information for depot.");
                window.getTextualView().setLockInstruction(true);
            }
        } catch (Exception e) {
            window.getTextualView().setLockInstruction(false);
            window.getTextualView().setTextAreaText("You have not selected any address of a request.");
            window.getTextualView().setLockInstruction(true);
        }
        if (num != -1) {
            Mission mission = controller.getMission();
            Request request = mission.deleteRequest(num);
            Graph g = controller.getGraph();

            ArrayList<Long> replacedRequestList = mission.getReplacedRequestsList(request);

            AddCommand cmd = new AddCommand(g, mission, controller.getTsp(), request, replacedRequestList);
            listOfCommands.add(new ReverseCommand(cmd));


            window.getGraphicalView().setPaintTour(true);
            window.getGraphicalView().repaint();
            window.getTextualView().updateRequestTable();
            window.getTextualView().setLockInstruction(false);
            window.getTextualView().setTextAreaText("You have deleted the request " + num);
            window.getTextualView().setLockInstruction(true);
        }

    }

    @Override
    public void undo(ListOfCommands listOfCommands, Window window){
        listOfCommands.undo();
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
    }

    @Override
    public void redo(ListOfCommands listOfCommands, Window window){
        listOfCommands.redo();
        window.getGraphicalView().setPaintTour(true);
        window.getGraphicalView().repaint();
        window.getTextualView().updateRequestTable();
    }
}
