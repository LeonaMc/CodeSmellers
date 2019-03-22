package CodeSmellers;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;

public class LongParamList implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> cleanClasses;
    private Report report;
    private HashMap<Class, ArrayList<Method>> longParamMap;

    public LongParamList(ArrayList<Class> loadedClasses){
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        cleanClasses = new ArrayList<>();
        longParamMap = new HashMap<>();
        report = new Report();
    }

    private void findLongParamMethods(Class cls) throws FileNotFoundException {
        ArrayList<Method> longParamMethods = new ArrayList<>();
        for (Method method: cls.getDeclaredMethods()){
            if(method.getParameterCount() >= 4){
                longParamMethods.add(method);
            }
        }
        if(longParamMethods.size() > 0){
            report.setCodeSmellData(cls, longParamMethods);
        }
        else{
            cleanClasses.add(cls);
        }
    }

    @Override
    public void reflectClass() {
        for(Class cls: loadedClasses){
            try {
                findLongParamMethods(cls);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        loadedClasses.removeAll(cleanClasses);
        report.setEffectedClasses(loadedClasses);
    }

    public Report getReport() {
        return report;
    }

    // test redundant
    public void printTestReport(){
        for(Class cls : loadedClasses){
            System.out.println("Long parameter methods in class " + cls.getSimpleName());
            for(Method method : longParamMap.get(cls)){
                System.out.print("\nParameter count for " + method.getName() +" is " + method.getParameterCount() + "\n"
                        + "Parameters are\n");
                for (Parameter parameter : method.getParameters()){
                    System.out.println(parameter.getName() + " of type " + parameter.getType());
                }
            }
        }
    }
}
