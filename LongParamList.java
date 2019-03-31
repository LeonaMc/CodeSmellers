package CodeSmellers;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;

// needs further implementation
public class LongParamList implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> cleanClasses;
    private HashMap<Class, ArrayList<Method>> methodHash;
    private Report report;

    public LongParamList(ArrayList<Class> loadedClasses){
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        cleanClasses = new ArrayList<>();
        methodHash = new HashMap<>();
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
            methodHash.put(cls, longParamMethods); // add data to report
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

        for (Class cls : loadedClasses) {
            ArrayList<Method> effectedMethods = new ArrayList<>();
            effectedMethods.addAll(methodHash.get(cls));
            String effectedMethodMessage = "\nName of effected class = " + cls.getSimpleName() +
                                            "\nNumber of effected methods = " + effectedMethods.size();
            double methodPercent = ((double) effectedMethods.size() / (double) cls.getDeclaredMethods().length) * 100;
            effectedMethodMessage += "\n" + report.df.format(methodPercent) + "% of methods effected\nMethod names\n";
            StringBuilder builder = new StringBuilder(effectedMethodMessage);
            for (Method method : effectedMethods) {
                builder.append("\n" + method.getName() + " has " + method.getParameters().length + " parameters\nParameter types are\n");
                for (Class parameter : method.getParameterTypes()){
                    builder.append(parameter.getTypeName() + "\n");
                }
            }
            report.putCodeSmellData(cls, builder.toString());
        }
    }
    // returns report
    public Report getReport() {
        return report;
    }
}
