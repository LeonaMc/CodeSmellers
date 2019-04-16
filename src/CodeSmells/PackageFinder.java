package CodeSmells;

import Interface.SourceReadable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
// finds package name if source file belongs to package. package name needed for class loader
public class PackageFinder implements SourceReadable {
    private boolean packageFound;
    private boolean classKeywordFound; // if file reader reaches "class" then no package found
    private String packageName; // stores package name if found
    private LineCounter lineCounter; //
    PackageFinder() {
        packageFound = false;
        classKeywordFound = false;
        packageName = null;
        lineCounter = new LineCounter();
    }

    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        int endOfMultiLine = 0; // stores start line number of multiline comment is there is one at start of file
        try {
            endOfMultiLine = lineCounter.countCommentBody(javaSource, 0); // countCommentBody finds start and end line number of multiline comment
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner fileScanner = new Scanner(javaSource);
        int lineNumber = 0; // current line number
        boolean multiLineCommentFound = false; // /* found if true
        while (fileScanner.hasNextLine()) {
            lineNumber++;
            packageName = fileScanner.nextLine();
            if(packageName.startsWith("//")){
                do{
                    packageName = fileScanner.nextLine();
                }while(packageName.startsWith("//"));
            }

            Scanner wordScanner = new Scanner(packageName);

            while (wordScanner.hasNext()) {
                String nextWord = wordScanner.next();
                if(nextWord.contains("/*")){
                    multiLineCommentFound = true;
                }
                if(!multiLineCommentFound){ // if /* not found
                    if (nextWord.equals(keyword)) {
                        packageFound = true;
                        wordScanner.close();
                        fileScanner.close();
                        break;
                    } else if (nextWord.equals("class")) {
                        packageName = null;
                        classKeywordFound = true;
                        wordScanner.close();
                        fileScanner.close();
                        break;
                    }
                }
                else{ // else check if current line has reached end of multiline
                    if(lineNumber == endOfMultiLine){
                        multiLineCommentFound = false; // set false so package name can be found
                    }
                }
            }
            if (packageFound || classKeywordFound) {
                break;
            }
        }
        if(packageName != null && !classKeywordFound){
            packageName = packageName.substring(8, packageName.length()-1); // remove keyword package and " "(8 characters) from string
        }
        else if(classKeywordFound){ // if class found before package file does not belong to a package
            packageName = null;
        }

        classKeywordFound = false; // reset booleans
        packageFound = false;
        return packageName;
    }
}
