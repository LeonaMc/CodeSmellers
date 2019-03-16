package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] packageArray = new String[2];
        DirectoryReader directoryReader = new DirectoryReader();
        String directoryPath = "/home/johnnymurf/Google Drive/Year 3/Semester2/SoftwareEngineering/test"; // Add path to root of directory here
        directoryReader.getFiles(directoryPath);

        if (DirectoryReader.getDirectoryLevel() > 0) {
            packageArray = directoryReader.getClasspath(directoryReader.getClassArrayList().get(0).getPath());
        } else {
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

        // prints list of loaded classes
        for (Class cls : loadedClasses) {
            //   System.out.println(cls.getName());
        }
            // prints list of java files
            for (File file : javaSource) {
                //  System.out.println(file.getName());
            }
            /*Make class constructors with loadedClasses and javaSource as params.
             * All code smell classes can be done by reading java files as text files from javaSource
             * or by reflecting on classes from loadedClasses or both depending on smell
             * Java source files are represented as File objects so can be treated as a normal text file i.e can use a file scanner to read*/



        FeatureEnvy featureEnvy = new FeatureEnvy(javaSource);
        featureEnvy.findFeatureEnvy();
        featureEnvy.printReport();
    }
}
