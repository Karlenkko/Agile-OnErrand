package Util;

public class ExceptionXML extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor of ExceptionXML, extends from Exception
     * used to throw some particular exceptions during the XML parsing
     * @param message the particular message about the problems during the XML parsing
     */
    public ExceptionXML(String message) {
        super(message);
    }
}
