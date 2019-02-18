package sqlite.pethoalpar.com.alqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Map;

import sqlite.pethoalpar.com.alqlite.annotation.Table;
import sqlite.pethoalpar.com.alqlite.exception.DatabaseAnnotationMissingExceptions;
import sqlite.pethoalpar.com.alqlite.exception.DatabaseBusinessException;
import sqlite.pethoalpar.com.alqlite.exception.NoResultException;
import sqlite.pethoalpar.com.alqlite.util.CursorUtil;
import sqlite.pethoalpar.com.alqlite.util.FieldUtil;
import sqlite.pethoalpar.com.alqlite.util.IdUtil;

/**
 * Created by pethoalpar on 6/25/2016.
 */
class SQLBusinessDelegate<T> extends SQLiteOpenHelper {

    private Class<T> clazz;

    public SQLBusinessDelegate(Context context, Class<T> clazz, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        this.clazz = clazz;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        StringBuilder sb = null;
        try {
            sb = SQLBuilder.buildCreateQuerry(this.clazz);
            db.execSQL(sb.toString());
        } catch (DatabaseAnnotationMissingExceptions databaseAnnotationMissingExceptions) {
            databaseAnnotationMissingExceptions.printStackTrace();
        }

    }

    public void init () {

    }

    public boolean update(Object entity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = FieldUtil.getContentValues(entity);
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            Object id = -1;
            id = IdUtil.getId(entity, id, clazz);
            if(!id.equals(-1)){
                db.update(table.name(), values, "id="+id,null);
                return true;
            }
        }
        return false;
    }

    public int deleteById ( int id ) throws DatabaseBusinessException {
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

    public Long insert ( Object entity ) throws NoResultException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = FieldUtil.getContentValues(entity);
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
            return CursorUtil.cursor2List(c, this.clazz).get(0);
        }
        throw new NoResultException();
    }

    public List<T> findAll() throws NoResultException{
        SQLiteDatabase db = this.getReadableDatabase();
        if(this.clazz.isAnnotationPresent(Table.class)){
            Table table = this.clazz.getAnnotation(Table.class);
            String[] selectionArgs = { };
            Cursor c = db.query(table.name(), null, null, selectionArgs, null, null, "id DESC");
            return CursorUtil.cursor2List(c, this.clazz);
        }
        throw new NoResultException();
    }

    public List<T> runQuery(String sql, String [] selectionArgs){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql,selectionArgs);
        return CursorUtil.cursor2List(c, this.clazz);
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
            return CursorUtil.cursor2List(c, this.clazz);
        }else {
            throw new NoResultException();
        }
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
