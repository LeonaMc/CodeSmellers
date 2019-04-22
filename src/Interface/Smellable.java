package Interface;

import java.text.DecimalFormat;

// interface for classes which reflect and read source
public interface Smellable extends SourceReadable, Reflectable{
    DecimalFormat df = new DecimalFormat("#.00");
}
