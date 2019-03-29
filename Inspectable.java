package CodeSmellers;

import java.text.DecimalFormat;

// find better name for interface, naming after smells pigeon holes its usage.
public interface Inspectable extends SourceReadable, Reflectable{
    DecimalFormat df = new DecimalFormat("#.00");
}
