package CodeSmellers;

import java.io.*;

public class LineCounter implements Countable {

    public LineCounter(){

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
}
