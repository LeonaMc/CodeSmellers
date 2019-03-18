package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String[] packageArray = new String[2];
        DirectoryReader directoryReader = new DirectoryReader();
        String directoryPath = "C:\\Users\\RickTheRuler\\Dropbox\\SoftwareEngineering2"; // Add path to root of directory here

        directoryReader.getFiles(directoryPath);

        if(directoryReader.getDirectoryLevel() > 0){
            packageArray = directoryReader.getClasspath(directoryReader.getClassArrayList().get(0).getPath());
        }
        else{
            packageArray[0] = null;
            packageArray[1] = directoryPath;
        }

        System.out.println(packageArray[0] + " " + packageArray[1]);

        try {
            directoryReader.loadClasses(packageArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Class> loadedClasses = new ArrayList<>(directoryReader.getLoadedClasses()); // classes ready for reflection
        ArrayList<File> javaSource = new ArrayList<>(directoryReader.getJavaSourceArrayList()); // can read java files as text

        /*Bloat Tests
        * Test for Large Classes*/
        System.out.println("==========================Test Large Class==========================");
        FindLargeClass findLargeClasses = new FindLargeClass(javaSource,loadedClasses);
        findLargeClasses.findLargeFiles();
        findLargeClasses.reflectClass();
        findLargeClasses.printTestReport();

        // Test for Long Methods
        System.out.println("==========================Test Long Method==========================");
        System.out.println("\n");
        FindLongMethods findLongMethods = new FindLongMethods(loadedClasses, javaSource);
        System.out.println("\nMethod Body From Find LongMethods");
        findLongMethods.reflectClass();

        // Test for primitive obsession
        System.out.println("==========================Test Primitive Obsession==========================");
        PrimitiveObsession primitiveObsession = new PrimitiveObsession(loadedClasses);
        primitiveObsession.reflectClass();
        primitiveObsession.printTestReport();

        // Test for long param list for methods
        System.out.println("==========================Test Long Method Param List==========================");
        FindLongParamList longParamList = new FindLongParamList(loadedClasses);
        longParamList.reflectClass();
        longParamList.printTestReport();
    }
}
