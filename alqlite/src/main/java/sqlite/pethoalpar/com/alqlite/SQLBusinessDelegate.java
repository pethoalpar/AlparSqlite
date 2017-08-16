package sqlite.pethoalpar.com.alqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import sqlite.pethoalpar.com.alqlite.annotation.Column;
import sqlite.pethoalpar.com.alqlite.annotation.Id;
import sqlite.pethoalpar.com.alqlite.annotation.Table;
import sqlite.pethoalpar.com.alqlite.exception.DatabaseBusinessException;
import sqlite.pethoalpar.com.alqlite.exception.NoResultException;

import sqlite.pethoalpar.com.alqlite.Types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pethoalpar on 6/25/2016.
 */
public class SQLBusinessDelegate<T> extends SQLiteOpenHelper {

    private Class<T> clazz;

    public SQLBusinessDelegate(Context context, Class<T> clazz, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        this.clazz = clazz;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        if(this.clazz.isAnnotationPresent(Table.class)){
            sb.append(clazz.getAnnotation(Table.class).name());
            sb.append("(");
            for(int i=0; i<clazz.getDeclaredFields().length; ++i){
                Field field = clazz.getDeclaredFields()[i];
                field.setAccessible(true);
                if(field.isAnnotationPresent(Column.class)){
                    sb.append(i>0?", " : " ");
                    Column annotation = field.getAnnotation(Column.class);
                    sb.append(annotation.columnName());
                    sb.append(" ");
                    sb.append(annotation.type());
                    if(field.isAnnotationPresent(Id.class)){
                        sb.append(" PRIMARY KEY");
                    }
                }
            }
        }
        sb.append(")");
        db.execSQL(sb.toString());
    }

    public boolean update(Object entity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(entity);
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            Object id = -1;
            for(int i=0; i<clazz.getDeclaredFields().length; ++i){
                Field field = clazz.getDeclaredFields()[i];
                if(field.isAnnotationPresent(Id.class)){
                    try{
                        id = field.get(entity);
                    }catch (IllegalAccessException e){
                        Log.w("Can not read the id!",e);
                    }
                }
            }
            if(!id.equals(-1)){
                db.update(table.name(), values, "id="+id,null);
                return true;
            }
        }
        return false;
    }

    private ContentValues getContentValues(Object entity){
        ContentValues values = new ContentValues();
        for(Field field : clazz.getFields()){
            field.setAccessible(true);
            if(!java.lang.reflect.Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(Column.class)){
                Column annotation = field.getAnnotation(Column.class);
                try{
                    switch (annotation.type()){

                        case INTEGER:
                            values.put(annotation.columnName(), (Integer)field.get(entity));
                            break;
                        case TEXT:
                            values.put(annotation.columnName(), field.get(entity).toString());
                            break;
                        case REAL:
                            values.put(annotation.columnName(), field.getDouble(entity));
                            break;
                        case BLOB:
                            values.put(annotation.columnName(), (byte[])field.get(entity));
                            break;
                        default:
                            break;
                    }
                }catch (IllegalAccessException e){
                    Log.w("Cannot read field!",e);
                }
            }
        }
        return values;
    }

    public int  deleteById(int id) throws DatabaseBusinessException{
        SQLiteDatabase db = this.getWritableDatabase();
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            String[] selectionArgs = { String.valueOf(id)};
            int deleteRowNumber = db.delete(table.name(),"id=?", selectionArgs);
            db.close();
            return deleteRowNumber;
        }
        throw new DatabaseBusinessException();
    }

    public Long save(Object entity) throws NoResultException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(entity);
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            Long id = db.insert(table.name(), null, values);
            db.close();
            return id;
        }
        throw new NoResultException();
    }

    public T findById(int id) throws NoResultException{
        SQLiteDatabase db = this.getReadableDatabase();
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            String where = "id=?";
            String[] selectionArgs = { String.valueOf(id)};
            Cursor c = db.query(table.name(), null, where, selectionArgs, null, null, "id DESC");
            return this.cursor2List(c).get(0);
        }
        throw new NoResultException();
    }

    public List<T> findAll() throws NoResultException{
        SQLiteDatabase db = this.getReadableDatabase();
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            String[] selectionArgs = { };
            Cursor c = db.query(table.name(), null, null, selectionArgs, null, null, "id DESC");
            return this.cursor2List(c);
        }
        throw new NoResultException();
    }

    public List<T> runQuery(String sql, String [] selectionArgs){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql,selectionArgs);
        return this.cursor2List(c);
    }

    public List<T> findEntityByParameter(Map<String, Object> params) throws NoResultException{
        SQLiteDatabase db = this.getReadableDatabase();
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            StringBuilder where = new StringBuilder();
            String[] selectionArgs = new String[params.size()];
            int i=0;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                where.append(entry.getKey()+"=:?");
                selectionArgs[i++] = entry.getValue().toString();
            }
            Cursor c = db.query(table.name(), null, where.toString(), selectionArgs, null, null, "id DESC");
            return this.cursor2List(c);
        }else {
            throw new NoResultException();
        }
    }

    private List<T> cursor2List(Cursor cursor){
        List<T> retList = new ArrayList<>();
        try{
            if(cursor.moveToFirst()){
                do{
                    T entity = clazz.newInstance();
                    String[] colNames = cursor.getColumnNames();
                    for (String colName : colNames){
                        int index = cursor.getColumnIndex(colName);
                        int type = cursor.getType(index);
                        Field field = this.getField(colName);
                        switch (type){
                            case  Cursor.FIELD_TYPE_INTEGER:
                                field.set(entity, cursor.getInt(index));
                                break;
                            case  Cursor.FIELD_TYPE_STRING:
                                field.set(entity, cursor.getString(index));
                                break;
                            case  Cursor.FIELD_TYPE_FLOAT:
                                field.setDouble(entity, cursor.getDouble(index));
                                break;
                            case  Cursor.FIELD_TYPE_BLOB:
                                field.set(entity, cursor.getBlob(index));
                                break;
                            default:
                                break;
                        }
                    }
                    retList.add(entity);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.w("Can not get field!",e);
        }
        return retList;
    }

    private Field getField(String columnName){
        Field[] fields = clazz.getFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(Column.class)){
                Column column = field.getAnnotation(Column.class);
                if(columnName.equalsIgnoreCase(column.columnName())){
                    return field;
                }
            }
        }
        return null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            db.execSQL("DROP TABLE IF EXISTS "+table.name());
            onCreate(db);
        }
    }
}
