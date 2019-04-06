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
    public int getNumPrimitiveMethods(Class host){
        int primitiveMethodCount = 0; // counter

        for(Method method : host.getDeclaredMethods()){ // for each method in host
            if (method.getReturnType().isPrimitive()){ // if method isPrimitive
                primitiveMethodCount++; // increment counter
            }
        }
        return primitiveMethodCount; // return final count
    }
    // count primitive fields of a class
    public int getNumPrimitiveFields(Class host){
        int primitiveFieldCount = 0; // counter

        for(Field field: host.getDeclaredFields()){ // for each field
            if (field.getType().isPrimitive()){ // if field is primitive
                primitiveFieldCount++; // increment counter
            }
        }
        return primitiveFieldCount; // return final count
    }
    // count primitive parameters of a method
    public int getNumPrimitiveParameters(Method host){
        int primitiveParamCount = 0; // counter

        for(Class parameterType: host.getParameterTypes()){ // for each parameter type of host method
            if(parameterType.isPrimitive()){ // if primitive
                primitiveParamCount++; // increment
            }
        }
        return primitiveParamCount; // return final count
    }

    @Reflecting
    @Override
    public void reflectClass() {
        for (Class cls: loadedClasses){ // for each class
            int fieldCount = 0; // set fieldCount to 0 for current class
            String message = "";
            for (Field field: cls.getDeclaredFields()){ // for each field
                if(field.getType().isPrimitive()){ // if type is primitive
                    fieldCount++; // increment counter
                }
            }
            if(fieldCount >= 5){ // heuristic = 5 needs to be changed
                message = "Class " + cls.getSimpleName() + " has " + fieldCount + " fields";
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
