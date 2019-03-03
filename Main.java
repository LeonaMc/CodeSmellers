package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String[] packageArray = new String[2];
        DirectoryReader directoryReader = new DirectoryReader();
        String directoryPath = ""; // Add path to root of directory here
        directoryReader.getFiles(directoryPath);

        if(DirectoryReader.getDirectoryLevel() > 0){
            packageArray = directoryReader.getClasspath(directoryReader.getClassArrayList().get(0).getPath());
        }
        else{
            packageArray[0] = null;
            packageArray[1] = directoryPath;
        }

        try {
            directoryReader.loadClasses(packageArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Class> loadedClasses = new ArrayList<>(directoryReader.getLoadedClasses()); // classes ready for reflection
        ArrayList<File> javaSource = new ArrayList<>(directoryReader.getJavaSourceArrayList()); // can read java files as text

        /*Make class constructors with loadedClasses and javaSource as params.
        * All code smell classes can be done by reading java files as text files from javaSource
        * or by reflecting on classes from loadedClasses or both depending on smell
        * */
    }
}
