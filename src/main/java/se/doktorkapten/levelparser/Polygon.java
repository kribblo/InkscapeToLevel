package se.doktorkapten.levelparser;

import java.util.ArrayList;
import java.util.HashMap;

public class Polygon {
    private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

    public Polygon(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
        // removeDuplicatePoint();
        sortPolygon();
    }

    protected void sortPolygon() {
        HashMap<Vertex, Vertex> mapper = new HashMap<Vertex, Vertex>();
        int i;
        int size = vertices.size();
        for (i = 0; i < size; i++) {
            Vertex start = vertices.get(i);
            Vertex stop = vertices.get((i + 1) % (size));

            // Zero-length vertex
            if (start.equals(stop)) {
                //System.out.println("Removed zero-length vertex");
                continue;
            }
            mapper.put(start, stop);
        }

        Vertex point = vertices.get(0);

        vertices.clear();
        do {
            vertices.add(point);
            point = mapper.remove(point);
        } while (point != null);

        removeDuplicatePoint();
    }

    private void removeDuplicatePoint() {
        // Sanity check
        int last = vertices.size() - 1;
        if (vertices.get(0).equals(vertices.get(last))) {
            //System.out.println("Removed duplicate point");
            vertices.remove(last);
        }
    }

    public boolean polygonIsClockwise(double levelHeight) {
        int i;
        double area = 0;
        int size = vertices.size();
        for (i = 0; i < size; i++) {
            Vertex start = vertices.get(i);
            Vertex stop = vertices.get((i + 1) % (size));
            double x1 = (start.getX());
            double y1 = (levelHeight - start.getY());
            double x2 = (stop.getX());
            double y2 = (levelHeight - stop.getY());

            area += x1 * y2;
            area -= y1 * x2;
        }
        area /= 2.0;

        return (area < 0);
    }

    /*
     * public void setVertices(ArrayList<Vertex> vertices) { this.vertices =
     * vertices; }
     */

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

}
