package CodeSmellers;

import java.io.*;
import java.util.ArrayList;
// need to better implement whole class
public class LazyClass implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<File> javaSource;
    private LineCounter lineCounter;
    private Report report;

    public LazyClass(ArrayList<Class> loadedClasses, ArrayList<File> javaSource){
        this.loadedClasses = new ArrayList<>();
        this.javaSource = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        this.javaSource.addAll(javaSource);
        report = new Report();
        lineCounter = new LineCounter();
    }

    public boolean findSmallClass() throws FileNotFoundException {
        boolean lazyFound = false;
        ArrayList<Class> cleanClasses = new ArrayList<>();
        ArrayList<File> cleanSource = new ArrayList<>();
        for (File file:javaSource){ // for each source file
            if(lineCounter.countLines(file) > 50){ // if num of lines not below 50
                cleanSource.add(file); // file is clean
                cleanClasses.add(loadedClasses.get(javaSource.indexOf(file))); // add files .class file to clean array
            }
        }
        if(cleanSource.size() > 0){ // if clean source not empty
            javaSource.removeAll(cleanSource); // remove all clean files, java source now holds only affected source
            lazyFound = true; //
        }
        else{
            System.out.println("No Lazy Classes");
        }// add else and put in report its clean for small classes
        if(cleanClasses.size() > 0){
            loadedClasses.removeAll(cleanClasses);
            report.setAffectedClasses(loadedClasses);// catch empty affected classes in Report
        }
        return lazyFound;
    }

    @Override
    public void reflectClass(){
        for(Class cls : loadedClasses){
            if(cls.getDeclaredMethods().length == 0 && cls.getDeclaredFields().length > 0){
                //System.out.println(cls.getSimpleName() + " is used for data\n");
                String data = cls.getSimpleName() + " is used for data";
                report.putReportData(cls, data);
            }
            else if(cls.getDeclaredMethods().length > 0 && cls.getDeclaredMethods().length < 5){
                //System.out.println(cls.getSimpleName() + " has too few methods\n");
                String data = cls.getSimpleName() + " has too few methods";
                report.putReportData(cls, data);
            }
            //add more
        }
    }

    public Report getReport(){
        return report;
    }
}
