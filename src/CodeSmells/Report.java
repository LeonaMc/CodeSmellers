package CodeSmells;
//REMINDER: see if removal of T is possible
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
public class Report<T> {
    DecimalFormat df = new DecimalFormat("#0.00");
    private ArrayList<Class> affectedClasses; // holds only affected classes of code smell report belongs to
    private HashMap<Class,T> reportData; // holds data found from code smell inspection. All keys are held in affected classes
    private HashMap<Method,String> longMethodData;
    private double percentOfFilesAffected; // overall percent of affected classes

    public Report(){
        affectedClasses = new ArrayList<>();
        reportData = new HashMap<>();
        longMethodData = new HashMap<>();
        percentOfFilesAffected = 0.0;
    }
    /* method is called by report object inside each smell class
    *  only classes affected by a smell are added*/
    public void setAffectedClasses(ArrayList<Class> affectedClasses){
        this.affectedClasses.addAll(affectedClasses);
    }
    /*method is called by report object inside each smell class
    * Affected class is key
    * formatted data is added after smell class has checked all files
    * data is any information collected relevant to smell e.g length of method and start/end line for long method smell*/
    public void putReportData(Class key, T data){
        this.reportData.put(key, data);
    }
    // used by long method smell only, affected method is key, data is method name etc
    public void putLongMethodData(Method key, String data){
        this.longMethodData.put(key, data);
    }
    // return list of affected classes for an individual code smell
    public ArrayList<Class> getAffectedClasses() {
        return affectedClasses;
    }
    // returns map which holds data collected in code smell class, see putReportData
    public HashMap<Class,T> getReportData() {
        return reportData;
    }
    // returns map for method data found in long method class
    public HashMap<Method, String> getLongMethodData(){
        return longMethodData;
    }
    // sets percentage of files affected compared to overall class files loadedClassSize = loadedClasses.size()
    public void setPercentage(int loadedClassSize){
        percentOfFilesAffected = ((double) affectedClasses.size()/(double)loadedClassSize)*100;
    }
    // return percentage of files affected by a smell
    public Double getPercentage(){
        return percentOfFilesAffected;
    }

    public String printNumAffectedClasses(){
        return "Number of affected classes = " + affectedClasses.size();
    }

    public String percentToString(){
        return df.format(percentOfFilesAffected)+"%";
    }
    // true if no classes are affected by this code smell
    public boolean isClean(){
        return affectedClasses.isEmpty();
    }
}
