package Data;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class XMLfileOpener extends FileFilter {

    private static String type = "XML";
    private static XMLfileOpener instance = null;
    private XMLfileOpener(){}

    /**
     * Get the instance of XMLfileOpener without creating another one
     * @return the instance of the objet XMLfileOpener
     */
    public static XMLfileOpener getInstance(){
        if(instance == null) instance = new XMLfileOpener();
        return instance;
    }

    /**
     * Choose the file to read or Save in a file chosen
     * @param read true represents to choose the file to read
     *             false represents to create a file to save data
     * @return the file chosen or created
     * @throws ExceptionXML
     */
    public File open(boolean read) throws ExceptionXML {
        int returnVal;
        JFileChooser jFileChooser = new JFileChooser(".");
        jFileChooser.setFileFilter(this);
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (read) {
            type = "XML";
            returnVal = jFileChooser.showOpenDialog(null);
        } else {
            type = "TXT";
            returnVal = jFileChooser.showSaveDialog(null);
            File file = jFileChooser.getSelectedFile();
            String fname = jFileChooser.getName(file);
            if (fname.indexOf(".txt") == -1) {
                file = new File(jFileChooser.getCurrentDirectory(),fname+".txt");
            }
            return file;
        }
        if (returnVal != JFileChooser.APPROVE_OPTION)
            throw new ExceptionXML("Problem when opening file");
        return new File(jFileChooser.getSelectedFile().getAbsolutePath());
    }

    /**
     * Filter the file chosen
     * @param f File which extension to be analysed
     * @return  true File type match the file XML
     *          false File type does not match the file XML
     */
    @Override
    public boolean accept(File f) {
        if (f == null) return false;
        if (f.isDirectory()) return true;
        String extension = getExtension(f);
        if (extension == null) return false;
        return extension.contentEquals("xml");
    }

    /**
     * Create the description of file
     * @return String of the description
     */
    @Override
    public String getDescription() {
        return type + " file";
    }

    /**
     * Get the extension of the file
     * @param f File to be split the extension
     * @return the extension of the file
     */
    private String getExtension(File f) {
        String filename = f.getName();
        int i = filename.lastIndexOf('.');
        if (i>0 && i<filename.length()-1)
            return filename.substring(i+1).toLowerCase();
        return null;
    }
}
