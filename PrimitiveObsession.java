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

    @Reflecting
    @Override
    public void reflectClass() {
        for (Class cls: loadedClasses){ // for each class
            int fieldCount = 0; // set fieldCount to 0 for current class
            for (Field field: cls.getDeclaredFields()){ // for each field
                if(field.getType().isPrimitive()){ // if type is primitive
                    fieldCount++; // increment counter
                }
            }
            if(fieldCount >= 5){ // heuristic = 5 needs to be changed
                report.putCodeSmellData(cls, fieldCount); // add code smell data for each effected class
            }
            else{
                cleanClasses.add(cls); // clean classes will be removed
            }
        }
        loadedClasses.removeAll(cleanClasses); // remove all clean classes
        report.setEffectedClasses(loadedClasses); // add effected classes to report
    }
    // returns report
    public Report getReport() {
        return report;
    }
}
