package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
// for large class get number of lines and number of primitives get percent of lines used by primitives
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

        System.out.println(packageArray[0] + " " + packageArray[1]);

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
        ArrayList<Class> largeClassEffectedClasses = findLargeClasses.getReport().getEffectedClasses();
        Report largeClassReport = findLargeClasses.getReport();
        double largePercent = ((double)largeClassReport.getEffectedClasses().size()/(double)loadedClasses.size())*100;
        System.out.println("Number of effected classes = " + largeClassReport.getEffectedClasses().size());
        System.out.println(largeClassReport.df.format(largePercent) + "%" + " of files in project effected");
        for(Class cls:largeClassEffectedClasses){
            System.out.println(largeClassReport.getCodeSmellData().get(cls));
        }
        System.out.println(newline);

        // Test for Long Methods
       // System.out.println("==========================Test Long Method==========================");
       // System.out.println("\n");
       // LongMethods findLongMethods = new LongMethods(loadedClasses, javaSource);
       // System.out.println("\nMethod Body From Find LongMethods");
       // findLongMethods.reflectClass();

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
