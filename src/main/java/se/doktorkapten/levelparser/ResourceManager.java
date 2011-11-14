package se.doktorkapten.levelparser;

import java.io.IOException;
import java.util.HashMap;

import org.w3c.dom.Document;

public class ResourceManager {

    /**
     * @uml.property name="debug"
     */
    public boolean debug = false;
    public final String TAG = this.getClass().getSimpleName();
    private HashMap<String, Document> documents;

    // private HashMap <String,Integer> resources;

    public ResourceManager() {
        documents = new HashMap<String, Document>();
    }

    public Document loadXmlFile(String path) throws IOException {
        /*
         * See if the texture has already been registered
         */
        if (documents.containsKey(path))
            return documents.get(path);

        Document dom = IOUtilities.loadXmlFile(path);
        documents.put(path, dom);
        if (dom == null)
            LogMessage.error(TAG, "loadXmlFile", "DOM null");
        return dom;
    }

    public Document loadSVG(String path) throws IOException {
        /*
         * See if the texture has already been registered
         */
        if (documents.containsKey(path))
            return documents.get(path);

        Document dom = IOUtilities.loadXmlFile(path);
        documents.put(path, dom);
        if (dom == null)
            LogMessage.error(TAG, "loadXmlFile", "DOM null");
        return dom;
    }
}
