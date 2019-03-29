package CodeSmellers;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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
        ArrayList<Method> longParamMethods = new ArrayList<>();
        for (Method method: cls.getDeclaredMethods()){
            if(method.getParameterCount() >= 4){
                longParamMethods.add(method);
            }
        }
        if(longParamMethods.size() > 0){
            report.putCodeSmellData(cls, longParamMethods);
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
}
