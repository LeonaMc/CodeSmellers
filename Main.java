package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main { //extends Application

    // Setting up GUI
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            Parent root = FXMLLoader.load(this.getClass().getResource("/CodeSmellers/Model/WelcomeScreen.fxml"));
//            primaryStage.setScene(new Scene(root));
//            primaryStage.show();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String newline = "\n";
        String[] packageArray = new String[2];
        DirectoryReader directoryReader = new DirectoryReader();
        String directoryPath = ""; // Add path to root of directory here
        directoryReader.getFiles(directoryPath);

        // Splash Screen
//        SplashScreen splashScreen = new SplashScreen();
//        splashScreen.setVisible(true);
//        Thread thread = Thread.currentThread();
//        thread.sleep(2500);
//        splashScreen.dispose();

//        // Calling the Welcome Screen for GUI in main
//        launch(args);

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
        // Large Class Finished
//        System.out.println("==========================Test Large Class==========================");
//        LargeClass findLargeClasses = new LargeClass(javaSource, loadedClasses);
//        findLargeClasses.findLargeFiles();
//        findLargeClasses.reflectClass();
//        Report largeClassReport = findLargeClasses.getReport();
//        ArrayList<Class> largeClassAffectedClasses = largeClassReport.getAffectedClasses();
//        largeClassReport.setPercentage(loadedClasses.size());
//        System.out.println(largeClassReport.printSizeOfAffectedClasses());
//        System.out.println(largeClassReport.getPercentage() + " of files in project effected");
//        for (Class cls : largeClassAffectedClasses) {
//            System.out.println(largeClassReport.getReportData().get(cls));
//
//        }

        //Long Methods finished
//        System.out.println("==========================Test Long Method==========================");
//        LongMethods findLongMethods = new LongMethods(loadedClasses, javaSource);
//        findLongMethods.reflectClass();
//        Report longMethodReport = findLongMethods.getReport();
//        longMethodReport.setPercentage(loadedClasses.size());
//        ArrayList<Class> longMethodEffectedClasses = longMethodReport.getAffectedClasses();
//        System.out.println(longMethodReport.printSizeOfAffectedClasses());
//        System.out.println(longMethodReport.getPercentage() + " of files in project effected");
//        System.out.print(newline);
//        for (Class cls : longMethodEffectedClasses) {
//            System.out.println(longMethodReport.getLongMethodData().get(longMethodReport.getReportData().get(cls)));
//            System.out.print(newline);
//        }

        //Test for primitive obsession
//        System.out.println("==========================Test Primitive Obsession==========================");
//        PrimitiveObsession primitiveObsession = new PrimitiveObsession(loadedClasses);
//        primitiveObsession.reflectClass();
//        Report primitiveReport = primitiveObsession.getReport();
//        ArrayList<Class> primitiveAffectedClasses = primitiveReport.getAffectedClasses();
//        System.out.println("Number of affected classes = " + primitiveAffectedClasses.size());
//        primitiveReport.setPercentage(loadedClasses.size());
//        System.out.println(primitiveReport.percentToString() + " of files in project effected\n");
//        for (Class cls : primitiveAffectedClasses) {
//            System.out.println(primitiveReport.getReportData().get(cls) +newline);
//        }

//        //Long Param List Done
//        System.out.println("==========================Test Long Parameter List==========================");
//        LongParamList longParamList = new LongParamList(loadedClasses);
//        longParamList.reflectClass();
//        Report longParamReport = longParamList.getReport();
//        ArrayList<Class> longParamAffectedClasses = longParamReport.getAffectedClasses();
//        System.out.println(longParamReport.printSizeOfAffectedClasses());
//        longParamReport.setPercentage(loadedClasses.size());
//        System.out.println(longParamReport.percentToString() + " of files affected");
//        for(Class cls : longParamAffectedClasses){
//            System.out.print(longParamReport.getReportData().get(cls));
//        }

//        System.out.println(newline);
        //Test for Too Many Literals
//        System.out.println("==========================Test Too Many Literals==========================");
//        TooManyLiterals tooManyLiterals = new TooManyLiterals(loadedClasses);
//        tooManyLiterals.reflectClass();

        //Test for Lazy Class
//        System.out.println("==========================Test Lazy Class==========================");
//        LazyClass lazyClass = new LazyClass(loadedClasses, javaSource);
//        lazyClass.reflectClass();
//        Report lazyClassReport = lazyClass.getReport();
//        ArrayList<Class> lazyAffectedClasses = lazyClassReport.getAffectedClasses();
//        System.out.println("Number of affected classes = " + lazyAffectedClasses.size());
//        lazyClassReport.setPercentage(loadedClasses.size());
//        System.out.println(lazyClassReport.percentToString() + " of files in project are affected");
//        for (Class affectedClass : lazyAffectedClasses) {
//            System.out.println("\nAffected class name = " + affectedClass.getSimpleName());
//            System.out.println(lazyClassReport.getReportData().get(affectedClass));
//        }

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
    }
}