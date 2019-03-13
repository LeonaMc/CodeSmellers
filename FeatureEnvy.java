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

import java.io.File;
import java.util.ArrayList;

public class FeatureEnvy {

    private Class[] javaClasses;
    private File[] javaSources;

    FeatureEnvy(Class[] javaClasses, File[] javaSources){
        this.javaClasses = javaClasses;
        this.javaSources = javaSources;
    }

    ArrayList<String> classNames = new ArrayList<>();
    private void getClassNames(){
        for(Class cl : javaClasses){
            System.out.println(cl.getName());
        }
    }
}
