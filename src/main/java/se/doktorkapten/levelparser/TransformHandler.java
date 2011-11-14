package se.doktorkapten.levelparser;

import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.TransformListHandler;

public class TransformHandler implements TransformListHandler {
    public final String TAG = this.getClass().getSimpleName();

    private boolean debug = false;

    public AffineTransformation at;

    public void startTransformList() throws ParseException {
        LogMessage.log(TAG, "startTransformList", "", debug);
        at = new AffineTransformation();
    }

    public void matrix(float a, float b, float c, float d, float e, float f) throws ParseException {
        LogMessage.log(TAG, "matrix", "a:" + a + ", b:" + b + ", c:" + c + ", d:" + d + ", e:" + e + ", f:" + f, debug);
        at.setTransformation(a, c, e, b, d, f);
    }

    public void rotate(float theta) throws ParseException {
        LogMessage.log(TAG, "rotate", "theta:" + theta, debug);
    }

    public void rotate(float theta, float cx, float cy) throws ParseException {
        LogMessage.log(TAG, "rotate", "theta:" + theta + ", cx:" + cx + ", cy:" + cy, debug);
    }

    public void translate(float tx) throws ParseException {
        LogMessage.log(TAG, "translate", "tx:" + tx, debug);
        at.translate(tx, 0);
    }

    public void translate(float tx, float ty) throws ParseException {
        LogMessage.log(TAG, "translate", "tx:" + tx + ", ty:" + ty, debug);
        at.translate(tx, ty);
    }

    public void scale(float sx) throws ParseException {
        LogMessage.log(TAG, "scale", "sx:" + sx, debug);
    }

    public void scale(float sx, float sy) throws ParseException {
        LogMessage.log(TAG, "scale", "sx:" + sx + ", sy:" + sy, debug);
    }

    public void skewX(float skx) throws ParseException {
        LogMessage.log(TAG, "skewX", "skx:" + skx, debug);
    }

    public void skewY(float sky) throws ParseException {
        LogMessage.log(TAG, "skewY", "sky:" + sky, debug);
    }

    public void endTransformList() throws ParseException {
        LogMessage.log(TAG, "entTransformList", "", debug);
    }

}
