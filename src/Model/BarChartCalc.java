package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import CodeSmells.DirectoryReader;
import CodeSmells.Inspection;
import CodeSmells.Report;
import Controller.ChangeSceneController;

public class BarChartCalc{
  
	ArrayList<Double> percentages = new ArrayList<Double>();
	ChangeSceneController c = new ChangeSceneController();
    
    public  HashMap<String, Report> runInspection() {
    	
    	String newline = "\n";
        String[] packageArray = new String[2];
        DirectoryReader directoryReader = new DirectoryReader();
        String directoryPath = ChangeSceneController.getTextPath(); // Add path to root of directory here
        System.out.println(directoryPath);
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

        Inspection inspection = new Inspection(directoryReader.getLoadedClasses(),directoryReader.getJavaSourceArrayList());
        inspection.runInspection();

        return inspection.getReports();
    }
}

	
