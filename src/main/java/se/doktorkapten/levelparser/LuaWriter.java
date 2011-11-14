package se.doktorkapten.levelparser;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class LuaWriter extends LevelWriter {
    private static final double MIN_LENGTH = 3.0;
    private static final double MIN_ANGLE = 1.0;

    public LuaWriter(LevelData levelData, CLIOptions options) {
        super(levelData, options);
    }

    @Override
    public void addContent(PrintStream ps) {
        addInstructions(ps);

        ps.println("return {");

        ps.println("\twidth = " + levelData.getWidth() + ",");
        ps.println("\theight = " + levelData.getHeight() + ",");

        addPositions(ps);

        if (options.isEdges()) {
            addEdges(ps);

        } else {
            addPolygons(ps);
        }

        ps.println("}");

    }

    /*
     * Code for loading level into lua
     */
    private void addInstructions(PrintStream ps) {
        ps.println("-- Read level file something like this:");
        ps.println("--");
        ps.println("--   local f,e=loadfile(filename)");
        ps.println("--   if f==nil then");
        ps.println("--     print(\"Error loading level: \"..e)");
        ps.println("--     return f,e");
        ps.println("--   else");
        ps.println("--     local levelData = f()");
        ps.println("--     return levelData");
        ps.println("--   end");
        ps.println("--");
        ps.println();
    }

    /*
     * Add positions of objects, if only one object in layer, do not put in
     * array
     */
    private void addPositions(PrintStream ps) {
        HashMap<String, ArrayList<Vertex>> positions = levelData.getPositions();
        for (String name : positions.keySet()) {
            ArrayList<Vertex> vertexList = positions.get(name);
            if (vertexList.size() == 1) {
                Vertex v = vertexList.get(0);
                ps.println("\t" + name + " = {x=" + v.x + ", y=" + v.y + "},");
            } else {
                ps.println("\t" + name + " = {");
                for (Vertex v : vertexList) {
                    ps.println("\t\t{x=" + v.x + ", y=" + v.y + "},");
                }
                ps.println("\t},");
            }
        }

    }

    private void addPolygons(PrintStream ps) {
        HashMap<String, ArrayList<Polygon>> paths = levelData.getPaths();
        for (String name : paths.keySet()) {
            ps.println("\t" + name + " = {");

            for (Polygon p : paths.get(name)) {
                ps.println("\t\t{");
                ArrayList<Vertex> points = p.getVertices();
                for (Vertex v : points) {
                    ps.println("\t\t\t{x=" + (int) v.x + ", y=" + (int) v.y + "},");
                }
                ps.println("\t\t},");
            }
            ps.println("\t}");
        }
    }

    /*
     * Outputs edges, with angles in radians
     * 
     * TODO: add function to output only the points
     * 
     * TODO: calculate edges and angles in Polygon.java, .getEdges()
     * 
     * TODO: optimize polygon in Polygon.java
     */

    private void addEdges(PrintStream ps) {
        int height = levelData.getHeight();
        HashMap<String, ArrayList<Polygon>> paths = levelData.getPaths();
        for (String name : paths.keySet()) {
            ps.println("\t" + name + " = {");

            for (Polygon p : paths.get(name)) {
                ps.println("\t\t{");
                ArrayList<Vertex> points = p.getVertices();

                boolean clockwise = p.polygonIsClockwise(height);

                int size = points.size();
                double lastAngle = -1;
                Vertex saved = null;

                for (int i = 0; i < size; i++) {
                    Vertex start = points.get(i);
                    Vertex stop = points.get((i + 1) % (size)); // loops
                                                                // around
                                                                // to
                                                                // close
                                                                // last+first
                    double x1 = Math.round(start.getX());
                    double y1 = Math.round(height - start.getY());
                    double x2 = Math.round(stop.getX());
                    double y2 = Math.round(height - stop.getY());

                    if (saved != null) {
                        x1 = saved.getX();
                        y1 = saved.getY();
                        saved = null;
                    }

                    double length = Math.abs(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));

                    double angle = Math.atan2(y2 - y1, x2 - x1);

                    if (clockwise) {
                        angle = Math.PI + angle;
                    }
                    if (angle < 0) {
                        angle = Math.PI * 2 + angle;
                    }
                    if (angle > Math.PI * 2) {
                        angle = 0;
                    }
                    double degrees = Math.toDegrees(angle);

                    if (lastAngle >= 0 && Math.abs(lastAngle - degrees) < MIN_ANGLE) {
                        // System.out.println("Removed too small angle");
                        saved = new Vertex(x1, y1);
                        continue;
                    }

                    if (length < MIN_LENGTH) {
                        // System.out.println("Removed too short edge");
                        saved = new Vertex(x1, y1);
                        continue;
                    }
                    ps.println("\t\t\t{x1=" + (int) x1 + ", y1=" + (int) y1 + ", x2=" + (int) x2 + ", y2=" + (int) y2 + ", angle=" + angle + "},");

                    lastAngle = degrees;
                }
                ps.println("\t\t},");
            }
            ps.println("\t},");
        }
    }

}
