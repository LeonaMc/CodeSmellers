package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class PrimitiveObsession implements Reflectable{
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> cleanClasses;
    private HashMap<Class,Integer> obsessedClasses;

    public PrimitiveObsession(ArrayList<Class> loadedClasses){
        obsessedClasses = new HashMap<>();
        cleanClasses = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
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
                obsessedClasses.put(cls, fieldCount);
            }
            else{
                cleanClasses.add(cls);
            }
        }
        loadedClasses.removeAll(cleanClasses);
    }

    public void printTestReport(){
        if(!obsessedClasses.isEmpty()){
            System.out.println("List of classes which have been flagged for primitive obsession");
            for(Class cls : loadedClasses){
                System.out.println("Class " + cls.getName() + " has " + obsessedClasses.get(cls) + " Primitive fields");
            }
            System.out.println("Advised to clean flagged classes or make new class for data\n"); // placeholder message
        }
    }
}
