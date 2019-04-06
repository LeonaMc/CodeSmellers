package CodeSmellers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class TooManyLiterals implements Reflectable {
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> cleanClasses;
    private Report report;

    public TooManyLiterals(ArrayList<Class> loadedClasses) {
        cleanClasses = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        report = new Report();
    }

    @Override
    public void reflectClass() {

    }
    // shortens code for every call to getSimpleName
    private String getFieldSimpleName(Field field){
        return field.getType().getSimpleName();
    }
    // method body will be moved to reflectClass when exceptions are handled correctly
    public void ref() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException {
        for (Class cls : loadedClasses) { // for each class
            System.out.println("Class: " + cls.getSimpleName()); // test print name
            for (final Field field : cls.getDeclaredFields()) { // for each field in class set filed to final
                if(field.getType().isPrimitive() && !field.isSynthetic()){ // only get primitives and reject synthetic variables(compiler made)
                    field.setAccessible(true); // if field modifier is private set to accessible
                    try{ // report needs to be implemented
                        System.out.println(Modifier.toString(field.getModifiers())+" "+field.getType()+" "+field.getName()+" = "+field.get(cls).toString()); // test print data
                    }
                    catch (IllegalArgumentException e){
                        continue;
                    }
                }
            }
            System.out.print("\n");
        }
    }
}
