package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import CodeSmells.DirectoryReader;
import CodeSmells.Inspection;
import Controller.ChangeSceneController;

public class BarChartCalc{
  
	ArrayList<Double> percentages = new ArrayList<Double>();
	ChangeSceneController c = new ChangeSceneController();
    
    public  ArrayList<Double> getResults() {
    	
    	String newline = "\n";
        String[] packageArray = new String[2];
        DirectoryReader directoryReader = new DirectoryReader();
        String directoryPath = "C:\\Eclipse\\ActuallyJavaFX"; // Add path to root of directory here
        
       
   //     System.out.println(c.getDirectoryPath());
        
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
        
        ArrayList<Class> loadedClasses = new ArrayList<>(directoryReader.getLoadedClasses()); // classes ready for reflection
        ArrayList<File> javaSource = new ArrayList<>(directoryReader.getJavaSourceArrayList()); // can read java files as text

        Inspection inspection = new Inspection(loadedClasses,javaSource);
        inspection.runInspection();

        final String FEATURE_ENVY = "FeatureEnvy";
        final String LARGE_CLASS = "LargeClass";
        final String LONG_METHOD = "LongMethod";
        final String LONG_PARAM = "LongParameter";
        	
        double featureEnvyPercent = Math.round(inspection.getReports().get(FEATURE_ENVY).getPercentage());
        double largeClassPercent = Math.round(inspection.getReports().get(LARGE_CLASS).getPercentage());
        double longMethodPercent = Math.round(inspection.getReports().get(LONG_METHOD).getPercentage());
        double longParameterPercent = Math.round(inspection.getReports().get(LONG_PARAM).getPercentage());
        
        // TODO: Change to real values when ready
        double duplicateCodePercent = Math.round(inspection.getReports().get(FEATURE_ENVY).getPercentage());
        double longclassPercent = Math.round(inspection.getReports().get(LARGE_CLASS).getPercentage());
        double inappropriateIntimacyPercent = Math.round(inspection.getReports().get(LONG_METHOD).getPercentage());
        double tooManyLiteralsPercent = Math.round(inspection.getReports().get(LONG_PARAM).getPercentage());   
       
        double primitiveObsessionsPercent = Math.round(inspection.getReports().get(FEATURE_ENVY).getPercentage());
        double godComplexPercent = Math.round(inspection.getReports().get(LARGE_CLASS).getPercentage());
        double lazyClassPercent = Math.round(inspection.getReports().get(LONG_METHOD).getPercentage());
        double commentsPercent = Math.round(inspection.getReports().get(LONG_PARAM).getPercentage());
        
        double switchStatementsPercent = Math.round(inspection.getReports().get(FEATURE_ENVY).getPercentage());
        double dataClumpPercent = Math.round(inspection.getReports().get(LARGE_CLASS).getPercentage());
        double largeMethodPercent = Math.round(inspection.getReports().get(LONG_METHOD).getPercentage());
        
        percentages.add(featureEnvyPercent);
        percentages.add(largeClassPercent);
        percentages.add(longMethodPercent);
        percentages.add(longParameterPercent); 
        percentages.add(primitiveObsessionsPercent);
        percentages.add(godComplexPercent);
        percentages.add(lazyClassPercent);
        percentages.add(commentsPercent);         
        percentages.add(switchStatementsPercent);
        percentages.add(dataClumpPercent);
        percentages.add(largeMethodPercent);
        
        return percentages;     
    }
}

	
