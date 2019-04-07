package CodeSmells;

import java.io.File;
import java.io.FileNotFoundException;
// implemented by LineCounter
public interface Countable {
    public int countLines(File javaSource) throws FileNotFoundException; // count lines in a source file
}
