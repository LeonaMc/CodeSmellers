package CodeSmells;

import Interface.Reflectable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Switches implements Reflectable {

    private ArrayList<File> javaSourceFiles; // all source files from project being inspected
    private ArrayList<Class> loadedClasses;
    private HashMap<Class, Integer> switchOccurrencesPerClass; // maps number of occurrences of switch statements in each class
    private ArrayList<Class> cleanClasses;
    private Report report;


    public Switches(ArrayList<File> javaSourceFiles, ArrayList<Class> loadedClasses) {
        this.switchOccurrencesPerClass = new HashMap<>();
        this.javaSourceFiles = new ArrayList<>();
        this.javaSourceFiles.addAll(javaSourceFiles);
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        this.cleanClasses = new ArrayList<>();
        this.report = new Report();
    }


    @Override
    public void reflectClass() {
        extractSwitches();
        for(Class clazz: loadedClasses){
            StringBuilder message = new StringBuilder();
            message.append("Class: ").append(clazz.getSimpleName()).append(" has ").append(switchOccurrencesPerClass.get(clazz)).append(" switch statements.\n");
            System.out.println(message);
            report.putReportData(clazz, message);
        }
    }

    private void extractSwitches()
    {

        int countSwitches = 0;

        File currFile;
        Class currClass;
        for(int i =0;i<javaSourceFiles.size();i++)
        {
            currFile = javaSourceFiles.get(i);
            currClass = loadedClasses.get(i);
            String source = extractSource(currFile);
            source = source.replaceAll("(/\\*([^*]|(\\*+[^*/]))*\\*+/)|(//.*)",""); // removing comments from source code
            String[] code = source.split("\n"); // splitting source code line by line
            for(String line: code){
                if(line.contains("switch(")){ // if uncommented code contains switch statement
                    countSwitches++;
                }
            }
            if(countSwitches == 0){ // if no switch statements
                cleanClasses.add(currClass);
            }
            switchOccurrencesPerClass.put(currClass, countSwitches); // initialise entry
            countSwitches = 0; // resets counter

        }

        if(cleanClasses.size() > 0) {
            loadedClasses.removeAll(cleanClasses);
            report.setAffectedClasses(loadedClasses);
        }
    }


    private String extractSource(File source) {
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(source));
            String currLine;
            while ((currLine = br.readLine()) != null)
            {
                stringBuilder.append(currLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }

    public Report getReport()
    {
        return this.report;
    }
}