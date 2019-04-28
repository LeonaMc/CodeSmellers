package CodeSmells;

import Annotation.Reflecting;
import Interface.Smellable;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LongMethods implements Smellable {
    private HashMap<Class, Method[]> classMethods; // map from class being inspected to long methods found in that class
    private ArrayList<Class> loadedClasses; // loaded classes from project
    private ArrayList<File> javaSourceFiles; // source files from project
    private Stack<Character> bracketStack; // used to track opening and closing brackets in method body
    private Report report; // report for this smell
    private char openingBrace;
    private char closingBrace;

    public LongMethods(ArrayList<Class> loadedClasses, ArrayList<File> javaSource) {
        this.loadedClasses = new ArrayList<>(loadedClasses);
        this.javaSourceFiles = new ArrayList<>(javaSource);
        report = new Report();
        report.setName("Long Method");
        bracketStack = new Stack<>();
        classMethods = new HashMap<>();
        openingBrace = '{'; // was originally in method body but was being added to bracket stack
        closingBrace = '}';
    }
    // helper method gets methods for each loaded class
    private void getClassMethods() {
        for (Class cls : loadedClasses) {
            classMethods.put(cls, cls.getDeclaredMethods());
        }
    }

    // gets start line of method being inspected
    // keyword is method name
    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(javaSource);
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream));
        int length = javaSource.getName().length();
        String javaName = javaSource.getName().substring(0,length-5); // remove .java from filename
        int index = 0;
        for(Class cls : loadedClasses){ // loop gets correct index in loaded classes corresponding to the current source file
            if(cls.getSimpleName().compareTo(javaName) == 0 ){
                index = loadedClasses.indexOf(cls);
                break;
            }
        }
        Class myClass = loadedClasses.get(index);
        Method myMethod = null;
        for (Method method : myClass.getDeclaredMethods()) { // get the correct method
            if (method.getName().equalsIgnoreCase(keyword)) {
                myMethod = method;
            }
        }
        String buildRegex; // regex to find method implementation in .java
        buildRegex = Modifier.toString(myMethod.getModifiers()); // get modifiers for method and add to regex
        buildRegex = buildRegex + " " + myMethod.getReturnType().getSimpleName(); // add return type to regex
        String regex = buildRegex + " " + keyword; // add method name to regex, adding comments now I can't remember why I created a new String.

        Pattern methodPattern = Pattern.compile(Pattern.quote(regex)); // regular expression to find first line of method
        Matcher matcher = methodPattern.matcher("");
        String line;
        int startLine = 0;
        int i = 0;
        try {
            while ((line = input.readLine()) != null) { // whilke file has lines to read
                matcher.reset(line); // reset matcher to new line
                i++; // incremenet i for each line passed without a match
                while (matcher.find()) {
                    startLine = i; // when match found i is the startline number
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileInputStream.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int endLine = 0;
        try {
            endLine = getMethodBody(startLine, javaSource); // call method which returns the endline number for current method
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (endLine - startLine > 30) { // if lines of method are greater than 30
            // return string to be added to report
            return "\nMethod " + keyword + " in " + javaSource.getName() + " is " + (endLine - startLine) + " lines long\n" +
                    "Method starts at line " + startLine + " and ends at line " + endLine + " in " + javaSource.getName();
        } else
            return null;
    }

    // gets method body by reading source file
    private int getMethodBody(int startLine, File javaSource) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(javaSource);
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;
        int endLine = 0, i = 0;
        boolean foundStart = false, foundEnd = false;

        while ((line = input.readLine()) != null) { // while lines to read
            i++; // increment counter
            if (i == startLine) { // if starting line number of method found
                foundStart = true; // set true
                for (int j = 0; j < line.length(); j++) { // for each character in current line
                    if (line.charAt(j) == openingBrace) { // if character is an opening brace
                        bracketStack.push(line.charAt(j)); // push opening bracket to stack, keeps track of brackets to determine end of method
                    }
                }
            } else if (foundStart) { // if start of method found
                for (int j = 0; j < line.length(); j++) { // for each character in current line
                    if (line.charAt(j) == openingBrace) { // check for opening brace
                        bracketStack.push(line.charAt(j)); // push brace to stack
                    } else if (line.charAt(j) == closingBrace) { // check for closing brace
                        bracketStack.pop(); // pop last opening brace from stack
                        if (bracketStack.isEmpty()) { // if stack empty end of method body found
                            endLine = i; // set endLine accordingly
                            foundEnd = true; //
                        }
                    }
                }
            }
            if (foundEnd) {
                break;
            }
        }
        return endLine;
    }

    @Reflecting
    @Override
    public void reflectClass() {
        getClassMethods(); // fill hashmap with methods for each class. class is key, array of methods is data
        ArrayList<Class> cleanClasses = new ArrayList<>();
        for (Class cls : loadedClasses) { // for each class
            Method[] tempClassMethods = classMethods.get(cls); // get  declared methods for current class cls
            ArrayList<Method> affectedMethods = new ArrayList<>(); // will hold methods for this class that are too long
            String affectedMethodMessage = null; // message to add to report
            String className = cls.getSimpleName();
            int index = 0;
            for(File file : javaSourceFiles){ // get correct java file index corresponding to current class cls
                int length = file.getName().length();
                if(file.getName().substring(0,length-5).compareTo(className) == 0 ){
                    index = javaSourceFiles.indexOf(file);
                    break;
                }
            }
            for (Method method : tempClassMethods) { // for each method of current cls
                try {
                    affectedMethodMessage = getKeyword(method.getName(), javaSourceFiles.get(index)); // inspect current method and get message
                    if (affectedMethodMessage != null) { // if not null then method has body which is too long
                        affectedMethods.add(method); // add method to list
                        report.putLongMethodData(method, affectedMethodMessage); // put method as key and message as data
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (!affectedMethods.isEmpty()) {
                for (Method method : affectedMethods) {
                    report.putReportData(cls, method);
                }
            } else if (affectedMethods.isEmpty()) {
                cleanClasses.add(cls);
            }
        }
        if (!cleanClasses.isEmpty()) {
            loadedClasses.removeAll(cleanClasses); // remove all clean classes
            report.setAffectedClasses(loadedClasses); // add only affected classes to report for this smell
        }
    }
    // return report
    public Report getReport(){
        return report;
    }
}
