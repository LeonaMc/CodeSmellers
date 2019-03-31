package CodeSmellers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
public class Report<T> {
    DecimalFormat df = new DecimalFormat("#.00");
    private ArrayList<Class> effectedClasses; // holds only effected classes of code smell report belongs to
    private HashMap<Class,T> codeSmellData; // holds data found from code smell inspection. All keys are held in effected classes
    private double percentOfFilesEffected; // overall percent of effected classes

    public Report(){
        effectedClasses = new ArrayList<>();
        codeSmellData = new HashMap<>();
        percentOfFilesEffected = 0.0;
    }
    //
    public void setEffectedClasses(ArrayList<Class> effectedClasses){
        this.effectedClasses.addAll(effectedClasses);
    }

    public void putCodeSmellData(Class key, T data){
        this.codeSmellData.put(key, data);
    }

    public ArrayList<Class> getEffectedClasses() {
        return effectedClasses;
    }

    public HashMap<Class,T> getCodeSmellData() {
        return codeSmellData;
    }

    private double setPercentage(int loadedClassSize){
        return ((double)effectedClasses.size()/(double)loadedClassSize)*100;
    }

    public String getPercentage(){
        return df.format(percentOfFilesEffected)+"%";
    }
}
