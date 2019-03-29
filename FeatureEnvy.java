/*
 * Class will be used to determine the Feature Envy code smell in a code base and return a smell rating
 * Feature Envy is when one class uses another class's methods too much
 *
 * Class will not use reflection as it does not show method calls inside other methods which may hide
 * a lot of feature envy
 *
 * Simplest way would be to search for instances of '.' in .java files as this hints at other class method calls.
 * Unfortunately this also counts static Java libraries which shouldn't be considered with the Feature Envy smell,
 * so this way isn't used.
 *
 * To test for Feature Envy:
 *  1. Get all class names as Strings from .java files passed to constructor and store both name and .java file
 *  2. Treat every .java file as text.
 *  3. Search through each .java file looking for instances of .java names.
 *     This will show when another Class is being instantiated.
 *  4. Take the next word after the instantiation.
 *     eg Class name = new Class() -> will take 'name'
 *  5. Search the file for the number of times 'name' appears
 *     The number of times name appears will highlight often a class is using another
 *     class's methods as it will appear like 'name.methodCall()'
 *  6. Return a smell rating based on a heuristic
 *
 *  Private inner class EachClassSmell will hold each class in the directory being examined
 *  EachClassSmell will contain a hash map of all the other classes and how many times they are called.
 */

package CodeSmellers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureEnvy implements SourceReadable,Reportable{

    private ArrayList<File> javaSources;
    private HashMap<String, String> instantiatedNameToClassName = new HashMap<>();
    protected static ArrayList<EachClassSmell> classSmellsList = new ArrayList<>();

    FeatureEnvy(ArrayList<File> javaSources) {
        this.javaSources = javaSources;
    }
    public float getDirectorySmell(){
        findFeatureEnvy();
        int numOfClasses = classSmellsList.size();
        float percentageSmell = 0f;
        int totalNumOfCalls = 0;

        for(EachClassSmell classSmell : classSmellsList){
            totalNumOfCalls += classSmell.getNumOfOtherClassCalls();
        }
        return 0;
    }
    private void findFeatureEnvy() {
        try {
            findInstantiatedNames();
            findNumberOfOtherClassCalls();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    Ignore this is for myself
    public void printReport() {
        for(EachClassSmell smell : classSmellsList) {
            System.out.println(smell.eachClassSmellToString());
        }
    }

    @Override
    public Report returnReport() {
        return null;
    }

    @Override
    public void formatData() {

    }

    //    Private inner class will hold each class, a list of other classes, and how many times the class uses those
    private class EachClassSmell{
        private  String className;
        private HashMap<String,Integer> otherClassToNumberOfCalls = new HashMap<>();

        EachClassSmell(String name, ArrayList<String> classNames) {
            className = name;
            for(String className: classNames){
                otherClassToNumberOfCalls.put(className,0);
            }
        }
        private void addClassAndNumOfCalls(String otherClassName, int count){
            otherClassToNumberOfCalls.put(otherClassName, otherClassToNumberOfCalls.get(otherClassName) + count);
        }
        private String eachClassSmellToString(){
            return className +": "+otherClassToNumberOfCalls.toString()+"\n";
        }
        private float getNumOfOtherClassCalls(){
            int num = 0;
            for(Map.Entry<String,Integer> classCalls : otherClassToNumberOfCalls.entrySet()){
                num += classCalls.getValue();
            }
            return num;
        }
    }
    private ArrayList<String> getClassNames() {
        ArrayList<String> classNames = new ArrayList<>();
        for (File file : javaSources) {
            if(checkValidFile(file)) {
                classNames.add(file.getName().replace(".java", ""));
            }
        }
        return classNames;
    }

    private void findInstantiatedNames() throws IOException {
        for(File file: javaSources) {
            if (checkValidFile(file)) {
                ArrayList<String> classNames = getClassNames();
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                for (String classname : classNames) {
                    Pattern pattern = Pattern.compile(classname + "\\W+(\\w+)");
                    Matcher matcher = pattern.matcher(content);
                    while(matcher.find()) {
                        if(checkValidRegex(matcher.group(0))) {
                            instantiatedNameToClassName.put(matcher.group(1)+".",classname); //dot is used for method calls
                        }
                    }
                }
            }
        }
    }

    private void findNumberOfOtherClassCalls() throws IOException {
        for(File file : javaSources){
            String fileToClassName = file.getName().replace(".java","");
            if(checkValidFile(file)) {
                EachClassSmell smell = new EachClassSmell(fileToClassName, getClassNames());
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                int count = 0;
                for (String instantiatedName : instantiatedNameToClassName.keySet()) {
                    Pattern pattern = Pattern.compile(instantiatedName);
                    Matcher matcher = pattern.matcher(content);
                    boolean foundFlag = false;
                    while (matcher.find()) {
                        if (instantiatedName.contentEquals(matcher.group(0))) {
                            foundFlag = !foundFlag;
                            count++;
                        }
                    }
                    if (foundFlag) {
                        smell.addClassAndNumOfCalls(instantiatedNameToClassName.get(instantiatedName), count);
                        count = 0;
                    }
                }
                classSmellsList.add(smell);
            }
        }
    }
    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        return null;
    }

    private boolean checkValidRegex(String input){
        String[] invalidChars = {"{","(",")"};
        for(String string: invalidChars){
            if(input.contains(string)){
                return false;
            }
        }
        return true;
    }
    private boolean checkValidFile(File file){
        String [] invalidStrings = {"test","main"};
        for(String string : invalidStrings){
            if(file.getName().toLowerCase().contains(string)){
                return false;
            }
        }
        return true;
    }


}

