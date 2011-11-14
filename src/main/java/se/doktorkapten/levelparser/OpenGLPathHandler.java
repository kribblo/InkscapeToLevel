package se.doktorkapten.levelparser;

import java.util.ArrayList;

import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathHandler;

public class OpenGLPathHandler implements PathHandler {
    public final String TAG = this.getClass().getSimpleName();

    private boolean debug = true;

    public ArrayList<Spline> splines = new ArrayList<Spline>();
    public Spline currentSpline;
    private SplineVertex currentPoint;

    public void startPath() throws ParseException {
        LogMessage.log(TAG, "startPath", "", debug);
        currentSpline = new Spline();
    }

    public void endPath() throws ParseException {
        LogMessage.log(TAG, "endPath", "", debug);
        splines.add(currentSpline);
    }

    /**
     * Parses a 'm' command.
     */
    public void movetoRel(float x, float y) throws ParseException {
        LogMessage.log(TAG, "movetoRel", "x:" + x + " y:" + y, debug);

        currentPoint = new SplineVertex(x, y);
        addCurrentPoint();

    }

    /**
     * Parses a 'M' command.
     */
    public void movetoAbs(float x, float y) throws ParseException {
        LogMessage.log(TAG, "movetoAbs", "x:" + x + " y:" + y, debug);

        currentPoint = new SplineVertex(x, y);
        addCurrentPoint();
    }

    public void closePath() throws ParseException {
        LogMessage.log(TAG, "closePath", "", debug);

        currentPoint = currentSpline.getFirst();
        addCurrentPoint();
    }

    /**
     * Parses a 'l' command.
     */
    public void linetoRel(float x, float y) throws ParseException {
        LogMessage.log(TAG, "linetoRel", "x:" + x + " y:" + y, debug);

        currentPoint = currentPoint.add(x, y);
        addCurrentPoint();
    }

    /**
     * Parses a 'L' command.
     */
    public void linetoAbs(float x, float y) throws ParseException {
        LogMessage.log(TAG, "linetoAbs", "x:" + x + " y:" + y, debug);

        currentPoint = new SplineVertex(x, y);
        addCurrentPoint();
    }

    /**
     * Parses a 'h' command.
     */
    public void linetoHorizontalRel(float x) throws ParseException {
        LogMessage.log(TAG, "linetoHorizontalRel", "x:" + x, debug);

        currentPoint = currentPoint.add(x, 0);
        addCurrentPoint();
    }

    /**
     * Parses a 'H' command.
     */
    public void linetoHorizontalAbs(float x) throws ParseException {
        LogMessage.log(TAG, "linetoHorizontalAbs", "x:" + x, debug);
        currentPoint = new SplineVertex(x, currentPoint.p.y);
        addCurrentPoint();
    }

    /**
     * Parses a 'v' command.
     */
    public void linetoVerticalRel(float y) throws ParseException {
        LogMessage.log(TAG, "linetoVerticalRel", "y:" + y, debug);
        currentPoint = currentPoint.add(0, y);
        addCurrentPoint();
    }

    /**
     * Parses a 'V' command.
     */
    public void linetoVerticalAbs(float y) throws ParseException {
        LogMessage.log(TAG, "linetoVerticalAbs", "y:" + y, debug);
        currentPoint = new SplineVertex(currentPoint.p.x, y);
        addCurrentPoint();
    }

    /**
     * Parses a 'c' command.
     */
    public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
        LogMessage.log(TAG, "curvetoCubicRel", "x1:" + x1 + ", y1:" + y1 + ", x2:" + x2 + ", y2:" + y2 + ", x:" + x + ", y:" + y, debug);
        // add control point to previous current point
        currentPoint.cp2 = currentPoint.p.add(x1, y1);

        // create a new current point relative to the last
        currentPoint = new SplineVertex(currentPoint.p.add(x, y), currentPoint.p.add(x2, y2), currentPoint.p.add(x, y));
        addCurrentPoint();
    }

    /**
     * Parses a 'C' command.
     */

    public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
        LogMessage.log(TAG, "curvetoCubicAbs", "x1:" + x1 + ", y1:" + y1 + ", x2:" + x2 + ", y2:" + y2 + ", x:" + x + ", y:" + y, debug);
        // add control point to previous current point
        currentPoint.cp2.set(x1, y1);
        // create a new current point relative to the last
        currentPoint = new SplineVertex(x, y, x2, y2, x, y);
        addCurrentPoint();

    }

    /**
     * Parses a 's' command.
     */

    public void curvetoCubicSmoothRel(float x2, float y2, float x, float y) throws ParseException {
        LogMessage.log(TAG, "curvetoCubicSmoothRel", "x2:" + x2 + ", y2:" + y2 + ", x:" + x + ", y:" + y, debug);

        SplineVertex sv = currentPoint;

        currentPoint.cp2.set(sv.p.x * 2 - sv.cp1.x, sv.p.y * 2 - sv.cp1.y);
        currentPoint = new SplineVertex(currentPoint.p.add(x, y), currentPoint.p.add(x2, y2), currentPoint.p.add(x, y));
        addCurrentPoint();

    }

    /**
     * Parses a 'S' command.
     */

    public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y) throws ParseException {
        LogMessage.log(TAG, "curvetoCubicSmoothAbs", "x2:" + x2 + ", y2:" + y2 + ", x:" + x + ", y:" + y, debug);
        SplineVertex sv = currentPoint;
        currentPoint.cp2.set(sv.p.x * 2 - sv.cp1.x, sv.p.y * 2 - sv.cp1.y);
        currentPoint = new SplineVertex(x, y, x2, y2, x, y);

        addCurrentPoint();

    }

    /**
     * Parses a 'q' command.
     */

    public void curvetoQuadraticRel(float x1, float y1, float x, float y) throws ParseException {
        LogMessage.log(TAG, "curvetoQuadraticRel", "x1:" + x1 + ", y1:" + y1 + ", x2:" + ", x:" + x + ", y:" + y, debug);

    }

    /**
     * Parses a 'Q' command.
     */

    public void curvetoQuadraticAbs(float x1, float y1, float x, float y) throws ParseException {
        LogMessage.log(TAG, "curvetoQuadraticAbs", "x1:" + x1 + ", y1:" + y1 + ", x2:" + ", x:" + x + ", y:" + y, debug);

    }

    /**
     * Parses a 't' command.
     */

    public void curvetoQuadraticSmoothRel(float x, float y) throws ParseException {
        LogMessage.log(TAG, "curvetoQuadratcSmoothRel", "x:" + x + ", y:" + y, debug);

    }

    /**
     * Parses a 'T' command.
     */

    public void curvetoQuadraticSmoothAbs(float x, float y) throws ParseException {
        LogMessage.log(TAG, "curvetoQuadratcSmoothAbs", "x:" + x + ", y:" + y, debug);

    }

    /**
     * Parses a 'a' command.
     */

    public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
        LogMessage.log(TAG, "arcRel", "", debug);

    }

    /**
     * Parses a 'A' command.
     */

    public void arcAbs(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
        LogMessage.log(TAG, "arcAbs", "", debug);

    }

    public void addCurrentPoint() {

        // Apply affine transform
        // currentPoint = transform(currentPoint);

        currentSpline.addPoint(currentPoint);
    }

}
