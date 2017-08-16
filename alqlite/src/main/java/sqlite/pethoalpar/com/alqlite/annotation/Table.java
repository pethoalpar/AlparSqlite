package sqlite.pethoalpar.com.alqlite.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by pethoalpar on 6/25/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name();
}
