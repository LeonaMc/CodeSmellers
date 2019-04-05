package CodeSmellers;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

// need to implement better data return
public class LargeClass implements Inspectable {
    private ArrayList<Class> loadedClasses; // All classes to be inspected, loaded for reflection
    private ArrayList<File> javaSource; // all source files from project being inspected
    private LineCounter lineCounter;
    private HashMap<File, Integer> fileLength;
    private Report report; // stores results of inspection

    LargeClass(ArrayList<File> javaSource, ArrayList<Class> loadedClasses){
        this.javaSource = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.javaSource.addAll(javaSource);
        this.loadedClasses.addAll(loadedClasses);
        fileLength = new HashMap<>();
        lineCounter = new LineCounter();
        report = new Report();
    }

    @Override // no implementation for this class?
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        return null;
    }
    // determines if class suffers from large class code smell
    // when loop is finished only effected classes and java source files will be left in each ArrayList
    public void findLargeFiles(){
        ArrayList<Class> cleanClasses = new ArrayList<>();
        ArrayList<File> cleanSource = new ArrayList<>();
        for (File file : javaSource){ // for each javaSource file
            try {
                int upperBound = 150; // heuristic
                int fileSize = lineCounter.countLines(file);
                if((fileSize > upperBound)){ // files large
                    fileLength.put(file,fileSize); // put size of file
                }
                else{
                    cleanClasses.add(loadedClasses.get(javaSource.indexOf(file)));
                    cleanSource.add(file);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        loadedClasses.removeAll(cleanClasses);
        javaSource.removeAll(cleanSource);
        report.setEffectedClasses(loadedClasses); // add list of effected classes to report
    }

    @Reflecting
    @Override
    public void reflectClass() {
        for (Class cls : loadedClasses){
            String reportMessage = "\nClass " + cls.getSimpleName() + " has length of " + fileLength.get(javaSource.get(loadedClasses.indexOf(cls))) + "\n";

            if(cls.getDeclaredMethods().length > 10){ // placeholder value, not sure how many methods is too many
                reportMessage += "Possible cause for large file is\n" + cls.getSimpleName() + " has " + cls.getDeclaredMethods().length + " Methods\n";
            }
            if (cls.getDeclaredFields().length > 10){
                reportMessage += "Possible cause for large file is\n" + cls.getSimpleName() + " has " + cls.getDeclaredFields().length + " Declared Fields\n";
            }
            report.putCodeSmellData(cls,reportMessage);
        }
    }
    // returns report
    public Report getReport() {
        return report;
    }
}
