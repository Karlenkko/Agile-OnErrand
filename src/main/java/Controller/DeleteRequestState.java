package Controller;

import Algorithm.Graph;
import Model.Intersection;
import Model.Mission;
import Model.Request;
import View.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class DeleteRequestState implements State {

    @Override
    public void leftClick(Controller controller, Window window, int positionX, int positionY){
        System.out.println("entering");
        Intersection intersection = controller.getMission().NearestRequest(positionX,positionY, window.getRate());
        controller.getMission().setDelete(intersection);

        JTable table = window.getTextualView().getRequestTable();
        /*
        ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
        listSelectionModel.setSelectionInterval(0, table.getRowCount()-1);
        table.setSelectionModel(listSelectionModel);

         */
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        for (int i = 0; i < table.getRowCount(); i++) {
            String requestType = (String) table.getValueAt(i, 1);
            if (controller.getMission().isDeletedRequest(requestType)) {
                table.changeSelection(i,1, true,false);
            }
        }
        window.getGraphicalView().setPaintAdd(true);
        window.getGraphicalView().repaint();

    }

    public void deleteRequest(Controller controller, Window window, ListOfCommands listOfCommands){
        JTable table = window.getTextualView().getRequestTable();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        if (table.getSelectedRowCount() > 2) {
            JOptionPane.showMessageDialog(null, "choose one request to be deleted on the request table", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

        controller.getMission().resetDelete();
        controller.setCurrentState(controller.calculatedState);
    }
}
