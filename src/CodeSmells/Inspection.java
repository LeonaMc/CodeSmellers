package CodeSmells;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
// Runs all code smell classes returns a report for each smell in a hash map with codesmell name as key and report as data
public class Inspection {
    private HashMap<String, Report> reports; // Key is Smell name, report holds all data found in project related to that smell
    private ArrayList<Class> loadedClasses;
    private ArrayList<File> javaSourceFiles;
    private static String desktopPath;

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
    private void runComments(){
        final String COMMENTS = "Comments";
        Comments comments = new Comments(javaSourceFiles, loadedClasses);
        comments.reflectClass();
        comments.getReport().setPercentage(loadedClasses.size());
        reports.put(COMMENTS,comments.getReport());
    }

    private void runSwitches(){
        final String SWITCHES = "Switches";
        Switches switches = new Switches(javaSourceFiles,loadedClasses);
        switches.reflectClass();
        switches.getReport().setPercentage(loadedClasses.size());
        reports.put(SWITCHES,switches.getReport());
    }

    // opens a folder on user desktop and prints report findings to .txt file inside the folder. Name of folder is system date and time so new report each time
    private void reportToFile() throws IOException {
        ArrayList<String> names = new ArrayList<>(); // implemented late so quick fix possibly messy
        names.add("LargeClass");
        names.add("LongMethod");
        names.add("LongParameter");
        names.add("LazyClass");
        names.add("PrimitiveObsession");
        names.add("TooManyLiterals");
        names.add("FeatureEnvy");
        names.add("Comments");
        names.add("Switches");

        String fileSeperator = "";
        // possibly better to set to documents but for ease of grading report folder created on desktop
        if(System.getProperty("os.name").contains("Windows")){ // if windows set path seperator
            fileSeperator ="\\Desktop\\ReportLocation\\";
        }
        else if(System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("Mac")){ // else if Linux or mac
            fileSeperator = "/Desktop/ReportLocation/";
        }
        String path = System.getProperty("user.home") + fileSeperator; // get path to user desktop

        File dir = new File(path); // new directory "Desktop/ReportLocation"
        if(dir.mkdir()){
            System.out.println("dir created\n");
        }
        else{
            System.out.println("dir not created\n");
        }
        DateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy-HH-mm-ss"); // filename will be date/time so new file created each time
        Date date = new Date();
        String fileName = dateFormat.format(date);
        fileName = fileName+".txt";
        desktopPath = path+fileName; // set path to open file to write to
        File file = new File(path+fileName);
        PrintStream fileWriter = null; // print stream to utilise System.setOut

        try {
            fileWriter = new PrintStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setOut(fileWriter); // set system.out to printStream object
        System.out.println("Report folder created on desktop, contains report of current inspection\n");
        for(String name: names){ // for each code smell name print report contents use name as key to report hash map
            System.out.println("================= "+name+" ================="); // seperates each smell report for readability
            Report report = getReports().get(name); // get report by name of smell
            ArrayList<Class> affectedClasses = report.getAffectedClasses(); // list of classes affected by current smell used as key to print report data for current affected class
            if (report.isClean()){
                System.out.println("Project is clean for "+ report.getName() +" code smell\n");
            }
            else{
                System.out.println(report.printNumAffectedClasses()); //
                System.out.println(report.percentToString() + " of files affected by " + report.getName());
                for(Class cls : affectedClasses){ // for each affected class print its report for current smell
                    if(name.equalsIgnoreCase("LongMethod")){
                        System.out.println(report.getLongMethodData().get(report.getReportData().get(cls))); // report slightly different for long method
                    }
                    else{
                        System.out.println(report.getReportData().get(cls)); // print report data for class cls from affected classes.
                    }
                }
            }
        }
        if (fileWriter != null) {
            System.setOut(System.out);
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
        runComments();
        runSwitches();
        reportToFile();
    }

    // gets
    public HashMap<String, Report> getReports() {
        return reports;
    }

    public static String getDesktopPath() {
        return desktopPath;
    }
}
