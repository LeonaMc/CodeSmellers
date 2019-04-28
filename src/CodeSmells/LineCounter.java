package CodeSmells;

import Interface.Countable;

import java.io.*;
import java.util.Stack;
// when class was created there were a few other helper methods planned to be implemented
// after refactoring some classes, this class ended up more or less redundant
public class LineCounter implements Countable {
    public LineCounter(){

    }
    // counts lines in a file used in large class and lazy class
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
}
