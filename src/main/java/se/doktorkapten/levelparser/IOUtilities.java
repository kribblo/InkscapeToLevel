package se.doktorkapten.levelparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class IOUtilities {
    public final String TAG = this.getClass().getSimpleName();

    public static Document loadXmlFile(String path, String folder, String fileName) {
        return IOUtilities.loadXmlFile(path + "/" + folder + "/" + fileName);
    }

    public static Document loadXmlFile(String path) {
        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        LogMessage.log("IOUtilities", "loadXmlFile", "Path:" + path);

        File file = new File(path);
        LogMessage.log("IOUtilities", "loadXmlFile", "Can read:" + file.canRead());

        /*
         * if (file.createNewFile()) { LogMessage.log("IOUtilities",
         * "writeToFile", "file created"); } else {
         * LogMessage.log("IOUtilities", "writeToFile", "file already exists");
         * }
         */

        try {

            // Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            // Document dom = db.parse(path);
            LogMessage.log("IOUtilities", "loadXmlFile", "Start parse:");
            Document dom = db.parse(file);
            LogMessage.log("IOUtilities", "loadXmlFile", "Finish parse:");

            return dom;

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            LogMessage.log("IOUtilities", "loadXmlFile", "ParserConfigurationException");
        } catch (SAXException se) {
            se.printStackTrace();
            LogMessage.log("IOUtilities", "loadXmlFile", "SAXException");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            LogMessage.log("IOUtilities", "loadXmlFile", "IOException");
        }
        return null;
    }

    public static String loadTextFile(String path) {
        File file = new File(path);
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            // repeat until all lines is read
            while ((text = reader.readLine()) != null) {
                contents.append(text).append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return contents.toString();
    }

    /**
     * Change the contents of text file in its entirety, overwriting any
     * existing text.
     * 
     * This style of implementation throws all exceptions to the caller.
     * 
     * @param aFile
     *            is an existing file which can be written to.
     * @throws IllegalArgumentException
     *             if param does not comply.
     * @throws FileNotFoundException
     *             if the file does not exist.
     * @throws IOException
     *             if problem encountered during write.
     */
    static public void writeToFile(String path, String folder, String fileName, String data) throws FileNotFoundException, IOException {
        writeToFile(path + "/" + folder + "/" + fileName, data);
    }

    static public void writeToFile(String path, String data) throws FileNotFoundException, IOException {

        File file = new File(path);

        if (file.createNewFile()) {
            LogMessage.log("IOUtilities", "writeToFile", "file created");
        } else {
            LogMessage.log("IOUtilities", "writeToFile", "file already exists");
        }

        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file);
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("Should not be a directory: " + file);
        }
        if (!file.canWrite()) {
            throw new IllegalArgumentException("File cannot be written: " + file);
        }

        // use buffering
        Writer output = new BufferedWriter(new FileWriter(file));
        try {
            // FileWriter always assumes default encoding is OK!
            output.write(data);
        } finally {
            output.close();
        }
    }

}
