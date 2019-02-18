package sqlite.pethoalpar.com.alqlite;

import java.lang.reflect.Field;

import sqlite.pethoalpar.com.alqlite.annotation.Column;
import sqlite.pethoalpar.com.alqlite.annotation.Id;
import sqlite.pethoalpar.com.alqlite.annotation.Table;
import sqlite.pethoalpar.com.alqlite.exception.DatabaseAnnotationMissingExceptions;
import sqlite.pethoalpar.com.alqlite.mapper.ColumnTypeMapper;

public class SQLBuilder {

    public static final StringBuilder buildCreateQuerry ( Class clazz ) throws DatabaseAnnotationMissingExceptions {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        if (clazz.isAnnotationPresent(Table.class)) {
            sb.append(( (Table) clazz.getAnnotation(Table.class) ).name());
            sb.append("(");
            for (int i = 0; i < clazz.getDeclaredFields().length; ++ i) {
                Field field = clazz.getDeclaredFields()[i];
                field.setAccessible(true);
                if (field.isAnnotationPresent(Column.class)) {
                    sb.append(i > 0 ? ", " : " ");
                    Column annotation = field.getAnnotation(Column.class);
                    sb.append(annotation.columnName());
                    sb.append(" ");
                    sb.append(ColumnTypeMapper.getType(field));
                    if (field.isAnnotationPresent(Id.class)) {
                        sb.append(" PRIMARY KEY");
                    }
                }
            }
        } else {
            throw new DatabaseAnnotationMissingExceptions();
        }
        sb.append(")");
        return sb;
    }
}
