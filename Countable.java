package CodeSmellers;

import java.io.FileNotFoundException;

public interface Countable {
    public int countComponents(Class cls) throws FileNotFoundException; // counts parameters, primitives, lines in methods or number of parameters
}
