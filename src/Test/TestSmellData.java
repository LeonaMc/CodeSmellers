package Test;

import CodeSmells.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class TestSmellData {
    public static void main(String[] args) throws IOException {
        String newline = "\n";
        String[] packageArray = new String[2];
        DirectoryReader directoryReader = new DirectoryReader();
        String directoryPath = ""; // Add path to root of directory here
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

        //Bloat Tests
        //Large Class Finished
        System.out.println("==========================TestSmellData Large Class==========================");
        LargeClass findLargeClasses = new LargeClass(javaSource, loadedClasses);
        findLargeClasses.findLargeFiles();
        findLargeClasses.reflectClass();
        Report largeClassReport = findLargeClasses.getReport();
        ArrayList<Class> largeClassAffectedClasses = largeClassReport.getAffectedClasses();
        largeClassReport.setPercentage(loadedClasses.size());
        System.out.println(largeClassReport.printNumAffectedClasses());
        System.out.println(largeClassReport.percentToString() + " of files in project affected by Large Class code smell");
        if(largeClassReport.isClean()){
            System.out.println("Project is clean for Large Class code smell\n");
        }
        else{
            for (Class cls : largeClassAffectedClasses) {
                System.out.println(largeClassReport.getReportData().get(cls));
            }
        }

        //Long Methods Finished
        System.out.println("==========================TestSmellData Long Method==========================");
        LongMethods findLongMethods = new LongMethods(loadedClasses, javaSource);
        findLongMethods.reflectClass();
        Report longMethodReport = findLongMethods.getReport();
        longMethodReport.setPercentage(loadedClasses.size());
        ArrayList<Class> longMethodEffectedClasses = longMethodReport.getAffectedClasses();
        System.out.println(longMethodReport.printNumAffectedClasses());
        System.out.println(longMethodReport.percentToString() + " of files in project affected by Long Method code smell\n");
        if (longMethodReport.isClean()){
            System.out.println("Project is clean for Long Method code smell\n");
        }
        else{
            for (Class cls : longMethodEffectedClasses) {
                System.out.println(longMethodReport.getLongMethodData().get(longMethodReport.getReportData().get(cls)));
                System.out.print(newline);
            }
        }

        //Primitive Obsession Finished
        System.out.println("==========================TestSmellData Primitive Obsession==========================");
        PrimitiveObsession primitiveObsession = new PrimitiveObsession(loadedClasses);
        primitiveObsession.reflectClass();
        Report primitiveReport = primitiveObsession.getReport();
        ArrayList<Class> primitiveAffectedClasses = primitiveReport.getAffectedClasses();
        System.out.println("Number of affected classes = " + primitiveAffectedClasses.size());
        primitiveReport.setPercentage(loadedClasses.size());
        System.out.println(primitiveReport.percentToString() + " of files in project affected by primitive obsession code smell\n");
        if(primitiveReport.isClean()){
            System.out.println("Project is clean for Primitive Obsession code smell\n");
        }
        else{
            for (Class cls : primitiveAffectedClasses) {
                System.out.println(primitiveReport.getReportData().get(cls) +newline);
            }
        }

        //Long Param List Finished
        System.out.println("==========================TestSmellData Long Parameter List==========================");
        LongParamList longParamList = new LongParamList(loadedClasses);
        longParamList.reflectClass();
        Report longParamReport = longParamList.getReport();
        ArrayList<Class> longParamAffectedClasses = longParamReport.getAffectedClasses();
        System.out.println(longParamReport.printNumAffectedClasses());
        longParamReport.setPercentage(loadedClasses.size());
        System.out.println(longParamReport.percentToString() + " of files in project affected by long parameter code smell\n");
        if (longParamReport.isClean()){
            System.out.println("Project is clean for Long Parameter List code smell\n");
        }
        else{
            for(Class cls : longParamAffectedClasses){
                System.out.print(longParamReport.getReportData().get(cls));
            }
        }

        //Too Many Literals Finished
        System.out.println("==========================TestSmellData Too Many Literals==========================");
        TooManyLiterals tooManyLiterals = new TooManyLiterals(loadedClasses);
        tooManyLiterals.reflectClass();
        Report literalReport = tooManyLiterals.getReport();
        System.out.println(literalReport.printNumAffectedClasses());
        literalReport.setPercentage(loadedClasses.size());
        ArrayList<Class> literalAffectedClasses = literalReport.getAffectedClasses();
        System.out.println(literalReport.percentToString() + " of files affected by TooManyLiterals code smell\n");
        if(literalReport.isClean()){
            System.out.println("Project is clean for Too Many Literal code smell\n");
        }
        else{
            for (Class cls: literalAffectedClasses){
                System.out.println(literalReport.getReportData().get(cls));
            }
        }

        //Lazy Class Finished
        System.out.println("=============================TestSmellData Lazy Class=============================");
        LazyClass lazyClass = new LazyClass(loadedClasses, javaSource);
        lazyClass.reflectClass();
        Report lazyClassReport = lazyClass.getReport();
        ArrayList<Class> lazyAffectedClasses = lazyClassReport.getAffectedClasses();
        System.out.println("Number of affected classes = " + lazyAffectedClasses.size());
        lazyClassReport.setPercentage(loadedClasses.size());
        System.out.println(lazyClassReport.percentToString() + " of files in project are affected\n");
        if(largeClassReport.isClean()){
            System.out.println("Project is clean for Lazy Class code smell\n");
        }
        else{
            for (Class affectedClass : lazyAffectedClasses) {
                System.out.println("Affected class name = " + affectedClass.getSimpleName());
                System.out.println(lazyClassReport.getReportData().get(affectedClass));
            }
        }

        //Feature Envy Finished
        System.out.println("==========================TestSmellData Feature Envy==========================");
        FeatureEnvy featureEnvy = new FeatureEnvy(javaSource,loadedClasses);
        featureEnvy.formatData();
        Report featureReport = featureEnvy.returnReport();
        featureReport.printNumAffectedClasses();
        featureReport.setPercentage(loadedClasses.size());
        System.out.println(featureReport.percentToString() + " of files in project are affected\n");
        ArrayList<Class> envyClasses = featureReport.getAffectedClasses();
        if(featureReport.isClean()){
            System.out.println("Project is clean for Feature Envy code smell\n");
        }
        else{
            for (Class cls: envyClasses){
                System.out.println(featureReport.getReportData().get(cls) + newline);
            }
        }
    }
}
