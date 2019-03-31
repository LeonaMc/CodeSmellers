package CodeSmellers;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
// needs further implementation
public class LongParamList implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> cleanClasses;
    private Report report;

    public LongParamList(ArrayList<Class> loadedClasses){
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        cleanClasses = new ArrayList<>();
        report = new Report();
    }

    private void findLongParamMethods(Class cls) throws FileNotFoundException {
        ArrayList<Method> longParamMethods = new ArrayList<>(); // effected methods
        for (Method method: cls.getDeclaredMethods()){ // for each method of class
            if(method.getParameterCount() >= 4){ // heuristic = 4
                longParamMethods.add(method); // add effected method
            }
        }
        if(longParamMethods.size() > 0){ // if current class has effected methods
            report.putCodeSmellData(cls, longParamMethods); // add data to report
        }
        else{
            cleanClasses.add(cls); // else add the clean class
        }
    }

    @Override
    public void reflectClass() {
        for(Class cls: loadedClasses){ // for each class to be inspected
            try {
                findLongParamMethods(cls); // run inspection
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        loadedClasses.removeAll(cleanClasses); // remove clean classes so only effected classes left
        report.setEffectedClasses(loadedClasses); // add effected classes classes to report
    }
    // returns report
    public Report getReport() {
        return report;
    }
}
