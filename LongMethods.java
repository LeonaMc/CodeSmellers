package CodeSmellers;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LongMethods implements Inspectable {
    private HashMap<Class, Method[]> classMethods;
    private ArrayList<Class> loadedClasses;
    private ArrayList<File> javaSourceFiles;
    private Stack<Integer> bracketStack = new Stack<>();

    LongMethods(ArrayList<Class> loadedClasses, ArrayList<File> javaSource) {
        this.loadedClasses = new ArrayList<>(loadedClasses);
        this.javaSourceFiles = new ArrayList<>(javaSource);
        classMethods = new HashMap<>();
    }

    public void getClassMethods() {
        for (Class cls : loadedClasses) { //
            classMethods.put(cls, cls.getDeclaredMethods());
        }
    }
    // Doesn't find body if method body is on one line or if no parameters
    public int getMethodBody(int startLine, File javaSource) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(javaSource);
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;
        int endLine = 0, i = 0;
        boolean foundStart = false, foundEnd = false;

        while ((line = input.readLine()) != null) {
            i++;
            if (i == startLine) {
                System.out.println(line);
                foundStart = true;
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '{') {
                        bracketStack.push((int) line.charAt(j));
                    }
                }
            } else if (foundStart) {
                System.out.println(line);
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '{') {
                        bracketStack.push((int) line.charAt(j));
                    } else if (line.charAt(j) == '}') {
                        bracketStack.pop();
                        if (bracketStack.isEmpty()) {
                            endLine = i;
                            foundEnd = true;
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

    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(javaSource);
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream));
        Class myClass =  loadedClasses.get(javaSourceFiles.indexOf(javaSource));
        Method myMethod = null;
        for(Method method: myClass.getDeclaredMethods()){
            if (method.getName().equalsIgnoreCase(keyword)){
                myMethod = method;
            }
        }
        System.out.println("myMethod " + myMethod.getName());
        String buildRegex = Modifier.toString(myMethod.getModifiers());
        System.out.println("BuildRegex " + buildRegex);
        String regex = buildRegex + " " + keyword;
        System.out.println("Regex " + regex);
        Pattern methodPattern = Pattern.compile(regex); // regular expression to find first line of method
        Matcher matcher = methodPattern.matcher("");
        String line;
        int startLine = 0;
        int i = 0;

        try {
            while ((line = input.readLine()) != null) {

                matcher.reset(line);
                i++;
                System.out.println(line);
                while (matcher.find()) {
                    System.out.println(line);
                    startLine = i;
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
            endLine = getMethodBody(startLine, javaSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Method " + keyword + " in " + javaSource.getName() + " is " + (endLine - startLine) + " lines long\n");
        System.out.println("Method starts at line " + startLine + " and ends at line " + endLine + " in " + javaSource.getName());
        return null;
    }

    @Reflecting
    @Override
    public void reflectClass() {
        getClassMethods();
        int fileToTest = 1; // change value to check different class
        //new File("src\\methodsToText").mkdir();
        System.out.println(loadedClasses.get(fileToTest).getSimpleName());
        String keyword = loadedClasses.get(fileToTest).getDeclaredMethods()[0].getName();
        System.out.println("Keyword " + keyword);

        try {
            getKeyword(keyword, javaSourceFiles.get(fileToTest));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*for(Class cls : loadedClasses){
            Method[] tempClassMethods = classMethods.get(cls);
            for(Method meth: tempClassMethods){
                try {
                    getKeyword(meth.getName(), javaSourceFiles.get(index));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
}
