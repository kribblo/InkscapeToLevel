package se.doktorkapten.levelparser;

public class SplineVertex {

    public Vertex cp1;
    public Vertex p;
    public Vertex cp2;

    // For cubic splines
    public SplineVertex(float x, float y, float x1, float y1, float x2, float y2) {
        construct(x, y, x1, y1, x2, y2);
    }

    public SplineVertex(Vertex p, Vertex cp1, Vertex cp2) {
        construct(p, cp1, cp2);
    }

    // For straight lines
    public SplineVertex(float x, float y) {
        construct(x, y, x, y, x, y);
    }

    public SplineVertex(Vertex p) {
        construct(p, p, p);
    }

    public void construct(float x, float y, float x1, float y1, float x2, float y2) {
        this.cp1 = new Vertex(x1, y1);
        this.p = new Vertex(x, y);
        this.cp2 = new Vertex(x2, y2);
    }

    public void construct(Vertex p, Vertex cp1, Vertex cp2) {
        this.cp1 = cp1;
        this.p = p;
        this.cp2 = cp2;
    }

    public SplineVertex add(Vertex v) {
        return new SplineVertex(v.add(p));
    }

    public SplineVertex add(float x, float y) {
        return new SplineVertex(p.add(x, y));
    }

    public SplineVertex transform(AffineTransformation at) {
        return new SplineVertex(p.transform(at), cp1.transform(at), cp2.transform(at));
    }

}
