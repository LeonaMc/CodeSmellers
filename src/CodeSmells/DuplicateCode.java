package CodeSmells;

import java.io.File;
import java.util.ArrayList;
// NO IMPLEMENTATION
public class DuplicateCode {
    ArrayList<Class> loadedClasses;
    ArrayList<File> javaSource;
    public DuplicateCode(ArrayList<Class> loadedClasses, ArrayList<File> javaSource){
        this.javaSource = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.javaSource.addAll(javaSource);
        this.loadedClasses.addAll(loadedClasses);
    }
}
