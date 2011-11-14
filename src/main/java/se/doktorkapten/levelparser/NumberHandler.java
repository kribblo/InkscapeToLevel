package se.doktorkapten.levelparser;

import org.apache.batik.parser.NumberListHandler;
import org.apache.batik.parser.ParseException;

public class NumberHandler implements NumberListHandler {
    public final String TAG = this.getClass().getSimpleName();

    float value;

    public void startNumberList() throws ParseException {
        LogMessage.log(TAG, "startNumberList", "");
    }

    public void endNumberList() throws ParseException {
        LogMessage.log(TAG, "endNumberList", "");
    }

    public void startNumber() throws ParseException {
        LogMessage.log(TAG, "startNumber", "");

    }

    public void endNumber() throws ParseException {
        LogMessage.log(TAG, "endNumber", "");
    }

    public void numberValue(float v) throws ParseException {
        LogMessage.log(TAG, "numberValue", "" + v);
    }

}
