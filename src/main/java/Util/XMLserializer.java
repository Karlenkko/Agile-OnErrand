package Util;

import java.io.File;
import java.util.Iterator;
import IHM.Meituan;
import Model.Intersection;
import Model.Map;
import Model.Mission;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Model.Segment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;


public class XMLserializer extends Meituan {

    private Element something;
    private Document document;

    private Map map = new Map();
    private Mission mission = new Mission();
    private Segment segment= new Segment();
    private static XMLserializer instance = null;
    private FileOpener XMLfileOpener;

    public XMLserializer() throws ParserConfigurationException {
    }

    public static XMLserializer getInstance() throws ParserConfigurationException {
        if (instance == null)
            instance = new XMLserializer();
        return instance;
    }

/**
 * Open an XML file and write an XML description of the map in it
 * @param map the map to serialise
 * @throws ParserConfigurationException
 * @throws TransformerFactoryConfigurationError
 * @throws TransformerException
 * @throws ExceptionXML
 */

public void save(Map map) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, ExceptionXML{
    File xml = XMLfileOpener.getInstance().open(false);
    StreamResult result = new StreamResult(xml);
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    document.appendChild(createSegmentElt(segment));
    DOMSource source = new DOMSource(document);
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    xformer.setOutputProperty(OutputKeys.INDENT, "yes");
    xformer.transform(source, result);
}

    private Node createSegmentElt(Segment segment) {
        Element racine = document.createElement("segment");
        return racine;
    }

    private void createAttribute(Element root, String name, String value){
        Attr attribut = document.createAttribute(name);
        root.setAttributeNode(attribut);
        attribut.setValue(value);
    }


}
