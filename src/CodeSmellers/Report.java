package CodeSmellers;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
public class Report<T> {
    DecimalFormat df = new DecimalFormat("#.00");
    private ArrayList<Class> affectedClasses; // holds only effected classes of code smell report belongs to
    private HashMap<Class,T> reportData; // holds data found from code smell inspection. All keys are held in affected classes
    private HashMap<Method,String> longMethodData;
    private double percentOfFilesAffected; // overall percent of affected classes

    public Report(){
        affectedClasses = new ArrayList<>();
        reportData = new HashMap<>();
        longMethodData = new HashMap<>();
        percentOfFilesAffected = 0.0;
    }
    // adds only classes affected by smell
    public void setAffectedClasses(ArrayList<Class> affectedClasses){
        this.affectedClasses.addAll(affectedClasses);
    }
    //
    public void putReportData(Class key, T data){
        this.reportData.put(key, data);
    }

    public void putLongMethodData(Method key, String data){
        this.longMethodData.put(key, data);
    }

    public ArrayList<Class> getAffectedClasses() {
        return affectedClasses;
    }

    public HashMap<Class,T> getReportData() {
        return reportData;
    }

    public HashMap<Method, String> getLongMethodData(){
        return longMethodData;
    }
    public void setPercentage(int loadedClassSize){
        percentOfFilesAffected = ((double) affectedClasses.size()/(double)loadedClassSize)*100;
    }

    public Double getPercentage(){
        return percentOfFilesAffected;
    }

    public String printSizeOfAffectedClasses(){
        return "Number of affected classes = " + affectedClasses.size();
    }

    public String percentToString(){
        return df.format(percentOfFilesAffected)+"%";
    }
}
