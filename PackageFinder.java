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

    public String getPackageName(){
        return packageName;
    }

    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(javaSource);
        while (fileScanner.hasNextLine()) {
            packageName = fileScanner.nextLine();
            Scanner wordScanner = new Scanner(packageName);

            while (wordScanner.hasNext()) {
                String nextWord = wordScanner.next();
                if (nextWord.equals(keyword)) {
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
        packageName = packageName.substring(8, packageName.length()-1);
        return packageName;
    }
}
