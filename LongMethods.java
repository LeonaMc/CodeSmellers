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
    private Stack<Character> bracketStack;
    private Report report;
    private char openingBrace;
    private char closingBrace;

    LongMethods(ArrayList<Class> loadedClasses, ArrayList<File> javaSource) {
        this.loadedClasses = new ArrayList<>(loadedClasses);
        this.javaSourceFiles = new ArrayList<>(javaSource);
        report = new Report();
        bracketStack = new Stack<>();
        classMethods = new HashMap<>();
        openingBrace = '{'; // was originally in method body but was being added to bracket stack
        closingBrace = '}';
    }

    public void getClassMethods() {
        for (Class cls : loadedClasses) { //
            classMethods.put(cls, cls.getDeclaredMethods());
        }
    }

    public Report getReport() {
        return report;
    }

    private int getMethodBody(int startLine, File javaSource) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(javaSource);
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;
        int endLine = 0, i = 0;
        boolean foundStart = false, foundEnd = false;

        while ((line = input.readLine()) != null) {
            i++;
            if (i == startLine) {
                foundStart = true;
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == openingBrace) {
                        bracketStack.push(line.charAt(j));
                    }
                }
            } else if (foundStart) {
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == openingBrace) {
                        bracketStack.push(line.charAt(j));
                    } else if (line.charAt(j) == closingBrace) {
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
        Class myClass = loadedClasses.get(javaSourceFiles.indexOf(javaSource));
        Method myMethod = null;
        for (Method method : myClass.getDeclaredMethods()) {
            if (method.getName().equalsIgnoreCase(keyword)) {
                myMethod = method;
            }
        }
        String buildRegex;
        buildRegex = Modifier.toString(myMethod.getModifiers()); // add catch for null getModifiers
        buildRegex = buildRegex + " " + myMethod.getReturnType().getSimpleName();
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

        if (endLine - startLine > 30) {
            return "Method " + keyword + " in " + javaSource.getName() + " is " + (endLine - startLine) + " lines long\n" +
                    "Method starts at line " + startLine + " and ends at line " + endLine + " in " + javaSource.getName();
        } else
            return null;
    }

    @Reflecting
    @Override
    public void reflectClass() {
        getClassMethods();
        ArrayList<Class> cleanClasses = new ArrayList<>();

        for (Class cls : loadedClasses) {
            Method[] tempClassMethods = classMethods.get(cls);
            ArrayList<Method> effectedMethods = new ArrayList<>();
            String effectedMethodMessage = null;
            for (Method method : tempClassMethods) {
                try {
                    effectedMethodMessage = getKeyword(method.getName(), javaSourceFiles.get(loadedClasses.indexOf(cls)));
                    if (effectedMethodMessage != null) {
                        effectedMethods.add(method);
                        report.putLongMethodData(method, effectedMethodMessage);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (!effectedMethods.isEmpty()) {
                for (Method method : effectedMethods) {
                    report.putCodeSmellData(cls, method);
                }
            } else if (effectedMethods.isEmpty()) {
                cleanClasses.add(cls);
            }
        }
        if (!cleanClasses.isEmpty()) {
            loadedClasses.removeAll(cleanClasses);
            report.setEffectedClasses(loadedClasses);
        }
    }
}
