package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PackageFinder implements SourceReadable {
    private boolean packageFound = false;
    private boolean classKeywordFound = false;
    private String packageName = null;

    PackageFinder() {

    }

    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(javaSource);
        while (fileScanner.hasNextLine()) {
            Scanner wordScanner = new Scanner(fileScanner.nextLine());

            while (wordScanner.hasNext()) {
                String nextWord = wordScanner.next();
                if (nextWord.equals(keyword)) {
                    packageName = nextWord;
                    packageFound = true;
                    wordScanner.close();
                    break;
                } else if (nextWord.equals("class")) {
                    packageName = null;
                    classKeywordFound = true;
                    wordScanner.close();
                    break;
                }
            }
            if (packageFound || classKeywordFound) {
                fileScanner.close();
                break;
            }
        }
        return packageName;
    }
}
