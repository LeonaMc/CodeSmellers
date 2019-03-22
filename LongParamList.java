package CodeSmellers;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;

public class FindLongParamList implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> cleanClasses;
    private HashMap<Class, ArrayList<Method>> longParamMap;

    public FindLongParamList(ArrayList<Class> loadedClasses){
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        cleanClasses = new ArrayList<>();
        longParamMap = new HashMap<>();
    }

    private void findLongParamMethods(Class cls) throws FileNotFoundException {
        ArrayList<Method> longParamMethods = new ArrayList<>();
        for (Method method: cls.getDeclaredMethods()){
            if(method.getParameterCount() >= 4){
                longParamMethods.add(method);
            }
        }
        if(longParamMethods.size() > 0){
            longParamMap.put(cls,longParamMethods);
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
    }

    // test
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
            System.out.println("Suggested to create a new methods and split functionality in class " + cls.getSimpleName() + "\n"); // template message
        }
    }
}
