package sqlite.pethoalpar.com.alqlite;

import android.content.Context;

import java.util.List;

import sqlite.pethoalpar.com.alqlite.exception.NoResultException;

public class DatabaseService<T> {

    private SQLBusinessDelegate<T> sqlBusinessDelegate;

    public DatabaseService ( Class<T> clazz, Context context ) {
        sqlBusinessDelegate = new SQLBusinessDelegate<T>(context, clazz, "database", null, 1);
    }

    public Long insert ( Object entity ) throws NoResultException {
        return sqlBusinessDelegate.insert(entity);
    }

    public T findById ( int id ) throws NoResultException {
        return sqlBusinessDelegate.findById(id);
    }

    public List<T> findAll () throws NoResultException {
        return sqlBusinessDelegate.findAll();
    }

    public boolean update ( Object entity ) {
        return sqlBusinessDelegate.update(entity);
    }
}
