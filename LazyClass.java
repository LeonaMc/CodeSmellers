package CodeSmellers;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

// need to better implement whole class
public class LazyClass implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<File> javaSource;
    private ArrayList<Class> cleanClasses;
    private LineCounter lineCounter;
    private Report report;

    public LazyClass(ArrayList<Class> loadedClasses, ArrayList<File> javaSource){
        this.loadedClasses = new ArrayList<>(loadedClasses);
        this.javaSource = new ArrayList<>(javaSource);
        cleanClasses = new ArrayList<>();
        report = new Report();
        lineCounter = new LineCounter();
    }
    // Add interface and annotation to clean classes
    private void findSmallClass() throws FileNotFoundException {

        ArrayList<File> cleanSource = new ArrayList<>();
        for (File file:javaSource){ // for each source file
            if(lineCounter.countLines(file) > 300){ // if num of lines more than 50
                cleanSource.add(file); // file is clean
                cleanClasses.add(loadedClasses.get(javaSource.indexOf(file))); // add files .class file to clean array
            }
        }
        if(cleanSource.size() > 0){ // if clean source not empty
            javaSource.removeAll(cleanSource); // remove all clean files, java source now holds only affected source
        }
        if(cleanClasses.size() > 0){
            loadedClasses.removeAll(cleanClasses);
            report.setAffectedClasses(loadedClasses); // catch empty affected classes in Report
        }
    }
    // if class found to be small this method attempts to find if it is referenced in many classes which may be an indication it is not a lazy class
    private boolean findReference(Class cls, Class innerClass){ // needs more testing, not working as it should be
        boolean foundReference = false;
        for (Field field: innerClass.getDeclaredFields()){
            if (field.getType().getSimpleName().compareTo(cls.getSimpleName()) == 0){
                System.out.println(field.getType().getSimpleName()+" "+cls.getSimpleName()+" "+innerClass.getSimpleName());
            }
            if(Modifier.isPrivate(field.getModifiers())){
                field.setAccessible(true);
                if (field.getType().equals(cls)){
                    foundReference = true;
                }
            }
            else if (field.getType().equals(cls)){
                foundReference = true;
            }
        }
        return foundReference;
    }

    @Override
    public void reflectClass(){
        try {
            findSmallClass();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Class> tempList = new ArrayList<>();
        tempList.addAll(loadedClasses);
        HashMap<Class,Class> classReferenceMap = new HashMap<>();
        for(Class cls: loadedClasses){
            for(Class innerCls: tempList){
                if (findReference(cls,innerCls)){
                    classReferenceMap.put(cls,innerCls);
                }
            }
        }

        for(Class cls: loadedClasses){
            System.out.println("Classes with references to " + cls.getSimpleName() + "\n");
            if(classReferenceMap.containsKey(cls)){
                System.out.println(classReferenceMap.values().toString()+"\n");
            }
        }

        for(Class cls : loadedClasses){
            if(cls.getDeclaredMethods().length == 0 && cls.getDeclaredFields().length > 0){
                String data = cls.getSimpleName() + " is used for data";
                report.putReportData(cls, data);
            }
            else if(cls.getDeclaredMethods().length > 0 && cls.getDeclaredMethods().length < 5){
                String data = cls.getSimpleName() + " has too few methods";
                report.putReportData(cls, data);
            }
        }
    }

    public Report getReport(){
        return report;
    }
}
