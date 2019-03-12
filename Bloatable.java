package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

// find better name for interface, naming after smells pigeon holes its usage.
public interface Bloatable extends SourceReadable, Reflectable{
    DecimalFormat df = new DecimalFormat("#.00");
    public int countLines(File javaSource) throws FileNotFoundException;
}
