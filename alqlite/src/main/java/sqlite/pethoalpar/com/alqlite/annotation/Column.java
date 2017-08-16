package sqlite.pethoalpar.com.alqlite.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sqlite.pethoalpar.com.alqlite.Types;

/**
 * Created by pethoalpar on 6/25/2016.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String columnName();
    Types type();
}
