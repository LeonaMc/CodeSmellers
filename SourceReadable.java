package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;

public interface SourceReadable {
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException;
}
