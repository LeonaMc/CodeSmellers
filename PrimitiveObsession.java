package CodeSmellers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PrimitiveObsession implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> cleanClasses;
    private Report report;

    public PrimitiveObsession(ArrayList<Class> loadedClasses){
        cleanClasses = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        report = new Report();
    }

    // count primitive methods of a class
    private int getNumPrimitiveMethods(Class cls){
        int primitiveMethodCount = 0; // counter

        for(Method method : cls.getDeclaredMethods()){ // for each method in host
            if (method.getReturnType().isPrimitive() && !method.getReturnType().equals(void.class)){ // if method isPrimitive and not void
                primitiveMethodCount++; // increment counter
            }
        }
        return primitiveMethodCount; // return final count
    }
    // count primitive fields of a class
    private int getNumPrimitiveFields(Class cls){
        int primitiveFieldCount = 0; // counter

        for(Field field: cls.getDeclaredFields()){ // for each field
            if (field.getType().isPrimitive()){ // if field is primitive
                primitiveFieldCount++; // increment counter
            }
        }
        return primitiveFieldCount; // return final count
    }

    @Reflecting
    @Override
    public void reflectClass() {
        for (Class cls: loadedClasses){ // for each class
            int primitiveCount = getNumPrimitiveMethods(cls) + getNumPrimitiveFields(cls); // set primitiveCount to number of primitive fields + methods
            String message = "";

            if(primitiveCount >= 10){ // heuristic = 10
                message = "Class " + cls.getSimpleName() + " has " + getNumPrimitiveFields(cls) + " primitive fields and " + getNumPrimitiveMethods(cls) + " primitive methods\n";
                if (getNumPrimitiveFields(cls)>0){
                    message += "\nList of Primitive Fields\n";
                    for(Field field: cls.getDeclaredFields()){
                        if (field.getType().isPrimitive()){
                            message += field.getType().getSimpleName()+" "+field.getName()+"\n"; // add each primitive field name and type to data string
                        }
                    }
                }
                if(getNumPrimitiveMethods(cls)>0){
                    message += "\nList of Primitive methods\n";
                    for(Method method : cls.getDeclaredMethods()){ // for each method in host
                        if (method.getReturnType().isPrimitive()  && !method.getReturnType().equals(void.class)){ // if method isPrimitive and not void
                            message += method.getReturnType().getSimpleName()+" "+method.getName()+"\n"; // add each primitive method name and return type to data message
                        }
                    }
                }
                report.putReportData(cls, message); // add code smell data for each effected class
            }
            else{
                cleanClasses.add(cls); // clean classes will be removed
            }
        }
        loadedClasses.removeAll(cleanClasses); // remove all clean classes
        report.setAffectedClasses(loadedClasses); // add effected classes to report
    }
    // returns report
    public Report getReport() {
        return report;
    }
}
