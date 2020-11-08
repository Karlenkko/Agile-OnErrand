package Controller;

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

        controller.setCurrentState(controller.addRequestState1);
        System.out.println("addRequest");
    }

    public void deleteRequest(Controller controller, Window window){
        JTable table = window.getTextualView().getRequestTable();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        int rowNumber = table.getSelectedRow();

        String type = (String)table.getValueAt(rowNumber,1);
        int num = -1;
        if (type.charAt(0) == 'p'){
            String[] res = type.split("p");
            num = Integer.parseInt(res[2]);

            for (int i = 0; i < table.getRowCount(); i++) {
                String requestType = (String)table.getValueAt(i,1);
                if(requestType.contains(res[2])){
                    tableModel.removeRow(i);
                    -- i;
                }
            }
        }else if(type.charAt(0) == 'd'){
            String[] res = type.split("y");
            num = Integer.parseInt(res[1]);

            for (int i = 0; i < table.getRowCount(); i++) {
                String requestType = (String)table.getValueAt(i,1);
                if(requestType.contains(res[1])){
                    tableModel.removeRow(i);
                    -- i;
                }
            }
        }else{
            // TODO: handle exception
            System.out.println("You cannot delete the information for depot.");
        }

        if (num != -1) {
            Request request = controller.getMission().deleteRequest(num);

            // delete the pickup of the request
            ArrayList<Long> addressToUpdate = controller.getMission().getBeforeAfterAddress(request.getPickup().getId());
            Long[] sequence = new Long[2];
            sequence[0] = addressToUpdate.get(0);
            sequence[1] = addressToUpdate.get(1);
            ArrayList<Long> bestSolIntersection = new ArrayList<>();
            bestSolIntersection.addAll(controller.getGraph().getShortestPaths(false).get(sequence[0]+" "+sequence[1]));
            double[] interAddressLength = new double[1];
            interAddressLength[0] = controller.getGraph().getCost(sequence[0], sequence[1]);
            controller.getMission().updatePartialTour(sequence, bestSolIntersection,interAddressLength);

            // delete the delivery of the request
            addressToUpdate = controller.getMission().getBeforeAfterAddress(request.getDelivery().getId());
            sequence = new Long[2];
            sequence[0] = addressToUpdate.get(0);
            sequence[1] = addressToUpdate.get(1);
            bestSolIntersection = new ArrayList<>();
            bestSolIntersection.addAll(controller.getGraph().getShortestPaths(false).get(sequence[0]+" "+sequence[1]));
            interAddressLength = new double[1];
            interAddressLength[0] = controller.getGraph().getCost(sequence[0], sequence[1]);
            controller.getMission().updatePartialTour(sequence, bestSolIntersection,interAddressLength);


            window.getGraphicalView().setPaintTour(true);
            window.getGraphicalView().repaint();
            window.getTextualView().updateRequestTable();
        }

    }

    @Override
    public void undo(ListOfCommands listOfCommands){
        listOfCommands.undo();
    }

    @Override
    public void redo(ListOfCommands listOfCommands){
        listOfCommands.redo();
    }
}
