package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface SourceReadable {
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException;
}
