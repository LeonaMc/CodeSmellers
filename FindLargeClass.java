package CodeSmellers;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

// Refactor Class to take class and java arrays in constructor, give length of file in report
public class FindLargeClass implements Bloatable {
    private ArrayList<Class> bloatedClassFiles;
    private ArrayList<File> bloatedSourceFiles; // may not need this!
    private int numberOfSourceFiles = 0;
    private HashMap<Class, ArrayList<String>> classReports; // see reflectClass()

    FindLargeClass(){
        bloatedClassFiles = new ArrayList<>();
        bloatedSourceFiles = new ArrayList<>();
        classReports = new HashMap<>();
    }

    @Override // no implementation for this class?
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        return null;
    }

    @ReadingSource
    @Override
    public int countLines(File javaSource) throws FileNotFoundException {
        FileReader fileReader = new FileReader(javaSource);
        LineNumberReader numberReader = new LineNumberReader(fileReader);
        int lines = 0;
        try {
            while (numberReader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            numberReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void findLargeFiles(ArrayList<File> javaSource, ArrayList<Class> loadedClass){
        numberOfSourceFiles = loadedClass.size();
        for (int i = 0; i < javaSource.size(); i++){
            try {
                int upperBound = 200;
                if(countLines(javaSource.get(i)) > upperBound){
                    bloatedSourceFiles.add(javaSource.get(i));
                    bloatedClassFiles.add(loadedClass.get(i));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Reflecting
    @Override
    public void reflectClass() {
        // if long class, reflect and find ways to improve e.g. count number of primitives and methods advise on results
        for (Class cls : bloatedClassFiles){
            ArrayList<String> report = new ArrayList<>();
            if(cls.getDeclaredMethods().length > 10){ // placeholder value, not sure how many methods is too many
                report.add(cls.getName() + " has " + cls.getDeclaredMethods().length + " Methods"); // need better report messages
            }
            if (cls.getDeclaredFields().length > 10){
                report.add(cls.getName() + " has " + cls.getDeclaredFields().length + " Declared Fields");
            }
            classReports.put(cls,report);
        }
    }

    public void printTestReport(){
        double bloated = 0;
        for (Class cls: bloatedClassFiles){
            String newline = "\n";
            System.out.println("Java Class: " + cls.getSimpleName() + " is too large " + newline);
            for(String report: classReports.get(cls)){
                System.out.println(report + newline);
            }
            bloated++;
        }
        double percent = (bloated /numberOfSourceFiles) * 100;
        System.out.println(Bloatable.df.format(percent) + "% of the files in your project are bloated");
    }

}
