import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class FindLongParamList implements Bloatable{
    private ArrayList<Class> classArrayList;

    public FindLongParamList(ArrayList<Class> classArrayList){
        this.classArrayList = new ArrayList<>();
        this.classArrayList.addAll(classArrayList);
    }

    public int countMethodParams(Method method) throws FileNotFoundException {
        return method.getParameterCount();
    }

    @Override
    public void reflectClass() {

    }

    @Override
    public int countLines(File javaSource) throws FileNotFoundException {
        return 0;
    }

    @Override
    public void printReport() {

    }

    @Override
    public String getKeyword(String keyword, File javaSource) throws FileNotFoundException {
        return null;
    }
}
