package CodeSmells;

import Annotation.Reflecting;
import Interface.Smellable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

// need to implement better data return
public class LargeClass implements Smellable {
    private ArrayList<Class> loadedClasses; // All classes to be inspected, loaded for reflection
    private ArrayList<File> javaSource; // all source files from project being inspected
    private LineCounter lineCounter;
    private HashMap<File, Integer> fileLength;
    private Report report; // stores results of inspection

    public LargeClass(ArrayList<File> javaSource, ArrayList<Class> loadedClasses){
        this.javaSource = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.javaSource.addAll(javaSource);
        this.loadedClasses.addAll(loadedClasses);
        fileLength = new HashMap<>();
        lineCounter = new LineCounter();
        report = new Report();
        report.setName("Large Class");
    }

    @Override // no implementation for this class?
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        return null;
    }
    // determines if class suffers from large class code smell
    // when loop is finished only affected classes and java source files will be left in each ArrayList
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
                    int length = file.getName().length();
                    String javaName = file.getName().substring(0,length-5);
                    int index = 0;
                    for(Class cls : loadedClasses){
                        if(cls.getSimpleName().compareTo(javaName) == 0 ){
                            index = loadedClasses.indexOf(cls);
                            break;
                        }
                    }
                    cleanClasses.add(loadedClasses.get(index));
                    cleanSource.add(file);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        loadedClasses.removeAll(cleanClasses);
        //loadedClasses.remove(/CodeSmells.Main.class);
        javaSource.removeAll(cleanSource);
        report.setAffectedClasses(loadedClasses); // add list of affected classes to report
    }

    @Reflecting
    @Override
    public void reflectClass() {
        for (Class cls : loadedClasses){
            String className = cls.getSimpleName();
            int index = 0;
            for(File file : javaSource){
                int length = file.getName().length();
                if(file.getName().substring(0,length-5).compareTo(className) == 0 ){
                    index = javaSource.indexOf(file);
                    break;
                }
            }
            String reportMessage = "\nClass " + cls.getSimpleName() + " has length of " + fileLength.get(javaSource.get(index));

            if(cls.getDeclaredMethods().length > 5){ // placeholder value, not sure how many methods is too many
                reportMessage += "\nPossible cause for large file is\n" + cls.getSimpleName() + " has " + cls.getDeclaredMethods().length + " Methods";
            }
            if(cls.getDeclaredMethods().length > 0 && cls.getDeclaredMethods().length <= 5){
                reportMessage += "\nPossible cause for large file is\n" + cls.getSimpleName() + " has only " + cls.getDeclaredMethods().length + " Methods\n" +
                        "Which may indicate long methods\n";
            }
            if (cls.getDeclaredFields().length > 5){
                reportMessage += "\nPossible cause for large file is\n" + cls.getSimpleName() + " has " + cls.getDeclaredFields().length + " Declared Fields";
            }
            report.putReportData(cls,reportMessage);
        }
    }
    // returns report
    public Report getReport() {
        return report;
    }
}
