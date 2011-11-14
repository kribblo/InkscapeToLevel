package se.doktorkapten.levelparser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public abstract class LevelWriter {
    protected LevelData levelData;
    protected CLIOptions options;

    public LevelWriter(LevelData levelData, CLIOptions options) {
        this.levelData = levelData;
        this.options = options;
    }

    public void writeLevel(String path) {
        FileOutputStream output;
        PrintStream ps;
        try {
            output = new FileOutputStream(path, false);
            ps = new PrintStream(output);

            addContent(ps);

            output.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public abstract void addContent(PrintStream ps);
}
