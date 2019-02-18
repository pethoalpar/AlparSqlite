package sqlite.pethoalpar.com.alqlite.util;

import android.util.Log;

import java.lang.reflect.Field;

import sqlite.pethoalpar.com.alqlite.annotation.Id;

public class IdUtil {

    public static final Object getId ( Object entity, Object id, Class clazz ) {
        for (int i = 0; i < clazz.getDeclaredFields().length; ++ i) {
            Field field = clazz.getDeclaredFields()[i];
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    id = field.get(entity);
                    return id;
                } catch (IllegalAccessException e) {
                    Log.w("Can not read the id!", e);
                }
            }
        }
        return null;
    }
}
