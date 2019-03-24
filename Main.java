package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

// for large class get number of lines and number of primitives get percent of lines used by primitives
// Change every instance of getIndexOf() in source and class arraylists with better solution
// Add logic to smell class or report to catch cases of no smell detected
// Add if reportFromClass.getEffectedClasses > 0 boolean classObject.IsEffected = true
// Can be used to reference which classes are effected as some code smells can be an indicator for others e.g large class with small number of methods
// might indicate long methods or primitive obsession etc
// further implementation of LazyClass needed. Small class alone does not indicate a lazy class, check for use in other classes(reflection). Some code smells are mutually exclusive e.g GodClass/LazyClass etc
// duplicate code starts by wednesday
// Need to add catch for compiler created variables in any class that will access class fields PrimitiveObsession/TooManyLiterals. Check bookmark.
// LongMethods working need to figure out what data is relevant to add to report, effected methods or string
// Finish TooManyLiterals check bookmark
// report file path of effected classes to user
public class Main { // add to misc smell class check for bad encapsulation e.g check public class variables
    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InstantiationException {
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
        // Test for Large Classes
        System.out.println("==========================Test Large Class==========================");
        LargeClass findLargeClasses = new LargeClass(javaSource,loadedClasses);
        findLargeClasses.findLargeFiles();
        findLargeClasses.reflectClass();
        Report largeClassReport = findLargeClasses.getReport();
        ArrayList<Class> largeClassEffectedClasses = largeClassReport.getEffectedClasses();
        double largePercent = ((double)largeClassReport.getEffectedClasses().size()/(double)loadedClasses.size())*100;
        System.out.println("Number of effected classes = " + largeClassReport.getEffectedClasses().size());
        System.out.println(largeClassReport.df.format(largePercent) + "%" + " of files in project effected");
        for(Class cls:largeClassEffectedClasses){
            System.out.println(largeClassReport.getCodeSmellData().get(cls));
        }
        System.out.println(newline);

       //  Test for Long Methods
//       System.out.println("==========================Test Long Method==========================");
//       LongMethods findLongMethods = new LongMethods(loadedClasses, javaSource);
//       findLongMethods.reflectClass();
//       Report longMethodReport = findLongMethods.getReport();
//       ArrayList<Class> longMethodEffectedClasses = longMethodReport.getEffectedClasses();
//       System.out.println("Size = " + longMethodEffectedClasses.size() + newline);
//       for(Class cls : longMethodEffectedClasses){
//           for(Method method: cls.getDeclaredMethods()){
//               System.out.println( + newline); // toString bug can be used
//           }
//
//       }

        // Test for primitive obsession
        System.out.println("==========================Test Primitive Obsession==========================");
        PrimitiveObsession primitiveObsession = new PrimitiveObsession(loadedClasses);
        primitiveObsession.reflectClass();
        Report primitiveReport = primitiveObsession.getReport();
        ArrayList<Class> primitiveEffectedClasses = primitiveReport.getEffectedClasses();
        System.out.println("Number of effected classes = " + primitiveEffectedClasses.size());
        double primitivePercent = ((double)primitiveEffectedClasses.size()/loadedClasses.size())*100;
        System.out.println(primitiveReport.df.format(primitivePercent) + "% of files in project effected\n");
        for(Class cls : primitiveEffectedClasses){
            System.out.println("Class " + cls.getSimpleName() + " has " + primitiveReport.getCodeSmellData().get(cls) + " primitive fields");
        }

        //Test long param list for methods
        System.out.println("==========================Test Long Method Param List==========================");
        LongParamList longParamList = new LongParamList(loadedClasses);
        longParamList.reflectClass();
        Report longParamReport = longParamList.getReport();
        ArrayList<Class> longParam = longParamReport.getEffectedClasses();
        System.out.println("Number of effected Classes = " + longParamReport.getEffectedClasses().size());
        double longParamPercent = ((double)longParam.size()/(double)loadedClasses.size())*100;
        System.out.println(longParamReport.df.format(longParamPercent) + "% of files effected");
        for (Class longParamClass : longParam){
            System.out.println("\nName of effected class = " + longParamClass.getSimpleName());
            ArrayList<Method> effectedMethods = (ArrayList<Method>)longParamReport.getCodeSmellData().get(longParamClass);

            System.out.println("Number of effected methods = " + effectedMethods.size());
            double methodPercent = ((double)effectedMethods.size()/(double)longParamClass.getDeclaredMethods().length)*100;
            System.out.println(longParamReport.df.format(methodPercent) + "% of methods effected\n");
            System.out.println("Method names");
            for (Method method: effectedMethods){
                System.out.println(method.getName() + " has " + method.getParameters().length + " parameters");
            }
        }
        System.out.println(newline);
        //Test for Too Many Literals
        //System.out.println("==========================Test Too Many Literals==========================");
        //TooManyLiterals tooManyLiterals = new TooManyLiterals(loadedClasses);
        //tooManyLiterals.ref();

        // Test for Lazy Class
        System.out.println("==========================Test Lazy Class==========================");
        LazyClass lazyClass = new LazyClass(loadedClasses,javaSource);
        if(lazyClass.findSmallClass()){
            lazyClass.reflectClass();
            Report lazyClassReport = lazyClass.getLazyClassReport();
            // for loop for smelly classes in report
            ArrayList<Class> lazyEffectedClasses = lazyClassReport.getEffectedClasses();
            System.out.println("Number of effected classes = " + lazyEffectedClasses.size());
            double lazyPercent = ((double) lazyEffectedClasses.size()/(double) loadedClasses.size()) * 100;
            System.out.println(largeClassReport.df.format(lazyPercent) + "% of files in project are effected");
            for (Class effectedClass: lazyEffectedClasses){
                System.out.println("\nEffected class name = " + effectedClass.getSimpleName());
                System.out.println(lazyClassReport.getCodeSmellData().get(effectedClass));
            }
        }
    }
}
