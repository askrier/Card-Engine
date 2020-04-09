package ooga.data;

import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;

import ooga.data.style.IStyle;
import ooga.data.style.StyleData;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

/**
 * Class for parsing XML files to determine Styling information for the view. Creates an IStyle instance from which styling information can be read.
 * <p>
 * Class based mainly on ConfigParser.java from spike_simulation by Rhondu Smithwick and Robert C.
 * Duvall https://coursework.cs.duke.edu/compsci308_2020spring/spike_simulation/blob/master/src/xml/XMLParser.java
 * <p>
 * Code for iterating through ResourceBundle keys based on:
 * https://www.programcreek.com/java-api-examples/?class=java.util.ResourceBundle&method=getKeys
 *
 * @author Tyler Jang
 */
public class StyleFactory implements Factory {

    public static String STYLE_TYPE = IStyle.DATA_TYPE;
    //TODO: INCORPORATE ERROR MESSAGES

    private static String WORD = "word";
    private static String NUMBER = "number";
    private static String STYLE =  "style";
    private static final String RESOURCES = "ooga.resources";
    private static final String RESOURCE_PACKAGE = RESOURCES + "." + STYLE + "_";
    private static final ResourceBundle wordResources = ResourceBundle.getBundle(RESOURCE_PACKAGE+WORD);
    private static final ResourceBundle numberResources = ResourceBundle.getBundle(RESOURCE_PACKAGE+NUMBER);
    //TODO: IMPLEMENT DEFAULTS

    //TODO: REMOVE HARD CODING?

    private static DocumentBuilder documentBuilder;

    public StyleFactory() {
        documentBuilder = XMLHelper.getDocumentBuilder();
    }

    /**
     * Get data contained in this XML file as an IStyle object. Tests to ensure that the file is valid.
     *
     * @param dataFile file from which to read configuration
     * @return    an IStyle with all of its configuration information stored
     * @throws XMLException if the file is not considered valid due to its root element or file ending
     */
    public static IStyle getStyle(File dataFile) {
        Element root = XMLHelper.getRootAndCheck(dataFile, STYLE_TYPE, INVALID_ERROR);

        Map<String, String> stringSettings = XMLHelper.readStringSettings(root, wordResources);
        Map<String, Integer> numberSettings = XMLHelper.readNumberSettings(root, numberResources);
        return new StyleData(dataFile.getPath(), stringSettings, numberSettings);
    }
}
