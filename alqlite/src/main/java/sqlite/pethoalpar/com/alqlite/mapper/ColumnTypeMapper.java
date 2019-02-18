package sqlite.pethoalpar.com.alqlite.mapper;

import java.lang.reflect.Field;

import sqlite.pethoalpar.com.alqlite.types.DatabaseTypes;

public class ColumnTypeMapper {


    public static DatabaseTypes getType ( Field field ) {
        switch (field.getType().getSimpleName()) {
            case "Integer":
            case "Short":
            case "int":
            case "Byte":
            case "Long":
                return DatabaseTypes.INTEGER;
            case "String":
                return DatabaseTypes.TEXT;
            case "byte":
                return DatabaseTypes.BLOB;
            case "Float":
            case "Double":
            case "float":
            case "double":
                return DatabaseTypes.REAL;
            default:
                return DatabaseTypes.TEXT;
        }
    }
}
