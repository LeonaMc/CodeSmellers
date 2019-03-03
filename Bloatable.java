import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

// find better name for interface, naming after smells pigeon holes its usage. Also Bloatable makes no sense.
public interface Bloatable extends SourceReadable {
    DecimalFormat df = new DecimalFormat("#.00");
    public void printReport();
    public void reflectClass();
    public int countLines(File javaSource) throws FileNotFoundException;
}
