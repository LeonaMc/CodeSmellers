/*
 * Class will be used to determine the data class code smell in a code base and return a smell rating
 * Data class is when class contains only getters and setters.
 *
 * To test for Data class:
 *  1. Find method declaration
 *  2. count number of statments and store in varible numOfStatements
 *  3. Check number of return statements takeaway from numofstatements
 *  4. check nubber of assignment takeaway from numofstatements
 *  5  check number of variable declaration from numofstatements
 *  6. if numofstatements 0 then is a data class
 *
 */

package Codesmellers;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class DataClass implements SourceReadable,Reportable{

        private ArrayList<File> javaSources;
        private ArrayList<Class> javaClasses;
        private Report report;

       DataClass(ArrayList<File> javaSources, ArrayList<Class> javaClasses, int numOfStatements) {
                this.javaSources = javaSources;
                this.javaClasses = javaClasses;
                report = new Report();
        }


        @Override
        public Report returnReport() {
                return null;
        }

        @Override
        public void formatData() {

        }

        @Override
        public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
                return null;
        }

        public int findStatements(){

        }
        public int findreturn(){

        }

        public int findassignments

        public int findariabledeclaration()

        public boolean isitdataclass(){
           if (numOfStatements == 0){
               return true;
           }
           else  return false;
    }
}


