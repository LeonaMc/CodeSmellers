package CodeSmells;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Inspection {
    private HashMap<String, Report> reports; // Key is Smell name, report holds all data found in project related to that smell
    private ArrayList<Class> loadedClasses;
    private ArrayList<File> javaSourceFiles;

    public Inspection(ArrayList<Class> loadedClasses, ArrayList<File> javaSource){
        this.loadedClasses = new ArrayList<>(loadedClasses);
        this.javaSourceFiles = new ArrayList<>(javaSource);
        reports = new HashMap<>();
    }
    // runs feature envy, sets percent of affected files and puts report in hashmap
    private void runFeatureEnvy(){
        final String FEATURE_ENVY = "FeatureEnvy";
        FeatureEnvy featureEnvy = new FeatureEnvy(javaSourceFiles,loadedClasses);
        featureEnvy.formatData();
        featureEnvy.returnReport().setPercentage(loadedClasses.size());
        reports.put(FEATURE_ENVY,featureEnvy.returnReport());
    }
    // runs large class, sets percent of affected files and puts report in hashmap
    private void runLargeClass(){
        final String LARGE_CLASS = "LargeClass";
        LargeClass largeClass = new LargeClass(javaSourceFiles,loadedClasses);
        largeClass.findLargeFiles();
        largeClass.reflectClass();
        largeClass.getReport().setPercentage(loadedClasses.size());
        reports.put(LARGE_CLASS,largeClass.getReport());
    }
    // runs long method, sets percent of affected files and puts report in hashmap
    private void runLongMethod(){
        final String LONG_METHOD = "LongMethod";
        LongMethods longMethods = new LongMethods(loadedClasses,javaSourceFiles);
        longMethods.reflectClass();
        longMethods.getReport().setPercentage(loadedClasses.size());
        reports.put(LONG_METHOD,longMethods.getReport());

    }
    // runs long param list, sets percent of affected files and puts report in hashmap
    private void runLongParamList(){
        final String LONG_PARAM = "LongParameter";
        LongParamList longParamList = new LongParamList(loadedClasses);
        longParamList.reflectClass();
        longParamList.getReport().setPercentage(loadedClasses.size());
        reports.put(LONG_PARAM,longParamList.getReport());
    }
    // runs lazy class, sets percent of affected files and puts report in hashmap
    private void runLazyClass(){
        //Lazy Class not finished
        final String LAZY = "LazyClass";
        LazyClass lazyClass = new LazyClass(loadedClasses,javaSourceFiles);
        lazyClass.reflectClass();
        lazyClass.getReport().setPercentage(loadedClasses.size());
        reports.put(LAZY,lazyClass.getReport());
    }
    // runs primitive obsession, sets percent of affected files and puts report in hashmap
    private void runPrimitiveObsession(){
        final String PRIMITIVE = "PrimitiveObsession";
        PrimitiveObsession primitiveObsession = new PrimitiveObsession(loadedClasses);
        primitiveObsession.reflectClass();
        primitiveObsession.getReport().setPercentage(loadedClasses.size());
        reports.put(PRIMITIVE,primitiveObsession.getReport());
    }
    // runs too many literals, sets percent of affected files and puts report in hashmap
    private void runTooManyLiterals(){
        final String LITERALS = "TooManyLiterals";
        TooManyLiterals tooManyLiterals = new TooManyLiterals(loadedClasses);
        tooManyLiterals.reflectClass();
        tooManyLiterals.getReport().setPercentage(loadedClasses.size());
        reports.put(LITERALS,tooManyLiterals.getReport());
    }

    private void reportToFile() throws IOException {
        ArrayList<String> names = new ArrayList<>();
        names.add("LargeClass");
        names.add("LongMethod");
        names.add("LongParameter");
        names.add("LazyClass");
        names.add("PrimitiveObsession");
        names.add("TooManyLiterals");
        names.add("FeatureEnvy");

        File dir = new File("/src/reportLocation");
        if(dir.mkdir()){
            System.out.println("dir created\n");
        }
        else{
            System.out.println("Not\n");
        }

        File file = new File("/src/reportLocation/report.txt");
        PrintStream fileWriter = null;

        try {
            fileWriter = new PrintStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setOut(fileWriter);
        for(String name: names){
            System.out.println("======================================== "+name+" ========================================");
            Report report = getReports().get(name);
            ArrayList<Class> affectedClasses = report.getAffectedClasses();
            if (report.isClean()){
                System.out.println("Project is clean for "+ report.getName() +" code smell\n");
            }
            else{
                System.out.println(report.printNumAffectedClasses());
                System.out.println(report.percentToString() + " of files affected by " + report.getName());
                for(Class cls : affectedClasses){
                    if(name.equalsIgnoreCase("LongMethod")){
                        System.out.println(report.getLongMethodData().get(report.getReportData().get(cls)));
                    }
                    else{
                        System.out.println(report.getReportData().get(cls));
                    }
                }
            }
        }
        if (fileWriter != null) {
            fileWriter.close();
        }
    }

    // runs all inspection classes
    public void runInspection() throws IOException {
        runFeatureEnvy();
        runLargeClass();
        runLazyClass();
        runLongMethod();
        runLongParamList();
        runPrimitiveObsession();
        runTooManyLiterals();
        reportToFile();
    }

    // gets
    public HashMap<String, Report> getReports() {
        return reports;
    }
}
