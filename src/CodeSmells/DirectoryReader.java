package CodeSmells;

import Annotation.ClassLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class DirectoryReader extends PackageFinder {
    private Queue<File> subDirectoryQueue; // used in getFiles() holds each subdirectory found in a project directory
    private ArrayList<File> javaSourceArrayList; // holds java source files found in getFiles
    private ArrayList<File> classArrayList; // holds .class files found in getFiles
    private String special; // special character "\\\\" for windows, "/" for linux or mac, set in constructor. Used in class loader
    private ArrayList<Class> loadedClasses = new ArrayList<>(); // array of loaded classes ready for reflection, filled in loadClasses()
    private int directoryLevel = 0;

    public DirectoryReader() {
        subDirectoryQueue = new LinkedList<>();
        javaSourceArrayList = new ArrayList<>();
        classArrayList = new ArrayList<>();

        if (System.getProperty("os.name").contains("Windows")) special = "\\\\";
        else if (System.getProperty("os.name").contains("Linux") ||
                System.getProperty("os.name").contains("Mac")) special = "/";
    }

    // getter
    public ArrayList<Class> getLoadedClasses() {
        return loadedClasses;
    }

    // getter
    public ArrayList<File> getJavaSourceArrayList() {
        return javaSourceArrayList;
    }

    // getter
    public ArrayList<File> getClassArrayList() {
        return classArrayList;
    }

    public int getDirectoryLevel() {
        return directoryLevel;
    }

    private String getClassName(int index){
        return getClassArrayList().get(index).getName();
    }

    /*Recursive Method
    *searches project directory for subfolders, .java and .class files
    *adds subfolders to queue, .java and .class files to seperate ArrayLists
    *Recursive call takes Queue.remove() as argument
    *@Param String directory is a string representing the root filepath of a project*/
    public void getFiles(String directory) {
        File rootDirectory;
        rootDirectory = new File(directory); // new file with path of @Param directory
        File[] rootDirectoryFiles = rootDirectory.listFiles(); // listFiles() returns array of Files in directory

        if (rootDirectoryFiles != null) {
            for (File file : rootDirectoryFiles) { // iterate through everything in this directory
                if (file.isFile()) { // if File
                    if (file.getName().endsWith(".java")) { // and if .java
                        javaSourceArrayList.add(file); // add to javaSource Array
                    } else if (file.getName().endsWith(".class")) { // else if File is .class
                        classArrayList.add(file); // add to class Array
                    }
                } else if (file.isDirectory()) {
                    subDirectoryQueue.add(file);  // if directory add to queue
                }
            }
        }

        if (!subDirectoryQueue.isEmpty()) { // recursion stops on empty queue
            directoryLevel += 1;
            getFiles(subDirectoryQueue.remove().getPath()); // remove earliest added directory and pass as Param to recursive call
        }
    }

    public String[] getClasspath(String path) {
        String[] splitPath;
        splitPath = path.split(special);
        boolean isEclipse = false;

        int index = 0;
        for (int i = 0; i < splitPath.length; i++) {
            if (splitPath[i].compareTo("production") == 0) {
                index = i + 1;
            } else if (splitPath[i].compareTo("bin") == 0 && !splitPath[i + 1].contains(".class")) { // find classpath if eclipse project
                isEclipse = true;
                index = i + 1;
            } else if (splitPath[i].compareTo("bin") == 0 && splitPath[i + 1].contains(".class")) {  // eclipse project with no package name
                index = i;
            }
        }

        String[] relevantPath = new String[2];
        relevantPath[0] = splitPath[index]; // add package name
        relevantPath[1] = "";

        if (isEclipse) {
            for (int i = 0; i <= index - 1; i++) {
                relevantPath[1] += splitPath[i] + special; // add root to classpath
            }
        } else
            for (int i = 0; i <= index; i++) {
                relevantPath[1] += splitPath[i] + special; // add root to classpath
            }
        return relevantPath; // array holding package name and path to root of package classpath
    }

    /*ClassLoader method
    * Loads classes from different projects or directories
    * loaded classes can then be reflected on
    * @Param String[] packageArray see getClassPath() def and implementation after this method
    * packageArray[0] = package name if there is one
    * packageArray[1] = classpath(just a filepath that is the root folder for the .class files) for package*/
    @ClassLoader
    public void loadClasses(String[] packageArray) throws FileNotFoundException {
        String path = packageArray[1];
        URL[] classUrl = new URL[0];
        try {
            classUrl = new URL[]{new URL("file:" + path + special)}; // classLoader takes URL array as Param
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        URLClassLoader cl = URLClassLoader.newInstance(classUrl);
        int index = 0;
        for (int i = 0; i < getClassArrayList().size(); i++) { // iterate through .class files
            if(!getClassArrayList().get(i).getName().contains("$")){ // don't try to load inner classes. Inner classes are compiled separate to the class they are defined in and have a $ in the name
                String className = getClassName(i).substring(0, getClassName(i).length() - 6); // remove .class from string
                String packageName = getKeyword("package", getJavaSourceArrayList().get(index));

                if (packageName != null) { // check if java source files belong to a package
                    className = packageName + "." + className; // if class has package set className to packageName.className, classLoader won't work otherwise
                }
                try {
                    Class loadedClass = cl.loadClass(className); // load classes for reflection
                    loadedClasses.add(loadedClass); // all new Class objects added to loadedClasses Array
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(index < getJavaSourceArrayList().size() - 1){
                    index++;
                }
            }
        }
    }
}