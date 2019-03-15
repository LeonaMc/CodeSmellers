package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PrimitiveObsession implements Reflectable{
    private ArrayList<File> javaSource;
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> flaggedClasses;

    public PrimitiveObsession(ArrayList<File> javaSource, ArrayList<Class> loadedClasses){
        flaggedClasses = new ArrayList<>();
        this.javaSource = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.javaSource.addAll(javaSource);
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
    private void inspectFlaggedClasses(){
        for(Class cls : flaggedClasses){
            /*for (Method method : cls.getDeclaredMethods()){
                if(method.getParameterTypes().length > 0){
                    for (Class c : method.getParameterTypes()){
                        System.out.println(method.getName() + " " + c.getSimpleName());
                    }
                    System.out.print("\n");
                }
            }*/

            for(Field field : cls.getDeclaredFields()){
                if(isPrimitive(field)){
                    System.out.println(field.getName() + " " + field.getType());
                }
            }
            System.out.print("\n");
        }
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
                flaggedClasses.add(cls);
            }
        }

        if(flaggedClasses.size() > 0){
            inspectFlaggedClasses();
        }
    }
}
