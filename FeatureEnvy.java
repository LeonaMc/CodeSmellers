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
 */

package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureEnvy implements SourceReadable,Reportable{

    //  private ArrayList<Class> javaClasses;
    private ArrayList<File> javaSources;
    private ArrayList<String> classNames = new ArrayList<>();
    private HashMap<String, String> instantiatedNameToClassName = new HashMap<>();
    private static ArrayList<EachClassFeatureEnvySmell> featureEnvySmellsList = new ArrayList<>();

    FeatureEnvy(ArrayList<File> javaSources) {
        this.javaSources = javaSources;
    }
    @Override
    public void printReport() {

    }
//    Private inner class will hold each class
    private class EachClassFeatureEnvySmell{
        private  String className;
        private String otherClassName;
        private int numberOfCalls;

        EachClassFeatureEnvySmell(String name, String otherName, int numOfCalls) {
            className = name;
            otherClassName= otherName;
            numberOfCalls = numOfCalls;
        }
        public String toString(){
            return className + " calls " + otherClassName +" " +numberOfCalls+" times";
        }
    }

    //   Get class names
    public void getClassNames() {
        for (File file : javaSources) {
            //ignore tests and main
            if(checkValidFile(file)) {
                classNames.add(file.getName().replace(".java", ""));
            }
        }
    }

    public void getInstantiatedNames() throws IOException {
        for(File file: javaSources) {
            if (checkValidFile(file)) {
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

    public void getNumberOfOtherClassCalls() throws IOException {
        for(File file : javaSources){
            if(checkValidFile(file)){
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                int count = 0;
                for(String instantiatedName : instantiatedNameToClassName.keySet()){
                    Pattern pattern = Pattern.compile("\\b"+instantiatedName+"\\b");
                    Matcher matcher = pattern.matcher(content);
                    boolean foundFlag = false;
                    while(matcher.find()){
                        if(instantiatedName.contentEquals(matcher.group(0))) {
                            foundFlag = !foundFlag;
                            count++;
                        }
                    }
                    if(foundFlag){
                        featureEnvySmellsList.add(new EachClassFeatureEnvySmell
                                (file.getName().replace(".java",""),
                                        instantiatedNameToClassName.get(instantiatedName), count));
                    }
                }
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

