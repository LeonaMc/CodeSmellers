package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;

public interface Countable {
    public int countLines(File javaSource) throws FileNotFoundException;
}
