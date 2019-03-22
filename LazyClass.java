package CodeSmellers;

import java.io.*;
import java.util.ArrayList;

public class LazyClass implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<File> javaSource;
    private LineCounter lineCounter;
    private Report lazyClassReport;

    public LazyClass(ArrayList<Class> loadedClasses, ArrayList<File> javaSource){
        this.loadedClasses = new ArrayList<>();
        this.javaSource = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        this.javaSource.addAll(javaSource);
        lazyClassReport = new Report();
        lineCounter = new LineCounter();
    }

    public boolean findSmallClass() throws FileNotFoundException {
        boolean lazyFound = false;
        ArrayList<Class> cleanClasses = new ArrayList<>();
        ArrayList<File> cleanSource = new ArrayList<>();
        for (File file:javaSource){
            if(lineCounter.countLines(file) > 50){
                cleanSource.add(file);
                cleanClasses.add(loadedClasses.get(javaSource.indexOf(file)));
            }
        }
        if(cleanSource.size() > 0){
            javaSource.removeAll(cleanSource);
            lazyFound = true;
        }
        else{
            System.out.println("No Lazy Classes");
        }// add else and put in lazyClassReport its clean for small classes
        if(cleanClasses.size() > 0){
            loadedClasses.removeAll(cleanClasses);
            lazyClassReport.setEffectedClasses(loadedClasses);// catch empty smelly classes in Report
        }
        return lazyFound;
    }

    @Override
    public void reflectClass(){
        for(Class cls : loadedClasses){
            if(cls.getDeclaredMethods().length == 0 && cls.getDeclaredFields().length > 0){
                //System.out.println(cls.getSimpleName() + " is used for data\n");
                String data = cls.getSimpleName() + " is used for data";
                lazyClassReport.setCodeSmellData(cls, data);
            }
            else if(cls.getDeclaredMethods().length > 0 && cls.getDeclaredMethods().length < 5){
                //System.out.println(cls.getSimpleName() + " has too few methods\n");
                String data = cls.getSimpleName() + " has too few methods";
                lazyClassReport.setCodeSmellData(cls, data);
            }
            //add more
        }
    }

    public Report getLazyClassReport(){
        return lazyClassReport;
    }
}
