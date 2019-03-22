package CodeSmellers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
/*each code smell class has a report object as class variable.
* if class being inspected is found clean add to clean ArrayList
* Remove all clean classes from loadedClass array
* add (loadedClasses - clean classes) to report Array effectedClasses
* size of effectedClasses array is number of classes effected, can get info from each class in effected array e.g class name etc
* HashMap will hold individual class data of code smell using class as key to data found in code smell class
* code smell class will return report*/
public class Report<T> {
    DecimalFormat df = new DecimalFormat("#.00");
    private ArrayList<Class> effectedClasses;
    private HashMap<Class,T> codeSmellData;

    public Report(){
        effectedClasses = new ArrayList<>();
        codeSmellData = new HashMap<>();
    }

    public void setEffectedClasses(ArrayList<Class> effectedClasses){
        this.effectedClasses.addAll(effectedClasses);
    }

    public void setCodeSmellData(Class key, T data){
        this.codeSmellData.put(key, data);
    }

    public ArrayList<Class> getEffectedClasses() {
        return effectedClasses;
    }

    public HashMap<Class,T> getCodeSmellData() {
        return codeSmellData;
    }
}
