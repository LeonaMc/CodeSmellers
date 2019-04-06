package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
// interface for reading source files
public interface SourceReadable {
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException;
}
