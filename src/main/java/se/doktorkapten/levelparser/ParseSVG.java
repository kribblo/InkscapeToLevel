package se.doktorkapten.levelparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.batik.parser.LengthParser;
import org.apache.batik.parser.PathParser;
import org.apache.batik.parser.TransformListParser;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseSVG {
    public final String TAG = this.getClass().getSimpleName();

    // to check for layer names
    private static Pattern p = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]*$");
    // public AffineTransformation at = new AffineTransformation();

    // public ArrayList<Spline> paths = new ArrayList<Spline>();

    // public float height;
    // public float width;

    private String path;
    private LevelData level;
    private Document dom;

    public ParseSVG(String path) throws LevelException {
        this.path = path;

        level = new LevelData();
        ResourceManager res = new ResourceManager();

        //Document dom;
        try {
            dom = res.loadXmlFile(path);
            Element baseElement = dom.getDocumentElement();

            // parse attributes from the SVG tag
            parseSVG(baseElement);
            parseLayers(baseElement);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LevelData getLevel() {
        return level;
    }

    /*
     * Get basic info
     */
    public void parseSVG(Element baseElement) {

        LengthParser lp = new LengthParser();
        LengthHandler lh = new LengthHandler();

        lp.setLengthHandler(lh);

        lp.parse(baseElement.getAttribute("width"));
        level.setWidth((int) lh.value);
        lp.parse(baseElement.getAttribute("height"));
        level.setHeight((int) lh.value);

    }

    private static boolean match(String s) {
        return p.matcher(s).matches();
    }

    /*
     * Walk all layers, build the level data
     */
    public void parseLayers(Element baseElement) throws LevelException {

        NodeList nl = baseElement.getElementsByTagName("g");

        // System.out.println(baseElement.getNodeName());
        for (int i = 0; i < nl.getLength(); i++) {
            Element el = (Element) nl.item(i);

            // find document layers
            if (!el.getAttribute("inkscape:groupmode").equals("layer") || !el.getParentNode().equals(baseElement)) {
                continue;
            }
            String label = el.getAttribute("inkscape:label");
            if (!match(label)) {
                throw new LevelException("Layer '" + label + "' has invalid name, must be within [a-zA-Z0-9_].");
            }

            String transform = el.getAttribute("transform");

            // base transform of each layer
            AffineTransformation aft = new AffineTransformation();
            aft.scale(1, -1);
            aft.translate(0, level.getHeight());
            aft.composeBefore(parseTransform(transform));

            ArrayList<Polygon> paths = extractPaths(el, aft);
            ArrayList<Vertex> positions = extractPositions(el, aft);
            if (paths.size() > 0 && positions.size() > 0) {
                throw new LevelException("Layer '" + label + "' has both paths and objects, must not mix.");
            }

            if (paths.size() > 0) {
                level.addPaths(label, paths);
            }
            if (positions.size() > 0) {
                level.addPositions(label, positions);                
            }

        }

    }

    /*
     * Positions of groups <g>
     */
    private ArrayList<Vertex> extractPositions(Element baseElement, AffineTransformation aft) {
        ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
        NodeList imageNodes = baseElement.getElementsByTagName("image");
        NodeList rectNodes = baseElement.getElementsByTagName("rect");
        getPosition(baseElement, vertexList, imageNodes);
        getPosition(baseElement, vertexList, rectNodes);

        /*
         * NodeList objectNodes = baseElement.getElementsByTagName("g");
         * 
         * for (int i = 0; i < objectNodes.getLength(); i++) { Element el =
         * (Element) objectNodes.item(i);
         * 
         * // only immediate children if
         * (!el.getParentNode().equals(baseElement)) { continue; } String
         * transform = el.getAttribute("transform"); Vertex v = new Vertex(0.0,
         * 0.0).transform(parseTransform(transform)).transform(aft); v.y =
         * level.getHeight() - v.y; vertexList.add(v); //
         * System.out.println((v.x) + "x" + v.y); }
         */
        return vertexList;
    }

    private void getPosition(Element baseElement, ArrayList<Vertex> vertexList, NodeList imageNodes) {
        for (int i = 0; i < imageNodes.getLength(); i++) {
            Element el = (Element) imageNodes.item(i);
            double x = Double.parseDouble(el.getAttribute("x"));
            double y = Double.parseDouble(el.getAttribute("y"));
            Vertex v = new Vertex(x,y);
            
            // Walk back up through potential parents (groups) and apply transforms
            // Works for simple translates at least, may not for more complex things
            Node parent = el;
            while ((parent = parent.getParentNode()) != null) {
                if(parent instanceof Element) {
                    String transform = ((Element)parent).getAttribute("transform");
                    v = v.transform(parseTransform(transform));
                }
            }
            vertexList.add(v);
        }
    }

    /*
     * Paths <path>
     */
    private ArrayList<Polygon> extractPaths(Element baseElement, AffineTransformation aft) {
        // System.out.println(label);
        ArrayList<Polygon> paths = new ArrayList<Polygon>();
        NodeList pathNodes = baseElement.getElementsByTagName("path");

        // Path parser
        OpenGLPathHandler ph = new OpenGLPathHandler();
        PathParser pp = new PathParser();
        pp.setPathHandler(ph);

        for (int i = 0; i < pathNodes.getLength(); i++) {
            Element el = (Element) pathNodes.item(i);

            // only immediate children
            //if (!el.getParentNode().equals(baseElement)) {
            //    continue;
            //}
            
            // Get path data
            String path = el.getAttribute("d");
            // LogMessage.print(path);
            pp.parse(path);

            // Get transformation data
            String transform = el.getAttribute("transform");

            // Transform the resulting spline

            Spline currentSpline = ph.currentSpline;
            currentSpline.transform(parseTransform(transform));
            currentSpline.transform(aft);

            // See Spline.java, refines the curves to polygon
            currentSpline.refine(0.1f);

            // paths.add(currentSpline);
            // System.out.println(transform);

            Polygon polygon = new Polygon(currentSpline.getVertices());
            paths.add(polygon);

        }
        return paths;
    }

    private AffineTransformation parseTransform(String transform) {
        TransformHandler th = new TransformHandler();
        TransformListParser tlp = new TransformListParser();

        tlp.setTransformListHandler(th);
        tlp.parse(transform);

        return th.at;
    }

    public void toPNG(String filename) throws TranscoderException, IOException {

        PNGTranscoder t = new PNGTranscoder();

        // Set the transcoding hints.
        // t.addTranscodingHint(PNGTranscoder.KEY_QUALITY,
        // new Float(.8));

        // Create the transcoder input.
        String svgURI = new File(path).toURI().toURL().toString();
        TranscoderInput input = new TranscoderInput(svgURI);

        // Create the transcoder output.
        OutputStream ostream;
        ostream = new FileOutputStream(filename);
        TranscoderOutput output = new TranscoderOutput(ostream);

        // Save the image.
        t.transcode(input, output);

        // Flush and close the stream.
        ostream.flush();
        ostream.close();
        // System.exit(0);

    }

    /*
     * public void parseGlobalTransform(Element baseElement) { // Get graphics
     * transform NodeList nl = baseElement.getElementsByTagName("g");
     * 
     * Element gTag = (Element) nl.item(0); //
     * System.out.println(gTag.getAttribute("id")); String transform =
     * gTag.getAttribute("transform"); // System.out.println(transform);
     * 
     * at.composeBefore(parseTransform(transform)); }
     */

    /*
     * public void parsePaths(Element baseElement) {
     * 
     * NodeList nl = baseElement.getElementsByTagName("path");
     * 
     * // Path parser OpenGLPathHandler ph = new OpenGLPathHandler(); PathParser
     * pp = new PathParser(); pp.setPathHandler(ph);
     * 
     * for (int i = 0; i < nl.getLength(); i++) { Element elm = (Element)
     * nl.item(i);
     * 
     * // Get path data String path = elm.getAttribute("d");
     * //LogMessage.print(path); pp.parse(path);
     * 
     * // Get transformation data String transform =
     * elm.getAttribute("transform");
     * 
     * // Transform the resulting spline
     * ph.currentSpline.transform(parseTransform(transform));
     * addPath(ph.currentSpline); } }
     */

    /*
     * public void addPath(Spline s) { // Apply global transform
     * s.transform(at); paths.add(s); }
     */

}
