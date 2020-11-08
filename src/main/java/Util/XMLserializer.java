package Util;

import IHM.Meituan;
import Model.Map;
import Model.Mission;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Vector;

public class XMLserializer extends Meituan {


    private Map map = new Map();
    private Mission mission = new Mission();
    private static XMLserializer instance = null;

    public XMLserializer() throws ParserConfigurationException {
    }

    public static XMLserializer getInstance() throws ParserConfigurationException {
        if (instance == null)
            instance = new XMLserializer();
        return instance;
    }
    


}
