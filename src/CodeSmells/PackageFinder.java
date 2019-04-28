package CodeSmells; //; asdasd

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
        // empty after refactor
    }

    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(javaSource);
        BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream));

        String line; // current line
        String pack = null; // package name
        Pattern pattern = Pattern.compile("package" + "\\W+(\\w+)"); // regex to find package name
        Matcher matcher = pattern.matcher(""); // empty matcher
        boolean pFound = false; // if true package name found
        try {
            while((line = input.readLine()) != null){ // while lines still to read
                matcher.reset(line); // reset empty matcher to current line
                while(matcher.find()){
                    pack = line.substring(8, line.indexOf(';')); // remove package and anything trailing from ; leaving just package name
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
        if(pack == null){
            return null; // return null and check on receiving end
        }
        return pack; // else return package name
    }
}
