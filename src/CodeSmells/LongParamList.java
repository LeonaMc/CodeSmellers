package CodeSmells;

import Interface.Reflectable;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

// needs further implementation
public class LongParamList implements Reflectable {
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
        report.setAffectedClasses(loadedClasses); // add effected classes classes to report

        for (Class cls : loadedClasses) {
            ArrayList<Method> affectedMethods = new ArrayList<>();
            affectedMethods.addAll(methodHash.get(cls));
            String affectedMethodMessage = "\nName of affected class = " + cls.getSimpleName() +
                                            "\nNumber of affected methods = " + affectedMethods.size();
            double methodPercent = ((double) affectedMethods.size() / (double) cls.getDeclaredMethods().length) * 100;
            affectedMethodMessage += "\n" + report.df.format(methodPercent) + "% of methods affected\nMethod names\n";
            StringBuilder builder = new StringBuilder(affectedMethodMessage);
            for (Method method : affectedMethods) {
                builder.append("\n" + method.getName() + " has " + method.getParameters().length + " parameters\nParameter types are\n");
                for (Class parameter : method.getParameterTypes()){
                    builder.append(parameter.getSimpleName() + "\n");
                }
            }
            report.putReportData(cls, builder.toString());
        }
    }
    // returns report
    public Report getReport() {
        return report;
    }
}
