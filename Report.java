package CodeSmellers;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
public class Report<T> {
    DecimalFormat df = new DecimalFormat("#.00");
    private ArrayList<Class> effectedClasses; // holds only effected classes of code smell report belongs to
    private HashMap<Class,T> codeSmellData; // holds data found from code smell inspection. All keys are held in effected classes
    private HashMap<Method,String> longMethodData;
    private double percentOfFilesEffected; // overall percent of effected classes

    public Report(){
        effectedClasses = new ArrayList<>();
        codeSmellData = new HashMap<>();
        longMethodData = new HashMap<>();
        percentOfFilesEffected = 0.0;
    }
    //
    public void setEffectedClasses(ArrayList<Class> effectedClasses){
        this.effectedClasses.addAll(effectedClasses);
    }

    public void putCodeSmellData(Class key, T data){
        this.codeSmellData.put(key, data);
    }

    public void putLongMethodData(Method key, String data){
        this.longMethodData.put(key, data);
    }

    public ArrayList<Class> getEffectedClasses() {
        return effectedClasses;
    }

    public HashMap<Class,T> getCodeSmellData() {
        return codeSmellData;
    }

    public HashMap<Method, String> getLongMethodData(){
        return longMethodData;
    }
    private double setPercentage(int loadedClassSize){
        return ((double)effectedClasses.size()/(double)loadedClassSize)*100;
    }

    public String getPercentage(){
        return df.format(percentOfFilesEffected)+"%";
    }
}
