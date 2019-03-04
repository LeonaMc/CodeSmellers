package CodeSmellers;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class FindLongParamList implements Countable{
    private ArrayList<Class> classArrayList;

    public FindLongParamList(ArrayList<Class> classArrayList){
        this.classArrayList = new ArrayList<>();
        this.classArrayList.addAll(classArrayList);
    }

    public int countMethodParams(Method method) throws FileNotFoundException {
        return method.getParameterCount();
    }

    @Override
    public int countComponents(Class cls) throws FileNotFoundException {
        return 0;
    }
}
