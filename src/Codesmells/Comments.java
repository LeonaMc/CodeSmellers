package CodeSmells;

import Interface.Reflectable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comments implements Reflectable{

    private ArrayList<File> javaSourceFiles; // all source files from project being inspected
    private ArrayList<Class> loadedClasses;
    private HashMap<Class, ArrayList<String>> commentOccurrencesPerClass; // maps file to the comments in the class

    private ArrayList<Class> cleanClasses;
    private Report report;

    public Comments(ArrayList<File> javaSourceFiles, ArrayList<Class> loadedClasses) {
        this.commentOccurrencesPerClass = new HashMap<>();
        this.javaSourceFiles = new ArrayList<>();
        this.javaSourceFiles.addAll(javaSourceFiles);
        this.loadedClasses = new ArrayList<>();
        this.loadedClasses.addAll(loadedClasses);
        this.cleanClasses = new ArrayList<>();
        this.report = new Report();
        report.setName("Comments");
    }

    @Override
    public void reflectClass() {
        extractComments();
        for(Class clazz: loadedClasses){
            StringBuilder message = new StringBuilder();
            message.append("Class: ").append(clazz.getSimpleName()).append(" has ").append(commentOccurrencesPerClass.get(clazz).size()).append(" comments.\n");
            System.out.println(message);
            report.putReportData(clazz, message);
        }

    }

    private void extractComments()
    {
        Pattern pattern = Pattern.compile("(/\\*([^*]|(\\*+[^*/]))*\\*+/)|(//.*)"); // pattern which detects line and block comments
        Matcher matcher;

        File currFile = null;
        Class currClass = null;
        for(int i =0;i<javaSourceFiles.size();i++)
        {
            currFile = javaSourceFiles.get(i);
            currClass = loadedClasses.get(i);
            String source = extractSource(currFile);
            matcher = pattern.matcher(source); // find comments in the source code
            //commentOccurrencesPerFile.put(currFile, new ArrayList<>()); // initialise key
            commentOccurrencesPerClass.put(currClass,new ArrayList<>());

            while(matcher.find()){ // while comments exist in the source code
                //commentOccurrencesPerFile.get(currFile).add(matcher.group()); // for current source code, add comment if exists
                commentOccurrencesPerClass.get(currClass).add(matcher.group());

            }

            // If less than 20 comments in the class, then its clean
            if(commentOccurrencesPerClass.get(currClass).size() < 20)
            {
                cleanClasses.add(currClass);
            }

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