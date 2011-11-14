package se.doktorkapten.levelparser;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FilenameUtils;

import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.Cli;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

import java.io.*;

public class InkscapeToLevel {

    static float width;
    static float height;

    public static void main(String[] args) {
        try {
            Cli<CLIOptions> cli = CliFactory.createCli(CLIOptions.class);
            CLIOptions options = cli.parseArguments(args);

            if (options.getFiles() == null || options.getFiles().size() != 1) {
                System.err.println("Needs exactly one input file");
                System.err.println(cli.getHelpMessage());
                System.exit(1);
            }

            String filePath = options.getFiles().get(0);

            String basenameAndPath = FilenameUtils.removeExtension(filePath);

            ParseSVG parser;
            parser = new ParseSVG(filePath);
            
            // TODO: more writers, add commandline options for it
            LevelWriter lw = new LuaWriter(parser.getLevel(), options);

            String outputPath = basenameAndPath + ".level";
            lw.writeLevel(outputPath);
            System.out.println("Wrote: " + outputPath);

            if (options.isImage()) {
                String imageFilename = basenameAndPath + ".png";
                writeImage(imageFilename, parser);
                System.out.println("Wrote: " + imageFilename);
            }

        } catch (LevelException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (ArgumentValidationException e) {
            System.err.println(e.getMessage());
            System.exit(1);

        }

    }

    /*
     * Output level as PNG.
     * TODO: hide visibility on position layers first, if possible
     */
    private static void writeImage(String imageFilename, ParseSVG parser) {
        try {
            parser.toPNG(imageFilename);
        } catch (TranscoderException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
