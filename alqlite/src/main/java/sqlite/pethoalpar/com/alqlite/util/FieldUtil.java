package sqlite.pethoalpar.com.alqlite.util;

import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Field;

import sqlite.pethoalpar.com.alqlite.annotation.Column;
import sqlite.pethoalpar.com.alqlite.mapper.ColumnTypeMapper;
import sqlite.pethoalpar.com.alqlite.types.DatabaseTypes;

public class FieldUtil {

    public static final Field getField ( String columnName, Class clazz ) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if (columnName.equalsIgnoreCase(column.columnName())) {
                    return field;
                }
            }
        }
        return null;
    }

    public static final ContentValues getContentValues ( Object entity ) {
        ContentValues values = new ContentValues();
        for (Field field : entity.getClass().getFields()) {
            field.setAccessible(true);
            if (! java.lang.reflect.Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(Column.class)) {
                try {
                    DatabaseTypes fieldType = ColumnTypeMapper.getType(field);
                    Column annotation = field.getAnnotation(Column.class);
                    switch (fieldType) {
                        case INTEGER:
                            values.put(annotation.columnName(), (Integer) field.get(entity));
                            break;
                        case TEXT:
                            values.put(annotation.columnName(), field.get(entity).toString());
                            break;
                        case REAL:
                            values.put(annotation.columnName(), field.getDouble(entity));
                            break;
                        case BLOB:
                            values.put(annotation.columnName(), (byte[]) field.get(entity));
                            break;
                        default:
                            break;
                    }
                } catch (IllegalAccessException e) {
                    Log.w("Cannot read field!", e);
                }
            }
        }
        return values;
    }
}
