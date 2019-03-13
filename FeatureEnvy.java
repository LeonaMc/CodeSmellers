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
 * */

package CodeSmellers;

import javafx.beans.binding.StringBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class FeatureEnvy {

    //  private ArrayList<Class> javaClasses;
    private ArrayList<File> javaSources;

    FeatureEnvy(ArrayList<File> javaSources) {
        this.javaSources = javaSources;
    }

    //   Get class names
    private ArrayList<String> classNames = new ArrayList<>();

    public void getClassNames() {
        for (File file : javaSources) {
            if(!file.getName().toLowerCase().contains("test")) { //ignore tests
                classNames.add(file.getName().replace(".java", ""));
            }
        }
    }

    //    Searches through each file looking for class name
//    Adds instantiated name to Array List of Hash Maps
//    Key will be instantiated Names. value will be class name
//    eg. Class foo = new Class() -> key=foo value=Class
//    String array list will just hold instantiated class names like foo
    private ArrayList<HashMap<String, String>> instantiatedToClassHash = new ArrayList<>();
    private ArrayList<String> instantiatedClassNames = new ArrayList<>();

    public void getInstantiatedNames() throws FileNotFoundException {
        System.out.println("Class Names: "+classNames.toString());
        for (File file : javaSources) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split(" ");

                int i = 0;
                for (String word : words) {
                    if (classNames.contains(word) && !word.toLowerCase().contains("test") && !word.contains("class")) { //ignore tests
                        // prevents duplicates
                        System.out.println("Array: "+Arrays.toString(words));
                        if (!instantiatedClassNames.contains(words[i + 1].replaceAll("[^A-Za-z0-9]", ""))) {

                            instantiatedClassNames.add(words[i + 1].replaceAll("[^A-Za-z0-9]", ""));
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(words[i + 1].replaceAll("[^A-Za-z0-9]", ""), word);
                            instantiatedToClassHash.add(hashMap);
                       }
                    }
                    i++;
                }
            }
        }
    }

    private HashMap<String, Integer> classToNumberOfCalls = new HashMap<>();
    private void classNamesAsKeys () {
        for (String name : classNames) {
            classToNumberOfCalls.put(name, 0);
        }
    }
    public void countFeatureEnvy () throws FileNotFoundException {
        classNamesAsKeys();
//            TODO finish this method
        int i = 0;
        int count = 0;
        for(String instantiatedName: instantiatedClassNames){
            System.out.println("****"+instantiatedClassNames);
            for(File file: javaSources){
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] words = line.split(" ");
                    for (String word : words) {
                        if (word.contains(instantiatedName)) {
                            String className = instantiatedToClassHash.get(i).get(instantiatedName);
                            classToNumberOfCalls.put(className, classToNumberOfCalls.get(className) + 1);
                        }
                    }
                }
            }
            i++;
        }
        System.out.println("Count Feature Envy: "+classToNumberOfCalls.toString());
    }
}

