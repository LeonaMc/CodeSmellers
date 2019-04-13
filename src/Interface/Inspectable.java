package Interface;

import java.text.DecimalFormat;

// interface for classes which reflect and read source
public interface Inspectable extends SourceReadable, Reflectable{
    DecimalFormat df = new DecimalFormat("#.00");
}
