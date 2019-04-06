package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
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
//        final String LAZY = "LazyClass";
//        LazyClass lazyClass = new LazyClass(loadedClasses,javaSourceFiles);
//        try {
//            lazyClass.findSmallClass();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        lazyClass.reflectClass();
//        lazyClass.getReport().setPercentage(loadedClasses.size());
//        reports.put(LAZY,lazyClass.getReport());
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
        // TO DO
    }
    // runs all inspection classes
    public void runInspection(){
        runFeatureEnvy();
        runLargeClass();
        //runLazyClass();
        runLongMethod();
        runLongParamList();
        //runPrimitiveObsession();
        //runTooManyLiterals();
    }
    // gets
    public HashMap<String, Report> getReports() {
        return reports;
    }
}
