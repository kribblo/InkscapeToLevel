package se.doktorkapten.levelparser;

/**
 * TODO Error class-level Javadoc.
 * 
 * <p>
 * ...
 * </p>
 * 
 * @author Copyright (c) CHP Consulting Ltd. 2010
 */
public class LogMessage {

    /**
     * @uml.property name="loggingEnabled"
     */
    public boolean loggingEnabled = false;

    public static void error(String TAG, String function, String desc) {
        System.out.print("Error - " + TAG + ":" + function + "," + desc + "\n");
    }

    public static void log(String TAG, String function, String desc) {
        LogMessage m = new LogMessage();
        if (m.loggingEnabled == true)
            System.out.print("Log - " + TAG + ":" + function + "," + desc + "\n");
    }

    public static void log(String TAG, String function, String desc, boolean debug) {
        LogMessage m = new LogMessage();
        if (m.loggingEnabled == true)
            if (debug == true)
                System.out.print("Log - " + TAG + ":" + function + "," + desc + "\n");
    }

    public static void print(String s) {
        System.out.print(s + "\n");
    }
}
