package CodeSmellers;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//
public class FindLongMethods implements Bloatable {
    private HashMap<Class, Method[]> classMethods;
    private ArrayList<Class> classSourceFiles;
    private ArrayList<File> javaSourceFiles;
    private Stack<Integer> bracketStack = new Stack<>();

    FindLongMethods(ArrayList<Class> classSourceFiles, ArrayList<File> javaSourceFiles) {
        this.classSourceFiles = new ArrayList<>(classSourceFiles);
        this.javaSourceFiles = new ArrayList<>(javaSourceFiles);
        classMethods = new HashMap<>();
    }

    public void getClassMethods() {
        for (Class cls : classSourceFiles) { //
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

        Pattern methodPattern = Pattern.compile(keyword + "\\(.*\\)\\{"); // regular expression to find first line of method
        Matcher matcher = methodPattern.matcher("");
        String line;
        int startLine = 0;
        int i = 0;

        try {
            while ((line = input.readLine()) != null) {

                matcher.reset(line);
                i++;
                while (matcher.find()) {
                    System.out.println("Line " + line);
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

    @Override
    public void reflectClass() {
        getClassMethods();
        int fileToTest = 1; // change value to check different class
        //new File("src\\methodsToText").mkdir();
        System.out.println(classSourceFiles.get(fileToTest).getSimpleName());
        String keyword = classSourceFiles.get(fileToTest).getDeclaredMethods()[0].getName();

        try {
            getKeyword(keyword, javaSourceFiles.get(fileToTest));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*for(Class cls : classSourceFiles){
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

    @Override
    public int countLines(File javaSource) throws FileNotFoundException {
        return 0;
    }

    @Override
    public void printReport() {

    }
}
