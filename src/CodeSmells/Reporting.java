package CodeSmells;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// for reporting methods
@Retention(RetentionPolicy.RUNTIME)
public @interface Reporting {
}
