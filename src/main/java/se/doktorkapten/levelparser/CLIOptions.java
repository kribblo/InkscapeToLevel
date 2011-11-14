package se.doktorkapten.levelparser;

import java.util.List;

import uk.co.flamingpenguin.jewel.cli.Option;
import uk.co.flamingpenguin.jewel.cli.Unparsed;

public interface CLIOptions {

    @Option(defaultValue = "lua", description="output format (default: lua)")
    String getFormat();

    @Option(description="create PNG image from SVG")
    boolean isImage();

    @Option(description="output edges instead of polygons for paths")
    boolean isEdges();

    @Unparsed
    List<String> getFiles();
    
    @Option(helpRequest = true, shortName = "h")
    boolean getHelp();
}
