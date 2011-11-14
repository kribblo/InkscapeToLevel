package se.doktorkapten.levelparser;

import org.apache.batik.parser.ParseException;

public class LengthHandler implements org.apache.batik.parser.LengthHandler {
    public final String TAG = this.getClass().getSimpleName();
    private boolean debug = true;

    public float value;

    public void startLength() throws ParseException {
        LogMessage.log(TAG, "startLength", "", debug);
    }

    public void lengthValue(float v) throws ParseException {
        LogMessage.log(TAG, "lengthValue", "" + v, debug);
        value = v;
    }

    public void em() throws ParseException {
        LogMessage.log(TAG, "em", "", debug);
    }

    public void ex() throws ParseException {
        LogMessage.log(TAG, "ex", "", debug);
    }

    public void in() throws ParseException {
        LogMessage.log(TAG, "in", "", debug);
    }

    public void cm() throws ParseException {
        LogMessage.log(TAG, "cm", "", debug);
    }

    public void mm() throws ParseException {
        LogMessage.log(TAG, "mm", "", debug);
    }

    public void pc() throws ParseException {
        LogMessage.log(TAG, "pc", "", debug);
    }

    public void pt() throws ParseException {
        LogMessage.log(TAG, "pt", "", debug);
    }

    public void px() throws ParseException {
        LogMessage.log(TAG, "px", "", debug);
    }

    public void percentage() throws ParseException {
        LogMessage.log(TAG, "%", "", debug);
    }

    public void endLength() throws ParseException {
        LogMessage.log(TAG, "endLength", "", debug);
    }

}
