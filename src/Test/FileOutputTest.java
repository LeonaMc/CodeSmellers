package Test;

import CodeSmells.DirectoryReader;
import CodeSmells.Inspection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
// Testing output to file for report
public class FileOutputTest {
    public static void main(String[] args) throws IOException {
        String[] packageArray = new String[2];
        DirectoryReader directoryReader = new DirectoryReader();
        String directoryPath = ""; // Add path to root of directory here
        directoryReader.getFiles(directoryPath);

        if(directoryReader.getDirectoryLevel() > 0){
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
        File file = new File("/src/reportLocation/report.txt");
        PrintStream fileWriter = null;
        try {
            fileWriter = new PrintStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.setOut(fileWriter);
        Inspection inspection = new Inspection(loadedClasses,javaSource);
        inspection.runInspection();
    }
}
