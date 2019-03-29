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
    private Stack<Integer> bracketStack;
    private Report report;

    LongMethods(ArrayList<Class> loadedClasses, ArrayList<File> javaSource) {
        this.loadedClasses = new ArrayList<>(loadedClasses);
        this.javaSourceFiles = new ArrayList<>(javaSource);
        report = new Report();
        bracketStack = new Stack<>();
        classMethods = new HashMap<>();
    }

    public void getClassMethods() {
        for (Class cls : loadedClasses) { //
            classMethods.put(cls, cls.getDeclaredMethods());
        }
    }

    public Report getReport() {
        return report;
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
                //System.out.println(line);
                foundStart = true;
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '{') {
                        bracketStack.push((int) line.charAt(j));
                    }
                }
            } else if (foundStart) {
                //System.out.println(line);
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '{') {
                        bracketStack.push((int) line.charAt(j));
                    } else if (line.charAt(j) == '}') {
                        bracketStack.pop();
                        if (bracketStack.isEmpty()) {
                            endLine = i+1;
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
        String buildRegex;
        buildRegex = Modifier.toString(myMethod.getModifiers()); // add catch for null getModifiers
        buildRegex = buildRegex + " " + myMethod.getReturnType().getSimpleName();
        //System.out.println("BuildRegex " + buildRegex);
        String regex = buildRegex + " " + keyword;

        Pattern methodPattern = Pattern.compile(Pattern.quote(regex)); // regular expression to find first line of method
        Matcher matcher = methodPattern.matcher("");
        String line;
        int startLine = 0;
        int i = 0;
        try {
            while ((line = input.readLine()) != null) {

                matcher.reset(line);
                i++;
                while (matcher.find()) {
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

        if(endLine-startLine > 30){
            return "Method " + keyword + " in " + javaSource.getName() + " is " + (endLine - startLine) + " lines long\n" +
                   "Method starts at line " + startLine + " and ends at line " + endLine + " in " + javaSource.getName();
        }
        else
        return null;
    }

    @Reflecting
    @Override
    public void reflectClass() {
        getClassMethods();
        ArrayList<Class> cleanClasses = new ArrayList<>();

        for(Class cls : loadedClasses){
            HashMap<Method,String> longMethodData = new HashMap<>();
            Method[] tempClassMethods = classMethods.get(cls);
            String effectedMethodMessage = null;
            for(Method method: tempClassMethods){
                try {
                    effectedMethodMessage = getKeyword(method.getName(), javaSourceFiles.get(loadedClasses.indexOf(cls)));
                    if(effectedMethodMessage != null){
                        longMethodData.put(method, effectedMethodMessage);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (!longMethodData.isEmpty()){
                report.putCodeSmellData(cls, longMethodData);
            }
        }

        for(Class cls : loadedClasses){
            if (!report.getCodeSmellData().containsKey(cls)){
                cleanClasses.add(cls);
            }
        }
        if(!cleanClasses.isEmpty()){
            loadedClasses.removeAll(cleanClasses);
            report.setEffectedClasses(loadedClasses);
        }
    }
}
