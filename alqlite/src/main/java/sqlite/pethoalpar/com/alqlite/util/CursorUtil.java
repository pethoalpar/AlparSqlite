package sqlite.pethoalpar.com.alqlite.util;

import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CursorUtil {

    public static final <T> List<T> cursor2List ( Cursor cursor, Class<T> clazz ) {
        List<T> retList = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    T entity = (T) clazz.newInstance();
                    String[] colNames = cursor.getColumnNames();
                    for (String colName : colNames) {
                        int index = cursor.getColumnIndex(colName);
                        int type = cursor.getType(index);
                        Field field = FieldUtil.getField(colName, clazz);
                        switch (type) {
                            case Cursor.FIELD_TYPE_INTEGER:
                                field.set(entity, cursor.getInt(index));
                                break;
                            case Cursor.FIELD_TYPE_STRING:
                                field.set(entity, cursor.getString(index));
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                field.setDouble(entity, cursor.getDouble(index));
                                break;
                            case Cursor.FIELD_TYPE_BLOB:
                                field.set(entity, cursor.getBlob(index));
                                break;
                            default:
                                break;
                        }
                    }
                    retList.add(entity);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.w("Can not get field!", e);
        }
        return retList;
    }
}
