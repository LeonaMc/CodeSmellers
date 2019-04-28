package CodeSmells;

import Interface.Reflectable;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

// need to better implement whole class
public class LazyClass implements Reflectable {
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
        report.setName("Lazy Class");
        lineCounter = new LineCounter();
    }

    // Add interface and annotation to clean classes
    private void findSmallClass() throws FileNotFoundException {
        ArrayList<File> cleanSource = new ArrayList<>();
        for(Class cls: loadedClasses){
            if (cls.isAnnotation() || cls.isInterface()){
                cleanClasses.add(cls);
            }
        }
        for (File file:javaSource){ // for each source file
            if(lineCounter.countLines(file) > 100){ // if num of lines more than 50
                cleanSource.add(file); // file is clean
                int length = file.getName().length();
                String javaName = file.getName().substring(0,length-5);
                int index = 0;
                for(Class cls : loadedClasses){
                    if(cls.getSimpleName().compareTo(javaName) == 0 ){
                        index = loadedClasses.indexOf(cls);
                        break;
                    }
                }
                cleanClasses.add(loadedClasses.get(index)); // add files .class file to clean array
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
    private boolean findReference(Class cls, Class innerClass){
        boolean foundReference = false;
        for (Field field: innerClass.getDeclaredFields()){ // check fields for field of type cls, means class object is used in other classes
            if(Modifier.isPrivate(field.getModifiers())){
                field.setAccessible(true);
                if (field.getType().equals(cls) && !cls.getClass().equals(innerClass)){
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
            findSmallClass(); // running this method will remove class with length greater than threshold from both source list and class list
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Class> tempList = new ArrayList<>(); // coming back now and reading the code I can't remember why I made an extra list. Maybe for the inner loop?
        // after findSmallClass() loadedClasses only holds classes with source under 150 lines
        tempList.addAll(loadedClasses); // add all flagged classes to tempo list
        HashMap<Class,ArrayList<String>> classReferenceMap = new HashMap<>();
        String data;
        for(Class cls: loadedClasses){ // for each class
            ArrayList<String> className = new ArrayList<>(); // will hold names of classes which create objects of type cls
            for(Class checkReferences: tempList){  // for each class in tempList
                if (findReference(cls,checkReferences)){ // if checkReference class has objects of type cls cls may not be lazy
                    className.add(checkReferences.getSimpleName()); // add name of class
                    classReferenceMap.put(cls,className); // map all class names that reference cls to cls
                }
            }
            // start of data for report
            // classes found with under 150 lines are flagged but are not necessarily lazy, further inspection needed
            data = "\nClass " + cls.getSimpleName() + " has been flagged as a lazy class\n"; //
            ArrayList<Method> publicMethods = new ArrayList<>();
            if(cls.getDeclaredMethods().length == 0){ // if no methods add contributing factor to report
                data += "Possible contributing factors are\n";
                data += cls.getSimpleName() + " has 0 methods which may indicate an unfinished class\n";
            }
            else if(cls.getDeclaredFields().length > 5 && cls.getDeclaredMethods().length <= 5 ){ // more fields than methods may indicate data class
                data += "Possible contributing factors are\n";
                data += cls.getSimpleName() + " has " + cls.getDeclaredFields().length + " fields and " + cls.getDeclaredMethods().length + " methods which may indicate a data class\n";
            }
            else if(cls.getDeclaredMethods().length > 0){ // find public methods
                for(Method method: cls.getDeclaredMethods()){
                    if(Modifier.isPublic(method.getModifiers())){
                        publicMethods.add(method);
                    }
                }
            }
            if(classReferenceMap.containsKey(cls)){ // let user know that class flagged as lazy may be false positive
                data += "\nPossible false positive result as " + cls.getSimpleName() + " is referenced in these classes\n";
                for (String string: classReferenceMap.get(cls)) {
                    data += string + "\n";
                }
            }
            if(!publicMethods.isEmpty()){
                data += "\nPossible false positive result as " + cls.getSimpleName() + " has public method(s)\n";
                for(Method method: publicMethods){
                    data += Modifier.toString(method.getModifiers()) +" "+ method.getReturnType().getSimpleName() +" "+ method.getName()+"\n";
                }
            }
            report.putReportData(cls, data);
        }
    }

    public Report getReport(){
        return report;
    }
}
