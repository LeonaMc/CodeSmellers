package CodeSmellers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class TooManyLiterals implements Reflectable {
    private ArrayList<Class> loadedClasses;
    private ArrayList<Class> cleanClasses;

    public TooManyLiterals(ArrayList<Class> loadedClasses) {
        cleanClasses = new ArrayList<>();
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
    }

    @Override
    public void reflectClass() {

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

    public void ref() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException {
        for (Class cls : loadedClasses) {
            for (final Field field : cls.getDeclaredFields()) {
                System.out.println(field.getType() +" "+ field.getName());
                if(isPrimitive(field) && !Modifier.isFinal(field.getModifiers())){
                    field.setAccessible(true);
                    if (field.get(cls.newInstance()) != null){
                        System.out.println(field.getName() + " " + field.get(cls.newInstance()).toString());
                    }
                }
            }
            System.out.print("\n");
        }
    }
}
