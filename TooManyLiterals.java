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

    private String getFieldSimpleName(Field field){
        return field.getType().getSimpleName();
    }

    public void ref() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException {
        for (Class cls : loadedClasses) {
            System.out.println("Class: " + cls.getSimpleName());
            for (final Field field : cls.getDeclaredFields()) {
                if(field.getType().isPrimitive() && !field.isSynthetic()){
                    field.setAccessible(true);
                    try{
                        System.out.println(Modifier.toString(field.getModifiers())+" "+field.getType()+" "+field.getName()+" = "+field.get(cls).toString());
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
