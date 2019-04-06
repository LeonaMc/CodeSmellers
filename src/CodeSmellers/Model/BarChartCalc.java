package CodeSmellers.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import CodeSmellers.DirectoryReader;
import CodeSmellers.Inspection;
import CodeSmellers.*;

public class BarChartCalc{
	
    String newline = "\n";
    String[] packageArray = new String[2];
    DirectoryReader directoryReader = new DirectoryReader();
    String directoryPath = "C:\\Eclipse\\ActuallyJavaFX"; // Add path to root of directory here
    
    public double getResults() {
    
    directoryReader.getFiles(directoryPath);
        
    if(directoryReader.getDirectoryLevel() > 0){
        packageArray = directoryReader.getClasspath(directoryReader.getClassArrayList().get(0).getPath());
    }
    else{
        packageArray[0] = null;
        packageArray[1] = directoryPath;
    }

    try {
        directoryReader.loadClasses(packageArray);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
	

  //  Inspection inspection = new Inspection(loadedClasses,javaSource);

    return 0;
    }
    
    public double result() {
    ArrayList<Class> loadedClasses = new ArrayList<>(directoryReader.getLoadedClasses()); // classes ready for reflection
    ArrayList<File> javaSource = new ArrayList<>(directoryReader.getJavaSourceArrayList()); // can read java files as text
    Inspection inspection = new Inspection(loadedClasses,javaSource);
    inspection.runInspection();

    final String FEATURE_ENVY = "FeatureEnvy";
    final String LARGE_CLASS = "LargeClass";
    final String LONG_METHOD = "LongMethod";
    final String LONG_PARAM = "LongParameter";

    Double featureEnvyPercent = inspection.getReports().get(FEATURE_ENVY).getPercentage();
    Double largeClassPercent = inspection.getReports().get(LARGE_CLASS).getPercentage();
    Double longMethodPercent = inspection.getReports().get(LONG_METHOD).getPercentage();
    Double longParameterPercent = inspection.getReports().get(LONG_PARAM).getPercentage();

    System.out.println("feature envy percent " + Math.round(featureEnvyPercent*100.0)/100.0);
    System.out.println("large class percent " + Math.round(largeClassPercent*100.0)/100.0);
    System.out.println("long method percent " + Math.round(longMethodPercent*100.0)/100.0);
    System.out.println("long parameter percent " + Math.round(longParameterPercent * 100.0)/100.0);  
    
    return Math.round(longMethodPercent*100.0)/100.0;
    }
}

	
