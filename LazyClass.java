package CodeSmellers;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        for(Class cls: loadedClasses){
            if (cls.isAnnotation() || cls.isInterface()){
                cleanClasses.add(cls);
            }
        }
        for (File file:javaSource){ // for each source file
            if(lineCounter.countLines(file) > 150){ // if num of lines more than 50
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
            findSmallClass();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Class> tempList = new ArrayList<>();
        tempList.addAll(loadedClasses);
        HashMap<Class,ArrayList<String>> classReferenceMap = new HashMap<>();
        String data;
        for(Class cls: loadedClasses){
            ArrayList<String> className = new ArrayList<>();
            for(Class innerCls: tempList){
                if (findReference(cls,innerCls)){
                    className.add(innerCls.getSimpleName());
                    classReferenceMap.put(cls,className);
                }
            }
            data = "Class " + cls.getSimpleName() + " has been flagged as a lazy class because it has less than 150 lines of code\n";
            ArrayList<Method> publicMethods = new ArrayList<>();
            if(cls.getDeclaredMethods().length == 0){
                data += "Possible contributing factors are\n";
                data += cls.getSimpleName() + " has 0 methods which may indicate an unfinished class\n";
            }
            else if(cls.getDeclaredFields().length > 5 && cls.getDeclaredMethods().length <= 5 ){
                data += "Possible contributing factors are\n";
                data += cls.getSimpleName() + " has " + cls.getDeclaredFields().length + " fields and " + cls.getDeclaredMethods().length + " methods which may indicate a data class\n";
            }
            else if(cls.getDeclaredMethods().length > 0){
                for(Method method: cls.getDeclaredMethods()){
                    if(Modifier.isPublic(method.getModifiers())){
                        publicMethods.add(method);
                    }
                }
            }
            if(classReferenceMap.containsKey(cls)){
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
