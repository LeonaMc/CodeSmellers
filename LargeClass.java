package CodeSmellers;

import java.io.*;
import java.util.ArrayList;

public class LargeClass implements Inspectable {
    private ArrayList<Class> loadedClasses; // All classes to be inspected, loaded for reflection
    private ArrayList<File> javaSource; // all source files from project being inspected
    private LineCounter lineCounter;
    private Report report; // stores results of inspection

    LargeClass(ArrayList<File> javaSource, ArrayList<Class> loadedClasses){
        this.javaSource = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.javaSource.addAll(javaSource);
        this.loadedClasses.addAll(loadedClasses);
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
        for (int i = 0; i < javaSource.size(); i++){ // iterate through each element of javaSource
            try {
                int upperBound = 200; // heuristic
                if(!(lineCounter.countLines(javaSource.get(i)) > upperBound)){ // files not large
                    loadedClasses.remove(i); // remove clean class
                    javaSource.remove(i); // remove clean file
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        report.setEffectedClasses(loadedClasses); // add list of effected classes to report
    }

    @Reflecting
    @Override
    public void reflectClass() {
        // if long class, reflect and find ways to improve e.g. count number of primitives and methods advise on results. If class large and has long methods
        for (Class cls : loadedClasses){
            if(cls.getDeclaredMethods().length > 10){ // placeholder value, not sure how many methods is too many
                String reportMessage = cls.getSimpleName() + " has " + cls.getDeclaredMethods().length + " Methods";
                report.getCodeSmellData().put(cls,reportMessage); // need better report messages
            }
            if (cls.getDeclaredFields().length > 10){
                String reportMessage = cls.getSimpleName() + " has " + cls.getDeclaredFields().length + " Declared Fields";
                report.putCodeSmellData(cls,reportMessage);
            }
        }
    }
    // returns report
    public Report getReport() {
        return report;
    }
}
