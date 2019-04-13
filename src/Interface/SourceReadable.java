package Interface;

import java.io.File;
import java.io.FileNotFoundException;

// interface for reading source files
public interface SourceReadable {
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException;
}
