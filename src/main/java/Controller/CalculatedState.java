package Controller;

import Algorithm.MapGraph;
import Algorithm.TSP;
import Algorithm.TSP1;
import Model.Request;
import Model.Segment;
import Util.ExceptionXML;
import Util.TourSerializer;
import View.TextualView;
import View.Window;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

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

    }

    public void deleteRequest(Controller controller, Window window){
        JTable table = window.getTextualView().getRequestTable();

        int rowNumber = table.getSelectedRow();

        String type = (String)table.getValueAt(rowNumber,1);
        int num;
        if (type.charAt(0) == 'p'){
            String[] res = type.split("p");
            num = Integer.parseInt(res[2]);
        }else{
            String[] res = type.split("y");
            num = Integer.parseInt(res[1]);
        }

        System.out.println(num);
    }
}
