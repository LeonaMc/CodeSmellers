package CodeSmells;

import Interface.SourceReadable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// finds package name if source file belongs to package. package name needed for class loader
public class PackageFinder implements SourceReadable {

    PackageFinder() {
        // empty
    }

    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(javaSource);
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream));

        String line;
        String p = null;
        Pattern pattern = Pattern.compile("package" + "\\W+(\\w+)");
        Matcher matcher = pattern.matcher("");
        boolean pFound = false;
        try {
            while((line = input.readLine()) != null){
                matcher.reset(line);
                while(matcher.find()){
                    p = line;
                    pFound = true;
                    break;
                }
                if(pFound)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(p);
        try {
            input.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(p == null){
            return null;
        }
        else {
            p = p.substring(8, p.length()-1);
        }
        return p;
    }
}
