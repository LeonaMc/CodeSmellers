package CodeSmells;

import Interface.Countable;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineCounter implements Countable {
    private Stack<String> characterStack;

    public LineCounter() {
        characterStack = new Stack<>();
    }

    @Override
    public int countLines(File javaSource) throws FileNotFoundException {
        FileReader fileReader = new FileReader(javaSource);
        LineNumberReader numberReader = new LineNumberReader(fileReader);
        int lines = 0;

        try {
            while (numberReader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            numberReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public int countCommentBody(File javaSource, int flag) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(javaSource); // new input stream
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream)); // Buffered reader for large files
        String line; // current line from reader
        int lineCounter = 0; // current line number
        int startCommentLine = 0; // line number of /*
        int endCommentLine = 0; // line number of */
        boolean startComment = false; // when true /* found
        boolean foundEnd = false; // when true */ found
        while ((line = input.readLine()) != null) { // while reader has lines to read
            lineCounter++; // increment on each new line
            for (int j = 0; j < line.length(); j++) { // iterate through characters of line to find start of /*
                if (line.charAt(j) == '/') { // if current character is /
                    if ((j + 1) < line.length()) { // check if j+1 is out of bounds
                        if (line.charAt(j + 1) == '*') { // if j+1 is not out of bounds check if next char is *
                            String openingComment = "/*"; // set string to opening multiline comment
                            characterStack.push(openingComment); // push to stack
                            if (!startComment) { //
                                startCommentLine = lineCounter; // set starting lineNumber of comment
                                startComment = true;
                            }
                        }
                    }
                } else if (line.charAt(j) == '*') { // check if next char is start of closing comment
                    if ((j + 1) < line.length()) { // make sure j+1 is not out of bounds
                        if (line.charAt(j + 1) == '/') {
                            characterStack.pop(); // if */ found pop last added /*
                        }
                    }
                    if (characterStack.isEmpty()) { // if stack is empty multiline comment end found
                        endCommentLine = lineCounter; // set end comment line number
                        foundEnd = true;
                        startComment = false;
                    }
                }
            }
            if (foundEnd) {
                break;
            }
        }
        if (flag == 0)
            return endCommentLine; // return if searching for line in file where multiline comment ends
        else if (flag == 1)
            return endCommentLine - startCommentLine; //return this for comment length
        else
            return -1; // return -1 if wrong flag param given, check return for -1
    }
}
