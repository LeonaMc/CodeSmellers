package CodeSmells;

import Interface.Reflectable;

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
        report.setName("Too Many Literals");
    }

    @Override
    public void reflectClass() {
        ArrayList<Class> cleanClasses = new ArrayList<>();
        int namedConstantCount = 0;
        int primitiveLiterals = 0;
        for (Class cls : loadedClasses) { // for each class
            for (final Field field : cls.getDeclaredFields()) { // for each field in class set file to final
                if(Modifier.isStatic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())){
                    namedConstantCount++;
                }
                if(field.getType().isPrimitive() && !field.isSynthetic()){ // only get primitives and reject synthetic variables(compiler made)
                    field.setAccessible(true); // if field modifier is private set to accessible
                    try{
                        if(field.get(cls) != null){ // check if primitive field has value assigned
                            primitiveLiterals++;
                        }
                    }
                    catch (IllegalArgumentException e){
                        continue;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(namedConstantCount > 0 || primitiveLiterals < 5){ // heuristic may need to be changed
                cleanClasses.add(cls);
            }
            namedConstantCount = 0;
            primitiveLiterals = 0;
        }

        loadedClasses.removeAll(cleanClasses);
        report.setAffectedClasses(loadedClasses);
        String message;
        for (Class cls : loadedClasses) { // for each class
            message = "Class: " + cls.getSimpleName() + "\n"; // test print name
            for (final Field field : cls.getDeclaredFields()) { // for each field in class set file to final
                if(field.getType().isPrimitive() && !field.isSynthetic()){ // only get primitives and reject synthetic variables(compiler made)
                    field.setAccessible(true); // if field modifier is private set to accessible
                    try{ // report needs to be implemented
                        message += Modifier.toString(field.getModifiers())+" "+field.getType()+" "+field.getName()+" = "+field.get(cls).toString()+"\n"; // test print data
                    }
                    catch (IllegalArgumentException e){
                        continue;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            report.putReportData(cls,message);
        }
    }

    // shortens code for every call to getSimpleName
    private String getFieldSimpleName(Field field){
        return field.getType().getSimpleName();
    }

    public Report getReport(){
        return report;
    }
}
