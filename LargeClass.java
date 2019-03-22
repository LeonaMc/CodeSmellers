package CodeSmellers;

import java.io.*;
import java.util.ArrayList;

// Refactor Class to take class and java arrays in constructor, give length of file in report
public class LargeClass implements Inspectable {
    private ArrayList<Class> loadedClasses;
    private ArrayList<File> javaSource; // may not need this!
    private int numberOfSourceFiles;
    private LineCounter lineCounter;
    private Report report;

    LargeClass(ArrayList<File> javaSource, ArrayList<Class> loadedClasses){
        this.javaSource = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.javaSource.addAll(javaSource);
        this.loadedClasses.addAll(loadedClasses);
        lineCounter = new LineCounter();
        report = new Report();
        numberOfSourceFiles = javaSource.size();
    }

    @Override // no implementation for this class?
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        return null;
    }

    public void findLargeFiles(){
        for (int i = 0; i < javaSource.size(); i++){
            try {
                int upperBound = 200;
                if(!(lineCounter.countLines(javaSource.get(i)) > upperBound)){ // files not large
                    loadedClasses.remove(i);
                    javaSource.remove(i);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        report.setEffectedClasses(loadedClasses);
    }

    @Reflecting
    @Override
    public void reflectClass() {
        // if long class, reflect and find ways to improve e.g. count number of primitives and methods advise on results
        for (Class cls : loadedClasses){
            if(cls.getDeclaredMethods().length > 10){ // placeholder value, not sure how many methods is too many
                String reportMessage = cls.getSimpleName() + " has " + cls.getDeclaredMethods().length + " Methods";
                report.getCodeSmellData().put(cls,reportMessage); // need better report messages
            }
            if (cls.getDeclaredFields().length > 10){
                String reportMessage = cls.getSimpleName() + " has " + cls.getDeclaredFields().length + " Declared Fields";
                report.setCodeSmellData(cls,reportMessage);
            }
            // if < 10 fields or methods indicates long method smell
        }
    }

    public Report getReport() {
        return report;
    }

    public void printTestReport(){
        double bloated = 0;
        for (Class cls: loadedClasses){
            String newline = "\n";
            System.out.println("Java Class: " + cls.getSimpleName() + " is too large " + newline);
           //for(String report: classReports.get(cls)){
           //    System.out.println(report + newline);
           //}
            bloated++;
        }
        double percent = (bloated/numberOfSourceFiles) * 100;
        System.out.println(Inspectable.df.format(percent) + "% of the files in your project are bloated");
    }
}
