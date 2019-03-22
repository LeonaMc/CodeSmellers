package CodeSmellers;

import java.lang.reflect.Field;
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

    private String getFieldSimpleName(Field field){
        return field.getType().getSimpleName();
    }

    private boolean isPrimitive(Field field){
        boolean isPrimitive = false;

        if(getFieldSimpleName(field).equalsIgnoreCase("int") ||
                getFieldSimpleName(field).equalsIgnoreCase("double") ||
                getFieldSimpleName(field).equalsIgnoreCase("boolean") ||
                getFieldSimpleName(field).equalsIgnoreCase("float") ||
                getFieldSimpleName(field).equalsIgnoreCase("String") ||
                getFieldSimpleName(field).equalsIgnoreCase("char")){

            isPrimitive = true;
        }
        return isPrimitive;
    }

    @Reflecting
    @Override
    public void reflectClass() {
        for (Class cls: loadedClasses){
            int fieldCount = 0;
            for (Field field: cls.getDeclaredFields()){
                if(isPrimitive(field)){
                    fieldCount++;
                }
            }
            if(fieldCount >= 5){
                report.setCodeSmellData(cls, fieldCount); // add code smell data for each effected class
            }
            else{
                cleanClasses.add(cls);
            }
        }
        loadedClasses.removeAll(cleanClasses);
        report.setEffectedClasses(loadedClasses); // add effected classes to report
    }

    public Report getReport() {
        return report;
    }

    //public void printTestReport(){
    //    if(!obsessedClasses.isEmpty()){
    //        System.out.println("List of classes which have been flagged for primitive obsession");
    //        for(Class cls : loadedClasses){
    //            System.out.println("Class " + cls.getName() + " has " + obsessedClasses.get(cls) + " Primitive fields");
    //        }
    //    }
    //}
}
